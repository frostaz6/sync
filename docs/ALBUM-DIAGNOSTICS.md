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

## 2026-10-30: HTTP 403 diagnosis

An on-device report identified the failing endpoint as
`https://oauth.reddit.com/comments/1xjajlb.json?limit=1` (403 AuthFailureError).
Redlib's October 20 investigation found the Reddit edge rejecting `/comments/`
while still accepting `/by_id/` on the same bearer and network connection
(redlib-org/redlib issue #591). Instead of retrieving comments just to get
`gallery_data`, the viewer now requests
`https://oauth.reddit.com/by_id/t3_<id>.json?raw_json=1`.
That endpoint returns a standard post Listing, parsed by the already-supported
JSONObject branch. The on-screen diagnostic dialog and Logcat tracing remain.
The new path is not yet proven against the user's exact session; test it in-app.

## 2026-11-05: /by_id route 403 again

ReSync reported a 403 on `/by_id/t3_1xoxzsn.json?raw_json=1`.
The gallery still exists and contains normal Reddit `gallery_data` and
`media_metadata`, so this is a transport/auth route failure rather than a
gallery parser bug. Reddit's October 30 route restrictions also included
`/by_id` (see r/BoostForReddit Patcheddit 2.0.1 announcement).

Experimental fix: ask `/duplicates/<id>.json?raw_json=1&limit=1` for the
original post listing, which is the first element of its two-listing response.
The gallery parser already handles an array/listing root. On HTTP 403 or 404,
retry the previous `/by_id` lookup once for sessions where it remains
accessible. The diagnostics v2 dialog displays both endpoints, omitting auth
headers and cookies. This is unverified on the user's device pending an APK
test, and it is not a confirmed permanent API route.

## 2026-11-05: follow-up — comments not loading in album test build

On the successful experimental album build, opening comments failed because
`OAuthCommentsRequest` still fetched the post from the blocked `/by_id/`
endpoint before its existing `GraphQLCommentsRequest` could run.

The comments post lookup now follows the same route choice as the working
gallery fetch: `/duplicates/<id>.json?raw_json=1&limit=1` first, parsing
the original post Listing from the first array entry. It validates the post
ID, then runs the unchanged Reddit `PostComments` GraphQL request and legacy
comment ingestion. On HTTP 403 or 404 at the first lookup it tries `/by_id`
once for compatibility. GraphQL failures remain separately logged under
`ReSyncComments`. This change needs on-device testing before being merged.
