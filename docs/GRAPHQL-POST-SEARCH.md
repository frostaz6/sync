# Post search via GraphQL (Oct 2026)

ReSync legacy post searches returned 403 HTML from
oauth.reddit.com/r/<sub>/search.json while ordinary feeds and
GraphQL comments continued to work.

Post searches now use Reddit Android's SearchPosts GraphQL persisted
operation (hash 8610e335...9860e4bf9), with website-session bearer
authentication already used by ReSync GraphQL comments. The query
includes the subreddit restriction as "subreddit:<name>" and preserves
relevance/hot/top/new/comments sorts, supported time ranges, NSFW
safe-search preference, and opaque cursor pagination.

GraphQLPostSearch adapts the response to a legacy Listing with t3
posts and after cursor, feeding the current OAuthPostsRequest
PostParserHelper / provider / filters / feed display. Includes basic
text, link, images, gallery and video metadata.

Community-name discovery (a different endpoint) and non-search feeds
are not changed. The diagnostic popup remains in this test build for
failed searches; it will capture GraphQL errors if this operation
is rejected on a particular account.

Reference: https://github.com/Amqx/reddit-gql
Test: search nomad in r/EmulationOnAndroid, global search, sorting,
time filter, pagination, image and gallery posts, both accounts.
Check the returned post list, thumbnails and opening comment threads.
If the GraphQL query is no longer accepted by Reddit, the diagnostic
dialog will identify that rather than silently returning empty posts.
This commit does not modify the published v1.0.0 release.
