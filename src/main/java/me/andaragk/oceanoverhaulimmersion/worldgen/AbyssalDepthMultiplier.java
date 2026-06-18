package me.andaragk.oceanoverhaulimmersion.worldgen;

import com.mojang.serialization.MapCodec;
import me.andaragk.oceanoverhaulimmersion.config.OOICommonConfig;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public final class AbyssalDepthMultiplier implements DensityFunction {
    public static final MapCodec<AbyssalDepthMultiplier> DATA_CODEC = MapCodec.unit(AbyssalDepthMultiplier::new);
    public static final KeyDispatchDataCodec<AbyssalDepthMultiplier> CODEC_HOLDER = KeyDispatchDataCodec.of(DATA_CODEC);

    @Override
    public double compute(FunctionContext context) {
        return OOICommonConfig.worldgenDepthMultiplier(AbyssalOceanRegion.isAbyssalRegion(context.blockX(), context.blockZ()));
    }

    @Override
    public void fillArray(double[] values, ContextProvider contextProvider) {
        contextProvider.fillAllDirectly(values, this);
    }

    @Override
    public DensityFunction mapAll(Visitor visitor) {
        return visitor.apply(this);
    }

    @Override
    public double minValue() {
        return 1.0D;
    }

    @Override
    public double maxValue() {
        return 4.0D;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC_HOLDER;
    }
}
