package me.andaragk.oceanoverhaulimmersion.client.depth;

public record WaterDepthState(boolean underwater, int surfaceY, int eyeY, int depth, DepthZone zone) {
    public static final WaterDepthState DRY = new WaterDepthState(false, 0, 0, 0, DepthZone.SURFACE);
}
