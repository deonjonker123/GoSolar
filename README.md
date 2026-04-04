# GoSolar

A lightweight, no-nonsense RF solar generation and storage mod for Minecraft 1.21.1 (NeoForge).

No multiblocks. No complex machines. No inverters. Just panels, batteries, and wireless power.

---

## Features

- Five tiers of solar panels, from basic early-game generation to serious endgame power
- Five tiers of batteries with built-in item charging and draining slots
- Wireless energy transmission via the Energy Transmitter and five tiers of Energy Receivers
- Jade support for all blocks
- Compatible with any RF-based energy system (Ender IO, Pipez, and others)

---

## Blocks

### Solar Panels

Solar panels generate RF when exposed to direct skylight during the day. Generation stops at night, during thunderstorms, and when the sky above is blocked.

| Panel | Generation |
|---|---|
| Basic Solar Panel | 128 RF/t |
| Hardened Solar Panel | 512 RF/t |
| Advanced Solar Panel | 2,048 RF/t |
| Elite Solar Panel | 8,192 RF/t |
| Ultimate Solar Panel | 32,768 RF/t |

### Batteries

Batteries store RF and can charge or drain powered items placed in their slots. Stored RF persists when the block is broken and picked up.

| Battery | Capacity | Transfer Rate |
|---|---|---|
| Basic Battery | 2,000,000 RF | 1,024 RF/t |
| Hardened Battery | 8,000,000 RF | 4,096 RF/t |
| Advanced Battery | 32,000,000 RF | 16,384 RF/t |
| Elite Battery | 64,000,000 RF | 65,536 RF/t |
| Ultimate Battery | 128,000,000 RF | 262,144 RF/t |

### Energy Transmitter

The Energy Transmitter accepts RF from any side and feeds it into the global wireless network pool. The pool is shared across all dimensions and persists across server restarts. Maximum pool capacity is 2,147,483,647 RF. The transmitter also supports item charging and draining via its GUI.

### Energy Receivers

Energy Receivers pull RF from the global wireless network pool and push it into any adjacent energy-accepting block. No pairing, no channels — if there's power in the pool, receivers distribute it.

| Receiver | Transfer Rate |
|---|---|
| Basic Energy Receiver | 1,024 RF/t |
| Hardened Energy Receiver | 4,096 RF/t |
| Advanced Energy Receiver | 16,384 RF/t |
| Elite Energy Receiver | 65,536 RF/t |
| Ultimate Energy Receiver | 262,144 RF/t |

---

## Compatibility

GoSolar works with any mod that uses the standard RF/FE energy API. Tested and working with Ender IO conduits and Pipez pipes. Designed for singleplayer and private modpacks — no access control on the wireless network by design.

---

## Notes

- Solar panels only generate power with a clear line of sight to the sky
- Thunderstorms stop solar generation entirely
- Battery RF is stored in the item's data component and survives breaking and replacing
- The wireless network pool is global and cross-dimensional — one transmitter serves all receivers everywhere
- This mod is intentionally lightweight and does not aim to coexist with heavy energy mods like Mekanism