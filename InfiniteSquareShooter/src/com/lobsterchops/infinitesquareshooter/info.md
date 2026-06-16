# Infinite Square Shooter
## Game Design Document

Date: June 2026

---

## Overview

| | |
|---|---|
| **Engine** | Java + Swing Toolkit |
| **Art** | Aseprite |
| **Sound** | Bfxr |
| **Music** | OpenGameArt |

---

## General Idea

2D arcade-shooter where the player controls a spaceship and must survive waves of enemies. The player can move in four directions and shoot projectiles to destroy incoming enemies. The game features power-ups, different enemy types, and a scoring system.

---

## Gameplay Mechanics

- **Fluid Controls** — Responsive WASD movement + mouse aiming for shooting.
- **Wave Progression** — Each wave introduces new enemy types and increases difficulty.
- **Upgrades** — Collect power-ups to enhance your ship's abilities (e.g., faster shooting, shields).
- **Scoring System** — Earn points for each enemy destroyed, with bonuses for completing waves.

---

## Specific Gameplay

- **Shooting** — Automatic lasers that fire in the direction of the mouse cursor.
- **Death** — When the player collides with an enemy or gets hit by a projectile, they die and the game ends.
- **Player Starting Health** — 3 lives (can be increased with power-ups).

---

## Enemy Types

### Basic Enemy
| Variant | Speed | Health | Behaviour |
|---------|-------|--------|-----------|
| Basic I | Slow | 2 hits | Moves straight towards the player |
| Basic II | Faster | 1 hit | Moves straight towards the player |
| Basic III | Fast | 1 hit | Moves straight towards the player |

---

### Zigzag Enemy
| Variant | Speed | Health | Behaviour |
|---------|-------|--------|-----------|
| Zigzag I | Medium | 2 hits | Moves in a zigzag pattern towards the player |
| Zigzag II | Faster | 1 hit | Moves in a zigzag pattern towards the player |
| Zigzag III | Fast | 1 hit | Moves in a zigzag pattern towards the player |

---

### Shooter Enemy
| Variant | Speed | Health | Behaviour |
|---------|-------|--------|-----------|
| Shooter I | Medium | 2 hits | Moves towards the player and shoots projectiles |
| Shooter II | Faster | 1 hit | Moves towards the player and shoots projectiles |
| Shooter III | Fast | 1 hit | Moves towards the player and shoots projectiles |

---

### Dasher Enemy
| Variant | Speed | Health | Behaviour |
|---------|-------|--------|-----------|
| Dasher I | Fast | 1 hit | Can dash to close distance faster |
| Dasher II | Fast | 2 hits | Can dash to close distance faster |
| Dasher III | Fast | 1 hit | Can dash to close distance faster |

---

### Spread Enemy
| Variant | Speed | Health | Behaviour |
|---------|-------|--------|-----------|
| Spread I | Medium | 2 hits | Moves towards the player and shoots spread projectiles |
| Spread II | Faster | 1 hit | Moves towards the player and shoots spread projectiles |
| Spread III | Fast | 1 hit | Moves towards the player and shoots spread projectiles |

---

### Tank Enemy
| Variant | Speed | Health | Behaviour |
|---------|-------|--------|-----------|
| Tank I | Slow | 4 hits | Moves slowly towards the player |
| Tank II | Slow | 6 hits | Moves slowly towards the player |
| Tank III | Slow | 8 hits | Moves slowly towards the player |

---

### Splitter Enemy
| Variant | Speed | Health | Behaviour |
|---------|-------|--------|-----------|
| Splitter I | Slow | 2 hits | On death, splits into **2** smaller enemies |
| Splitter II | Fast | 1 hit | On death, splits into **3** smaller enemies |
| Splitter III | Fast | 1 hit | On death, splits into **4** smaller enemies |

> Does not shoot. Smaller enemies continue to attack the player after splitting.

---

### Orbiter Enemy
| Variant | Speed | Health | Behaviour |
|---------|-------|--------|-----------|
| Orbiter I | Medium | 2 hits | Moves in a circular pattern around the player while shooting |
| Orbiter II | Faster | 1 hit | Moves in a circular pattern around the player while shooting |
| Orbiter III | Fast | 1 hit | Moves in a circular pattern around the player while shooting |

---

### Bomber Enemy
| Variant | Speed | Health | Behaviour |
|---------|-------|--------|-----------|
| Bomber I | Medium | 5 hits | Moves towards player and drops bombs that explode on impact |
| Bomber II | Faster | 3 hits | Moves towards player and drops bombs that explode on impact |
| Bomber III | Fast | 1 hit | Moves towards player and drops bombs that explode on impact |

---

### Ghost Enemy
| Variant | Speed | Health | Behaviour |
|---------|-------|--------|-----------|
| Ghost I | Medium | 2 hits | Brief invisibility phases |
| Ghost II | Medium | 1 hit | Longer invisibility phases |
| Ghost III | Medium | 1 hit | Longest invisibility phases |

> Does not shoot. Periodically becomes invisible/intangible, making it hard to track.

---

### Homing Enemy
| Variant | Speed | Health | Behaviour |
|---------|-------|--------|-----------|
| Homing I | Medium | 2 hits | Fires a single **slow** homing projectile |
| Homing II | Medium | 1 hit | Fires a single **faster** homing projectile |
| Homing III | Medium | 1 hit | Fires **multiple fast** homing projectiles |

---

### Swarm Enemy
| Variant | Group Size | Health | Behaviour |
|---------|------------|--------|-----------|
| Swarm I | 6–8 enemies | 1 hit | Tiny, fast-moving enemies in large groups |
| Swarm II | Larger (scales with wave) | 1 hit | Group size increases each wave |
| Swarm III | Largest (scales with wave) | 1 hit | Group size increases each wave |

---

## Boss Types

### 🏰 The Fortress
A massive slow-moving tank that fills a large portion of the screen. Has multiple **turrets** on its body that each shoot independently — the player must destroy each turret before being able to damage the core. Forces the player to prioritize targets while dodging fire from multiple directions.

- **Health:** Very high
- **Key Mechanic:** Turret-first priority — core is invulnerable until all turrets are destroyed

---

### 👑 The Swarm Queen
Doesn't move much itself but constantly spawns Swarm Enemies. Has a **protective shell** that only opens briefly every 15–20 seconds, exposing its weak point. Forces the player to manage both the boss and its spawns simultaneously.

- **Health:** High
- **Key Mechanic:** Shell opens briefly on a timer — only window to deal damage

---

### 👻 The Phantom
Stays mostly invisible, only briefly flashing visible when it attacks. Fires homing projectiles and teleports aggressively. Tests the player's ability to track and react rather than aim precisely.

- **Health:** High (no shield)
- **Key Mechanic:** Visibility only during attacks — requires reaction-based play

---

### 💥 The Splitter King
A massive enemy that splits into two medium versions at half health, which then each split again on death — **three tiers total**. The screen gets increasingly chaotic as the fight progresses.

- **Health:** High per tier
- **Key Mechanic:** Three-tier split cascade — chaos escalates throughout the fight

---

### 🪞 The Mimic
Copies the player's movement pattern with a short delay, mirroring their position on the opposite side of the screen. Also fires projectiles in the same direction the player shoots. Gets disorienting fast and punishes repetitive movement patterns.

- **Health:** Medium-High
- **Key Mechanic:** Mirrors player position and shooting — forces unpredictable movement

---

## Wave Progression

### Act 1 — Level I Enemies (Waves 1–12)

| Wave | Enemies | Notes |
|------|---------|-------|
| 1 | Basic I | Tutorial wave — ease the player in |
| 2 | Basic I, Zigzag I | Introduce movement variety |
| 3 | Zigzag I, Dasher I | Speed pressure begins |
| 4 | Shooter I | Introduce projectile threats |
| 5 | Basic I, Shooter I, Zigzag I | First mixed wave |
| 6 | Swarm I | Overwhelming numbers — tests movement |
| 7 | Bomber I, Tank I | Slow but dangerous combo |
| 8 | Orbiter I, Homing I | Positional pressure from all angles |
| 9 | Ghost I, Dasher I | Hard to track + fast closers |
| 10 | Spread I, Shooter I | Heavy projectile wave |
| 11 | Splitter I, Swarm I | Chaotic split + swarm combo |
| 12 | All Level I types | Gauntlet before the boss |
| **BOSS** | **👑 The Swarm Queen** | Fits thematically after a Swarm-heavy act |

---

### Act 2 — Level II Enemies (Waves 13–24)

| Wave | Enemies | Notes |
|------|---------|-------|
| 13 | Basic II | Reintroduction at higher speed |
| 14 | Basic II, Zigzag II | Faster movement pressure |
| 15 | Dasher II, Zigzag II | Aggressive closers |
| 16 | Shooter II, Homing II | Smarter projectiles |
| 17 | Basic II, Shooter II, Spread II | Dense projectile wave |
| 18 | Swarm II | Larger swarm groups than Act 1 |
| 19 | Tank II, Bomber II | High durability + explosion hazards |
| 20 | Orbiter II, Ghost II | Disorienting combo |
| 21 | Splitter II, Dasher II | Splits flood screen + dashers close in |
| 22 | Homing II, Spread II | Near-unavoidable projectile wave |
| 23 | Ghost II, Swarm II | Invisible threats amid chaos |
| 24 | All Level II types | Gauntlet before the boss |
| **BOSS** | **🏰 The Fortress** | Massive, multi-turret — fits the tank-heavy act |

---

### Act 3 — Level III Enemies (Waves 25–36)

| Wave | Enemies | Notes |
|------|---------|-------|
| 25 | Basic III, Zigzag III | Everything is faster now |
| 26 | Dasher III, Shooter III | Fast and aggressive |
| 27 | Spread III, Homing III | Brutal projectile density |
| 28 | Tank III | Extremely tanky — tests sustained damage |
| 29 | Swarm III, Orbiter III | Screen completely filled |
| 30 | Ghost III, Dasher III | Fast invisible threats |
| 31 | Bomber III, Tank III | Explosion spam + durability |
| 32 | Splitter III, Swarm III | Most chaotic wave yet |
| 33 | Shooter III, Spread III, Homing III | Pure projectile hell |
| 34 | Ghost III, Orbiter III, Zigzag III | Movement nightmare |
| 35 | All Level III types (half spawn) | Controlled chaos warmup |
| 36 | All Level III types (full spawn) | Final gauntlet |
| **BOSS** | **💥 The Splitter King** | Escalating chaos mirrors the act's intensity |

---

### Final Act — Endgame

| Stage | Description |
|-------|-------------|
| **Boss Rush** | Cycle through all previous bosses back to back with short breather waves between |
| **The Phantom** | Second-to-last boss — tests reaction and tracking |
| **The Mimic** | Final boss — mirrors the player; the ultimate skill check |
| **Endless Mode** | After completion, waves repeat with increasing spawn rates and mixed levels |



