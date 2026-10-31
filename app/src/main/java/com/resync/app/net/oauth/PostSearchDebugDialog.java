package com.resync.app.net.oauth;

import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;

import com.android.volley.NetworkResponse;
import com.android.volley.VolleyError;
import com.resync.app.RedditApplication;
import com.resync.app.helpers.ContextHelper;
import com.resync.app.helpers.ui.AlertDialogHelper;

import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;

/**
 * Temporary ReSync diagnostics for legacy post-search errors.
 * Never includes request headers, bearer JWTs, login cookies or raw session info.
 */
final class PostSearchDebugDialog {
    private static java.lang.ref.WeakReference<AlertDialog> activeDialog;

    private PostSearchDebugDialog() {}

    static String format(String url, VolleyError error) {
        StringBuilder text = new StringBuilder();
        text.append("ReSync post-search diagnostics\n");
        text.append("Request: GET ").append(redact(url)).append("\n");
        text.append("Auth method: Reddit website-session bearer (value omitted)\n");
        text.append("Error class: ")
                .append(error == null ? "null" : error.getClass().getName()).append("\n");
        NetworkResponse response = error == null ? null : error.networkResponse;
        if (response == null) {
            text.append("HTTP status: unavailable (no network response)\n");
            if (error != null && error.getCause() != null) {
                text.append("Cause: ").append(error.getCause().getClass().getSimpleName()).append("\n");
            }
            text.append("Response body: unavailable\n");
            return text.toString();
        }

        text.append("HTTP status: ").append(response.statusCode).append("\n");
        text.append("Network time: ").append(response.networkTimeMs).append(" ms\n");
        text.append("Response headers (safe subset):\n");
        boolean found = false;
        if (response.headers != null) {
            for (Map.Entry<String, String> entry : response.headers.entrySet()) {
                String key = entry.getKey();
                if (key == null) continue;
                String lower = key.toLowerCase(Locale.US);
                if (lower.equals("content-type") || lower.equals("retry-after")
                        || lower.equals("x-ratelimit-remaining")
                        || lower.equals("x-ratelimit-used")
                        || lower.equals("x-ratelimit-reset")
                        || lower.equals("www-authenticate") || lower.equals("server")
                        || lower.equals("via") || lower.equals("x-cache")
                        || lower.equals("x-served-by")) {
                    text.append("  ").append(key).append(": ")
                            .append(redact(trim(entry.getValue(), 400))).append("\n");
                    found = true;
                }
            }
        }
        if (!found) text.append("  (none provided)\n");
        text.append("Response body (first 6000 chars, sensitive values redacted):\n");
        if (response.data == null || response.data.length == 0) {
            text.append("(empty)\n");
        } else {
            String body = new String(response.data, StandardCharsets.UTF_8);
            text.append(redact(trim(body, 6000))).append("\n");
            if (body.length() > 6000) text.append("[truncated]\n");
        }
        return text.toString();
    }

    private static String trim(String value, int maxChars) {
        if (value == null) return "";
        return value.length() <= maxChars ? value : value.substring(0, maxChars);
    }

    private static String redact(String text) {
        if (text == null) return "";
        return text
                .replaceAll("(?i)([?&](?:access_token|refresh_token|token|code|state|secret|password|key|session)=)[^&\\s]*", "$1[REDACTED]")
                .replaceAll("(?i)(Bearer\\s+)[A-Za-z0-9._~+/-]+", "$1[REDACTED]")
                .replaceAll("(?i)((?:reddit_session|access_token|refresh_token|client_secret|api_key|authorization|set-cookie)\\s*[:=]\\s*)[^\\s,;<>]+", "$1[REDACTED]");
    }

    static void show(Context requestContext, String text) {
        Activity activity = ContextHelper.getBaseActivity(requestContext);
        if (activity == null) activity = RedditApplication.getCurrentActivity();
        if (activity == null) return;
        final Activity ui = activity;
        ui.runOnUiThread(() -> {
            if (ui.isFinishing() || ui.isDestroyed()) return;
            AlertDialog current = activeDialog == null ? null : activeDialog.get();
            if (current != null && current.isShowing()) return;
            AlertDialog dialog = AlertDialogHelper.newBuilder(ui)
                    .setTitle("Post search error — copy diagnostics")
                    .setMessage(text)
                    .setPositiveButton("Copy", (d, which) -> {
                        ClipboardManager clipboard = (ClipboardManager)
                                ui.getSystemService(Context.CLIPBOARD_SERVICE);
                        if (clipboard != null) {
                            clipboard.setPrimaryClip(ClipData.newPlainText(
                                    "ReSync search diagnostic", text));
                            Toast.makeText(ui, "Search diagnostics copied", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Close", (d, which) -> d.dismiss())
                    .create();
            activeDialog = new java.lang.ref.WeakReference<>(dialog);
            dialog.show();
        });
    }
}
