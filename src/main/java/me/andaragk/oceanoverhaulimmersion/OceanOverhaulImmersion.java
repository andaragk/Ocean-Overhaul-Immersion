package me.andaragk.oceanoverhaulimmersion;

import com.mojang.logging.LogUtils;
import me.andaragk.oceanoverhaulimmersion.config.OOIClientConfig;
import me.andaragk.oceanoverhaulimmersion.config.OOICommonConfig;
import me.andaragk.oceanoverhaulimmersion.worldgen.OOIDensityFunctions;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import org.slf4j.Logger;

@Mod(OceanOverhaulImmersion.MOD_ID)
public final class OceanOverhaulImmersion {
    public static final String MOD_ID = "oceanoverhaulimmersion";
    public static final String MOD_NAME = "Ocean Overhaul: Immersion";
    public static final Logger LOGGER = LogUtils.getLogger();

    public OceanOverhaulImmersion(IEventBus modEventBus, ModContainer modContainer) {
        modContainer.registerConfig(ModConfig.Type.CLIENT, OOIClientConfig.SPEC);
        modContainer.registerConfig(ModConfig.Type.COMMON, OOICommonConfig.SPEC);
        modEventBus.addListener(OOIDensityFunctions::register);
    }
}
