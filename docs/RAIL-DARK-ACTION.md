# Dark mode: move existing More actions command into the left navigation rail

The original ReSync/Sync More actions "Dark mode" action (code 7) lives in
ActionsBottomSheetFragment.performAction(). It already persists the standard
per-account dark/light/follow-system flags and broadcasts a theme update.

The first rail implementation introduced a separate NightHelper.toggleManual()
that didn't follow the same transitions. That custom method is now removed.

The rail item directly underneath Settings now calls
ActionsBottomSheetFragment.performAction(..., 7, null) and sets its icon from
ActionsManager.getIconRes(7), so it's exactly the same action, including its
existing icon states and any toasts. The rail doesn't change the selected page.

Action 7 is removed from the More actions grid and customize menu. Existing
stored action orders are migrated by removing only code 7, leaving the rest
in their prior order. The More actions implementation of the dark mode logic
stays unchanged and reusable by the rail.

No changes to random subreddit, comments, inline-image rendering, login,
or APK signing. Test from light, dark, and follow-system settings, restart,
switch accounts, and ensure the icon and theme both update promptly.
