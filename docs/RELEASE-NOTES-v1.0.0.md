## ReSync v1.0.0

First release of this ReSync fork.

- Fixed native Reddit image albums using the `/by_id/` endpoint.
- Reworked Reddit comment loading to use `/by_id/` post metadata and the GraphQL comment forest after Reddit blocked both legacy and encoded comment URLs.
- Improved gallery parsing with copyable diagnostics.
- Retained web-session Reddit sign-in fixes.
- Fixed adding and switching Reddit accounts with separately encrypted login sessions.
- Kept favorite subreddits separate for each account, with safe migration of the old shared list.
- Permanently signed APK for future in-place updates.

Download `ReSync-v1.0.0.apk` below.

**One-time migration:** Previous GitHub Actions test builds had disposable signatures. Android cannot update them in place. Export your app settings before uninstalling the test version; once you install this permanently signed release, future APKs signed using the same key and newer version codes will update over it.
