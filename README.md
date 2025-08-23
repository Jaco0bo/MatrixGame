# Matrix Game

**Author:** Andrés Jacobo Sepúlveda

A small threaded simulation inspired by *The Matrix*. Neo (the protagonist) tries to reach a telephone (`T`) to escape the Matrix while Agents (`A`) try to capture him. The simulation uses threads to compute moves concurrently and a central arbiter to resolve conflicts and update the board each tick.

---

## Quick overview

- Board size: **10 × 10**.
- Cell types:
  - `.` — Empty cell
  - `#` — Wall (impassable)
  - `T` — Telephone (Neo's goal)
  - `N` — Neo (player/protagonist)
  - `A` — Agent (enemy)
- The game runs in discrete **ticks**. Each tick has 3 phases:
  1. **Decision**: every entity computes its intended move (concurrently).
  2. **Apply**: a single thread (the *Game* / arbiter) resolves conflicts and updates the board.
  3. **Render**: board printed to console (or UI updated).

---

## Example map (ASCII)

A # # # . T # .

. . . . . . . A . .

. # . . . . . . . .

N # . . . T . #

. . . . . . . . . .

. # # . . . . . .

A . . . . . . . #


## Features & highlights

- **Threaded decision making**: Neo and each Agent run in their own thread and compute moves concurrently.
- **Synchronized application**: modifications to the board are done only by the `Game` thread to avoid race conditions.
- **Determinism support**: you can run the simulation deterministically by using a fixed random seed and deterministic tie-breakers (useful for tests).
- **Pluggable AI**: Neo and Agents use simple BFS pathfinding by default; you can replace or improve the AI (A*, heuristics, random behavior).
- **Easy map parsing**: ASCII maps are parsed at startup; you can edit the map file or string to run different scenarios.

---

## How to build & run

### Plain `javac` / `java`
From the project root:

```bash
# compile all java files (adjust path if needed)
javac -d out $(find src/main/java -name "*.java")

# run the main class (replace package path as needed)
java -cp out org.escuelaing.edu.co.model.Main
```

## Configuration options

- Map: edit the ASCII map string in Main or load from a file (implement easy file reading in Main if desired).

- Random seed: set a globalSeed in MapParser or Main to have reproducible runs.

- Tick speed: adjust the sleep/delay in the Game thread to slow down or speed up rendering.

- Agents count: add/remove A characters in the ASCII map to change the number of agents.


## Design details

### Synchronization model

- Entities compute their next move using a snapshot (GameState.snapshot(...)) and set their intent.

- The Phaser is used to synchronize phases. Typical pattern:

  1. Decision — entities set intent and call phaser.arriveAndAwaitAdvance().

  2. Apply — Game thread resolves intents and updates board (resolveAndApply()).

  3. Render — Game prints/updates UI and advances the Phaser.

- Only the Game thread modifies the board state to avoid concurrent writes.

### Determinism

- Determinism means: given the same initial conditions (map, seed, rules), the game will produce the same sequence of moves and result every run.

- Determinism is achieved by:

  1. Using a fixed Random seed.

  2. Deterministic tie-breakers (e.g., entity ID order).

  3. Avoiding wall-clock or thread-scheduling dependent logic.

### Conflict resolution (default rules)

- If multiple entities want the same target cell, the winner is determined by a deterministic tie-breaker (e.g. entity ID).

- If an Agent moves into Neo’s cell → Neo is captured (game over).

- If Neo moves into T → Neo wins (game over).

- If two entities attempt to cross paths in the same tick, the chosen rule will determine if crossing is allowed or considered a capture (current implementation treats crossing as capture if an Agent passes through Neo’s cell).

## Contributing

- Suggest adding tests and documenting non-deterministic scenarios.

- Contributing: create issues or PRs for bug fixes, new AI strategies, map-loading from files, or UI improvements.
