# Adding and switching Reddit accounts

ReSync originally reused a single application-wide Reddit WebView cookie jar.
When "Add account" opened Reddit's login page, the already signed-in session
was present. Reddit immediately displayed "Welcome back" and the app silently
signed in as the existing user again. The old web-login flow also did not add
the account to the account picker.

Now the app saves the current Reddit web cookies encrypted with AndroidKeyStore
AES-GCM, clears the WebView cookie jar, and opens Reddit's real login page.
When the new account's identity is confirmed, the app securely stores its web
session and registers the username in the existing account picker. Selecting
an account restores that account's saved session *before* updating the app's
active user. Canceling Add account restores the previous account's cookies.

No login credentials or cookies are uploaded to GitHub or written to Logcat.
The shared WebView cookie jar is cleared when adding or switching accounts,
which can also sign out sites previously used in ReSync's *internal* WebView.

## How to test
1. Before installing a new test build, back up settings if it requires an
   uninstall due to the disposable Actions signing key.
2. Open the account picker > Add account. Reddit must show a fresh login, not
   "Welcome back."
3. Sign into a DIFFERENT Reddit account; it should appear in the picker.
4. Switch to the first account and back. Confirm account identity on both.
5. Cancel a third "Add account"; the currently selected account should be
   unaffected.

Known limitation: accounts originally saved by legacy OAuth without a saved
WebView session must be re-added if their old credentials are no longer usable.

Android requires the same package and certificate to install over an APK.
The signing setup script in scripts/setup-release-signing.sh must be run
locally once before the first permanently signed v1.0.0 release can publish.
