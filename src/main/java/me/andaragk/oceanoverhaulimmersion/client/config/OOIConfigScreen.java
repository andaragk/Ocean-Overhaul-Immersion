package me.andaragk.oceanoverhaulimmersion.client.config;

import me.andaragk.oceanoverhaulimmersion.OceanOverhaulImmersion;
import me.andaragk.oceanoverhaulimmersion.config.OOIClientConfig;
import me.andaragk.oceanoverhaulimmersion.config.OOICommonConfig;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public final class OOIConfigScreen extends Screen {
    private final Screen parent;
    private int page;

    public OOIConfigScreen(Screen parent) {
        super(Component.literal(OceanOverhaulImmersion.MOD_NAME));
        this.parent = parent;
    }

    @Override
    protected void init() {
        int center = this.width / 2;
        int y = 42;

        if (page == 0) {
            addToggle(center, y, "Immersion", OOIClientConfig.enableImmersion, value -> OOIClientConfig.setEnableImmersion(value));
            y += 24;
            addToggle(center, y, "Underwater fog", OOIClientConfig.enableUnderwaterFog, value -> OOIClientConfig.setEnableUnderwaterFog(value));
            y += 24;
            addToggle(center, y, "Underwater sound", OOIClientConfig.enableUnderwaterSound, value -> OOIClientConfig.setEnableUnderwaterSound(value));
            y += 24;
            addToggle(center, y, "Calm bubbles", OOICommonConfig.enableCalmBubbles, value -> OOICommonConfig.setEnableCalmBubbles(value));
            y += 34;
            addNumber(center, y, "Fog strength", "%.2f".formatted(OOIClientConfig.fogStrength), () -> OOIClientConfig.setFogStrength(OOIClientConfig.fogStrength - 0.1D), () -> OOIClientConfig.setFogStrength(OOIClientConfig.fogStrength + 0.1D));
        } else if (page == 1) {
            addToggle(center, y, "Surface waves", OOIClientConfig.enableSurfaceWaves, value -> OOIClientConfig.setEnableSurfaceWaves(value));
            y += 24;
            addNumber(center, y, "Wave height", "%.2f".formatted(OOIClientConfig.surfaceWaveHeight), () -> OOIClientConfig.setSurfaceWaveHeight(OOIClientConfig.surfaceWaveHeight - 0.01D), () -> OOIClientConfig.setSurfaceWaveHeight(OOIClientConfig.surfaceWaveHeight + 0.01D));
            y += 24;
            addNumber(center, y, "Wave speed", "%.2f".formatted(OOIClientConfig.surfaceWaveSpeed), () -> OOIClientConfig.setSurfaceWaveSpeed(OOIClientConfig.surfaceWaveSpeed - 0.1D), () -> OOIClientConfig.setSurfaceWaveSpeed(OOIClientConfig.surfaceWaveSpeed + 0.1D));
            y += 24;
            addNumber(center, y, "Wave scale", "%.1f".formatted(OOIClientConfig.surfaceWaveScale), () -> OOIClientConfig.setSurfaceWaveScale(OOIClientConfig.surfaceWaveScale - 1.0D), () -> OOIClientConfig.setSurfaceWaveScale(OOIClientConfig.surfaceWaveScale + 1.0D));
            y += 24;
            addNumber(center, y, "Wave radius", String.valueOf(OOIClientConfig.surfaceWaveRadius), () -> OOIClientConfig.setSurfaceWaveRadius(OOIClientConfig.surfaceWaveRadius - 2), () -> OOIClientConfig.setSurfaceWaveRadius(OOIClientConfig.surfaceWaveRadius + 2));
            y += 24;
            addNumber(center, y, "Wave detail", String.valueOf(OOIClientConfig.surfaceWaveGridStep), () -> OOIClientConfig.setSurfaceWaveGridStep(OOIClientConfig.surfaceWaveGridStep - 1), () -> OOIClientConfig.setSurfaceWaveGridStep(OOIClientConfig.surfaceWaveGridStep + 1));
            y += 24;
            addNumber(center, y, "Wave opacity", "%.2f".formatted(OOIClientConfig.surfaceWaveOpacity), () -> OOIClientConfig.setSurfaceWaveOpacity(OOIClientConfig.surfaceWaveOpacity - 0.05D), () -> OOIClientConfig.setSurfaceWaveOpacity(OOIClientConfig.surfaceWaveOpacity + 0.05D));
        } else if (page == 2) {
            addToggle(center, y, "True deep oceans", OOICommonConfig.enablePreabyssalAndAbyssalDepth, value -> OOICommonConfig.setEnablePreabyssalAndAbyssalDepth(value));
            y += 24;
            addToggle(center, y, "Abyssal oceans", OOICommonConfig.enableAbyssalOceans, value -> OOICommonConfig.setEnableAbyssalOceans(value));
            y += 24;
            addToggle(center, y, "Hadal trenches", OOICommonConfig.enableHadalTrenches, value -> OOICommonConfig.setEnableHadalTrenches(value));
            y += 34;
            addNumber(center, y, "Deep ocean multiplier", "%.2f".formatted(OOICommonConfig.abyssalDepthMultiplier), () -> OOICommonConfig.setAbyssalDepthMultiplier(OOICommonConfig.abyssalDepthMultiplier - 0.05D), () -> OOICommonConfig.setAbyssalDepthMultiplier(OOICommonConfig.abyssalDepthMultiplier + 0.05D));
            y += 24;
            addNumber(center, y, "Abyssal ocean multiplier", "%.2f".formatted(OOICommonConfig.abyssalOceanDepthMultiplier), () -> OOICommonConfig.setAbyssalOceanDepthMultiplier(OOICommonConfig.abyssalOceanDepthMultiplier - 0.05D), () -> OOICommonConfig.setAbyssalOceanDepthMultiplier(OOICommonConfig.abyssalOceanDepthMultiplier + 0.05D));
            y += 24;
            addNumber(center, y, "Abyssal ocean rarity", OOICommonConfig.abyssalOceanRarityPercent + "%", () -> OOICommonConfig.setAbyssalOceanRarityPercent(OOICommonConfig.abyssalOceanRarityPercent - 1), () -> OOICommonConfig.setAbyssalOceanRarityPercent(OOICommonConfig.abyssalOceanRarityPercent + 1));
        } else if (page == 3) {
            addToggle(center, y, "Large abyssal basins", OOICommonConfig.enableLargeAbyssalBasins, value -> OOICommonConfig.setEnableLargeAbyssalBasins(value));
            y += 24;
            addNumber(center, y, "Abyssal region scale", OOICommonConfig.abyssalRegionScaleBlocks + " blocks", () -> OOICommonConfig.setAbyssalRegionScaleBlocks(OOICommonConfig.abyssalRegionScaleBlocks - 128), () -> OOICommonConfig.setAbyssalRegionScaleBlocks(OOICommonConfig.abyssalRegionScaleBlocks + 128));
            y += 24;
            addNumber(center, y, "Abyssal smoothness", OOICommonConfig.abyssalRegionSmoothnessPercent + "%", () -> OOICommonConfig.setAbyssalRegionSmoothnessPercent(OOICommonConfig.abyssalRegionSmoothnessPercent - 5), () -> OOICommonConfig.setAbyssalRegionSmoothnessPercent(OOICommonConfig.abyssalRegionSmoothnessPercent + 5));
        } else {
            addToggle(center, y, "Depth debug log", OOIClientConfig.enableDepthDebugLog, value -> OOIClientConfig.setEnableDepthDebugLog(value));
            y += 24;
            addToggle(center, y, "Shader diagnostics", OOIClientConfig.enableShaderDiagnosticOverlay, value -> OOIClientConfig.setEnableShaderDiagnosticOverlay(value));
            y += 24;
            addNumber(center, y, "Depth update ticks", String.valueOf(OOIClientConfig.depthUpdateIntervalTicks), () -> OOIClientConfig.setDepthUpdateIntervalTicks(OOIClientConfig.depthUpdateIntervalTicks - 1), () -> OOIClientConfig.setDepthUpdateIntervalTicks(OOIClientConfig.depthUpdateIntervalTicks + 1));
            y += 24;
            addNumber(center, y, "Surface search distance", String.valueOf(OOIClientConfig.maxSurfaceSearchDistance), () -> OOIClientConfig.setMaxSurfaceSearchDistance(OOIClientConfig.maxSurfaceSearchDistance - 8), () -> OOIClientConfig.setMaxSurfaceSearchDistance(OOIClientConfig.maxSurfaceSearchDistance + 8));
            y += 34;
            addNumber(center, y, "Abyssal floor Y", String.valueOf(OOICommonConfig.abyssalFloorY), () -> OOICommonConfig.setAbyssalFloorY(OOICommonConfig.abyssalFloorY - 1), () -> OOICommonConfig.setAbyssalFloorY(OOICommonConfig.abyssalFloorY + 1));
            y += 24;
            addNumber(center, y, "Hadal floor Y", String.valueOf(OOICommonConfig.hadalFloorY), () -> OOICommonConfig.setHadalFloorY(OOICommonConfig.hadalFloorY - 1), () -> OOICommonConfig.setHadalFloorY(OOICommonConfig.hadalFloorY + 1));
            y += 24;
            addNumber(center, y, "Abyssal chance", OOICommonConfig.abyssalChancePercent + "%", () -> OOICommonConfig.setAbyssalChancePercent(OOICommonConfig.abyssalChancePercent - 1), () -> OOICommonConfig.setAbyssalChancePercent(OOICommonConfig.abyssalChancePercent + 1));
            y += 24;
            addNumber(center, y, "Hadal chance", OOICommonConfig.hadalChancePercent + "%", () -> OOICommonConfig.setHadalChancePercent(OOICommonConfig.hadalChancePercent - 1), () -> OOICommonConfig.setHadalChancePercent(OOICommonConfig.hadalChancePercent + 1));
        }

        addNavigation(center);
    }

    private void addNavigation(int center) {
        int y = this.height - 30;
        this.addRenderableWidget(Button.builder(Component.literal("<"), button -> {
            page = Math.max(0, page - 1);
            rebuildWidgets();
        }).bounds(center - 155, y, 36, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal(pageLabel()), button -> {
        }).bounds(center - 115, y, 110, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal(">"), button -> {
            page = Math.min(4, page + 1);
            rebuildWidgets();
        }).bounds(center - 1, y, 36, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("Done"), button -> saveAndClose())
                .bounds(center + 45, y, 110, 20)
                .build());
    }

    private void addToggle(int center, int y, String label, boolean value, BooleanSetter setter) {
        this.addRenderableWidget(Button.builder(toggleText(label, value), button -> {
            setter.set(!value);
            rebuildWidgets();
        }).bounds(center - 155, y, 310, 20).build());
    }

    private void addNumber(int center, int y, String label, String value, Runnable decrement, Runnable increment) {
        this.addRenderableWidget(Button.builder(Component.literal("-"), button -> {
            decrement.run();
            rebuildWidgets();
        }).bounds(center - 155, y, 24, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal(label + ": " + value), button -> {
        }).bounds(center - 127, y, 254, 20).build());

        this.addRenderableWidget(Button.builder(Component.literal("+"), button -> {
            increment.run();
            rebuildWidgets();
        }).bounds(center + 131, y, 24, 20).build());
    }

    private Component toggleText(String label, boolean value) {
        return Component.literal(label + ": " + (value ? "ON" : "OFF"));
    }

    private String pageLabel() {
        return switch (page) {
            case 0 -> "Immersion 1/5";
            case 1 -> "Surface 2/5";
            case 2 -> "Worldgen 3/5";
            case 3 -> "Abyssal 4/5";
            default -> "Advanced 5/5";
        };
    }

    private String pageHint() {
        return page == 2 || page == 3 ? "Worldgen changes affect newly generated chunks." : "";
    }

    private void saveAndClose() {
        OOIClientConfig.save();
        OOICommonConfig.save();
        this.minecraft.setScreen(parent);
    }

    @Override
    public void onClose() {
        saveAndClose();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTick) {
        super.render(graphics, mouseX, mouseY, partialTick);
        graphics.drawCenteredString(this.font, this.title, this.width / 2, 15, 0xFFFFFF);
        graphics.drawCenteredString(this.font, Component.literal(pageHint()), this.width / 2, 28, 0xA0A0A0);
    }

    private interface BooleanSetter {
        void set(boolean value);
    }
}
