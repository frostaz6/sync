# Permanent APK signing and first ReSync release

Reddit album loading works using `/by_id/t3_<post-id>.json?raw_json=1`. The
previous `/comments/` endpoint returned 403, which the on-device diagnostics
helped identify.

## Set up once on your own Linux computer

Install and authenticate GitHub CLI (`gh auth login`), plus Java `keytool` and
OpenSSL. Then execute:

```bash
curl -fL https://raw.githubusercontent.com/frostaz6/sync/main/scripts/setup-release-signing.sh -o setup-release-signing.sh
bash setup-release-signing.sh
```

This creates a private PKCS12 key and a strong password in
`~/.config/resync/signing/` (or `$XDG_CONFIG_HOME/resync/signing/`),
reusing them on subsequent runs. It uploads them through `gh secret set` as
the encrypted GitHub Actions secrets `RESYNC_KEYSTORE_B64` and
`RESYNC_KEYSTORE_PASSWORD`. It also pins the public certificate fingerprint
in variable `RESYNC_SIGNING_CERT_SHA256`.

The script then launches a workflow-dispatched build that creates and uploads
the permanently signed first release `v1.0.0`. **Back up both the .p12
keystore and its password securely.** Neither belongs in a public repo.
Builds verify the fingerprint and refuse to publish an APK with a temporary
signature or a changed signing identity.

See https://github.com/frostaz6/sync/releases/tag/v1.0.0 after the release
workflow completes.

## One final reinstall

Previous temporary runner signing certificates cannot be recovered from APKs.
Android checks the certificate and versionCode, so even the next signed APK
cannot update these test installations. Export settings first, uninstall the
temporary test APK once, install signed `v1.0.0` and keep the signing key.
Subsequent releases with that same certificate and newer versionCode can
update without resetting app data. Normal Android backups are not reliable
here because the manifest opts out with `allowBackup=false`.
