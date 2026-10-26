# DuskMoon

A top-down 2D pixel adventure game featuring procedural world generation, dynamic tile transitions, a continuous distance-field water depth system, and modern gamepad support.

---

## Modernization Highlights

- **Game Engine & Runtime:**
  - Migrated from legacy `org.newdawn.slick` (Slick2D / LWJGL 2) to **[libGDX](https://libgdx.com/)** with **LWJGL 3**.
  - Replaced obsolete 32-bit native binaries with modern 64-bit multiplatform dependencies.
  - Fully compatible with modern Java runtimes (Java 17, Java 21, and Java 27).
  - Pixel-perfect nearest-neighbor scaling inside a 16:9 virtual canvas (`640x360` scaled to `1280x720`) managed by `FitViewport`.
- **Accurate Swimming & Continuous Depth Map:**
  - Replaced the legacy directional estimation with a continuous distance-field **`DepthMap`**.
  - Calculates Euclidean distance from shorelines across an 8-way connected grid using multi-source BFS.
  - Samples continuous sub-pixel immersion depth $d \in [0.0, 1.0]$ using bilinear interpolation.
  - Smoothly drives underwater sprite clipping, translucent submerged body rendering, ground shadow attenuation, and water ripple thresholding.
- **Build & Dependency System:**
  - Migrated from Maven (`pom.xml`) to a clean multi-module **Gradle** build (`core` and `lwjgl3`).
  - Bundled with the official Gradle wrapper (`gradlew` / `gradlew.bat`).
- **Controller & Input Support:**
  - Full keyboard mapping (`WASD` / Arrows, `Space`, `Shift`, `+` / `-`).
  - Native Gamepad and Xbox controller integration via `gdx-controllers`.

---

## Project Structure

```text
duskmoon/
├── assets/                     # Shared runtime assets (spritesheets, textures, icons)
│   ├── tileset.png
│   ├── fem_blond_attack.png
│   ├── generic_dagger_m.png
│   └── logo.png
├── core/                       # Platform-independent core game logic
│   └── src/main/java/de/cirrus/dusk/
│       ├── DuskMoon.java       # Main Game application lifecycle
│       ├── Art.java            # Texture and sprite sheet management
│       ├── InputHandler.java   # Keyboard, mouse, and gamepad input processor
│       ├── gfx/Screen.java     # SpriteBatch-based pixel rendering and shadow pipeline
│       ├── level/              # World tile grid, LevelMap, DepthMap, and entities
│       │   └── map/            # Procedural world generator, biomes, features, and noise
│       │       ├── biome/      # Biome model (Ocean, Plains, Forest, Mountain, Desert)
│       │       ├── feature/    # Decorators (SandPatch, Tree, Flower, Cactus, DungeonStairs)
│       │       ├── noise/      # NoiseMap (midpoint displacement) and TerrainContext
│       │       ├── WorldGenerator.java # Modular world synthesis engine
│       │       └── LevelGen.java       # Generation façade & validation utility
│       └── states/Game.java    # PlayScreen game loop, camera scrolling, fixed 60 UPS
└── lwjgl3/                     # Desktop LWJGL 3 launcher & window configuration
    └── src/main/java/de/cirrus/dusk/lwjgl3/
        ├── Lwjgl3Launcher.java # Desktop entrypoint and window setup
        └── StartupHelper.java  # Platform initialization (macOS -XstartOnFirstThread, Windows)
```

---

## Requirements

- **Java JDK:** Version 17 or higher (tested on Java 17, 21, and 27)
- **Gradle:** Bundled via `./gradlew` / `gradlew.bat`

---

## Quickstart & Instructions

### 1. Run the Game
```bash
# On Windows:
.\gradlew.bat run

# On Linux / macOS:
./gradlew run
```

### 2. Run the Interactive Level Generator Preview
Tests procedural map generation with a standalone Swing visualization preview:
```bash
.\gradlew.bat :core:runLevelGen
```

### 3. Run the Test Suite
Executes unit tests (including Depth Map distance-transform and interpolation tests):
```bash
.\gradlew.bat test
```

### 4. Build Desktop Distributions
Compiles the application, runs tests, and packages runnable `.zip` / `.tar` distributions into `lwjgl3/build/distributions/`:
```bash
.\gradlew.bat build
```

---

## Controls

| Action | Keyboard | Gamepad / Xbox Controller |
| :--- | :--- | :--- |
| **Move** | `W`, `A`, `S`, `D` or Arrow Keys | Left Analog Stick / D-Pad |
| **Attack** | `Space` | `A` Button |
| **Sprint / Modifier** | `Left Shift` | — |
| **Game Tempo** | `+` / `-` (Numpad or top-row) | — |
| **Mouse Interaction** | Left / Right Click & Drag | — |

---

## Swimming Depth Mechanics

The `DepthMap` computes continuous water depth for each coordinate:
- **Land:** Depth is `0.0`. Character walks normally on terrain.
- **Shore / Wading ($0.0 < d < 0.4$):** Character steps into water; sprite is incrementally submerged at the waterline, with lower body rendered at 30% opacity.
- **Deeper Water ($d \ge 0.4$):** Dynamic water ripple animation rings are generated around the character.
- **Full Swimming ($d \to 1.0$):** Maximum immersion depth achieved, slanting ground shadow transitions smoothly into the water body.

---

## Procedural World Generation Architecture

The world generation pipeline is structured into independent, extensible packages:

1. **Noise Generation (`de.cirrus.dusk.level.map.noise`):**
   - **`NoiseMap`:** Midpoint displacement 2D fractal noise generator with configurable octave feature sizes.
   - **`TerrainContext`:** Encapsulates synchronized elevation, moisture, and temperature noise grids along with resolved biomes.
2. **Biomes (`de.cirrus.dusk.level.map.biome`):**
   - **`Biome`:** Abstract base defining biome classification and default surface tiles.
   - **Implemented Biomes:** `OceanBiome`, `PlainsBiome`, `ForestBiome`, `MountainBiome`, and `DesertBiome`.
   - **`BiomeRegistry`:** Maps multi-variable climate (elevation, moisture, and temperature) to specific biomes.
3. **World Features & Decorators (`de.cirrus.dusk.level.map.feature`):**
   - **`WorldFeature`:** Common interface for terrain modification passes.
   - **`SandPatchFeature`:** Procedurally places sandy shorelines and inland sand formations.
   - **`TreeFeature`:** Creates clustered forest groves, scattered vegetation, and high-density timber zones in Forest biomes.
   - **`FlowerFeature`:** Distributes colorful wildflower patches on grass with randomized palette metadata.
   - **`CactusFeature`:** Plants cacti across arid sand regions.
   - **`DungeonStairsFeature`:** Places dungeon descent staircases safely enclosed inside solid rock formations.
4. **Synthesis & Validation (`de.cirrus.dusk.level.map`):**
   - **`WorldGenerator`:** Orchestrates noise generation, radial island falloff attenuation, biome surfacing, and ordered feature decoration.
   - **`LevelGen`:** Provides backward-compatible entrypoints (`createTopMap`, `createAndValidateTopMap`), threshold validation, and a standalone Swing previewer.

