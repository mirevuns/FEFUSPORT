## Modern Design Variant

This repository now ships two independent visual skins that reuse the same
business logic:

- `classicDebug` / `classicRelease` keep the original look.
- `modernDebug` / `modernRelease` load all resources from `app/src/modern`.

### What's inside `app/src/modern`

- New Material 3 palette (`values/colors.xml`) with deep navy surfaces and neon
  accents.
- Updated typography styles (`values/styles.xml`) relying on condensed and
  medium sans-serif system fonts.
- Rebuilt layouts for the onboarding, auth, activity feed, profile, detail and
  change-password screens with gradients, glass cards and larger spacing.
- Custom drawables for gradients, chips, tab indicators and floating buttons.

### Build / run

From the project root run, for example:

```
./gradlew :app:assembleModernDebug
```

or pick the *modern* flavor inside Android Studio’s Build Variants panel.

### Integrating the new visuals

The Kotlin layer did not require changes—IDs match the original layouts, so you
can switch flavors without touching view logic. If you only need the updated
assets, copy the relevant XML files from `app/src/modern/res` into another
project that uses the same view IDs.

