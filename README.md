# Ocean Overhaul: Immersion

Ocean Overhaul: Immersion transforms Minecraft water into a more atmospheric environment without turning the ocean into an exploration-content mod.

The mod focuses on perception: underwater visibility, depth, fog, light, isolation, and optional deeper ocean worldgen.

## Current Status

Version: `0.2.5`

This is an early test build for Minecraft `1.21.1` on NeoForge. The core immersion features are usable, while the abyssal worldgen layer is still experimental and should be tested on new worlds or newly generated chunks.

## Features

- Client-side underwater depth tracking.
- Depth-based immersion zones:
  - Littoral / epipelagic: 1-15 blocks below the surface.
  - Mesopelagic / bathyal: 16-45 blocks below the surface.
  - Bathypelagic / midnight: 46-75 blocks below the surface.
  - Abyssopelagic: 76-110 blocks below the surface.
  - Hadal: 111+ blocks below the surface.
- Progressive vanilla underwater fog and visibility changes.
- Smooth fog transitions when entering, leaving, or descending through water.
- Optional Calm Bubbles behavior: bubble columns stay visible but no longer push or pull entities.
- PaperWorks API alpha integration for publishing ocean immersion state.

## Experimental Worldgen

Worldgen features require Lithostitched at runtime.

- `enable_true_deep_oceans`
  - Makes vanilla deep-ocean biomes behave as true deep oceans.
  - Default depth multiplier: `2.25`.
  - This keeps deep oceans impressive without turning all of them into abyssal oceans.

- `enable_abyssal_oceans`
  - Adds dedicated abyssal ocean biome variants:
    - `oceanoverhaulimmersion:abyssal_ocean`
    - `oceanoverhaulimmersion:cold_abyssal_ocean`
    - `oceanoverhaulimmersion:frozen_abyssal_ocean`
    - `oceanoverhaulimmersion:lukewarm_abyssal_ocean`
  - Default depth multiplier: `3.25`.
  - Default regional rarity: `12%`.

- `enable_hadal_trenches`
  - Still experimental.
  - Reserved for future large, rare, smoother hadal trench shapes.

## Ocean Monuments

Abyssal ocean biomes are added to the vanilla `#minecraft:is_deep_ocean` biome tag so other mods can still recognize them as true deep ocean biomes.

Ocean monument spawning is blocked separately by overriding `#minecraft:has_structure/ocean_monument` to list only the vanilla deep-ocean biomes. This keeps abyssal oceans deep-ocean compatible without making them monument spawn biomes.

## Shader Notes

The current fog system modifies the vanilla/NeoForge fog pipeline. Shader packs such as BSL through Iris can override or reinterpret that pipeline, so shader behavior may differ from vanilla.

Observed shader darkening at depth should be treated cautiously until tested with controlled comparisons: same location, same time, same depth, shader on/off, with screenshots or video.

## Design Boundaries

This mod does not add mobs, structures, loot, ores, equipment, bosses, hardcore oxygen systems, or a complete weather system.

Surface waves, wakes, particles, sediments, and shader-specific integration are planned as later work, not part of the current stable core.

## License

Apache-2.0. This is an original mod by andaragk.
