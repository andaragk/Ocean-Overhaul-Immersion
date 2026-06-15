package me.andaragk.oceanoverhaulimmersion.mixin;

import me.andaragk.oceanoverhaulimmersion.config.OOICommonConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BubbleColumnBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BubbleColumnBlock.class)
public abstract class BubbleColumnBlockMixin {
    @Inject(method = "entityInside", at = @At("HEAD"), cancellable = true)
    private void oceanoverhaulimmersion$disableBubbleColumnPushPull(BlockState state, Level level, BlockPos pos, Entity entity, CallbackInfo callbackInfo) {
        if (OOICommonConfig.enableCalmBubbles) {
            callbackInfo.cancel();
        }
    }
}
