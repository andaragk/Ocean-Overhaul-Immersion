package me.andaragk.oceanoverhaulimmersion.compat.paperworks;

import java.util.EnumSet;
import java.util.Set;
import me.andaragk.oceanoverhaulimmersion.OceanOverhaulImmersion;
import me.andaragk.oceanoverhaulimmersion.client.OceanImmersionClient;
import me.andaragk.oceanoverhaulimmersion.client.depth.DepthZone;
import me.andaragk.oceanoverhaulimmersion.client.depth.WaterDepthState;
import me.andaragk.paperworks.api.ocean.OceanCondition;
import me.andaragk.paperworks.api.ocean.OceanDepthBand;
import me.andaragk.paperworks.api.ocean.OceanImmersionState;
import me.andaragk.paperworks.api.ocean.PaperWorksOcean;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;

@EventBusSubscriber(modid = OceanOverhaulImmersion.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class PaperWorksOceanBridge {
    private PaperWorksOceanBridge() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        PaperWorksOcean.registerProvider(PaperWorksOceanBridge::currentOceanState);
    }

    private static OceanImmersionState currentOceanState() {
        WaterDepthState state = OceanImmersionClient.waterDepthState();
        if (!state.underwater()) {
            return OceanImmersionState.DRY;
        }

        OceanDepthBand band = toPaperWorksDepthBand(state.zone());
        float normalizedDepth = Math.min(1.0F, state.depth() / 127.0F);
        return new OceanImmersionState(
                true,
                state.surfaceY(),
                state.eyeY(),
                state.depth(),
                band,
                normalizedDepth,
                darknessFor(band),
                isolationFor(band),
                conditionsFor(band)
        );
    }

    private static OceanDepthBand toPaperWorksDepthBand(DepthZone zone) {
        return switch (zone) {
            case SURFACE -> OceanDepthBand.SURFACE;
            case LITTORAL -> OceanDepthBand.LITTORAL_EPIPELAGIC;
            case BATHYAL -> OceanDepthBand.MESOPELAGIC_BATHYAL;
            case MIDNIGHT -> OceanDepthBand.BATHYPELAGIC_MIDNIGHT;
            case ABYSSAL -> OceanDepthBand.ABYSSOPELAGIC;
            case HADAL -> OceanDepthBand.HADAL;
        };
    }

    private static float darknessFor(OceanDepthBand band) {
        return switch (band) {
            case SURFACE -> 0.0F;
            case LITTORAL_EPIPELAGIC -> 0.1F;
            case MESOPELAGIC_BATHYAL -> 0.35F;
            case BATHYPELAGIC_MIDNIGHT -> 0.65F;
            case ABYSSOPELAGIC -> 0.85F;
            case HADAL -> 1.0F;
        };
    }

    private static float isolationFor(OceanDepthBand band) {
        return switch (band) {
            case SURFACE -> 0.0F;
            case LITTORAL_EPIPELAGIC -> 0.15F;
            case MESOPELAGIC_BATHYAL -> 0.35F;
            case BATHYPELAGIC_MIDNIGHT -> 0.7F;
            case ABYSSOPELAGIC, HADAL -> 1.0F;
        };
    }

    private static Set<OceanCondition> conditionsFor(OceanDepthBand band) {
        EnumSet<OceanCondition> conditions = EnumSet.of(OceanCondition.AQUATIC, OceanCondition.UNDERWATER);
        if (band.ordinal() >= OceanDepthBand.MESOPELAGIC_BATHYAL.ordinal()) {
            conditions.add(OceanCondition.FILTERED_LIGHT);
        }
        if (band.ordinal() >= OceanDepthBand.BATHYPELAGIC_MIDNIGHT.ordinal()) {
            conditions.add(OceanCondition.LOW_VISIBILITY);
            conditions.add(OceanCondition.ISOLATED);
        }
        if (band.ordinal() >= OceanDepthBand.ABYSSOPELAGIC.ordinal()) {
            conditions.add(OceanCondition.PRESSURIZED);
        }
        return Set.copyOf(conditions);
    }
}
