package me.andaragk.oceanoverhaulimmersion.client.surface;

public record OceanSurfaceSample(
        int x,
        float y,
        int z,
        int size,
        int depth,
        int shoreDistance,
        float waveStrength,
        float directionX,
        float directionZ
) {
}
