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

    private static final ModConfigSpec.IntValue DEPTH_UPDATE_INTERVAL_TICKS = BUILDER
            .comment("How often the client recalculates underwater depth. Higher values are lighter, lower values react faster.")
            .defineInRange("depth_update_interval_ticks", 10, 1, 100);

    private static final ModConfigSpec.IntValue MAX_SURFACE_SEARCH_DISTANCE = BUILDER
            .comment("Maximum vertical blocks searched above the player to find the water surface.")
            .defineInRange("max_surface_search_distance", 140, 8, 256);

    private static final ModConfigSpec.DoubleValue FOG_STRENGTH = BUILDER
            .comment("Global multiplier for Ocean Overhaul underwater fog intensity.")
            .defineInRange("fog_strength", 1.0D, 0.0D, 2.0D);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enableImmersion;
    public static boolean enableUnderwaterFog;
    public static boolean enableUnderwaterSound;
    public static boolean enableDepthDebugLog;
    public static int depthUpdateIntervalTicks;
    public static int maxSurfaceSearchDistance;
    public static double fogStrength;

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
        depthUpdateIntervalTicks = DEPTH_UPDATE_INTERVAL_TICKS.get();
        maxSurfaceSearchDistance = MAX_SURFACE_SEARCH_DISTANCE.get();
        fogStrength = FOG_STRENGTH.get();
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

    public static void save() {
        SPEC.save();
    }
}
