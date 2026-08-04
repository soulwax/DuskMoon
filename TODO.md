# DuskMoon Migration TODO (to libGDX)

> Process note: I will keep reflecting all planning refinements, validation outcomes, and task-level adjustments directly in this `TODO.md`.

## Framework Decision
- Chosen framework: **libGDX (latest stable)**
- Runtime target: Java 17+
- Initial target platform: Desktop (LWJGL3 backend)

## Goals
1. Preserve gameplay behavior and feel from original version.
2. Modernize build, runtime, and asset pipeline.
3. Enable future multi-platform expansion.

## Work Breakdown

### Phase 0 — Baseline Capture
- [ ] Build and run legacy game from current `pom.xml`.
- [ ] Record controls, movement behavior, collision quirks.
- [ ] Capture screenshots/video of representative gameplay.
- [ ] Inventory assets and map each file to usage in code.

### Phase 1 — New Project Bootstrapping
- [x] Create libGDX multi-module project (`core`, `lwjgl3`).
- [ ] Configure Java 17 toolchain.
- [ ] Set up Gradle wrapper and reproducible build.
- [ ] Add CI desktop build check.

### Phase 2 — Core Architecture Port
- [ ] Implement `GameApp extends com.badlogic.gdx.Game`.
- [ ] Convert current game state to `Screen` (`PlayScreen`).
- [ ] Implement fixed timestep loop (60 UPS with interpolation optional).
- [ ] Keep package boundaries aligned with legacy architecture.

### Phase 3 — Rendering Port
- [ ] Replace software screen blit with `SpriteBatch`.
- [ ] Add `OrthographicCamera` and camera-follow logic.
- [ ] Build tile atlas region mapper from `tileset.png`.
- [ ] Port tile draw ordering and depth behavior.
- [ ] Validate pixel-perfect nearest-neighbor rendering.

### Phase 4 — Input Port
- [ ] Replace legacy `InputHandler` with libGDX input system.
- [ ] Implement action mapping (move, attack, interact).
- [ ] Preserve key repeat and movement responsiveness.

### Phase 5 — World + Entities
- [ ] Port `Level` and `LevelGen`.
- [ ] Port all `Tile` subclasses and interaction rules.
- [ ] Port `Entity`, `Mob`, `Player` update/collision logic.
- [ ] Preserve update order to avoid behavior drift.

### Phase 6 — Asset Pipeline
- [ ] Move runtime assets to `assets/`.
- [ ] Add centralized `AssetManager` loader.
- [ ] Add loading screen and failure handling.
- [ ] Configure texture filters: Nearest/Nearest.

### Phase 7 — Audio/Platform Modernization
- [ ] Replace low-level/native-specific audio usage with libGDX API.
- [ ] Remove custom natives bootstrapping (`Natives.java`) after parity.
- [ ] Remove obsolete `lib/natives/*` from runtime path.

### Phase 8 — Validation
- [ ] Create parity checklist (movement, collisions, generation, rendering).
- [ ] Add debug overlay (FPS, tick ms, entity/tile counts).
- [ ] Run side-by-side comparison against legacy captures.
- [ ] Fix drift bugs and document intentional changes.

### Phase 9 — Packaging & Release
- [ ] Create desktop distribution artifact.
- [ ] Add startup scripts and README run instructions.
- [ ] Tag migration milestone release.

## File Mapping (Legacy → New)
- `de.cirrus.dusk.DuskMoon` → `core/.../GameApp` + `lwjgl3/.../Lwjgl3Launcher`
- `states.Game` → `core/.../screens/PlayScreen`
- `gfx.Screen` → `core/.../render/WorldRenderer`
- `InputHandler` → `core/.../input/InputController`
- `level/*` + `level/tile/*` → `core/.../world/*`
- `entities/*` → `core/.../entity/*`
- `resources/*` → `assets/*`

## Risks
- Collision/physics behavior drift due to loop timing differences.
- Sprite alignment drift from camera/tile origin mismatch.
- Procedural generation differences from RNG/API changes.

## Controller Compatibility (Xbox / Gamepad) — Full Implementation Details
- [ ] Left stick controls movement with deadzone normalization.
- [ ] A button maps to attack with edge-triggered press semantics (`justPressed`).
- [ ] Quantize analog movement to stable directional intent for tile/grid parity.
- [ ] Merge keyboard and controller inputs deterministically.
- [ ] Reserve other buttons (B/X/Y, Start, Back, triggers) for future actions.
- [ ] Ensure polling logic is deterministic at fixed update timestep.
- [ ] Add adapter tests/checklist for drift, repeat, and accidental double-trigger.

### Ergonomic Mapping Rules
- Move: Left stick (primary), D-pad optional parity fallback.
- Attack: A button press edge only.
- LB/RB: reserved for tempo/stance later (not yet bound in gameplay).
- Other controls: intentionally unbound for now to reduce accidental behavior changes.

### Implementation Notes
- Introduce `GameInputState` as the single action state model.
- Implement `XboxControllerAdapter` to map raw controller events to actions.
- Keep API explicit and legacy-style naming where possible for readability/parity.
- Apply deadzone before normalization.
- Use edge detection cache for `A` button to expose `attackPressedThisFrame`.

### Determinism Notes
- Read controller each tick, not each render frame.
- Avoid time-based analog smoothing in first parity pass.
- Keep quantization thresholds constant and documented.
- Never emit repeated attack from held A; only edge events.

### Controller Testing Checklist
- [ ] A press triggers exactly one attack event per press.
- [ ] Holding A does not retrigger attack.
- [ ] Diagonal stick movement behaves consistently at deadzone boundary.
- [ ] Keyboard and controller pressed together resolves predictably.
- [ ] Disconnect/reconnect controller does not crash input pipeline.

## House Rules
- Preserve legacy architecture naming intent while modernizing framework.
- Prefer small, explicit classes over broad abstractions in early migration.
- Do not break legacy Maven build during parallel `gdx/` migration work.
- Keep TODO progress and validation notes continuously updated here.

## Validation Notes (Latest Review)
- Verified framework choice is stated before implementation phases.
- Verified phased plan is step-by-step and execution-oriented.
- Verified migration mapping from legacy classes/modules to libGDX targets.
- Verified risks and done criteria are included and clear.
- Verified generated `gdx/` scaffold exists and legacy tree remains intact.

## Implementation Progress (Scaffolding Pass)
- [x] Generated libGDX project in `gdx/` (graphical Liftoff flow).
- [ ] Simplify core dependencies for migration baseline.
- [ ] Rename placeholder packages to `de.cirrus.dusk.gdx`.
- [ ] Add `GameApp`, `PlayScreen`, `GameInputState`, `XboxControllerAdapter`.
- [ ] Wire desktop launcher to `GameApp`.
- [ ] Run compile verification (`core:classes`, `lwjgl3:classes`).

## Done Criteria
- Gameplay parity reached for core loop and controls.
- Desktop build reproducible on clean environment.
- Legacy native bootstrapping removed.
- Migration docs and runbook completed.
- `TODO.md` remains the single source of truth for ongoing plan updates and review notes.
