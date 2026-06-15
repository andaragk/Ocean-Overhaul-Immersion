package me.andaragk.oceanoverhaulimmersion.client.config;

import me.andaragk.oceanoverhaulimmersion.OceanOverhaulImmersion;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModList;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@EventBusSubscriber(modid = OceanOverhaulImmersion.MOD_ID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public final class OOIConfigScreenRegistration {
    private OOIConfigScreenRegistration() {
    }

    @SubscribeEvent
    public static void onClientSetup(FMLClientSetupEvent event) {
        IConfigScreenFactory factory = (modContainer, parent) -> new OOIConfigScreen(parent);
        ModList.get().getModContainerById(OceanOverhaulImmersion.MOD_ID)
                .ifPresent(container -> container.registerExtensionPoint(IConfigScreenFactory.class, factory));
    }
}
