# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Commands

```bash
# Format code (run before committing)
./gradlew spotlessApply

# Check formatting without applying
./gradlew spotlessCheck

# Run lint
./gradlew lint

# Run unit tests
./gradlew test

# Build debug APK
./gradlew assembleDebug

# Install on connected device/emulator
./gradlew installDebug
```

## Architecture

Single-module Android app (`app/`) using MVVM + Clean Architecture with Jetpack Compose.

**Package structure** under `com.aidsyla.mubble`:
- `app/` — `MainActivity` (`@AndroidEntryPoint`) and `MubbleApplication` (`@HiltAndroidApp`)
- `ui/` — `AppScreen` (root composable with bottom nav), `AppState` (nav/UI state holder)
- `feature/<name>/` — each feature has `Screen.kt`, `Navigation.kt`, `ViewModel.kt`
- `common/components/` — shared Compose components
- `common/navigation/` — `AppNavHost`, `Routes` (type-safe routes via `@Serializable`)
- `data/` — repositories (`UserDataRepository` interface + `Impl`, dummy data objects)
- `di/` — Hilt modules (`DataStoreModule`, `RepositoryModule`)
- `model/` — domain models

**Navigation**: Type-safe with `@Serializable` data classes/objects in `Routes.kt`. Navigation extensions live in `<Feature>Navigation.kt` files. `AppNavHost` wraps everything in `SharedTransitionLayout` for shared element transitions.

**Data layer**: Currently uses dummy/mock repositories (`DummyPostRepository`, `DummyCommentRepository`, singleton `UserRepo`/`ChatRepo` objects). `UserDataRepository` uses DataStore for persisted preferences (dark theme config).

**DI**: Hilt throughout. `DataStoreModule` provides DataStore singleton; `RepositoryModule` binds repository interfaces to implementations.

**State**: ViewModels expose `StateFlow` (using `stateIn()`). Compose UI collects with `collectAsStateWithLifecycle`.

## Code Style

Spotless + Ktlint (1.8.0) enforces formatting. Disabled Ktlint rules (configured in root `build.gradle.kts`):
- Trailing commas, binary expression wrapping, chain method continuation
- Class/function signature formatting, condition wrapping
- Function naming for `@Composable` and `@Test` functions
- Backing property naming

Always run `./gradlew spotlessApply` before committing.

## Key Library Versions

- Kotlin 2.3.21
- AGP 9.2.1, Compose BOM 2026.05.00
- Material3 1.5.0-alpha19 (expressive components available)
- Navigation Compose 2.9.8 (type-safe routes)
- Hilt 2.59.2
- Media3 ExoPlayer 1.10.1 (video playback in `feature/videos/`)
- DataStore Preferences 1.2.1
