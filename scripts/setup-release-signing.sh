#!/usr/bin/env bash
# Generate ONE signing identity locally, upload encrypted GitHub Actions secrets,
# and launch the first permanently signed ReSync release. Never commit this key.
set -euo pipefail
umask 077
REPO=frostaz6/sync
BASE="${XDG_CONFIG_HOME:-$HOME/.config}/resync/signing"
KEYSTORE="$BASE/resync-release.p12"
PASSWORD_FILE="$BASE/resync-release-password.txt"

for cmd in gh keytool openssl base64; do
  command -v "$cmd" >/dev/null || { echo "Missing: $cmd (install GitHub CLI, JDK and OpenSSL)" >&2; exit 1; }
done
gh auth status >/dev/null || { echo "First run: gh auth login" >&2; exit 1; }
mkdir -p "$BASE"
chmod 700 "$BASE"
if [[ -e "$KEYSTORE" || -e "$PASSWORD_FILE" ]]; then
  if [[ ! -s "$KEYSTORE" || ! -s "$PASSWORD_FILE" ]]; then
    echo "Partial existing key configuration at $BASE. Inspect before retrying." >&2
    exit 1
  fi
  echo "Reusing existing signing key."
else
  openssl rand -hex 24 > "$PASSWORD_FILE"
  chmod 600 "$PASSWORD_FILE"
  pass=$(cat "$PASSWORD_FILE")
  keytool -genkeypair -noprompt -storetype PKCS12 -keystore "$KEYSTORE" \
    -alias resync -keyalg RSA -keysize 3072 -sigalg SHA256withRSA \
    -validity 10000 -dname "CN=ReSync Android Release, OU=Personal Build, O=ReSync" \
    -storepass "$pass" -keypass "$pass"
  unset pass
  chmod 600 "$KEYSTORE"
fi
pass=$(cat "$PASSWORD_FILE")
keytool -list -storetype PKCS12 -keystore "$KEYSTORE" \
  -storepass "$pass" -alias resync >/dev/null
fingerprint=$(keytool -list -v -storetype PKCS12 -keystore "$KEYSTORE" \
  -storepass "$pass" -alias resync \
  | sed -n 's/^[[:space:]]*SHA256: //p' | head -n 1 | tr -d ':' | tr '[:upper:]' '[:lower:]')
unset pass
if [[ ! "$fingerprint" =~ ^[0-9a-f]{64}$ ]]; then
  echo "Certificate fingerprint not found; aborting." >&2
  exit 1
fi
base64 "$KEYSTORE" | tr -d '\n' | gh secret set RESYNC_KEYSTORE_B64 -R "$REPO"
gh secret set RESYNC_KEYSTORE_PASSWORD -R "$REPO" < "$PASSWORD_FILE"
gh variable set RESYNC_SIGNING_CERT_SHA256 -R "$REPO" --body "$fingerprint"
echo "Signing certificate SHA256: $fingerprint"
echo "BACK UP these files securely. Never regenerate the signing key:"
echo "  $KEYSTORE"
echo "  $PASSWORD_FILE"
gh workflow run build-apk.yml -R "$REPO" --ref main -f publish_first_release=true
echo "Build: https://github.com/$REPO/actions"
echo "Release: https://github.com/$REPO/releases/tag/v1.0.0"
