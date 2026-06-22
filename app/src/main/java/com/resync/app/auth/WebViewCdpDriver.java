package com.resync.app.auth;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Minimal Chrome DevTools Protocol client that attaches to <b>our own app's WebView</b> over its
 * on-device debug socket, so we can feed it <b>trusted</b> input.
 *
 * <p><b>Why:</b> reCAPTCHA distinguishes real input from script via {@code Event.isTrusted}. JS in
 * the page can only ever produce {@code isTrusted == false} events. But CDP {@code Input.*} events
 * traverse Chromium's real {@code RenderWidgetHost} input pipeline and arrive in the DOM as
 * {@code isTrusted == true} — same as a finger. This is the on-device analog of how
 * Puppeteer/undetected drivers type "for real".
 *
 * <p><b>How it's permissionless:</b> {@code WebView.setWebContentsDebuggingEnabled(true)} opens an
 * abstract-namespace unix socket {@code @webview_devtools_remote_<pid>}. Abstract sockets aren't
 * filesystem-permission gated, and we connect to <i>our own</i> process's socket (same UID) — no
 * root, no adb, no INJECT_EVENTS, no accessibility service. We discover the page target via
 * {@code GET /json}, then speak CDP over a hand-rolled WebSocket on the {@link LocalSocket} (the
 * Java CDP libraries are desktop-JVM-only and don't run on ART).
 *
 * <p>All methods block and must run off the main thread. Best-effort: the caller falls back to JS
 * injection if {@link #connect()} or any op fails.
 */
public final class WebViewCdpDriver {

    private static final String TAG = "WebViewCdpDriver";

    private LocalSocket socket;
    private InputStream in;
    private OutputStream out;
    private Thread reader;
    private volatile boolean open;

    private final AtomicInteger nextId = new AtomicInteger(1);
    private final ConcurrentHashMap<Integer, CountDownLatch> latches = new ConcurrentHashMap<>();
    private final ConcurrentHashMap<Integer, JSONObject> results = new ConcurrentHashMap<>();
    private final SecureRandom rnd = new SecureRandom();

    /**
     * When set, target discovery prefers the page whose URL contains this substring, and otherwise
     * the first NON-reddit page (used by the website-preview screenshotter to attach to its own
     * off-screen WebView rather than the reddit login WebView). When null, the original
     * reddit-login behaviour is kept.
     */
    private volatile String preferUrlSubstring;

    /** Connect, preferring the page whose URL matches {@code preferUrlSubstring} (else first non-reddit). */
    public boolean connect(String preferUrlSubstring) {
        this.preferUrlSubstring = preferUrlSubstring;
        return connect();
    }

    /** Connect to this process's WebView devtools socket and open a CDP WebSocket to the page. */
    public boolean connect() {
        try {
            String sockName = findDevtoolsSocketName();
            if (sockName == null) {
                android.util.Log.i(TAG, "no webview_devtools_remote socket found");
                return false;
            }
            socket = new LocalSocket();
            socket.connect(new LocalSocketAddress(sockName, LocalSocketAddress.Namespace.ABSTRACT));
            socket.setSoTimeout(15000);
            in = socket.getInputStream();
            out = socket.getOutputStream();

            String wsPath = discoverPageWebSocketPath();
            if (wsPath == null) {
                android.util.Log.i(TAG, "no page target found via /json");
                close();
                return false;
            }
            if (!websocketHandshake(wsPath)) {
                android.util.Log.i(TAG, "websocket handshake failed");
                close();
                return false;
            }
            open = true;
            reader = new Thread(this::readLoop, "cdp-reader");
            reader.setDaemon(true);
            reader.start();
            android.util.Log.i(TAG, "CDP connected via " + sockName);
            return true;
        } catch (Throwable t) {
            android.util.Log.i(TAG, "connect failed: " + t);
            close();
            return false;
        }
    }

    /** Reads /proc/net/unix for an abstract @..._devtools_remote socket, preferring our pid's. */
    private static String findDevtoolsSocketName() {
        String myPid = String.valueOf(android.os.Process.myPid());
        String fallback = null;
        try (BufferedReader br = new BufferedReader(new InputStreamReader(
                new java.io.FileInputStream("/proc/net/unix")))) {
            String line;
            while ((line = br.readLine()) != null) {
                int at = line.indexOf('@');
                if (at < 0) continue;
                String name = line.substring(at + 1).trim();
                if (!name.contains("_devtools_remote")) continue;
                if (name.endsWith("_" + myPid)) return name; // our own webview, exact match
                if (name.startsWith("webview_devtools_remote")) fallback = name;
            }
        } catch (Throwable t) {
            // fall through to constructed name
        }
        if (fallback != null) return fallback;
        // Last resort: the conventional name for our pid.
        return "webview_devtools_remote_" + myPid;
    }

    /** {@code GET /json} over the raw socket; returns the page target's ws path, e.g. /devtools/page/ID. */
    private String discoverPageWebSocketPath() throws Exception {
        out.write(("GET /json HTTP/1.1\r\nHost: localhost\r\nConnection: keep-alive\r\n\r\n")
                .getBytes("UTF-8"));
        out.flush();
        String body = readHttpResponseBody();
        if (body == null) return null;
        JSONArray arr = new JSONArray(body);
        String anyPath = null, anyPagePath = null, preferredPath = null, redditPath = null, nonRedditPath = null;
        for (int i = 0; i < arr.length(); i++) {
            JSONObject o = arr.optJSONObject(i);
            if (o == null) continue;
            String ws = o.optString("webSocketDebuggerUrl", null);
            if (ws == null) continue;
            String path = pathOf(ws);
            boolean isPage = "page".equals(o.optString("type"));
            String url = o.optString("url", "");
            if (isPage && preferUrlSubstring != null && url.contains(preferUrlSubstring) && preferredPath == null) {
                preferredPath = path;
            }
            if (isPage && url.contains("reddit.com") && redditPath == null) redditPath = path;
            if (isPage && !url.contains("reddit.com") && nonRedditPath == null) nonRedditPath = path;
            if (isPage && anyPagePath == null) anyPagePath = path;
            if (anyPath == null) anyPath = path;
        }
        if (preferUrlSubstring != null) {
            // Preview screenshotter: our own off-screen WebView (match its URL, else any non-login page).
            if (preferredPath != null) return preferredPath;
            if (nonRedditPath != null) return nonRedditPath;
        } else if (redditPath != null) {
            return redditPath; // login: exactly our reddit WebView
        }
        return anyPagePath != null ? anyPagePath : anyPath;
    }

    private static String pathOf(String wsUrl) {
        // ws://host/devtools/page/ID  ->  /devtools/page/ID
        int scheme = wsUrl.indexOf("://");
        String rest = scheme >= 0 ? wsUrl.substring(scheme + 3) : wsUrl;
        int slash = rest.indexOf('/');
        return slash >= 0 ? rest.substring(slash) : "/";
    }

    /** Reads an HTTP/1.1 response (headers + body) off {@link #in}; returns the body string. */
    private String readHttpResponseBody() throws Exception {
        // Read headers
        ByteArrayOutputStream head = new ByteArrayOutputStream();
        int state = 0; // counts \r\n\r\n
        int b;
        while ((b = in.read()) != -1) {
            head.write(b);
            if (b == '\r') continue;
            if (b == '\n') { state++; if (state == 2) break; } else state = 0;
        }
        String headers = head.toString("UTF-8");
        int contentLength = -1;
        for (String h : headers.split("\r\n")) {
            String hl = h.toLowerCase();
            if (hl.startsWith("content-length:")) {
                try { contentLength = Integer.parseInt(h.substring(h.indexOf(':') + 1).trim()); } catch (Exception ignored) {}
            }
        }
        ByteArrayOutputStream body = new ByteArrayOutputStream();
        if (contentLength >= 0) {
            for (int i = 0; i < contentLength; i++) {
                int c = in.read(); if (c == -1) break; body.write(c);
            }
        } else {
            // No content-length: read until the JSON array looks balanced (devtools returns small JSON).
            int depth = 0; boolean started = false;
            while ((b = in.read()) != -1) {
                body.write(b);
                if (b == '[') { depth++; started = true; }
                else if (b == ']') { depth--; if (started && depth == 0) break; }
            }
        }
        return body.toString("UTF-8");
    }

    private boolean websocketHandshake(String path) throws Exception {
        byte[] key = new byte[16];
        rnd.nextBytes(key);
        String secKey = android.util.Base64.encodeToString(key, android.util.Base64.NO_WRAP);
        String req = "GET " + path + " HTTP/1.1\r\n"
                + "Host: localhost\r\n"
                + "Upgrade: websocket\r\n"
                + "Connection: Upgrade\r\n"
                + "Sec-WebSocket-Key: " + secKey + "\r\n"
                + "Sec-WebSocket-Version: 13\r\n\r\n";
        out.write(req.getBytes("UTF-8"));
        out.flush();
        // Read status + headers (until blank line)
        ByteArrayOutputStream head = new ByteArrayOutputStream();
        int state = 0, b;
        while ((b = in.read()) != -1) {
            head.write(b);
            if (b == '\r') continue;
            if (b == '\n') { state++; if (state == 2) break; } else state = 0;
        }
        return head.toString("UTF-8").contains("101");
    }

    // ---- WebSocket framing ----

    private synchronized void sendText(String text) throws Exception {
        byte[] payload = text.getBytes("UTF-8");
        ByteArrayOutputStream f = new ByteArrayOutputStream();
        f.write(0x81); // FIN + text
        int len = payload.length;
        if (len < 126) {
            f.write(0x80 | len);
        } else if (len <= 0xFFFF) {
            f.write(0x80 | 126);
            f.write((len >>> 8) & 0xFF); f.write(len & 0xFF);
        } else {
            f.write(0x80 | 127);
            for (int i = 7; i >= 0; i--) f.write((int) (((long) len >>> (8 * i)) & 0xFF));
        }
        byte[] mask = new byte[4];
        rnd.nextBytes(mask);
        f.write(mask);
        for (int i = 0; i < payload.length; i++) f.write(payload[i] ^ mask[i % 4]);
        out.write(f.toByteArray());
        out.flush();
    }

    private void readLoop() {
        try {
            while (open) {
                int firstByte = in.read();
                if (firstByte == -1) break;
                int opcode = firstByte & 0x0F;
                int secondByte = in.read();
                if (secondByte == -1) break;
                boolean masked = (secondByte & 0x80) != 0;
                long len = secondByte & 0x7F;
                if (len == 126) {
                    len = ((long) readByte() << 8) | readByte();
                } else if (len == 127) {
                    len = 0; for (int i = 0; i < 8; i++) len = (len << 8) | readByte();
                }
                byte[] maskKey = new byte[4];
                if (masked) for (int i = 0; i < 4; i++) maskKey[i] = (byte) readByte();
                byte[] payload = new byte[(int) len];
                int read = 0;
                while (read < payload.length) {
                    int r = in.read(payload, read, payload.length - read);
                    if (r == -1) { open = false; break; }
                    read += r;
                }
                if (masked) for (int i = 0; i < payload.length; i++) payload[i] ^= maskKey[i % 4];

                if (opcode == 0x8) { open = false; break; }      // close
                if (opcode == 0x9) { continue; }                  // ping (ignore; server rarely pings client)
                if (opcode == 0x1 || opcode == 0x0) {              // text / continuation
                    onMessage(new String(payload, "UTF-8"));
                }
            }
        } catch (Throwable t) {
            // socket closed / error
        } finally {
            open = false;
        }
    }

    private int readByte() throws Exception {
        int c = in.read();
        if (c == -1) throw new java.io.EOFException();
        return c & 0xFF;
    }

    private void onMessage(String json) {
        try {
            JSONObject o = new JSONObject(json);
            if (o.has("id")) {
                int id = o.getInt("id");
                results.put(id, o);
                CountDownLatch l = latches.get(id);
                if (l != null) l.countDown();
            }
        } catch (Throwable ignored) {}
    }

    /** Send a CDP method and wait up to {@code timeoutMs} for its response. */
    private JSONObject call(String method, JSONObject params, long timeoutMs) {
        if (!open) return null;
        int id = nextId.getAndIncrement();
        CountDownLatch latch = new CountDownLatch(1);
        latches.put(id, latch);
        try {
            JSONObject msg = new JSONObject().put("id", id).put("method", method);
            if (params != null) msg.put("params", params);
            sendText(msg.toString());
            latch.await(timeoutMs, TimeUnit.MILLISECONDS);
            return results.remove(id);
        } catch (Throwable t) {
            return null;
        } finally {
            latches.remove(id);
        }
    }

    // ---- High-level helpers ----

    /** Runtime.evaluate; returns result.result.value as a String (or null). */
    public String evaluate(String js) {
        try {
            JSONObject p = new JSONObject().put("expression", js).put("returnByValue", true);
            JSONObject resp = call("Runtime.evaluate", p, 8000);
            if (resp == null) return null;
            JSONObject r = resp.optJSONObject("result");
            JSONObject inner = r != null ? r.optJSONObject("result") : null;
            if (inner == null) return null;
            if (inner.isNull("value")) return null;
            return inner.optString("value", null);
        } catch (Throwable t) {
            return null;
        }
    }

    /** Trusted mouse click at viewport coords. */
    public void mouseClick(double x, double y) {
        dispatchMouse("mousePressed", x, y);
        sleep(40 + rnd.nextInt(60));
        dispatchMouse("mouseReleased", x, y);
    }

    private void dispatchMouse(String type, double x, double y) {
        try {
            JSONObject p = new JSONObject().put("type", type).put("x", x).put("y", y)
                    .put("button", "left").put("clickCount", 1);
            call("Input.dispatchMouseEvent", p, 4000);
        } catch (Throwable ignored) {}
    }

    /** Trusted mouse move (behavioral signal). */
    public void mouseMove(double x, double y) {
        try {
            call("Input.dispatchMouseEvent", new JSONObject().put("type", "mouseMoved")
                    .put("x", x).put("y", y), 3000);
        } catch (Throwable ignored) {}
    }

    /** Trusted scroll via mouse wheel. */
    public void scroll(double x, double y, double deltaY) {
        try {
            call("Input.dispatchMouseEvent", new JSONObject().put("type", "mouseWheel")
                    .put("x", x).put("y", y).put("deltaX", 0).put("deltaY", deltaY), 3000);
        } catch (Throwable ignored) {}
    }

    /** Navigate the page (CDP Page.navigate). */
    public void navigate(String url) {
        try {
            call("Page.navigate", new JSONObject().put("url", url), 6000);
        } catch (Throwable ignored) {}
    }

    /** Enable the Page domain (required before some Page.* operations). */
    public boolean pageEnable() {
        return call("Page.enable", null, 4000) != null;
    }

    /**
     * Capture a JPEG screenshot of the attached page via CDP {@code Page.captureScreenshot}.
     * Returns the decoded JPEG bytes, or {@code null} on failure.
     */
    public byte[] captureScreenshot() {
        try {
            JSONObject p = new JSONObject().put("format", "jpeg").put("quality", 75);
            JSONObject resp = call("Page.captureScreenshot", p, 15000);
            if (resp == null) return null;
            JSONObject result = resp.optJSONObject("result");
            String data = result != null ? result.optString("data", null) : null;
            if (data == null || data.isEmpty()) return null;
            return android.util.Base64.decode(data, android.util.Base64.DEFAULT);
        } catch (Throwable t) {
            return null;
        }
    }

    /** Trusted typing: per-char keyDown(text)+keyUp with human-like jitter. */
    public void typeText(String text) {
        for (int i = 0; i < text.length(); i++) {
            String ch = String.valueOf(text.charAt(i));
            try {
                call("Input.dispatchKeyEvent", new JSONObject().put("type", "keyDown")
                        .put("text", ch).put("key", ch).put("unmodifiedText", ch), 3000);
                call("Input.dispatchKeyEvent", new JSONObject().put("type", "keyUp")
                        .put("key", ch), 3000);
            } catch (Throwable ignored) {}
            sleep(55 + rnd.nextInt(110));
        }
    }

    private static void sleep(long ms) {
        try { Thread.sleep(ms); } catch (InterruptedException ignored) {}
    }

    public void close() {
        open = false;
        try { if (reader != null) reader.interrupt(); } catch (Throwable ignored) {}
        try { if (socket != null) socket.close(); } catch (Throwable ignored) {}
        socket = null; in = null; out = null;
    }
}
