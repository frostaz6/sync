# ReSync

A revived, buildable build of the discontinued **Sync for Reddit** Android client.

The original Sync for Reddit was a closed-source app that stopped working after Reddit shut off
third-party API access in 2023 and was never updated again. ReSync is a reconstruction of that app
into a normal, readable Android Studio / Gradle project that **builds and runs on modern devices**,
with the dead bits replaced so it actually loads content again.

> ⚠️ This is an unofficial, fan-made preservation project. It is **not affiliated with, endorsed by,
> or supported by** the original author or Reddit. See [Legal / takedown](#legal--takedown) below.

## What's different from the original

- **Works without the official Reddit API.** Sign-in was reworked so the app authenticates against
  current Reddit instead of the revoked OAuth client, so the front page, feeds, comments, profiles, and
  search load again (logged-in and anonymous browsing).
- **De-Google'd / de-monetised.** In-app purchasing & billing, Crashlytics, and analytics are removed.
  All previously paid "Ultra" features (previews, inline quotes, on-device translation) are unlocked.
- **Media fixes.** RedGifs playback rewritten for their current API; website/link previews use
  client-side `og:image` extraction; imgur albums use the public imgur API.
- **Self-contained.** Privacy policy / terms are bundled locally; formerly remote-hosted UI assets are
  shipped in-app. No dependency on the original project's dead backend servers.
- **Rebranded** to `ReSync` / package `com.resync.app` so it installs alongside (not over) anything else.

## Build

See **[BUILD.md](BUILD.md)** for the full guide, including the optional third-party API keys (what to
get and where to put them).

```bash
./gradlew assembleDebug      # -> app/build/outputs/apk/debug/app-debug.apk
```

Requirements:
- **JDK 17**
- **Android SDK** with platform/compile SDK **35** (build-tools 34+)
- Create a `local.properties` with your SDK path, e.g. `sdk.dir=C:\\Android\\Sdk`

Install the resulting `app-debug.apk` on a device (minSdk 23 / Android 6.0+). Tested on Android 11 and
Android 16.

### API keys

The original app's third-party API keys have been **stripped** from this source and replaced with
`YOUR_*` placeholders — the app builds and the core Reddit experience works without them. A few
optional integrations need your own keys to function; supply them if you want those features:

| Placeholder | Where | Enables |
|---|---|---|
| `YOUR_YOUTUBE_DATA_API_KEY` | `CustomYouTubeVideoActivity`, `UrlSelectionDialogBottomSheet` | In-app YouTube playback (otherwise falls back to the external YouTube app) |
| `YOUR_IMGUR_CLIENT_ID` | `net/thirdparty/Imgur*Request` | Imgur album/image loading via the Imgur API |
| `YOUR_RAPIDAPI_KEY` | `UploadImgurRequest` | Imgur uploads via RapidAPI |
| `YOUR_REDDIT_CLIENT_ID` | `auth/OAuthLoginHelper` | Legacy Reddit OAuth login (unused — the app uses a different sign-in path) |

Firebase keys/config are intentionally absent (the original backend is dead); the app runs without them.

## Status

| | |
|---|---|
| **Builds** | ✅ `./gradlew assembleDebug` produces a working APK |
| **Runs** | ✅ installs and launches; UI renders fully (Material You theming, custom fonts, feed) |
| **Loads content** | ✅ front page, feeds, comments, profiles, search, anonymous browsing |
| **Media** | ✅ images, videos, RedGifs, imgur albums, link previews |
| Ads / billing / analytics / crash reporting | Removed |

## Changelog

See [CHANGELOG.md](CHANGELOG.md) for the full list of changes relative to the original app.

## Project layout

```
.
├── app/
│   ├── src/main/java/   # the reconstructed app source (real, readable names)
│   ├── src/main/res/    # resources
│   ├── src/main/assets/ # bundled assets (legal page, UI overlays, ML models)
│   └── build.gradle     # full Maven dependency list (androidx, okhttp, glide, exoplayer, …)
├── build.gradle, settings.gradle, gradle.properties
└── gradlew, gradlew.bat, gradle/
```

Third-party libraries are resolved as real Maven artifacts (androidx, OkHttp, Glide, ExoPlayer, Volley,
Otto, Material, Firebase, …) — so you read the app code, not decompiled library code.

## Legal / takedown

This repository is a non-commercial preservation / interoperability effort for an abandoned app, made
available for educational and archival purposes. It is **not affiliated with the original Sync for
Reddit, its author, or Reddit Inc.**, and no endorsement is implied. Reddit, Sync for Reddit, and all
related names, logos, and trademarks belong to their respective owners.

If you are the original author (or another rights holder) and would like this repository taken down,
**open an issue or contact the repository owner and it will be removed promptly, no questions asked.**
