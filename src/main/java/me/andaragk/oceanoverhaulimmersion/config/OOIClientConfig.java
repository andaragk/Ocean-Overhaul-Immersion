package me.andaragk.oceanoverhaulimmersion.config;

import me.andaragk.oceanoverhaulimmersion.OceanOverhaulImmersion;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = OceanOverhaulImmersion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class OOIClientConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ENABLE_IMMERSION = BUILDER
            .comment("Master switch for client-side ocean immersion effects.")
            .define("enable_immersion", true);

    private static final ModConfigSpec.BooleanValue ENABLE_UNDERWATER_FOG = BUILDER
            .comment("Enables progressive underwater visibility and fog changes.")
            .define("enable_underwater_fog", true);

    private static final ModConfigSpec.BooleanValue ENABLE_UNDERWATER_SOUND = BUILDER
            .comment("Reserved for the underwater sound controller. Kept enabled for the MVP state model; advanced audio changes arrive later.")
            .define("enable_underwater_sound", true);

    private static final ModConfigSpec.BooleanValue ENABLE_DEPTH_DEBUG_LOG = BUILDER
            .comment("Logs depth zone changes on the client. Useful for testing, disabled by default.")
            .define("enable_depth_debug_log", false);

    private static final ModConfigSpec.BooleanValue ENABLE_SHADER_DIAGNOSTIC_OVERLAY = BUILDER
            .comment(
                    "Shows a small in-game diagnostic overlay for underwater fog and shader state.",
                    "Useful to verify whether Ocean Overhaul is applying fog values while a shader pack is active.")
            .define("enable_shader_diagnostic_overlay", false);

    private static final ModConfigSpec.IntValue DEPTH_UPDATE_INTERVAL_TICKS = BUILDER
            .comment("How often the client recalculates underwater depth. Higher values are lighter, lower values react faster.")
            .defineInRange("depth_update_interval_ticks", 10, 1, 100);

    private static final ModConfigSpec.IntValue MAX_SURFACE_SEARCH_DISTANCE = BUILDER
            .comment("Maximum vertical blocks searched above the player to find the water surface.")
            .defineInRange("max_surface_search_distance", 140, 8, 256);

    private static final ModConfigSpec.DoubleValue FOG_STRENGTH = BUILDER
            .comment("Global multiplier for Ocean Overhaul underwater fog intensity.")
            .defineInRange("fog_strength", 1.0D, 0.0D, 2.0D);

    private static final ModConfigSpec.BooleanValue ENABLE_SURFACE_WAVES = BUILDER
            .comment(
                    "Enables lightweight client-side visual waves on nearby water surfaces.",
                    "This is a visual overlay only: it does not change water physics, collisions, or world state.")
            .define("enable_surface_waves", true);

    private static final ModConfigSpec.DoubleValue SURFACE_WAVE_HEIGHT = BUILDER
            .comment("Visual wave height in blocks.")
            .defineInRange("surface_wave_height", 0.08D, 0.0D, 0.5D);

    private static final ModConfigSpec.DoubleValue SURFACE_WAVE_SPEED = BUILDER
            .comment("Visual wave animation speed.")
            .defineInRange("surface_wave_speed", 1.0D, 0.0D, 4.0D);

    private static final ModConfigSpec.DoubleValue SURFACE_WAVE_SCALE = BUILDER
            .comment("Horizontal scale of visual waves. Higher values create broader waves.")
            .defineInRange("surface_wave_scale", 10.0D, 2.0D, 48.0D);

    private static final ModConfigSpec.IntValue SURFACE_WAVE_RADIUS = BUILDER
            .comment("Radius around the player where visual waves are sampled and rendered.")
            .defineInRange("surface_wave_radius", 18, 4, 48);

    private static final ModConfigSpec.IntValue SURFACE_WAVE_GRID_STEP = BUILDER
            .comment("Spacing between sampled wave quads. Higher values are lighter but less detailed.")
            .defineInRange("surface_wave_grid_step", 2, 1, 6);

    private static final ModConfigSpec.DoubleValue SURFACE_WAVE_OPACITY = BUILDER
            .comment("Opacity of the visual wave highlight overlay.")
            .defineInRange("surface_wave_opacity", 0.20D, 0.0D, 0.75D);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enableImmersion;
    public static boolean enableUnderwaterFog;
    public static boolean enableUnderwaterSound;
    public static boolean enableDepthDebugLog;
    public static boolean enableShaderDiagnosticOverlay;
    public static int depthUpdateIntervalTicks;
    public static int maxSurfaceSearchDistance;
    public static double fogStrength;
    public static boolean enableSurfaceWaves;
    public static double surfaceWaveHeight;
    public static double surfaceWaveSpeed;
    public static double surfaceWaveScale;
    public static int surfaceWaveRadius;
    public static int surfaceWaveGridStep;
    public static double surfaceWaveOpacity;

    private OOIClientConfig() {
    }

    @SubscribeEvent
    static void onLoad(ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) {
            return;
        }

        enableImmersion = ENABLE_IMMERSION.get();
        enableUnderwaterFog = ENABLE_UNDERWATER_FOG.get();
        enableUnderwaterSound = ENABLE_UNDERWATER_SOUND.get();
        enableDepthDebugLog = ENABLE_DEPTH_DEBUG_LOG.get();
        enableShaderDiagnosticOverlay = ENABLE_SHADER_DIAGNOSTIC_OVERLAY.get();
        depthUpdateIntervalTicks = DEPTH_UPDATE_INTERVAL_TICKS.get();
        maxSurfaceSearchDistance = MAX_SURFACE_SEARCH_DISTANCE.get();
        fogStrength = FOG_STRENGTH.get();
        enableSurfaceWaves = ENABLE_SURFACE_WAVES.get();
        surfaceWaveHeight = SURFACE_WAVE_HEIGHT.get();
        surfaceWaveSpeed = SURFACE_WAVE_SPEED.get();
        surfaceWaveScale = SURFACE_WAVE_SCALE.get();
        surfaceWaveRadius = SURFACE_WAVE_RADIUS.get();
        surfaceWaveGridStep = SURFACE_WAVE_GRID_STEP.get();
        surfaceWaveOpacity = SURFACE_WAVE_OPACITY.get();
    }

    public static void setEnableImmersion(boolean value) {
        ENABLE_IMMERSION.set(value);
        enableImmersion = value;
    }

    public static void setEnableUnderwaterFog(boolean value) {
        ENABLE_UNDERWATER_FOG.set(value);
        enableUnderwaterFog = value;
    }

    public static void setEnableUnderwaterSound(boolean value) {
        ENABLE_UNDERWATER_SOUND.set(value);
        enableUnderwaterSound = value;
    }

    public static void setEnableDepthDebugLog(boolean value) {
        ENABLE_DEPTH_DEBUG_LOG.set(value);
        enableDepthDebugLog = value;
    }

    public static void setEnableShaderDiagnosticOverlay(boolean value) {
        ENABLE_SHADER_DIAGNOSTIC_OVERLAY.set(value);
        enableShaderDiagnosticOverlay = value;
    }

    public static void setDepthUpdateIntervalTicks(int value) {
        depthUpdateIntervalTicks = Math.max(1, Math.min(100, value));
        DEPTH_UPDATE_INTERVAL_TICKS.set(depthUpdateIntervalTicks);
    }

    public static void setMaxSurfaceSearchDistance(int value) {
        maxSurfaceSearchDistance = Math.max(8, Math.min(256, value));
        MAX_SURFACE_SEARCH_DISTANCE.set(maxSurfaceSearchDistance);
    }

    public static void setFogStrength(double value) {
        fogStrength = Math.max(0.0D, Math.min(2.0D, value));
        FOG_STRENGTH.set(fogStrength);
    }

    public static void setEnableSurfaceWaves(boolean value) {
        ENABLE_SURFACE_WAVES.set(value);
        enableSurfaceWaves = value;
    }

    public static void setSurfaceWaveHeight(double value) {
        surfaceWaveHeight = Math.max(0.0D, Math.min(0.5D, value));
        SURFACE_WAVE_HEIGHT.set(surfaceWaveHeight);
    }

    public static void setSurfaceWaveSpeed(double value) {
        surfaceWaveSpeed = Math.max(0.0D, Math.min(4.0D, value));
        SURFACE_WAVE_SPEED.set(surfaceWaveSpeed);
    }

    public static void setSurfaceWaveScale(double value) {
        surfaceWaveScale = Math.max(2.0D, Math.min(48.0D, value));
        SURFACE_WAVE_SCALE.set(surfaceWaveScale);
    }

    public static void setSurfaceWaveRadius(int value) {
        surfaceWaveRadius = Math.max(4, Math.min(48, value));
        SURFACE_WAVE_RADIUS.set(surfaceWaveRadius);
    }

    public static void setSurfaceWaveGridStep(int value) {
        surfaceWaveGridStep = Math.max(1, Math.min(6, value));
        SURFACE_WAVE_GRID_STEP.set(surfaceWaveGridStep);
    }

    public static void setSurfaceWaveOpacity(double value) {
        surfaceWaveOpacity = Math.max(0.0D, Math.min(0.75D, value));
        SURFACE_WAVE_OPACITY.set(surfaceWaveOpacity);
    }

    public static void save() {
        SPEC.save();
    }
}
