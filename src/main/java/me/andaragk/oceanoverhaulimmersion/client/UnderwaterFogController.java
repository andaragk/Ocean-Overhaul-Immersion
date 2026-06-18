package me.andaragk.oceanoverhaulimmersion.client;

import com.mojang.blaze3d.shaders.FogShape;
import me.andaragk.oceanoverhaulimmersion.OceanOverhaulImmersion;
import me.andaragk.oceanoverhaulimmersion.client.depth.DepthZone;
import me.andaragk.oceanoverhaulimmersion.client.depth.WaterDepthState;
import me.andaragk.oceanoverhaulimmersion.config.OOIClientConfig;
import net.minecraft.world.level.material.FogType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ViewportEvent;

@EventBusSubscriber(modid = OceanOverhaulImmersion.MOD_ID, value = Dist.CLIENT)
public final class UnderwaterFogController {
    private static AppliedFogState lastAppliedFogState = AppliedFogState.NONE;

    private UnderwaterFogController() {
    }

    @SubscribeEvent
    public static void onRenderFog(ViewportEvent.RenderFog event) {
        WaterDepthState state = OceanImmersionClient.waterDepthState();
        if (event.getType() == FogType.WATER && !state.underwater()) {
            state = OceanImmersionClient.refreshNow();
        }
        if (!shouldApply(state) || event.getType() != FogType.WATER) {
            return;
        }

        FogProfile profile = FogProfile.forDepth(state.depth());
        float strength = (float) OOIClientConfig.fogStrength;
        float farDistance = lerp(event.getFarPlaneDistance(), profile.farDistance(), strength);
        float nearDistance = lerp(event.getNearPlaneDistance(), profile.nearDistance(), strength);

        event.setNearPlaneDistance(nearDistance);
        event.setFarPlaneDistance(Math.max(nearDistance + 0.25F, farDistance));
        event.setFogShape(FogShape.SPHERE);
        event.setCanceled(true);
        lastAppliedFogState = new AppliedFogState(true, state.depth(), state.zone(), nearDistance, Math.max(nearDistance + 0.25F, farDistance), profile.red(), profile.green(), profile.blue());
    }

    @SubscribeEvent
    public static void onFogColor(ViewportEvent.ComputeFogColor event) {
        WaterDepthState state = OceanImmersionClient.waterDepthState();
        if (!state.underwater()) {
            state = OceanImmersionClient.refreshNow();
        }
        if (!shouldApply(state)) {
            lastAppliedFogState = AppliedFogState.NONE;
            return;
        }

        FogProfile profile = FogProfile.forDepth(state.depth());
        float strength = (float) OOIClientConfig.fogStrength;
        event.setRed(lerp(event.getRed(), profile.red(), strength));
        event.setGreen(lerp(event.getGreen(), profile.green(), strength));
        event.setBlue(lerp(event.getBlue(), profile.blue(), strength));
    }

    public static AppliedFogState lastAppliedFogState() {
        return lastAppliedFogState;
    }

    private static boolean shouldApply(WaterDepthState state) {
        return OOIClientConfig.enableImmersion && OOIClientConfig.enableUnderwaterFog && state.underwater();
    }

    private static float lerp(float from, float to, float factor) {
        float clamped = Math.max(0.0F, Math.min(1.0F, factor));
        return from + (to - from) * clamped;
    }

    private record FogProfile(float nearDistance, float farDistance, float red, float green, float blue) {
        static FogProfile forDepth(int depth) {
            if (depth < DepthZone.BATHYAL.startDepth()) {
                return blend(new FogProfile(-6.0F, 48.0F, 0.13F, 0.52F, 0.66F), new FogProfile(-5.0F, 38.0F, 0.10F, 0.42F, 0.56F), depth / 15.0F);
            }
            if (depth < DepthZone.MIDNIGHT.startDepth()) {
                return blend(new FogProfile(-5.0F, 38.0F, 0.10F, 0.42F, 0.56F), new FogProfile(-4.0F, 24.0F, 0.045F, 0.22F, 0.34F), (depth - 16.0F) / 29.0F);
            }
            if (depth < DepthZone.ABYSSAL.startDepth()) {
                return blend(new FogProfile(-4.0F, 24.0F, 0.045F, 0.22F, 0.34F), new FogProfile(-3.0F, 13.0F, 0.012F, 0.060F, 0.13F), (depth - 46.0F) / 29.0F);
            }
            if (depth < DepthZone.HADAL.startDepth()) {
                return blend(new FogProfile(-3.0F, 13.0F, 0.012F, 0.060F, 0.13F), new FogProfile(-2.0F, 7.0F, 0.002F, 0.015F, 0.035F), (depth - 76.0F) / 34.0F);
            }
            return blend(new FogProfile(-2.0F, 7.0F, 0.002F, 0.015F, 0.035F), new FogProfile(-1.5F, 5.0F, 0.001F, 0.006F, 0.015F), Math.min(1.0F, (depth - 111.0F) / 16.0F));
        }

        private static FogProfile blend(FogProfile from, FogProfile to, float progress) {
            float eased = smoothstep(Math.max(0.0F, Math.min(1.0F, progress)));
            return new FogProfile(
                    lerp(from.nearDistance, to.nearDistance, eased),
                    lerp(from.farDistance, to.farDistance, eased),
                    lerp(from.red, to.red, eased),
                    lerp(from.green, to.green, eased),
                    lerp(from.blue, to.blue, eased)
            );
        }

        private static float smoothstep(float value) {
            return value * value * (3.0F - 2.0F * value);
        }
    }

    public record AppliedFogState(boolean active, int depth, DepthZone zone, float nearDistance, float farDistance, float red, float green, float blue) {
        public static final AppliedFogState NONE = new AppliedFogState(false, 0, DepthZone.SURFACE, 0.0F, 0.0F, 0.0F, 0.0F, 0.0F);
    }
}
