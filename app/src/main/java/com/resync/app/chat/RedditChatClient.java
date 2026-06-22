package com.resync.app.chat;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Thin Matrix Client-Server API client for <b>Reddit Chat</b>.
 *
 * <p>Reddit Chat is standard Matrix on homeserver {@code https://matrix.redditspace.com} (spec
 * v1.2). Crucially:
 * <ul>
 *   <li><b>Auth is the reddit web-session JWT, used directly.</b> The homeserver's only login flow
 *       is {@code com.reddit.token}; POSTing {@code {"type":"com.reddit.token","token":<reddit JWT>}}
 *       to {@code /login} returns a normal Matrix {@code access_token}. So we reuse the exact bearer
 *       {@code WebSessionManager} already mints — no separate token exchange.</li>
 *   <li><b>Rooms are unencrypted</b> ({@code io.element.e2ee.default=false}), so this is just
 *       authenticated REST + a sync long-poll — no Olm/Megolm, no Matrix SDK needed.</li>
 * </ul>
 *
 * <p>This class is stateless apart from the injected {@link OkHttpClient}; the caller holds the
 * {@link Session}. All methods are blocking and must be called off the main thread
 * (see {@link RedditChatManager}).
 */
public final class RedditChatClient {

    public static final String HOMESERVER = "https://matrix.redditspace.com";
    private static final String BASE = HOMESERVER + "/_matrix/client/v3";
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final OkHttpClient http;

    public RedditChatClient(OkHttpClient http) {
        this.http = http;
    }

    /** A logged-in Matrix session. */
    public static final class Session {
        public final String accessToken;
        public final String userId;
        public final String deviceId;

        public Session(String accessToken, String userId, String deviceId) {
            this.accessToken = accessToken;
            this.userId = userId;
            this.deviceId = deviceId;
        }
    }

    /** Result of a {@code /sync}: the rooms (newest activity first) and the next pagination token. */
    public static final class SyncResult {
        public final List<ChatRoom> rooms;
        public final String nextBatch;

        SyncResult(List<ChatRoom> rooms, String nextBatch) {
            this.rooms = rooms;
            this.nextBatch = nextBatch;
        }
    }

    /**
     * {@code POST /login} with the reddit JWT as a {@code com.reddit.token}. Returns the Matrix
     * session. Throws on any non-200 / missing token.
     */
    public Session login(String redditJwt) throws IOException, JSONException {
        JSONObject body = new JSONObject()
                .put("type", "com.reddit.token")
                .put("token", redditJwt);
        Request req = new Request.Builder()
                .url(BASE + "/login")
                .post(RequestBody.create(body.toString(), JSON))
                .build();
        try (Response r = http.newCall(req).execute()) {
            String s = r.body() != null ? r.body().string() : "";
            JSONObject j = new JSONObject(s.isEmpty() ? "{}" : s);
            String token = j.optString("access_token", null);
            if (!r.isSuccessful() || token == null) {
                throw new IOException("Matrix login failed (" + r.code() + "): "
                        + j.optString("error", s));
            }
            return new Session(token, j.optString("user_id", null), j.optString("device_id", null));
        }
    }

    /**
     * {@code GET /sync}. With {@code since == null} this is the initial sync (full room list);
     * pass the previous {@link SyncResult#nextBatch} and a long {@code timeoutMs} to long-poll for
     * updates. Rooms are returned sorted by most-recent activity.
     */
    public SyncResult sync(Session session, String since, int timeoutMs)
            throws IOException, JSONException {
        String filter = URLEncoder.encode("{\"room\":{\"timeline\":{\"limit\":20}}}", "UTF-8");
        StringBuilder url = new StringBuilder(BASE)
                .append("/sync?timeout=").append(timeoutMs)
                .append("&filter=").append(filter);
        if (since != null) {
            url.append("&since=").append(URLEncoder.encode(since, "UTF-8"));
        }
        JSONObject root = getJson(session, url.toString());
        String nextBatch = root.optString("next_batch", null);

        List<ChatRoom> rooms = new ArrayList<>();
        JSONObject join = root.optJSONObject("rooms");
        join = join != null ? join.optJSONObject("join") : null;
        if (join != null) {
            for (java.util.Iterator<String> it = join.keys(); it.hasNext(); ) {
                String roomId = it.next();
                JSONObject room = join.optJSONObject(roomId);
                if (room == null) continue;
                rooms.add(parseRoomSummary(roomId, room, session.userId));
            }
        }
        Collections.sort(rooms, new Comparator<ChatRoom>() {
            @Override public int compare(ChatRoom a, ChatRoom b) {
                return Long.compare(b.lastTimestampMs, a.lastTimestampMs);
            }
        });
        return new SyncResult(rooms, nextBatch);
    }

    /** Builds a {@link ChatRoom} summary (name + last message) from a /sync joined-room object. */
    private ChatRoom parseRoomSummary(String roomId, JSONObject room, String myUserId)
            throws JSONException {
        Map<String, String> names = new HashMap<>();
        String roomName = null;

        JSONObject state = room.optJSONObject("state");
        JSONArray stateEvents = state != null ? state.optJSONArray("events") : null;
        if (stateEvents != null) {
            for (int i = 0; i < stateEvents.length(); i++) {
                JSONObject ev = stateEvents.optJSONObject(i);
                if (ev == null) continue;
                String type = ev.optString("type");
                JSONObject content = ev.optJSONObject("content");
                if ("m.room.name".equals(type) && content != null) {
                    roomName = content.optString("name", null);
                } else if ("m.room.member".equals(type) && content != null) {
                    String dn = content.optString("displayname", null);
                    String who = ev.optString("state_key", null);
                    if (who != null && dn != null && !dn.isEmpty()) names.put(who, dn);
                }
            }
        }

        // Last message + its timestamp from the timeline.
        String lastBody = null;
        long lastTs = 0L;
        JSONObject timeline = room.optJSONObject("timeline");
        JSONArray tlEvents = timeline != null ? timeline.optJSONArray("events") : null;
        if (tlEvents != null) {
            for (int i = tlEvents.length() - 1; i >= 0; i--) {
                JSONObject ev = tlEvents.optJSONObject(i);
                if (ev == null) continue;
                // capture latest member names from the timeline too
                if ("m.room.member".equals(ev.optString("type"))) {
                    JSONObject c = ev.optJSONObject("content");
                    String dn = c != null ? c.optString("displayname", null) : null;
                    String who = ev.optString("state_key", null);
                    if (who != null && dn != null && !dn.isEmpty() && !names.containsKey(who)) {
                        names.put(who, dn);
                    }
                }
                if (lastBody == null && "m.room.message".equals(ev.optString("type"))) {
                    JSONObject c = ev.optJSONObject("content");
                    lastBody = c != null ? c.optString("body", "") : "";
                    lastTs = ev.optLong("origin_server_ts", 0L);
                }
            }
        }

        String displayName = roomName;
        if (displayName == null || displayName.isEmpty()) {
            // DM: name it after the other participant.
            displayName = otherMemberName(names, myUserId);
        }
        if (displayName == null || displayName.isEmpty()) displayName = "Chat";
        return new ChatRoom(roomId, displayName, lastBody, lastTs);
    }

    private static String otherMemberName(Map<String, String> names, String myUserId) {
        for (Map.Entry<String, String> e : names.entrySet()) {
            if (myUserId == null || !myUserId.equals(e.getKey())) return e.getValue();
        }
        return null;
    }

    /**
     * {@code GET /rooms/{id}/messages?dir=b} — recent messages, oldest-first in the returned list.
     * Resolves sender display names from the member events in the same response.
     */
    public List<ChatMessage> messages(Session session, String roomId, int limit)
            throws IOException, JSONException {
        String url = BASE + "/rooms/" + enc(roomId) + "/messages?dir=b&limit=" + limit;
        JSONObject root = getJson(session, url);

        Map<String, String> names = new HashMap<>();
        JSONArray stateArr = root.optJSONArray("state");
        if (stateArr != null) collectMemberNames(stateArr, names);

        JSONArray chunk = root.optJSONArray("chunk");
        List<ChatMessage> out = new ArrayList<>();
        if (chunk != null) {
            collectMemberNames(chunk, names); // member events may also appear in the chunk
            for (int i = 0; i < chunk.length(); i++) {
                ChatMessage m = parseMessage(chunk.optJSONObject(i), names, session.userId);
                if (m != null) out.add(m);
            }
        }
        // dir=b returns newest-first; reverse to chronological for display.
        Collections.reverse(out);
        return out;
    }

    private static void collectMemberNames(JSONArray events, Map<String, String> names) {
        for (int i = 0; i < events.length(); i++) {
            JSONObject ev = events.optJSONObject(i);
            if (ev == null || !"m.room.member".equals(ev.optString("type"))) continue;
            JSONObject c = ev.optJSONObject("content");
            String dn = c != null ? c.optString("displayname", null) : null;
            String who = ev.optString("state_key", null);
            if (who != null && dn != null && !dn.isEmpty()) names.put(who, dn);
        }
    }

    private static ChatMessage parseMessage(JSONObject ev, Map<String, String> names, String myUserId) {
        if (ev == null || !"m.room.message".equals(ev.optString("type"))) return null;
        JSONObject content = ev.optJSONObject("content");
        if (content == null) return null;
        String sender = ev.optString("sender", "");
        String name = names.get(sender);
        if (name == null || name.isEmpty()) name = localpart(sender);
        return new ChatMessage(
                ev.optString("event_id", ""),
                sender,
                name,
                content.optString("body", ""),
                ev.optLong("origin_server_ts", 0L),
                myUserId != null && myUserId.equals(sender));
    }

    /** {@code @t2_abc:reddit.com} -> {@code t2_abc}; best-effort readable fallback. */
    private static String localpart(String userId) {
        if (userId == null) return "";
        String s = userId.startsWith("@") ? userId.substring(1) : userId;
        int colon = s.indexOf(':');
        return colon > 0 ? s.substring(0, colon) : s;
    }

    /**
     * {@code PUT /rooms/{id}/send/m.room.message/{txnId}} with an {@code m.text} body. Returns the
     * new event id. {@code txnId} must be unique per send (reusing one dedupes retries).
     */
    public String send(Session session, String roomId, String text, String txnId)
            throws IOException, JSONException {
        JSONObject body = new JSONObject().put("msgtype", "m.text").put("body", text);
        Request req = authBuilder(session,
                BASE + "/rooms/" + enc(roomId) + "/send/m.room.message/" + enc(txnId))
                .put(RequestBody.create(body.toString(), JSON))
                .build();
        try (Response r = http.newCall(req).execute()) {
            String s = r.body() != null ? r.body().string() : "";
            JSONObject j = new JSONObject(s.isEmpty() ? "{}" : s);
            if (!r.isSuccessful()) {
                throw new IOException("Matrix send failed (" + r.code() + "): "
                        + j.optString("error", s));
            }
            return j.optString("event_id", null);
        }
    }

    // ---- helpers ----

    private JSONObject getJson(Session session, String url) throws IOException, JSONException {
        try (Response r = http.newCall(authBuilder(session, url).get().build()).execute()) {
            String s = r.body() != null ? r.body().string() : "";
            JSONObject j = new JSONObject(s.isEmpty() ? "{}" : s);
            if (!r.isSuccessful()) {
                throw new IOException("Matrix GET failed (" + r.code() + "): "
                        + j.optString("error", s));
            }
            return j;
        }
    }

    private static Request.Builder authBuilder(Session session, String url) {
        return new Request.Builder().url(url)
                .header("Authorization", "Bearer " + session.accessToken);
    }

    private static String enc(String s) {
        try {
            return URLEncoder.encode(s, "UTF-8");
        } catch (Exception e) {
            return s;
        }
    }
}
