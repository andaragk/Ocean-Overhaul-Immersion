package me.andaragk.oceanoverhaulimmersion.client;

import me.andaragk.oceanoverhaulimmersion.OceanOverhaulImmersion;
import me.andaragk.oceanoverhaulimmersion.client.depth.DepthZone;
import me.andaragk.oceanoverhaulimmersion.client.depth.WaterDepthAnalyzer;
import me.andaragk.oceanoverhaulimmersion.client.depth.WaterDepthState;
import me.andaragk.oceanoverhaulimmersion.config.OOIClientConfig;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;

@EventBusSubscriber(modid = OceanOverhaulImmersion.MOD_ID, value = Dist.CLIENT)
public final class OceanImmersionClient {
    private static WaterDepthState waterDepthState = WaterDepthState.DRY;
    private static DepthZone lastLoggedZone = DepthZone.SURFACE;
    private static int ticksUntilUpdate;

    private OceanImmersionClient() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (!OOIClientConfig.enableImmersion) {
            waterDepthState = WaterDepthState.DRY;
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        boolean isCurrentlyUnderwater = minecraft.player != null && minecraft.player.isEyeInFluid(net.minecraft.tags.FluidTags.WATER);
        if (isCurrentlyUnderwater != waterDepthState.underwater()) {
            refresh(minecraft);
            ticksUntilUpdate = OOIClientConfig.depthUpdateIntervalTicks;
            return;
        }

        if (ticksUntilUpdate-- > 0) {
            return;
        }

        ticksUntilUpdate = OOIClientConfig.depthUpdateIntervalTicks;
        refresh(minecraft);
    }

    public static WaterDepthState refreshNow() {
        Minecraft minecraft = Minecraft.getInstance();
        refresh(minecraft);
        return waterDepthState;
    }

    private static void refresh(Minecraft minecraft) {
        waterDepthState = WaterDepthAnalyzer.analyze(minecraft.player);
        if (OOIClientConfig.enableDepthDebugLog && waterDepthState.zone() != lastLoggedZone) {
            lastLoggedZone = waterDepthState.zone();
            OceanOverhaulImmersion.LOGGER.info("Ocean depth zone: {} ({} blocks below surface)", waterDepthState.zone().id(), waterDepthState.depth());
        }
    }

    public static WaterDepthState waterDepthState() {
        return waterDepthState;
    }
}
