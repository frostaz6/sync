package com.resync.app.chat;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.resync.app.auth.WebSessionManager;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Orchestrates Reddit Chat (Matrix) access for the UI: turns the existing reddit web-session bearer
 * into a Matrix session and exposes async room/message/send operations with main-thread callbacks.
 *
 * <p>All network work runs on a single background thread. The Matrix session is minted lazily on
 * first use ({@link #ensureSessionBlocking}) by reusing {@link WebSessionManager#getTokenBlocking}
 * and {@link RedditChatClient#login}. On an auth error the session is dropped and re-minted once.
 */
public final class RedditChatManager {

    private static final String TAG = "RedditChatManager";
    private static RedditChatManager instance;

    private final Context appContext;
    private final RedditChatClient client;
    private final ExecutorService io = Executors.newSingleThreadExecutor();
    private final Handler main = new Handler(Looper.getMainLooper());
    private final AtomicLong txn = new AtomicLong(1);

    private volatile RedditChatClient.Session session;

    public interface Callback<T> {
        void onResult(T value);
        void onError(String reason);
    }

    private RedditChatManager(Context context) {
        this.appContext = context.getApplicationContext();
        OkHttpClient http = new OkHttpClient.Builder()
                // /sync long-polls for up to ~30s, so allow a generous read timeout.
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)
                .build();
        this.client = new RedditChatClient(http);
    }

    public static synchronized RedditChatManager get(Context context) {
        if (instance == null) {
            instance = new RedditChatManager(context);
        }
        return instance;
    }

    /** Drop the Matrix session (e.g. on logout). The reddit bearer side is handled by WebSessionManager. */
    public void invalidate() {
        session = null;
    }

    /**
     * Ensures a valid Matrix session, minting one from the reddit web-session bearer if needed.
     * Blocking — call on the io thread only. Throws if there is no reddit session (logged out) or
     * login fails.
     */
    private RedditChatClient.Session ensureSessionBlocking() throws Exception {
        RedditChatClient.Session s = session;
        if (s != null) return s;
        String jwt = WebSessionManager.get(appContext).getTokenBlocking(15000);
        if (jwt == null || jwt.isEmpty()) {
            throw new IllegalStateException("Not signed in to reddit");
        }
        s = client.login(jwt);
        session = s;
        android.util.Log.i(TAG, "Matrix session established for " + s.userId);
        return s;
    }

    /** True if the error looks like an expired/invalid Matrix token, warranting a re-login. */
    private static boolean isAuthError(Throwable t) {
        String m = t.getMessage();
        return m != null && (m.contains("401") || m.contains("M_UNKNOWN_TOKEN")
                || m.contains("M_MISSING_TOKEN") || m.contains("403"));
    }

    /** Runs {@code work} on the io thread with one transparent re-login retry on auth failure. */
    private <T> void run(final Work<T> work, final Callback<T> cb) {
        io.execute(new Runnable() {
            @Override public void run() {
                try {
                    deliver(cb, work.run(ensureSessionBlocking()));
                } catch (Exception first) {
                    if (isAuthError(first)) {
                        session = null; // force re-mint
                        try {
                            deliver(cb, work.run(ensureSessionBlocking()));
                            return;
                        } catch (Exception ignored) {
                            // fall through to report the original
                        }
                    }
                    final String reason = first.getMessage() != null ? first.getMessage()
                            : first.toString();
                    android.util.Log.i(TAG, "Chat op failed: " + reason);
                    main.post(new Runnable() {
                        @Override public void run() { cb.onError(reason); }
                    });
                }
            }
        });
    }

    private <T> void deliver(final Callback<T> cb, final T value) {
        main.post(new Runnable() {
            @Override public void run() { cb.onResult(value); }
        });
    }

    private interface Work<T> {
        T run(RedditChatClient.Session session) throws Exception;
    }

    // ---- public API ----

    /** Loads the room/DM list (most recent first). */
    public void loadRooms(final Callback<List<ChatRoom>> cb) {
        run(new Work<List<ChatRoom>>() {
            @Override public List<ChatRoom> run(RedditChatClient.Session s) throws Exception {
                return client.sync(s, null, 0).rooms;
            }
        }, cb);
    }

    /** Loads recent messages for a room (chronological). */
    public void loadMessages(final String roomId, final Callback<List<ChatMessage>> cb) {
        run(new Work<List<ChatMessage>>() {
            @Override public List<ChatMessage> run(RedditChatClient.Session s) throws Exception {
                return client.messages(s, roomId, 40);
            }
        }, cb);
    }

    /** Sends a text message; returns the new event id. */
    public void send(final String roomId, final String text, final Callback<String> cb) {
        final String txnId = "sync-" + System.currentTimeMillis() + "-" + txn.getAndIncrement();
        run(new Work<String>() {
            @Override public String run(RedditChatClient.Session s) throws Exception {
                return client.send(s, roomId, text, txnId);
            }
        }, cb);
    }
}
