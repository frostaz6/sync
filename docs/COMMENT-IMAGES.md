# Native images inside GraphQL comments

Reddit's PostComments GraphQL payload gives comment Markdown separately from
content.richtextMedia. The Markdown contains IDs such as
\`![img](wfqz4qxpl6ch1)\` instead of a fetchable URL. Our initial GraphQL
adapter copied only Markdown; Sync treated the ID as an image src and nothing
displayed.

GraphQLCommentMedia now maps each richtextMedia ID to its HTTPS media URL and
rewrites those native image tags *before* the existing comment parser handles
the body. When media metadata is absent, known-safe native image IDs get
preview.redd.it URLs. GIPHY-style placeholders stay with Sync's existing
GIPHY path; other comment text and Markdown are unchanged.

The HTML image renderer now displays Reddit-native \`![img](https://...)\`
and \`![gif](https://...)\` from trusted Reddit image hosts at readable
inline preview size, instead of the legacy 42dp emote size. Clicking the
image opens its full source.

Test posts with a native uploaded comment image, multiple comment images,
a Reddit-hosted GIF, ordinary markdown image URLs, a GIPHY GIF, and a
nested comment. Check both accounts. This change requires an on-device test;
it doesn't update the published GitHub Release until a separate patch release
is explicitly prepared.

No login, signing, GraphQL query, gallery or account state code changed.
