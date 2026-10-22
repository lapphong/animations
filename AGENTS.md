# AGENTS.md

Jetpack Compose demo app (`:app`) showing two animation libraries and an embedded Flutter module.

## Stack

- AGP 9.0.1 / Gradle 9.2.1; compileSdk & targetSdk 37, minSdk 26; Java 11 bytecode with JDK 21.
- Kotlin is compiled by AGP 9's built-in Kotlin support — no `org.jetbrains.kotlin.android` plugin is applied.
- Compose BOM 2024.09.00 + Material 3; Navigation 3 1.1.5 (`NavDisplay`; navigation-compose is not used).
- Koin 4.1.1 (`koinInject`, `singleOf`), kotlinx.serialization 1.9.0 for `NavKey` routes.
- Timber 5.0.1 for logs, Glide Compose 1.0.0-beta09 for images, Lottie Compose 6.7.1 for animations.
- Flutter 3.41 add-to-app (Dart 3.11 + `flutter_reactions`), consumed as local AARs.
- Namespace `com.animations`; release applicationId `com.animsapp.buzzlp`; debug/profile append `.dev`.

## Project structure

```
app/                      # :app — UI, navigation, DI, Flutter embedding
  src/main/java/com/animations/
    ui/                   # Splash, Home, demo screens + theme/
    widgets/              # AppImage/AppIcon/AppLottie, Center*, buttons, EmbeddedFlutterView
    shared/nav/           # NavKeys, Navigator, NavigationState, transitions
    shared/extensions/    # Modifier/draw/gradient helpers
    models/               # OpenTo enum + shortcut-to-route mapping
    di/                   # Koin module
    utils/                # Flutter engine/activity, shortcuts, SharedPrefs
  src/test/               # one placeholder unit test
  src/androidTest/        # FlutterScreenTest + placeholder instrumented test
  libs/                   # checked-in AARs that :app actually compiles against
carousel_slider/          # standalone library module, rebuilt manually into app/libs
disintegration/           # standalone library module, rebuilt manually into app/libs
counter/                  # Flutter add-to-app module (not a Gradle project)
gradle/libs.versions.toml # all dependency and plugin versions
.opencode/                # review command + reviewer subagent
.agents/skills/           # vendored Android skills
```

## Commands

Use the `android` CLI for device/emulator actions and `./gradlew` for compile/test/lint.

### Build & test

```bash
./gradlew :app:assembleAnimsappDebug
./gradlew :app:installAnimsappDebug                # device/emulator required
./gradlew :app:compileAnimsappDebugKotlin          # fastest code-only check

./gradlew :app:testAnimsappDebugUnitTest
./gradlew :app:testAnimsappDebugUnitTest --tests 'com.example.animations.ExampleUnitTest'
./gradlew :app:connectedAnimsappDebugAndroidTest   # device required; runs FlutterScreenTest
./gradlew :app:connectedAnimsappDebugAndroidTest \
  -Pandroid.testInstrumentationRunnerArguments.class=com.animations.FlutterScreenTest

cd counter && flutter test
```

- The only flavor is `animsapp`, so install/test tasks are flavor-prefixed (`installDebug` does not exist).
- `./gradlew :app:lintAnimsappDebug` exists, but `lint { abortOnError = false }` means a passing build says nothing
  about lint.
- APK names are generated (`animsapp_v<version>_<buildType>_<yyyyMMdd>.apk`); pick the newest with `ls -t` instead of
  hardcoding a path.
- There is no CI; verify locally before reporting done. Minimum: compile + unit test.
- **HARD RULE**: UI/Flutter changes also need a device run; confirm with a screenshot/layout dump, not just a passing
  build.

### Device / emulator

```bash
android emulator list
android emulator start --avd=<name>
android run --apks="$(ls -t app/build/outputs/apk/animsapp/debug/*.apk | head -1)"
android screen capture -o /tmp/screen.png          # add -a for numbered tappable boxes
android layout                                      # view tree for finding tap targets
$ANDROID_HOME/platform-tools/adb logcat -d -v time | grep -E "<tag>"
```

- `adb` is not on PATH; call it via `$ANDROID_HOME/platform-tools/adb`.
- Prefer a connected real device; start an emulator only when no device is attached.

## Flutter module (`counter/`)

- Not a Gradle subproject. `:app` consumes `com.example.counter:flutter_{debug,release,profile}` from the local Maven
  repo `counter/build/host/outputs/repo` (wired in `settings.gradle.kts`).
- `counter/build/` is gitignored: after a fresh clone or any Dart change, run `cd counter && flutter build aar`
  **before** any Android build. A normal Gradle build does not pick up `counter/lib` changes, and there is no hot
  reload inside the Android app. Default build number is `1.0`, which must match `flutter_release` in
  `gradle/libs.versions.toml`.
- Method channel `com.animations/flutter` is implemented on both sides (`FlutterEngineManager.kt` and
  `counter/lib/main.dart`); rename in both or neither.

## Animation libraries are prebuilt AARs

`carousel_slider/` and `disintegration/` exist as Gradle modules, but `:app` does **not** depend on them: it compiles
the checked-in `app/libs/*-1.0.0.aar` files. Editing module source has no effect until you rebuild and copy:

```bash
./gradlew :carousel_slider:bundleReleaseAar   # AGP 9 name; plain bundleRelease is ambiguous
cp carousel_slider/build/outputs/aar/carousel_slider-1.0.0.aar app/libs/
```

Same for `disintegration`. Alternatively temporarily switch the dependency to `project(":...")` in
`app/build.gradle.kts`.

## Banned patterns

Run these before submitting. New or changed code must add no hits; hits already present are legacy and should be fixed
only when touching that file.

| Banned                                                                                  | Use instead                                                                   | Why                                                         |
|-----------------------------------------------------------------------------------------|-------------------------------------------------------------------------------|-------------------------------------------------------------|
| `Image(painterResource(...))` / `Icon(painterResource(...))` outside `widgets/Image.kt` | `AppImage` / `AppIcon`                                                        | One wrapper owns description, alignment, tint               |
| `LottieAnimation(...)` outside `widgets/Image.kt`                                       | `AppLottie`                                                                   | One wrapper owns playback, scale, alignment                 |
| `Modifier.clickable` in screen code (`ui/`)                                             | `GradientButton` or `Modifier.onClickNotRipple("EventName")`                  | Debounces 400ms, adds a `testTag`, avoids double navigation |
| Raw `Color.White/Black/Gray` or `Color(0xFF…)` in `ui/` outside `ui/theme/`             | Named token in `ui/theme/Color.kt`                                            | Single source of truth for colors                           |
| Plain `Row`/`Column`/`Box` only to center one child                                     | `CenterRow` / `CenterColumn` / `CenterBox`, or chain modifiers onto the child | Less boilerplate                                            |
| Plain `var x = ...` as composable state                                                 | `var x by remember { mutableStateOf(...) }` (`mutableIntStateOf` for numbers) | Plain vars reset on recomposition                           |
| `derivedStateOf` over a plain value                                                     | Compute directly or `remember(key)`                                           | It only tracks snapshot `State`; plain values go stale      |
| `derivedStateOf` unwrapped with `by` in the same composable                             | Compute directly                                                              | Reading state already triggers recomposition                |
| Unwrapped plain Boolean read inside another `derivedStateOf`                            | Read the `State` object (`progress()`)                                        | Snapshot tracking is on the State, not the value            |
| `State` object used as a `remember` key                                                 | Plain value or no key                                                         | Recreates the value for no benefit                          |
| `viewModelScope.launch { }` around blocking IO/CPU                                      | `launch(Dispatchers.IO)`                                                      | `viewModelScope` is `Main.immediate`                        |
| Unmanaged `CoroutineScope` inside a composable                                          | `rememberCoroutineScope()` / `LaunchedEffect`                                 | Scope must follow composition/lifecycle                     |
| `LaunchedEffect` whose body has no suspend call                                         | `SideEffect` (unless ordering after another effect matters)                   | No need to spawn a coroutine                                |
| Enum entries in PascalCase (`Design1`)                                                  | `UPPER_SNAKE_CASE` (`DESIGN1`)                                                | Kotlin convention; existing enums comply                    |

```bash
# Must be 0 hits repo-wide.
grep -rn "painterResource" app/src/main/java --include="*.kt" | grep -v "widgets/Image.kt"
grep -rn "LottieAnimation" app/src/main/java --include="*.kt" | grep -v "widgets/Image.kt"

# Must be 0 hits in `ui/`; widgets own their own clickables.
grep -rn "Modifier\.clickable" app/src/main/java/com/animations/ui --include="*.kt"

# Must be 0 hits on files you changed; older screens may still hit (legacy).
grep -rnE "Color\.(White|Black|Gray)|Color\(0x" app/src/main/java/com/animations/ui --include="*.kt" | grep -v "/ui/theme/"
```

The color check currently has 8 legacy hits in `CarouselSliderDemo.kt`, `DisintegrationDemo.kt`, and
`SplashScreen.kt`; the other greps are clean. Rows without a grep (centering, `var`, `derivedStateOf`, `remember`,
coroutines, `LaunchedEffect`, enum) are review checks: fix or justify each hit on the files you touched.

## Resources

| Asset        | Location                 | Naming                                                                                   |
|--------------|--------------------------|------------------------------------------------------------------------------------------|
| Vector icon  | `res/drawable/`          | `ic_<feature>_<name>.xml`                                                                |
| Raster art   | `res/drawable/`          | `img_<feature>_<name>.webp`                                                              |
| Lottie       | `res/raw/`               | `lottie_<name>.json`                                                                     |
| Font         | `res/font/`              | snake_case lowercase, declared in `ui/theme/Type.kt`                                     |
| String       | `res/values/strings.xml` | `name` = snake_case of the value                                                         |
| System theme | `res/values/`            | `themes.xml` + `colors.xml` for window/system bar; Compose colors in `ui/theme/Color.kt` |
| Other XML    | `res/xml/`               | `backup_rules.xml`, `data_extraction_rules.xml`                                          |

String rules:

- `name` = snake_case of the value (`Carousel Slider` → `carousel_slider`); platform names like `app_name` stay as-is.
- Add a feature prefix only when the text is 40+ characters or ambiguous (`title_ob1`, `error_a11y_permission_denied`).
- Mark brand/non-translatable strings `translatable="false"`; use `<plurals>` for counts.
- Convert Figma icons to vector XML; don't keep raw `.svg` files in `res/drawable/`.

Current files already follow this: `ic_shortcut_*`, `img_app_logo.webp`, `img_splash_background.webp`,
`res/raw/lottie_click.json`.

## Review

- `/review-plan <plan>` reviews a plan across architecture, code quality, tests, and performance before coding.
- The `android-reviewer` subagent reviews the current diff read-only against this file. Ask the main agent to use it.
- Both live under `.opencode/` and follow the active ponytail rules: smallest complete change, flag over-engineering,
  no unasked features.

## Graphify

- `graphify-out/` is a generated knowledge graph and is gitignored. After committing and pushing, regenerate it and
  prune older date snapshots in one command (CLI form; `/graphify update .` does not prune):
  `graphify update . && ls -1d graphify-out/[0-9]*/ | sort | sed '$d' | xargs rm -rf`
- Treat codebase questions as graph queries first when `graphify-out/` exists: read `graphify-out/GRAPH_REPORT.md` for
  architecture, then `graphify query "<question>"` or `graphify explain "<symbol>"` for focus.
- **After a context compaction or token reset, re-read `graphify-out/GRAPH_REPORT.md` before continuing work** to
  rebuild project context.
- `GRAPH_REPORT.md` records the commit it was built from; if `git rev-parse HEAD` differs, the graph is stale — run
  `graphify update .`.
- Only the newest `graphify-out/YYYY-MM-DD/` snapshot is kept; the command above prunes the rest. Don't hand-edit
  `graph.json` or `GRAPH_REPORT.md`.
- If `graphify-out/` is missing, regenerate it or ask the user; don't invent its contents.

## Gotchas

- Any invocation whose task names contain a release assemble/bundle task — including another module's
  `:carousel_slider:bundleReleaseAar` — increments `VERSION_CODE` in `app/version.properties` during configuration. Use
  debug tasks for verification and revert that file if it gets dirtied.
- Release signing uses the committed `app/animsapp.jks` via `app/signingConfigs.gradle`.
- Commit messages follow Conventional Commits (`feat:`, `fix:`, `refactor:`) in English.
- `.agents/skills/` contains vendored Android skill docs (AGP 9, R8, Perfetto) — leave them alone unless the task is
  about them.
