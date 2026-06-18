package me.andaragk.oceanoverhaulimmersion.config;

import me.andaragk.oceanoverhaulimmersion.OceanOverhaulImmersion;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = OceanOverhaulImmersion.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public final class OOICommonConfig {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final ModConfigSpec.BooleanValue ENABLE_PREABYSSAL_AND_ABYSSAL_DEPTH = BUILDER
            .comment(
                    "Experimental worldgen module: makes vanilla deep-ocean biomes behave as true deep oceans without turning them into abyssal ocean biomes.",
                    "Disabled by default for maximum worldgen compatibility. Test on new worlds first.")
            .define("enable_true_deep_oceans", false);

    private static final ModConfigSpec.BooleanValue ENABLE_HADAL_TRENCHES = BUILDER
            .comment(
                    "Experimental worldgen module: allows rare oceanic trenches below abyssal areas, enabling hadal immersion zones.",
                    "Requires enable_preabyssal_and_abyssal_depth. Test on new worlds first.")
            .define("enable_hadal_trenches", false);

    private static final ModConfigSpec.BooleanValue ENABLE_CALM_BUBBLES = BUILDER
            .comment(
                    "Disables bubble-column push and pull while keeping the bubble columns visually present.",
                    "This integrates the Calm Bubbles behavior into Ocean Overhaul: Immersion.")
            .define("enable_calm_bubbles", false);

    private static final ModConfigSpec.BooleanValue ENABLE_ABYSSAL_OCEANS = BUILDER
            .comment(
                    "Experimental Lithostitched biome module: allows rare abyssal ocean biomes to replace parts of vanilla deep-ocean biomes.",
                    "Requires enable_true_deep_oceans and Lithostitched at runtime.")
            .define("enable_abyssal_oceans", false);

    private static final ModConfigSpec.IntValue ABYSSAL_FLOOR_Y = BUILDER
            .comment(
                    "Experimental target floor Y for abyssal plains.",
                    "With vanilla sea level near Y=63, Y=-40 gives about 103 blocks of depth: abyssal, but not quite hadal.")
            .defineInRange("abyssal_floor_y", -40, -47, -13);

    private static final ModConfigSpec.IntValue HADAL_FLOOR_Y = BUILDER
            .comment(
                    "Experimental target floor Y for rare hadal trenches.",
                    "Vanilla 1.21 bottoms out near Y=-64, so this should stay between Y=-63 and Y=-48.")
            .defineInRange("hadal_floor_y", -60, -63, -48);

    private static final ModConfigSpec.IntValue ABYSSAL_CHANCE_PERCENT = BUILDER
            .comment("Experimental chance per deep-ocean column to become abyssal. Lower is safer for testing.")
            .defineInRange("abyssal_chance_percent", 22, 0, 100);

    private static final ModConfigSpec.IntValue HADAL_CHANCE_PERCENT = BUILDER
            .comment("Experimental chance per abyssal column to become hadal trench. Requires hadal trenches enabled.")
            .defineInRange("hadal_chance_percent", 4, 0, 100);

    private static final ModConfigSpec.DoubleValue ABYSSAL_DEPTH_MULTIPLIER = BUILDER
            .comment(
                    "Experimental Lithostitched worldgen depth multiplier for regular deep-ocean basins.",
                    "1.0 keeps vanilla shape. 2.25 keeps deep oceans impressive without turning all of them into abyssal oceans.",
                    "Requires Lithostitched at runtime to affect terrain generation.")
            .defineInRange("deep_ocean_depth_multiplier", 2.25D, 1.0D, 3.0D);

    private static final ModConfigSpec.DoubleValue ABYSSAL_OCEAN_DEPTH_MULTIPLIER = BUILDER
            .comment(
                    "Experimental Lithostitched worldgen depth multiplier for dedicated abyssal ocean biomes.",
                    "Only applies where the abyssal-ocean regional mask is active.")
            .defineInRange("abyssal_ocean_depth_multiplier", 3.25D, 1.0D, 4.0D);

    private static final ModConfigSpec.IntValue ABYSSAL_OCEAN_RARITY_PERCENT = BUILDER
            .comment(
                    "Approximate share of eligible deep-ocean regions that may become abyssal oceans.",
                    "Lower values keep abyssal oceans rare and far apart.")
            .defineInRange("abyssal_ocean_rarity_percent", 12, 0, 100);

    private static final ModConfigSpec.IntValue ABYSSAL_REGION_SCALE_BLOCKS = BUILDER
            .comment(
                    "Approximate X/Z scale of abyssal-ocean regions, in blocks.",
                    "Larger values create wider abyssal basins instead of small pockets.")
            .defineInRange("abyssal_region_scale_blocks", 768, 128, 4096);

    private static final ModConfigSpec.IntValue ABYSSAL_REGION_SMOOTHNESS_PERCENT = BUILDER
            .comment(
                    "Softens abyssal-region borders by mixing nearby regional noise.",
                    "Higher values make basins feel less spotty while remaining deterministic.")
            .defineInRange("abyssal_region_smoothness_percent", 65, 0, 100);

    private static final ModConfigSpec.BooleanValue ENABLE_LARGE_ABYSSAL_BASINS = BUILDER
            .comment(
                    "Favors broad abyssal basins over compact abyssal pockets.",
                    "This is still lightweight: it only changes the regional mask scale.")
            .define("enable_large_abyssal_basins", true);

    public static final ModConfigSpec SPEC = BUILDER.build();

    public static boolean enablePreabyssalAndAbyssalDepth;
    public static boolean enableHadalTrenches;
    public static boolean enableCalmBubbles;
    public static boolean enableAbyssalOceans;
    public static int abyssalFloorY;
    public static int hadalFloorY;
    public static int abyssalChancePercent;
    public static int hadalChancePercent;
    public static double abyssalDepthMultiplier;
    public static double abyssalOceanDepthMultiplier;
    public static int abyssalOceanRarityPercent;
    public static int abyssalRegionScaleBlocks;
    public static int abyssalRegionSmoothnessPercent;
    public static boolean enableLargeAbyssalBasins;

    private OOICommonConfig() {
    }

    @SubscribeEvent
    static void onLoad(ModConfigEvent event) {
        if (event.getConfig().getSpec() != SPEC) {
            return;
        }

        enablePreabyssalAndAbyssalDepth = ENABLE_PREABYSSAL_AND_ABYSSAL_DEPTH.get();
        enableHadalTrenches = ENABLE_HADAL_TRENCHES.get() && enablePreabyssalAndAbyssalDepth;
        enableCalmBubbles = ENABLE_CALM_BUBBLES.get();
        enableAbyssalOceans = ENABLE_ABYSSAL_OCEANS.get() && enablePreabyssalAndAbyssalDepth;
        abyssalFloorY = ABYSSAL_FLOOR_Y.get();
        hadalFloorY = HADAL_FLOOR_Y.get();
        abyssalChancePercent = ABYSSAL_CHANCE_PERCENT.get();
        hadalChancePercent = HADAL_CHANCE_PERCENT.get();
        abyssalDepthMultiplier = ABYSSAL_DEPTH_MULTIPLIER.get();
        abyssalOceanDepthMultiplier = ABYSSAL_OCEAN_DEPTH_MULTIPLIER.get();
        abyssalOceanRarityPercent = ABYSSAL_OCEAN_RARITY_PERCENT.get();
        abyssalRegionScaleBlocks = ABYSSAL_REGION_SCALE_BLOCKS.get();
        abyssalRegionSmoothnessPercent = ABYSSAL_REGION_SMOOTHNESS_PERCENT.get();
        enableLargeAbyssalBasins = ENABLE_LARGE_ABYSSAL_BASINS.get();
    }

    public static void setEnablePreabyssalAndAbyssalDepth(boolean value) {
        ENABLE_PREABYSSAL_AND_ABYSSAL_DEPTH.set(value);
        enablePreabyssalAndAbyssalDepth = value;
        if (!value) {
            setEnableHadalTrenches(false);
            setEnableAbyssalOceans(false);
        }
    }

    public static void setEnableHadalTrenches(boolean value) {
        enableHadalTrenches = value && enablePreabyssalAndAbyssalDepth;
        ENABLE_HADAL_TRENCHES.set(enableHadalTrenches);
    }

    public static void setEnableCalmBubbles(boolean value) {
        ENABLE_CALM_BUBBLES.set(value);
        enableCalmBubbles = value;
    }

    public static void setEnableAbyssalOceans(boolean value) {
        enableAbyssalOceans = value && enablePreabyssalAndAbyssalDepth;
        ENABLE_ABYSSAL_OCEANS.set(enableAbyssalOceans);
    }

    public static void setAbyssalFloorY(int value) {
        abyssalFloorY = Math.max(-60, Math.min(20, value));
        ABYSSAL_FLOOR_Y.set(abyssalFloorY);
    }

    public static void setHadalFloorY(int value) {
        hadalFloorY = Math.max(-63, Math.min(-48, value));
        HADAL_FLOOR_Y.set(hadalFloorY);
    }

    public static void setAbyssalChancePercent(int value) {
        abyssalChancePercent = Math.max(0, Math.min(100, value));
        ABYSSAL_CHANCE_PERCENT.set(abyssalChancePercent);
    }

    public static void setHadalChancePercent(int value) {
        hadalChancePercent = Math.max(0, Math.min(100, value));
        HADAL_CHANCE_PERCENT.set(hadalChancePercent);
    }

    public static void setAbyssalDepthMultiplier(double value) {
        abyssalDepthMultiplier = Math.max(1.0D, Math.min(3.0D, value));
        ABYSSAL_DEPTH_MULTIPLIER.set(abyssalDepthMultiplier);
    }

    public static void setAbyssalOceanDepthMultiplier(double value) {
        abyssalOceanDepthMultiplier = Math.max(1.0D, Math.min(4.0D, value));
        ABYSSAL_OCEAN_DEPTH_MULTIPLIER.set(abyssalOceanDepthMultiplier);
    }

    public static void setAbyssalOceanRarityPercent(int value) {
        abyssalOceanRarityPercent = Math.max(0, Math.min(100, value));
        ABYSSAL_OCEAN_RARITY_PERCENT.set(abyssalOceanRarityPercent);
    }

    public static void setAbyssalRegionScaleBlocks(int value) {
        abyssalRegionScaleBlocks = Math.max(128, Math.min(4096, value));
        ABYSSAL_REGION_SCALE_BLOCKS.set(abyssalRegionScaleBlocks);
    }

    public static void setAbyssalRegionSmoothnessPercent(int value) {
        abyssalRegionSmoothnessPercent = Math.max(0, Math.min(100, value));
        ABYSSAL_REGION_SMOOTHNESS_PERCENT.set(abyssalRegionSmoothnessPercent);
    }

    public static void setEnableLargeAbyssalBasins(boolean value) {
        enableLargeAbyssalBasins = value;
        ENABLE_LARGE_ABYSSAL_BASINS.set(value);
    }

    public static double worldgenDepthMultiplier() {
        return enablePreabyssalAndAbyssalDepth ? abyssalDepthMultiplier : 1.0D;
    }

    public static double worldgenDepthMultiplier(boolean abyssalRegion) {
        if (!enablePreabyssalAndAbyssalDepth) {
            return 1.0D;
        }
        return abyssalRegion && enableAbyssalOceans ? abyssalOceanDepthMultiplier : abyssalDepthMultiplier;
    }

    public static void save() {
        SPEC.save();
    }
}
