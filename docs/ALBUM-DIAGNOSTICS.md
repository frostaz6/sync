# Album loading diagnostics

The original album viewer threw away Volley errors and showed only
"Error loading album". Logger.debug()/Logger.verbose() are no-ops.

This build logs gallery requests, HTTP status, response size, parsing stage
and count of usable media entries to Android Logcat under "ReSyncGallery".
It also displays an on-device dialog with a "Copy details" button.

## Test from the phone

1. Open a Reddit image album in ReSync.
2. When the error dialog appears, tap **Copy details**.
3. Paste the text back into the debugging conversation. This includes the
   public post endpoint, HTTP status (when available), error type, parse
   stage and exception. It does not include tokens, cookies, headers,
   or the Reddit response body.

For additional details with USB debugging, run:

    adb logcat -c
    adb logcat -v time -s ReSyncGallery:V AndroidRuntime:E

Then reproduce the failure and save the relevant output. These diagnostic
requests skip the old HTTP cache, so results come from fresh responses.

## Install safety

Android requires the same package name, signing certificate and an increasing
versionCode for in-place upgrades. The Actions workflow currently falls back
to disposable signing keys unless RESYNC_KEYSTORE_B64 and
RESYNC_KEYSTORE_PASSWORD repository secrets are set. Its temporary-debug
artifacts cannot replace other temporary-debug APKs, even when the build
succeeds. Before uninstalling, export app configuration where possible.
Never commit keystores or signing passwords to this public repo.
