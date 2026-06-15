package me.andaragk.oceanoverhaulimmersion.worldgen;

import me.andaragk.oceanoverhaulimmersion.OceanOverhaulImmersion;
import me.andaragk.oceanoverhaulimmersion.config.OOICommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.event.level.ChunkEvent;

public final class ExperimentalAbyssalDepthGenerator {
    private static final int SEA_SURFACE_Y = 63;
    private static final int TOP_WATER_Y = SEA_SURFACE_Y - 1;
    private static final int MAX_CHANGED_COLUMNS_PER_CHUNK = 96;
    private static final BlockState WATER = Blocks.WATER.defaultBlockState();
    private static final BlockState DEEPSLATE = Blocks.DEEPSLATE.defaultBlockState();

    private ExperimentalAbyssalDepthGenerator() {
    }

    @SubscribeEvent
    public static void onChunkLoad(ChunkEvent.Load event) {
        if (!OOICommonConfig.enablePreabyssalAndAbyssalDepth || !event.isNewChunk() || !(event.getLevel() instanceof ServerLevel level)) {
            return;
        }
        if (level.dimension() != Level.OVERWORLD) {
            return;
        }

        ChunkAccess chunk = event.getChunk();
        ChunkPos chunkPos = chunk.getPos();
        if (!isSafeDeepOceanChunk(chunk, chunkPos)) {
            return;
        }

        int changedColumns = deepenChunk(level, chunk);
        if (changedColumns > 0) {
            chunk.setUnsaved(true);
            OceanOverhaulImmersion.LOGGER.debug("Deepened {} abyssal ocean columns in chunk {}", changedColumns, chunkPos);
        }
    }

    private static int deepenChunk(ServerLevel level, ChunkAccess chunk) {
        ChunkPos chunkPos = chunk.getPos();
        int minY = Math.max(level.getMinBuildHeight() + 1, Math.min(OOICommonConfig.hadalFloorY, OOICommonConfig.abyssalFloorY));
        int maxTargetY = Math.min(OOICommonConfig.abyssalFloorY, SEA_SURFACE_Y - 32);
        int changedColumns = 0;
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();

        for (int localX = 0; localX < 16; localX++) {
            int worldX = chunkPos.getMinBlockX() + localX;
            for (int localZ = 0; localZ < 16; localZ++) {
                if (changedColumns >= MAX_CHANGED_COLUMNS_PER_CHUNK) {
                    return changedColumns;
                }

                int worldZ = chunkPos.getMinBlockZ() + localZ;
                if (!isDeepOcean(chunk, worldX, worldZ)) {
                    continue;
                }
                if (localTexture(worldX, worldZ) < 35) {
                    continue;
                }

                int abyssStrength = abyssStrength(worldX, worldZ);
                if (abyssStrength < 100 - OOICommonConfig.abyssalChancePercent) {
                    continue;
                }

                int floorY = targetFloorY(worldX, worldZ, maxTargetY, minY, abyssStrength);
                if (floorY >= SEA_SURFACE_Y - 32) {
                    continue;
                }

                carveColumn(chunk, pos, worldX, worldZ, floorY);
                changedColumns++;
            }
        }

        return changedColumns;
    }

    private static int targetFloorY(int worldX, int worldZ, int abyssalFloorY, int minY, int abyssStrength) {
        float intensity = Math.max(0.0F, Math.min(1.0F, (abyssStrength - (100 - OOICommonConfig.abyssalChancePercent)) / Math.max(1.0F, (float) OOICommonConfig.abyssalChancePercent)));
        int transitionFloor = Math.round(lerp(SEA_SURFACE_Y - 32, abyssalFloorY, smoothstep(intensity))) + terrainVariation(worldX, worldZ);
        int floorY = transitionFloor;

        int trenchStrength = trenchStrength(worldX, worldZ);
        if (OOICommonConfig.enableHadalTrenches && trenchStrength >= 100 - OOICommonConfig.hadalChancePercent) {
            float trenchIntensity = Math.max(0.0F, Math.min(1.0F, (trenchStrength - (100 - OOICommonConfig.hadalChancePercent)) / Math.max(1.0F, (float) OOICommonConfig.hadalChancePercent)));
            floorY = Math.round(lerp(floorY, OOICommonConfig.hadalFloorY, smoothstep(trenchIntensity))) + Math.min(2, terrainVariation(worldX + 31, worldZ - 17));
        }
        return Math.max(minY, Math.min(SEA_SURFACE_Y - 32, floorY));
    }

    private static void carveColumn(ChunkAccess chunk, BlockPos.MutableBlockPos pos, int worldX, int worldZ, int floorY) {
        for (int y = floorY + 1; y <= TOP_WATER_Y; y++) {
            pos.set(worldX, y, worldZ);
            chunk.setBlockState(pos, WATER, false);
        }

        pos.set(worldX, floorY, worldZ);
        chunk.setBlockState(pos, DEEPSLATE, false);
    }

    private static boolean isDeepOcean(ChunkAccess chunk, int worldX, int worldZ) {
        Holder<Biome> biome = chunk.getNoiseBiome(QuartPos.fromBlock(worldX), QuartPos.fromBlock(SEA_SURFACE_Y), QuartPos.fromBlock(worldZ));
        return biome.is(Tags.Biomes.IS_DEEP_OCEAN);
    }

    private static boolean isSafeDeepOceanChunk(ChunkAccess chunk, ChunkPos chunkPos) {
        return isDeepOcean(chunk, chunkPos.getMinBlockX() + 2, chunkPos.getMinBlockZ() + 2)
                && isDeepOcean(chunk, chunkPos.getMinBlockX() + 13, chunkPos.getMinBlockZ() + 2)
                && isDeepOcean(chunk, chunkPos.getMinBlockX() + 2, chunkPos.getMinBlockZ() + 13)
                && isDeepOcean(chunk, chunkPos.getMinBlockX() + 13, chunkPos.getMinBlockZ() + 13)
                && isDeepOcean(chunk, chunkPos.getMiddleBlockX(), chunkPos.getMiddleBlockZ());
    }

    private static int terrainVariation(int worldX, int worldZ) {
        return Math.floorMod(hash(worldX >> 2, worldZ >> 2, 49979687), 9) - 4;
    }

    private static int abyssStrength(int worldX, int worldZ) {
        int regional = Math.floorMod(hash(worldX >> 5, worldZ >> 5, 15485863), 100);
        int local = Math.floorMod(hash(worldX >> 2, worldZ >> 2, 86028121), 100);
        return Math.round(regional * 0.7F + local * 0.3F);
    }

    private static int trenchStrength(int worldX, int worldZ) {
        int regional = Math.floorMod(hash(worldX >> 4, worldZ >> 6, 32452843), 100);
        int local = Math.floorMod(hash(worldX >> 2, worldZ >> 2, 67867967), 100);
        return Math.round(regional * 0.75F + local * 0.25F);
    }

    private static int localTexture(int worldX, int worldZ) {
        return Math.floorMod(hash(worldX, worldZ, 104395301), 100);
    }

    private static float lerp(float from, float to, float factor) {
        return from + (to - from) * factor;
    }

    private static float smoothstep(float value) {
        return value * value * (3.0F - 2.0F * value);
    }

    private static int hash(int x, int z, int salt) {
        int value = x * 73428767 ^ z * 912931 ^ salt;
        value ^= value >>> 13;
        value *= 1274126177;
        value ^= value >>> 16;
        return value;
    }
}
