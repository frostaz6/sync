# Inline image overlap after aspect-ratio update

Fixes ReSync's dynamic comment-image span occupying more height than the text
layout reserved, causing the bitmap to paint over subsequent comment text.

Cause: EmoteImageSpan initially reserves a 4:3 image slot. After Glide loads
the actual portrait/landscape image, the new aspect-aware dimensions update
the span but requestLayout() does not invalidate TextView's existing
StaticLayout line metrics. It therefore still reserves the placeholder height.

Fix:
- Only for full-size Reddit comment images, avoid PrecomputedTextCompat;
  preserve standard text layout for all other image/emote uses.
- On the image's first dimension change, post a main-thread setText() using
  a new SpannableString copy. This preserves all original span objects,
  click listeners, and already-loaded image; it also rebuilds TextView's line
  metrics so later lines shift down.
- Guard the callback against recycled comment ViewHolders by verifying
  both the active TextView and the presence of this span in current text.
- Existing image bounds, Glide loading, GIF behavior, comment parsing,
  login, and signing remain unchanged.

Testing: open comments containing portrait, landscape, and square photos,
images followed by text, replies after photos, nested comments, and rapidly
scroll through many comments to exercise ViewHolder recycling. Check that
images do not overlap or hide any text and keep their original proportions.
