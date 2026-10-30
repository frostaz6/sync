# October 2026 Reddit comment loading fix

Reddit now returns 403 for /comments/ JSON endpoints, including the
encoded-slash workaround previously implemented in this fork. ReSync now:
1. Fetches the post Listing from /by_id/t3_<post>.json.
2. Posts Reddit's Android PostComments persisted GraphQL operation to
   https://gql-fed.reddit.com/ using the app's existing web-session bearer.
3. Converts its comment forest to legacy JSON and runs the existing Sync
   CommentParserHelper and PostParserHelper database/UI code.

The conversion retains raw comment markdown, parent/reply nesting, score,
distinguished/stickied status, and basic author flair. It handles focused
comment permalinks and best/new/top/old/controversial/QA sorts.

Limitations: the query hash is Reddit-controlled and could change; the first
request is capped at 200 comments and 10 levels. The legacy "load more"
action still uses a separately blocked endpoint, so GraphQL cursors aren't
exposed as nonfunctional legacy "more" buttons. Some rich-text/media
formatting may differ until RTJSON rendering is ported.

Sources: redlib-org/redlib#603 and polak14/redlib@a63460b. These
implementations have reported passing live tests. This ReSync adaptation
needs a phone test to verify compatibility with the user's Reddit session.

For device troubleshooting:
    adb logcat -v time -s ReSyncComments:V AndroidRuntime:E
No tokens, cookies or response bodies are logged.

The earlier working album, gallery grid, multi-account and favorites fixes
are unchanged.
