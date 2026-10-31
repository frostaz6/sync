# Random subreddit and left navigation theme toggle

Reddit retired /r/random and /r/randnsfw in 2024. RandomRequest now
selects a random public subreddit from current OAuth-authenticated
subreddit discovery listings instead of hitting those removed redirects.
The normal Random action searches a randomized initial letter, falling
back to popular and new listings. The NSFW action searches related
discoverable communities and only accepts over18 results. Private,
quarantined, non-matching and invalid names are excluded; the existing
user subreddit filter still applies. At most three listing sources and
six excluded results per source are attempted, avoiding endless loops.
The method selects from returned listings, not the entire universe of
Reddit communities, and requires the same working OAuth session used
by ReSync's other subreddit browsing.

The left-hand NavigationRailView menu now includes a dark/light button
directly underneath Settings. The original Sync moon/sun drawable
resources are reused. A tap switches between forced dark and forced
light modes, clearing follow-system and automatic flags, saving the
same per-account preference keys used in Settings > Dark mode, and
broadcasting the change through the normal ThemeReceiver mechanism.
The rail button does not change the selected destination and updates
its icon/title after theme changes.

Device tests: tap Random several times; verify different accessible
public communities, filters, and NSFW selection (when permitted).
Tap moon/sun repeatedly and verify immediate appearance changes,
selected-tab preservation, persistence after restart, and per-account
theme settings consistency. Check rail sizing on folded/unfolded phones.
