package com.resync.app.auth;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.text.TextUtils;
import android.util.Base64;
import android.webkit.CookieManager;

import com.resync.app.provider.RedditProvider;
import com.resync.app.singleton.AccountSingleton;

import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

/**
 * Keeps Reddit WebView cookie sessions separate for each ReSync account.
 *
 * WebView CookieManager is application-wide. The login flow must save the active
 * session before clearing that jar to let another Reddit account sign in.
 * The session cookie is a credential; store it ONLY as AndroidKeyStore AES-GCM
 * ciphertext, never in provider rows, logs, or plain preferences.
 */
public final class WebSessionAccountStore {
    private static final String REDDIT_URL = "https://www.reddit.com/";
    private static final String PREFS = "resync_secure_reddit_sessions";
    private static final String KEY_ALIAS = "resync.reddit.session.aes.v1";
    private static final String STORE_TYPE = "AndroidKeyStore";
    private static final String WEB_SESSION = "web_session";

    public interface Callback {
        void onComplete(boolean ok);
    }

    private WebSessionAccountStore() {}

    private static String keyFor(String username) {
        return "account_" + username.toLowerCase(Locale.ROOT);
    }

    private static boolean isRealAccount(String username) {
        return !TextUtils.isEmpty(username)
                && !"Logged out".equalsIgnoreCase(username)
                && !"Reddit user".equalsIgnoreCase(username);
    }

    private static boolean containsSession(String cookies) {
        if (TextUtils.isEmpty(cookies)) return false;
        for (String cookie : cookies.split(";")) {
            if (cookie.trim().startsWith("reddit_session=")) return true;
        }
        return false;
    }

    private static SecretKey getKey() throws Exception {
        KeyStore store = KeyStore.getInstance(STORE_TYPE);
        store.load(null);
        KeyStore.Entry entry = store.getEntry(KEY_ALIAS, null);
        if (entry instanceof KeyStore.SecretKeyEntry) {
            return ((KeyStore.SecretKeyEntry) entry).getSecretKey();
        }
        KeyGenerator generator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, STORE_TYPE);
        generator.init(new KeyGenParameterSpec.Builder(KEY_ALIAS,
                KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
                .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
                .setKeySize(256).build());
        return generator.generateKey();
    }

    private static String encrypt(String plaintext) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, getKey());
        byte[] ciphertext = cipher.doFinal(plaintext.getBytes(StandardCharsets.UTF_8));
        return Base64.encodeToString(cipher.getIV(), Base64.NO_WRAP)
                + "." + Base64.encodeToString(ciphertext, Base64.NO_WRAP);
    }

    private static String decrypt(String sealed) throws Exception {
        String[] parts = sealed.split("\\.", 2);
        if (parts.length != 2) throw new IllegalArgumentException("Invalid session record");
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, getKey(),
                new GCMParameterSpec(128, Base64.decode(parts[0], Base64.NO_WRAP)));
        return new String(cipher.doFinal(Base64.decode(parts[1], Base64.NO_WRAP)),
                StandardCharsets.UTF_8);
    }

    /** Save the CURRENT WebView Reddit account under its verified username. */
    public static boolean save(Context context, String username) {
        if (!isRealAccount(username)) return false;
        String cookies = CookieManager.getInstance().getCookie(REDDIT_URL);
        if (!containsSession(cookies)) return false;
        try {
            context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                    .edit().putString(keyFor(username), encrypt(cookies)).apply();
            return true;
        } catch (Exception e) {
            android.util.Log.e("ReSyncAccounts", "Failed to protect account session", e);
            return false;
        }
    }

    /** Avoid putting session cookies into unencrypted ContentProvider account rows. */
    public static boolean ensureAccountRow(Context context, String username) {
        if (!isRealAccount(username)) return false;
        Cursor cursor = null;
        try {
            cursor = context.getContentResolver().query(RedditProvider.URI_ACCOUNTS,
                    new String[]{"account_name"}, null, null, null);
            if (cursor != null) {
                int nameIndex = cursor.getColumnIndex("account_name");
                while (cursor.moveToNext()) {
                    if (nameIndex >= 0 && username.equalsIgnoreCase(cursor.getString(nameIndex))) {
                        return true;
                    }
                }
            }
            ContentValues row = new ContentValues();
            row.put("account_name", username);
            row.put("account_refresh_token", WEB_SESSION); // sentinel, NOT a cookie/token
            context.getContentResolver().insert(RedditProvider.URI_ACCOUNTS, row);
            return true;
        } catch (Exception e) {
            android.util.Log.e("ReSyncAccounts", "Failed to save account entry", e);
            return false;
        } finally {
            if (cursor != null) cursor.close();
        }
    }

    /** Clear the shared in-app WebView jar before showing Reddit's login UI. */
    public static void clearForNewLogin(Context context, Callback callback) {
        WebSessionManager.get(context).invalidate();
        CookieManager manager = CookieManager.getInstance();
        manager.removeAllCookies(value -> {
            manager.flush();
            WebSessionManager.get(context).invalidate();
            callback.onComplete(!containsSession(manager.getCookie(REDDIT_URL)));
        });
    }

    /** Restore a saved account. Callback runs on the WebView/UI thread. */
    public static void restore(Context context, String username, Callback callback) {
        if (!isRealAccount(username)) {
            callback.onComplete(false);
            return;
        }
        String cookies;
        try {
            String sealed = context.getApplicationContext().getSharedPreferences(
                    PREFS, Context.MODE_PRIVATE).getString(keyFor(username), null);
            if (sealed == null) {
                callback.onComplete(false);
                return;
            }
            cookies = decrypt(sealed);
            if (!containsSession(cookies)) {
                callback.onComplete(false);
                return;
            }
        } catch (Exception e) {
            android.util.Log.e("ReSyncAccounts", "Could not decrypt saved account", e);
            callback.onComplete(false);
            return;
        }

        WebSessionManager.get(context).invalidate();
        CookieManager manager = CookieManager.getInstance();
        final String[] parts = cookies.split(";");
        manager.removeAllCookies(ignored -> {
            manager.flush();
            AtomicInteger remaining = new AtomicInteger(0);
            AtomicBoolean failed = new AtomicBoolean(false);
            for (String part : parts) {
                String cookie = part.trim();
                int equals = cookie.indexOf('=');
                if (equals <= 0) continue;
                String name = cookie.substring(0, equals);
                // A __Host- cookie cannot carry Domain. Ordinary Reddit cookies
                // are restored for reddit.com and its subdomains.
                String attrs = name.startsWith("__Host-")
                        ? "; Path=/; Secure"
                        : "; Domain=.reddit.com; Path=/; Secure";
                remaining.incrementAndGet();
                manager.setCookie(REDDIT_URL, cookie + attrs, accepted -> {
                    if (!Boolean.TRUE.equals(accepted)) failed.set(true);
                    if (remaining.decrementAndGet() == 0) {
                        manager.flush();
                        WebSessionManager.get(context).invalidate();
                        callback.onComplete(!failed.get()
                                && containsSession(manager.getCookie(REDDIT_URL)));
                    }
                });
            }
            if (remaining.get() == 0) {
                callback.onComplete(false);
            }
        });
    }

    /** Save the outgoing account before swapping to a different cookie session. */
    public static void switchTo(Context context, String username, Callback callback) {
        String active = AccountSingleton.getInstance().getUsername();
        if (isRealAccount(active) && !active.equalsIgnoreCase(username)) {
            // Existing WebView cookies may have been refreshed since last switch.
            save(context, active);
        }
        restore(context, username, callback);
    }

    public static void remove(Context context, String username) {
        if (!TextUtils.isEmpty(username)) {
            context.getApplicationContext().getSharedPreferences(PREFS, Context.MODE_PRIVATE)
                    .edit().remove(keyFor(username)).apply();
        }
    }
}
