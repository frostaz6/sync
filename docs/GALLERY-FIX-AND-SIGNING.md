# ReSync gallery fix and stable APK signing

The app opens native Reddit galleries using `OAuthGalleryRequest`. In October 2026 Reddit
stopped responding successfully to `/api/info.json?id=t3_...`. ReSync now requests
`https://oauth.reddit.com/comments/<post-id>.json?limit=1` and extracts the first listing's
post `data.children[0].data`. The existing gallery viewer still reads
`gallery_data.items` and `media_metadata`; unrelated post requests are unchanged.

## Build and download

Open **Actions > Build ReSync gallery-fix APK > Run workflow** (branch `main`).
The `resync-gallery-persistent-apk` artifact contains `ReSync-gallery-fix.apk`,
`apk-build-info.txt`, and its SHA-256 digest. Extract APK from GitHub's ZIP and install.

The workflow can also run without signing secrets, but produces an explicitly marked
`resync-gallery-temporary-debug-apk` whose runner-generated signature CHANGES each time.
**Do not install temporary-debug builds** if you want reliable in-place updates.

## Configure signing once

Obtain your privately generated signing bundle (it is **not** in this public repository).
Keep `resync-test-signing.p12` and `password.txt` in private storage. The provided
`setup-github-signing.sh` script, run on Linux with `gh auth login`, installs two
encrypted repository Actions secrets to `frostaz6/sync`:

- `RESYNC_KEYSTORE_B64`: base64 of your PKCS12 keystore (alias `resync`).
- `RESYNC_KEYSTORE_PASSWORD`: the keystore/key password.

For manual setup: `base64 -w 0 resync-test-signing.p12` is the first secret value; the
contents of `password.txt` are the second. Set them in **Settings > Secrets and
variables > Actions > Repository secrets**. The workflow loads them only during CI,
checks the key alias, and never uploads the private key.

### Android signature limitation

**An APK cannot update an installed app signed by a different key.** ReSync's earlier
test builds were signed by disposable GitHub runner keys, and those original private
keys were not preserved. This new permanent key therefore cannot update those existing
installations. Android requires one final uninstall/reinstall when switching to the
permanent key; afterwards, keep the same keystore and use newer versions produced
from this workflow to update without losing app data.

ReSync's manifest specifies `android:allowBackup="false"` and the app's debug
build type specifies `debuggable false`, so Android normally cannot save or
restore its data across this one-time uninstall. Save any configuration using
a supported in-app export before uninstalling, where available. Do **not** expect
`adb backup` or `run-as` to transfer the data.

**Never publish the keystore or its password.** If both leak, someone else can sign
a malicious package recognized as an update to your app, potentially accessing saved
Reddit credentials and data.

The gallery network fix is compiled and built by CI but still needs testing against
Reddit on a logged-in device. It is independent of the October 2026 `media_metadata`
`status: failed` issue affecting some API-uploaded galleries.
