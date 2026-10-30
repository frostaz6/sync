## ReSync v1.0.0

First release of this ReSync fork.

- Fixed native Reddit image albums using the `/by_id/` endpoint.
- Improved gallery parsing with copyable diagnostics.
- Retained web-session Reddit sign-in fixes.
- Fixed adding and switching Reddit accounts with separately encrypted login sessions.
- Permanently signed APK for future in-place updates.

Download `ReSync-v1.0.0.apk` below.

**One-time migration:** Previous GitHub Actions test builds had disposable signatures. Android cannot update them in place. Export your app settings before uninstalling the test version; once you install this permanently signed release, future APKs signed using the same key and newer version codes will update over it.
