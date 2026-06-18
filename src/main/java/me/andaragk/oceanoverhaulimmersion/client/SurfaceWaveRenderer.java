package me.andaragk.oceanoverhaulimmersion.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.List;
import me.andaragk.oceanoverhaulimmersion.OceanOverhaulImmersion;
import me.andaragk.oceanoverhaulimmersion.client.surface.OceanSurfaceSample;
import me.andaragk.oceanoverhaulimmersion.client.surface.OceanSurfaceSampler;
import me.andaragk.oceanoverhaulimmersion.client.surface.ProceduralWaveField;
import me.andaragk.oceanoverhaulimmersion.config.OOIClientConfig;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RenderLevelStageEvent;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import org.joml.Matrix4f;

@EventBusSubscriber(modid = OceanOverhaulImmersion.MOD_ID, value = Dist.CLIENT)
public final class SurfaceWaveRenderer {
    private static List<OceanSurfaceSample> waveSamples = List.of();
    private static int ticksUntilRefresh;

    private SurfaceWaveRenderer() {
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        if (!OOIClientConfig.enableImmersion || !OOIClientConfig.enableSurfaceWaves) {
            waveSamples = List.of();
            return;
        }

        if (ticksUntilRefresh-- > 0) {
            return;
        }

        ticksUntilRefresh = 8;
        waveSamples = OceanSurfaceSampler.sampleAroundPlayer(Minecraft.getInstance());
    }

    @SubscribeEvent
    public static void onRenderLevelStage(RenderLevelStageEvent event) {
        if (!OOIClientConfig.enableImmersion || !OOIClientConfig.enableSurfaceWaves || waveSamples.isEmpty()) {
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
        VertexConsumer consumer = minecraft.renderBuffers().bufferSource().getBuffer(RenderType.translucent());
        TextureAtlasSprite waterSprite = minecraft.getModelManager().getBlockModelShaper().getBlockModel(net.minecraft.world.level.block.Blocks.WATER.defaultBlockState()).getParticleIcon();

        for (OceanSurfaceSample sample : waveSamples) {
            renderTile(consumer, pose, sample, waterSprite, minecraft.level, time, alpha);
        }

        minecraft.renderBuffers().bufferSource().endBatch(RenderType.translucent());
        poseStack.popPose();
    }

    private static void renderTile(VertexConsumer consumer, Matrix4f pose, OceanSurfaceSample sample, TextureAtlasSprite sprite, ClientLevel level, float time, float alpha) {
        float x0 = sample.x();
        float x1 = sample.x() + sample.size();
        float z0 = sample.z();
        float z1 = sample.z() + sample.size();
        float y = sample.y();
        float r = 0.86F;
        float g = 0.96F;
        float b = 1.0F;
        float u0 = sprite.getU(Math.floorMod((int) x0, 16) / 16.0F);
        float u1 = sprite.getU(Math.floorMod((int) x1, 16) / 16.0F);
        float v0 = sprite.getV(Math.floorMod((int) z0, 16) / 16.0F);
        float v1 = sprite.getV(Math.floorMod((int) z1, 16) / 16.0F);
        int light = LevelRenderer.getLightColor(level, BlockPos.containing(x0, y, z0));

        vertex(consumer, pose, x0, y + ProceduralWaveField.height(sample, x0, z0, time), z0, r, g, b, alpha, u0, v0, light);
        vertex(consumer, pose, x0, y + ProceduralWaveField.height(sample, x0, z1, time), z1, r, g, b, alpha, u0, v1, light);
        vertex(consumer, pose, x1, y + ProceduralWaveField.height(sample, x1, z1, time), z1, r, g, b, alpha, u1, v1, light);
        vertex(consumer, pose, x1, y + ProceduralWaveField.height(sample, x1, z0, time), z0, r, g, b, alpha, u1, v0, light);
    }

    private static void vertex(VertexConsumer consumer, Matrix4f pose, float x, float y, float z, float red, float green, float blue, float alpha, float u, float v, int light) {
        consumer.addVertex(pose, x, y, z)
                .setColor(red, green, blue, alpha)
                .setUv(u, v)
                .setLight(light)
                .setNormal(0.0F, 1.0F, 0.0F);
    }

}
