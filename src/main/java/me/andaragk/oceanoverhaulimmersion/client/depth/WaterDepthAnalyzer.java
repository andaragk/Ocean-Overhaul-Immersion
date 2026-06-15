package me.andaragk.oceanoverhaulimmersion.client.depth;

import me.andaragk.oceanoverhaulimmersion.config.OOIClientConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public final class WaterDepthAnalyzer {
    private WaterDepthAnalyzer() {
    }

    public static WaterDepthState analyze(Player player) {
        if (player == null || player.level() == null || !player.isEyeInFluid(FluidTags.WATER)) {
            return WaterDepthState.DRY;
        }

        Level level = player.level();
        int eyeY = BlockPos.containing(player.getX(), player.getEyeY(), player.getZ()).getY();
        int surfaceY = findSurfaceY(level, player, eyeY);
        int depth = Math.max(0, surfaceY - eyeY);

        return new WaterDepthState(true, surfaceY, eyeY, depth, DepthZone.fromDepth(depth));
    }

    private static int findSurfaceY(Level level, Player player, int eyeY) {
        int maxY = Math.min(level.getMaxBuildHeight() - 1, eyeY + OOIClientConfig.maxSurfaceSearchDistance);
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos(
                (int) Math.floor(player.getX()),
                eyeY,
                (int) Math.floor(player.getZ())
        );

        int lastWaterY = eyeY;
        for (int y = eyeY; y <= maxY; y++) {
            cursor.setY(y);
            if (!level.getFluidState(cursor).is(FluidTags.WATER)) {
                return lastWaterY + 1;
            }
            lastWaterY = y;
        }

        return lastWaterY;
    }
}
