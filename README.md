# DuskMoon

A 2D top-down retro adventure game featuring procedural world generation, fluid tile transitions, depth/swimming immersion mechanics, and gamepad support.

## Modernization

The codebase has been modernized:
- **Engine / Framework:** Migrated from legacy `org.newdawn.slick` (Slick2D / LWJGL 2) to **[libGDX](https://libgdx.com/)** with modern **LWJGL 3**.
  - Eliminates deprecated 32-bit natives, manual library path bootstrapping, and compatibility barriers on modern operating systems and Java versions (Java 17+ / Java 21+ / Java 27).
  - Modern hardware acceleration via OpenGL / GLFW.
  - Fully supports gamepad and Xbox controllers via `gdx-controllers`.
  - Maintains pixel-perfect rendering with nearest-neighbor scaling and aspect ratio handling via `FitViewport`.
- **Build System:** Migrated from legacy Maven (`pom.xml`) to **Gradle** with official wrapper support (`gradlew`).

## Project Structure

- `core/`: Core game logic, entities, procedural generation (`LevelGen`), tiles, and rendering abstractions (independent of desktop-specific windowing).
- `lwjgl3/`: Desktop LWJGL 3 application launcher, desktop window lifecycle, and platform-specific entry points.
- `assets/`: Texture sheets (`tileset.png`, `fem_blond_attack.png`, `generic_dagger_m.png`), icons, and visual assets.

## Requirements

- **Java:** JDK 17 or higher (tested with Java 17, 21, and 27)
- Gradle (provided via bundled `./gradlew` / `gradlew.bat`)

## Building and Running

### Run the Game
```bash
# On Linux / macOS:
./gradlew run

# On Windows:
.\gradlew.bat run
```

### Run Standalone Level Generator Preview
To test procedural world generation in an interactive Swing preview window:
```bash
./gradlew :core:runLevelGen
```

### Build Distributions
To compile, test, and generate desktop distribution archives (`.zip` and `.tar` with launcher scripts):
```bash
./gradlew build
# Packaged zips will be in: lwjgl3/build/distributions/
```

## Controls

| Action | Keyboard | Gamepad / Xbox Controller |
| :--- | :--- | :--- |
| **Move** | `W`, `A`, `S`, `D` or Arrow Keys | Left Analog Stick / D-Pad |
| **Attack** | `Space` | `A` Button |
| **Sprint / Mod** | `Left Shift` | — |
| **Adjust Tempo** | `+` / `-` (Numpad or top row) | — |

