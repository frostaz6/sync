# Account-specific favorite subreddits

The original ReSync favourites manager used:
\`PreferencesHelper.getAccountPrefs("FavouriteSubredditManager")\`.

That API falls back to app-global preferences unless the username's preferences
file contains a special \`set\` marker. The fork's new WebView account-login
flow did not initialize that marker. Thus two web-session accounts were reading
and modifying the same \`user_favorites\` string and Reddit's background account
refresh could merge their favourites.

The fix makes only favorites use each username's own SharedPreferences file,
whether or not a \`set\` marker exists. It eliminates an asynchronous write
race: the old SaveTask read the active username later on a worker thread, so a
rapid account change could save the old list into a different account.

## Existing favorites and migration

Do not clear or delete the original app-wide \`user_favorites\`. When the oldest
stored real Reddit account first loads with no own favorites, ReSync copies the
legacy global list into that account's preferences. If an account already has
its own list, it is never overwritten. Other accounts start with their own
existing lists or empty lists and may populate from Reddit's
\`user_has_favorited\` values on the next subreddit refresh.

This deliberately does **not** copy the already mixed global list to every
account. It is impossible to know which account a locally-added favourite
originally belonged to if it had already been mixed together. The global
copy remains intact as a backup; users can correct any misattribution.

## Verification on phone

1. Switch to the first Reddit account, check its favorites.
2. Switch to the other, check that favorites are different or empty until
   its own Reddit favorites finish refreshing.
3. Favorite a new subreddit only on the second account.
4. Switch to the first: it should not show the second account's new favorite.
5. Switch back: the new favorite should remain on the second account.
6. Repeat after closing and reopening ReSync.

Do not uninstall a working APK merely to test this fix without first exporting
settings: the Actions builds remain temporarily signed until the permanent
signing script is run on the user's Linux PC.
