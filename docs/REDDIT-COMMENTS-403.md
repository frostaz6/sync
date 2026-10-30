# Reddit 403 comments workaround

In late October 2026, Reddit started blocking the normal
\`oauth.reddit.com/r/<sub>/comments/<post>.json\` endpoints with HTTP 403
while feeds, galleries via \`/by_id/\`, and many other OAuth requests still
returned normally. This is the same path-specific restriction observed by
Redlib in [issue #591](https://github.com/redlib-org/redlib/issues/591).

\`OAuthCommentsRequest.buildUrl\` now encodes the single path separator
between a subreddit and the comments route as \`%2F\`. A request like
\`/r/CFB/comments/POST.json\` becomes \`/r/CFB%2Fcomments/POST.json\`.
For requests without a subreddit, \`/comments/POST.json\` becomes
\`/comments%2FPOST.json\`. All other query parameters and the
existing CommentParserHelper stay as before.

The upstream workaround was demonstrated with a 200 response and the
expected two-part JSON. Whether it works for every session remains an
on-device test, and Reddit may eventually normalize or block these paths.
The request URL and HTTP status are emitted to Logcat under
\`ReSyncComments\`, without dumping bearer tokens or response data.

## Test cases

1. Open a normal Reddit post; comments should load beneath the post.
2. Open posts in different subreddits.
3. Switch comment sort modes (best, new, top).
4. Open a direct comment permalink or a link without a subreddit.
5. Switch between signed-in accounts and retry.
6. Try a deep thread's **load more** button. This is a distinct request to
   \`/api/morechildren\`, which Reddit has also blocked and is not repaired
   by this first-stage comments route fix.

The previously working gallery, multi-account, favorites, and grid changes
are not touched. Release signing still requires the setup on the owner's
Linux computer; Actions test APKs use temporary signing until then.
