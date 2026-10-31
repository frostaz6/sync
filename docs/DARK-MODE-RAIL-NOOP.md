# Make the relocated Dark mode button visibly respond on every tap

The rail now calls the original Sync action ID 7, but that action used to
cycle system -> light -> dark. A user whose system was light saw no change on
the first tap; likewise, dark -> follow-system could look unchanged if the
system was already dark. It appeared as though the rail button did nothing.

Action 7 is now a direct opposite-mode switch based on NightHelper.isNight().
It still writes the original per-account night_mode_force_on, force_off,
follow and auto keys, then calls NightHelper.refresh(true) as before. The
follow-system and automatic options remain in Settings > Dark mode.

To avoid waiting for an async broadcast, the original BaseMaterialActivity
refreshNightMode() flow is also called immediately. ThemeReceiver still
notifies other active screens; its second refresh is a no-op when the mode
has already been applied. The rail continues to read its moon/auto icon
from ActionsManager.getIconRes(7); there is no custom NightHelper toggle.

Only the relocated theme behavior was changed. The Random subreddit fix,
More actions order, comment images, authentication and signing are intact.

Device test: start with system-follow LIGHT and press once: now DARK.
Press again: LIGHT. Start with system-follow DARK: press once LIGHT.
Verify theme updates immediately in toolbar, rail and posts without needing
another tap; restart and verify saved mode persists. Settings can still
choose follow-system / automatic mode.
