package me.andaragk.oceanoverhaulimmersion.worldgen;

import me.andaragk.oceanoverhaulimmersion.OceanOverhaulImmersion;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.RegisterEvent;

public final class OOIDensityFunctions {
    private OOIDensityFunctions() {
    }

    public static void register(RegisterEvent event) {
        event.register(Registries.DENSITY_FUNCTION_TYPE, helper -> helper.register(
                ResourceLocation.fromNamespaceAndPath(OceanOverhaulImmersion.MOD_ID, "abyssal_depth_multiplier"),
                AbyssalDepthMultiplier.DATA_CODEC));
        event.register(Registries.DENSITY_FUNCTION_TYPE, helper -> helper.register(
                ResourceLocation.fromNamespaceAndPath(OceanOverhaulImmersion.MOD_ID, "abyssal_ocean_region"),
                AbyssalOceanRegion.DATA_CODEC));
    }
}
