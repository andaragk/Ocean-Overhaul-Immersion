package me.andaragk.oceanoverhaulimmersion.client.surface;

import java.util.ArrayList;
import java.util.List;
import me.andaragk.oceanoverhaulimmersion.config.OOIClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.levelgen.Heightmap;

public final class OceanSurfaceSampler {
    private static final int MAX_DEPTH_SCAN = 80;
    private static final int MAX_SHORE_SCAN = 18;

    private OceanSurfaceSampler() {
    }

    public static List<OceanSurfaceSample> sampleAroundPlayer(Minecraft minecraft) {
        List<OceanSurfaceSample> samples = new ArrayList<>();
        if (minecraft.level == null || minecraft.player == null) {
            return samples;
        }

        ClientLevel level = minecraft.level;
        BlockPos center = minecraft.player.blockPosition();
        int radius = OOIClientConfig.surfaceWaveRadius;
        int step = OOIClientConfig.surfaceWaveGridStep;
        BlockPos.MutableBlockPos cursor = new BlockPos.MutableBlockPos();

        for (int x = center.getX() - radius; x <= center.getX() + radius; x += step) {
            for (int z = center.getZ() - radius; z <= center.getZ() + radius; z += step) {
                if (center.distSqr(new BlockPos(x, center.getY(), z)) > radius * radius) {
                    continue;
                }

                int surfaceY = findWaterSurfaceY(level, x, z, cursor);
                if (surfaceY == Integer.MIN_VALUE) {
                    continue;
                }

                int depth = waterDepth(level, x, surfaceY, z, cursor);
                ShoreInfo shore = shoreInfo(level, x, surfaceY, z, cursor);
                float strength = waveStrength(depth, shore.distance());
                samples.add(new OceanSurfaceSample(x, surfaceY + 0.035F, z, step, depth, shore.distance(), strength, shore.directionX(), shore.directionZ()));
            }
        }

        return samples;
    }

    private static int findWaterSurfaceY(ClientLevel level, int x, int z, BlockPos.MutableBlockPos cursor) {
        int topY = level.getHeight(Heightmap.Types.WORLD_SURFACE, x, z) + 1;
        int bottomY = Math.max(level.getMinBuildHeight(), topY - 12);
        for (int y = topY; y >= bottomY; y--) {
            cursor.set(x, y, z);
            if (!level.getFluidState(cursor).is(FluidTags.WATER)) {
                continue;
            }

            cursor.set(x, y + 1, z);
            if (!level.getFluidState(cursor).is(FluidTags.WATER)) {
                return y;
            }
        }
        return Integer.MIN_VALUE;
    }

    private static int waterDepth(ClientLevel level, int x, int surfaceY, int z, BlockPos.MutableBlockPos cursor) {
        int depth = 0;
        int minY = Math.max(level.getMinBuildHeight(), surfaceY - MAX_DEPTH_SCAN);
        for (int y = surfaceY; y >= minY; y--) {
            cursor.set(x, y, z);
            if (!level.getFluidState(cursor).is(FluidTags.WATER)) {
                break;
            }
            depth++;
        }
        return depth;
    }

    private static ShoreInfo shoreInfo(ClientLevel level, int x, int surfaceY, int z, BlockPos.MutableBlockPos cursor) {
        int bestDistance = MAX_SHORE_SCAN + 1;
        int bestDx = 0;
        int bestDz = 0;

        for (int dx = -MAX_SHORE_SCAN; dx <= MAX_SHORE_SCAN; dx += 2) {
            for (int dz = -MAX_SHORE_SCAN; dz <= MAX_SHORE_SCAN; dz += 2) {
                int distance = Math.max(Math.abs(dx), Math.abs(dz));
                if (distance == 0 || distance >= bestDistance) {
                    continue;
                }

                cursor.set(x + dx, surfaceY, z + dz);
                if (level.getFluidState(cursor).is(FluidTags.WATER)) {
                    continue;
                }

                bestDistance = distance;
                bestDx = dx;
                bestDz = dz;
            }
        }

        if (bestDistance > MAX_SHORE_SCAN) {
            float angle = regionalDirection(x, z);
            return new ShoreInfo(MAX_SHORE_SCAN + 1, (float) Math.cos(angle), (float) Math.sin(angle));
        }

        float length = Math.max(1.0F, (float) Math.sqrt(bestDx * bestDx + bestDz * bestDz));
        return new ShoreInfo(bestDistance, bestDx / length, bestDz / length);
    }

    private static float waveStrength(int depth, int shoreDistance) {
        float depthFactor = smoothstep(clamp01((depth - 2.0F) / 18.0F));
        float shoreFactor = smoothstep(clamp01((shoreDistance - 4.0F) / 12.0F));
        return Math.max(0.15F, depthFactor * 0.65F + shoreFactor * 0.35F);
    }

    private static float regionalDirection(int x, int z) {
        int cellX = Math.floorDiv(x, 256);
        int cellZ = Math.floorDiv(z, 256);
        int hash = cellX * 73428767 ^ cellZ * 912931 ^ 0x51F15EED;
        hash ^= hash >>> 13;
        hash *= 1274126177;
        hash ^= hash >>> 16;
        return (hash & 0xFFFF) / 65535.0F * ((float) Math.PI * 2.0F);
    }

    private static float smoothstep(float value) {
        return value * value * (3.0F - 2.0F * value);
    }

    private static float clamp01(float value) {
        return Math.max(0.0F, Math.min(1.0F, value));
    }

    private record ShoreInfo(int distance, float directionX, float directionZ) {
    }
}
