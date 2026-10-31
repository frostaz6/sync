# Keep Reddit comment images at their native aspect ratio

ReSync's embedded-image support originally created an EmoteImageSpan with
a display height fixed to 3/4 of the available width. Its drawing code
stretches the bitmap to that rectangle, distorting portrait and square photos.

The GraphQL comment-image branch now uses EmoteImageSpan.forCommentImage().
This opt-in span updates its width/height from the actual decoded image
dimensions in Glide's onResourceReady callback, then requests a TextView
layout pass. The width is capped at the available comment width (320dp
maximum) and height is limited to twice that width; very tall images shrink
proportionally within both bounds.

The normal emote/GIPHY path retains its previous fixed dimensions. Glide
skips bitmap transformations for full-size comment images so no square crop
or forced reshape is introduced before the drawable dimensions are read.
Clicking an image continues to open its original source URL.

Verify visually using portrait, landscape, tall, square, static and animated
Reddit-native images in nested comments. This change does not affect
OAuth login, GraphQL requests, albums, account switching or signing.
