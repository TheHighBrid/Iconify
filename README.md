# Iconify

Iconify is a premium app-icon transformation tool. It converts app logos or app icon references into separate, standalone, front-facing, inflated 3D icon prompts with a cohesive high-end visual system.

This repo now includes a lightweight native Android MVP that can be built into an APK.

## What the APK does

The Android app is a clean prompt-builder for Iconify. It lets you:

- Enter an app name, logo description, or icon reference note
- Generate a polished inflated-3D-icon prompt
- Copy the prompt to paste into your image generation tool

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

`1.0.0` MVP prompt-builder.

Future versions can add direct image upload, style presets, saved icon projects, export folders, and API-powered generation.
