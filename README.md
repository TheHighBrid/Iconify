# Iconify

Iconify is an Android app that turns uploaded app icons or logo images into standalone bubbly 3D inflated-style PNG renders.

This is no longer a prompt-helper app. The APK now performs local bitmap processing inside the app.

## What the APK does

- Upload an icon image from your Android device
- Crop and center the logo/icon automatically
- Render a bubbly inflated 3D-style version locally
- Add soft edge depth, gloss, studio shadow, and subtle texture
- Preview the output inside the app
- Save the generated PNG to the gallery
- Share the generated PNG from the app
- Display issues/fixes inside the APK itself

## Best input

Use a clean transparent PNG app icon or logo. JPGs and screenshots can work, but transparent PNGs give the best result.

## Build the APK with GitHub Actions

1. Go to the repository on GitHub.
2. Open the **Actions** tab.
3. Select **Build Android APK**.
4. Open the latest successful workflow run.
5. Download the artifact named **iconify-debug-apk**.
6. Unzip it and install `app-debug.apk` on your Android phone.

## Build locally

Requirements:

- Android Studio, or Android SDK command-line tools
- Java 17
- Gradle 8.10.2+

Run:

```bash
gradle :app:assembleDebug --no-daemon
```

The APK will be created here:

```bash
app/build/outputs/apk/debug/app-debug.apk
```

## Install on Android

Because this is a debug APK, Android may ask you to allow installation from unknown sources. After allowing it, tap the APK again to install.

## Current version

`1.1.0` local renderer MVP.

Future versions can add stronger geometric deformation, batch generation, style presets, background controls, project history, and optional API-powered generation.
