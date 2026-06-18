package me.andaragk.oceanoverhaulimmersion.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ArrayList;
import java.util.List;
import me.andaragk.oceanoverhaulimmersion.OceanOverhaulImmersion;
import me.andaragk.oceanoverhaulimmersion.config.OOIClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = OceanOverhaulImmersion.MOD_ID, value = Dist.CLIENT)
public final class SurfaceWaveRenderer {
    private static final List<WaveTile> WAVE_TILES = new ArrayList<>();
    private static int ticksUntilRefresh;

    private SurfaceWaveRenderer() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (!OOIClientConfig.enableImmersion || !OOIClientConfig.enableSurfaceWaves) {
            WAVE_TILES.clear();
            return;
        }

        if (ticksUntilRefresh-- > 0) {
            return;
        }

        ticksUntilRefresh = 8;
        rebuildTiles(Minecraft.getInstance());
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (!OOIClientConfig.enableImmersion || !OOIClientConfig.enableSurfaceWaves || WAVE_TILES.isEmpty()) {
            return;
        }
        if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_TRANSLUCENT_BLOCKS) {
            return;
        }

        Minecraft minecraft = Minecraft.getInstance();
        if (minecraft.level == null || minecraft.player == null) {
            return;
        }

        Vec3 camera = event.getCamera().getPosition();
        float time = (minecraft.level.getGameTime() + event.getPartialTick().getGameTimeDeltaPartialTick(false)) * (float) OOIClientConfig.surfaceWaveSpeed;
        float alpha = (float) OOIClientConfig.surfaceWaveOpacity;
        if (alpha <= 0.0F) {
            return;
        }

        PoseStack poseStack = event.getPoseStack();
        poseStack.pushPose();
        poseStack.translate(-camera.x, -camera.y, -camera.z);
        Matrix4f pose = poseStack.last().pose();
        VertexConsumer consumer = minecraft.renderBuffers().bufferSource().getBuffer(RenderType.debugQuads());

        for (WaveTile tile : WAVE_TILES) {
            renderTile(consumer, pose, tile, time, alpha);
        }

        minecraft.renderBuffers().bufferSource().endBatch(RenderType.debugQuads());
        poseStack.popPose();
    }

    private static void rebuildTiles(Minecraft minecraft) {
        WAVE_TILES.clear();
        if (minecraft.level == null || minecraft.player == null) {
            return;
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

                WAVE_TILES.add(new WaveTile(x, surfaceY + 0.035F, z, step));
            }
        }
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

    private static void renderTile(VertexConsumer consumer, Matrix4f pose, WaveTile tile, float time, float alpha) {
        float x0 = tile.x();
        float x1 = tile.x() + tile.size();
        float z0 = tile.z();
        float z1 = tile.z() + tile.size();
        float y = tile.y();
        float r = 0.58F;
        float g = 0.86F;
        float b = 0.95F;

        vertex(consumer, pose, x0, y + wave(x0, z0, time), z0, r, g, b, alpha);
        vertex(consumer, pose, x0, y + wave(x0, z1, time), z1, r, g, b, alpha);
        vertex(consumer, pose, x1, y + wave(x1, z1, time), z1, r, g, b, alpha);
        vertex(consumer, pose, x1, y + wave(x1, z0, time), z0, r, g, b, alpha);
    }

    private static float wave(float x, float z, float time) {
        float scale = (float) OOIClientConfig.surfaceWaveScale;
        float height = (float) OOIClientConfig.surfaceWaveHeight;
        float primary = (float) Math.sin((x * 0.85F + z * 0.35F) / scale + time * 0.10F);
        float secondary = (float) Math.sin((x * -0.25F + z * 0.95F) / (scale * 0.65F) + time * 0.16F);
        return (primary * 0.65F + secondary * 0.35F) * height;
    }

    private static void vertex(VertexConsumer consumer, Matrix4f pose, float x, float y, float z, float red, float green, float blue, float alpha) {
        consumer.addVertex(pose, x, y, z).setColor(red, green, blue, alpha);
    }

    private record WaveTile(int x, float y, int z, int size) {
    }
}
