# Ocean Overhaul: Immersion

Ocean Overhaul: Immersion transforms Minecraft water into a more atmospheric environment without turning the ocean into an exploration-content mod.

The mod focuses on perception: underwater visibility, depth, fog, light, isolation, and optional deeper ocean worldgen.

## Current Status

Version: `0.3.2`

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
- Experimental client-side visual wave overlay, disabled by default.
- Optional Calm Bubbles behavior: bubble columns stay visible but no longer push or pull entities.
- PaperWorks API alpha integration for publishing ocean immersion state.

## Surface Waves

The first `0.3.x` surface module includes an experimental animated wave overlay around the player.

This overlay is mainly a diagnostic prototype. It proves that Ocean Overhaul can animate water-adjacent visuals, but it is not the intended final wave rendering path. The final wave system should integrate deeper into water rendering or shader-aware rendering so it does not look like a separate mesh placed on top of the ocean.

- `enable_surface_waves`
  - Legacy name for the first prototype.
  - Replaced internally by `enable_experimental_wave_overlay` so older local configs do not accidentally keep the visible overlay enabled.

- `enable_experimental_wave_overlay`
  - Enables or disables the experimental visual wave overlay.
  - Disabled by default.
  - Client-side only.
  - Does not modify collisions, hitboxes, water physics, or server state.

- `surface_wave_height`
  - Controls the vertical amplitude of the visual waves.

- `surface_wave_speed`
  - Controls wave animation speed.

- `surface_wave_scale`
  - Controls horizontal wave width.

- `surface_wave_radius`
  - Controls how far from the player wave surfaces are sampled.

- `surface_wave_grid_step`
  - Controls sampling/detail. Lower values are more detailed but heavier.

- `surface_wave_opacity`
  - Controls the visibility of the visual overlay.

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
  - Default regional scale: `768` blocks.
  - Large basin mode is enabled by default to favor broad abyssal ocean areas instead of compact pockets.
  - Regional smoothness is configurable to reduce spotty biome borders.

- `enable_hadal_trenches`
  - Still experimental.
  - Reserved for future large, rare, smoother hadal trench shapes.

## Ocean Monuments

Abyssal ocean biomes are added to the vanilla `#minecraft:is_deep_ocean` biome tag so other mods can still recognize them as true deep ocean biomes.

Ocean monument spawning is blocked separately by overriding `#minecraft:has_structure/ocean_monument` to list only the vanilla deep-ocean biomes. This keeps abyssal oceans deep-ocean compatible without making them monument spawn biomes.

## Shader Notes

The current fog system modifies the vanilla/NeoForge fog pipeline. Shader packs such as BSL through Iris can override or reinterpret that pipeline, so shader behavior may differ from vanilla.

Observed shader darkening at depth should be treated cautiously until tested with controlled comparisons: same location, same time, same depth, shader on/off, with screenshots or video.

The client config includes `enable_shader_diagnostic_overlay`, which displays the current Ocean Overhaul depth, computed fog values, and PaperWorks/Iris shader state while underwater. This helps separate "Ocean Overhaul is applying its fog profile" from "the active shader pack visually uses or ignores that profile".

## Design Boundaries

This mod does not add mobs, structures, loot, ores, equipment, bosses, hardcore oxygen systems, or a complete weather system.

Wakes, splashes, particles, sediments, vine physics, and shader-specific wave injection are planned as later work.

## License

Apache-2.0. This is an original mod by andaragk.
