# Subreddit New feed: 403 recovery

The legacy OAuth GET /r/CFB/new.json is returning a 403 with ReSync's
website-session bearer. This is separate from the post-search endpoint
already migrated to Reddit GraphQL.

Ordinary single-community feeds sorted NEW now use Reddit Android's
persisted SubredditFeedElements operation via gql-fed.reddit.com.
Hash: 97046e2a48050f10a507db354a8da60955ffccb586dcf63163bdfc6610d5cf7a
Variables: subredditName, sort NEW, pageSize 25,
includeSubredditInPosts true, optional after cursor.

The response's postFeed.elements.edges is converted to a legacy t3 Listing.
It reuses GraphQLPostSearch's post adapter plus the existing Sync post
parser, filters, database and UI. Paging uses pageInfo.endCursor.

Scoped narrowly to sort New for plain subreddits to avoid regressing
currently working home, All, Popular, search, custom feeds and other sorts.
The copied-error diagnostics already installed for search now also apply
to this New feed, so GraphQL HTTP rejections are reportable.

Test: r/CFB New, r/EmulationOnAndroid New, scrolling to page two,
refresh, switch between Hot and New, open a post and its comments, and
test both accounts. On-device testing is required; this is a dev build,
not a replacement for the published v1.0.1 APK.

Reference: Amqx/reddit-gql src/client.ts and src/operations.ts.
