package me.andaragk.oceanoverhaulimmersion.client.surface;

import me.andaragk.oceanoverhaulimmersion.config.OOIClientConfig;

public final class ProceduralWaveField {
    private ProceduralWaveField() {
    }

    public static float height(OceanSurfaceSample sample, float x, float z, float time) {
        float baseHeight = (float) OOIClientConfig.surfaceWaveHeight * sample.waveStrength();
        float scale = (float) OOIClientConfig.surfaceWaveScale;
        float dirX = sample.directionX();
        float dirZ = sample.directionZ();
        float sideX = -dirZ;
        float sideZ = dirX;

        float towardShore = x * dirX + z * dirZ;
        float alongShore = x * sideX + z * sideZ;
        float swell = (float) Math.sin(towardShore / scale + time * 0.105F) * 0.58F;
        float chop = (float) Math.sin((towardShore * 0.65F + alongShore * 0.35F) / (scale * 0.55F) + time * 0.18F) * 0.27F;
        float cross = (float) Math.sin((x * 0.41F - z * 0.73F) / (scale * 0.9F) + time * 0.075F) * 0.15F;

        return (swell + chop + cross) * baseHeight;
    }
}
