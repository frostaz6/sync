package com.resync.app.helpers;

/**
 * No-op stand-in for the former Firebase Crashlytics reporter. ReSync does not ship Crashlytics, so
 * these methods do nothing; the class is kept (with its original signatures) so the ~60 existing call
 * sites compile unchanged. Local diagnostics go through {@link com.resync.app.util.Logger}/android.util.Log.
 */
public final class CrashlyticsHelper {

    public static boolean isEnabled() {
        return false;
    }

    public static void logEvent(int eventType, String eventName, String message) {
        // no-op (Crashlytics removed)
    }

    public static void logException(Exception exc) {
        // no-op (Crashlytics removed)
    }
}
