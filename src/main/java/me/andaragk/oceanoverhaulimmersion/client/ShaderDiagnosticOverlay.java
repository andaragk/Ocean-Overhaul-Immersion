package me.andaragk.oceanoverhaulimmersion.client;

import java.util.Locale;
import me.andaragk.oceanoverhaulimmersion.OceanOverhaulImmersion;
import me.andaragk.oceanoverhaulimmersion.client.depth.WaterDepthState;
import me.andaragk.oceanoverhaulimmersion.config.OOIClientConfig;
import me.andaragk.paperworks.api.shader.PaperWorksShaders;
import me.andaragk.paperworks.api.shader.ShaderState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

@EventBusSubscriber(modid = OceanOverhaulImmersion.MOD_ID, value = Dist.CLIENT)
public final class ShaderDiagnosticOverlay {
    private ShaderDiagnosticOverlay() {
    }

    @SubscribeEvent
    public static void onRenderGui(RenderGuiEvent.Post event) {
        if (!OOIClientConfig.enableShaderDiagnosticOverlay) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.player == null || minecraft.options.hideGui) {
            return;
        }

        WaterDepthState depthState = OceanImmersionClient.waterDepthState();
        if (!depthState.underwater()) {
            return;
        }

        ShaderState shaderState = PaperWorksShaders.currentState();
        UnderwaterFogController.AppliedFogState fogState = UnderwaterFogController.lastAppliedFogState();
        GuiGraphics graphics = event.getGuiGraphics();
        int x = 8;
        int y = 8;
        int color = 0xDDEEFF;

        draw(graphics, x, y, "OOI shader diagnostic", 0xFFFFFF);
        y += 10;
        draw(graphics, x, y, "depth=" + depthState.depth() + " zone=" + depthState.zone().id(), color);
        y += 10;
        draw(graphics, x, y, "shader=" + shaderState.backendId() + " active=" + shaderState.shaderPackActive(), color);
        y += 10;
        draw(graphics, x, y, "custom uniforms=" + shaderState.customAquaticParametersSupported(), color);
        y += 10;
        draw(graphics, x, y, "fog applied=" + fogState.active() + " near=" + fmt(fogState.nearDistance()) + " far=" + fmt(fogState.farDistance()), color);
        y += 10;
        draw(graphics, x, y, "fog rgb=" + fmt(fogState.red()) + "," + fmt(fogState.green()) + "," + fmt(fogState.blue()), color);
    }

    private static void draw(GuiGraphics graphics, int x, int y, String text, int color) {
        graphics.drawString(Minecraft.getInstance().font, Component.literal(text), x, y, color, true);
    }

    private static String fmt(float value) {
        return String.format(Locale.ROOT, "%.3f", value);
    }
}
