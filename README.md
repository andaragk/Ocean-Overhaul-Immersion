# Ocean Overhaul: Immersion

Ocean Overhaul: Immersion transforms Minecraft water into a more atmospheric environment without adding exploration content.

## MVP 0.1.x

- Client-side underwater depth tracking.
- Vertical water-surface search on the Y axis only.
- Ocean-inspired immersion zones:
  - Littoral / epipelagic: 1-15 blocks below surface.
  - Mesopelagic / bathyal: 16-45 blocks below surface.
  - Bathypelagic / midnight: 46-75 blocks below surface.
  - Abyssopelagic: 76-110 blocks below surface.
  - Hadal: 111+ blocks below surface.
- Progressive underwater fog color and visibility per zone, with smoothed transitions.
- Experimental optional abyssal depth generation for newly generated deep-ocean chunks.
- Experimental optional hadal trench generation, dependent on abyssal depth generation.
- In-game configuration screen exposed through the NeoForge mod list config button.
- Optional Calm Bubbles behavior: bubble columns remain visible but no longer push or pull entities.
- PaperWorks API 0.0.1-alpha integration: Ocean Overhaul publishes its ocean immersion state through the first Axis Mundi API alpha.

## Design Boundaries

This mod does not add mobs, structures, loot, ores, equipment, bosses, complex biomes, hardcore oxygen systems, or a complete weather system.

Surface waves, wakes, particles, and sediments are reserved for later versions.

## Experimental Worldgen

The common config contains:

- `enable_preabyssal_and_abyssal_depth`
- `enable_hadal_trenches`
- `abyssal_floor_y`, default `-40`
- `hadal_floor_y`, default `-60`

Both are disabled by default. Enable them only on a new test world for now. The first option allows some deep-ocean columns to descend toward abyssal depths. The second adds rarer hadal trench columns below those abyssal areas.

The current experimental generator avoids raising the water above the vanilla ocean surface and only affects newly generated deep-ocean chunks that look safely offshore. Final bathymetry still needs a proper noise/worldgen integration before publication.

The current prototype caps per-chunk edits to avoid full square chunks being carved during testing.

## Shader Notes

The current underwater fog hook affects the vanilla/NeoForge fog pipeline. Shader packs such as BSL through Iris may replace that pipeline, so the visual changes can be reduced or invisible while shaders are enabled. Shader-aware immersion is tracked as a separate compatibility task.

## License

Apache-2.0. This is an original mod by andaragk.
