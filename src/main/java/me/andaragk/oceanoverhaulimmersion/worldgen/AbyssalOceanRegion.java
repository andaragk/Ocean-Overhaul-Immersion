package me.andaragk.oceanoverhaulimmersion.worldgen;

import com.mojang.serialization.MapCodec;
import me.andaragk.oceanoverhaulimmersion.config.OOICommonConfig;
import net.minecraft.util.KeyDispatchDataCodec;
import net.minecraft.world.level.levelgen.DensityFunction;

public final class AbyssalOceanRegion implements DensityFunction {
    public static final MapCodec<AbyssalOceanRegion> DATA_CODEC = MapCodec.unit(AbyssalOceanRegion::new);
    public static final KeyDispatchDataCodec<AbyssalOceanRegion> CODEC_HOLDER = KeyDispatchDataCodec.of(DATA_CODEC);

    @Override
    public double compute(FunctionContext context) {
        return isAbyssalRegion(context.blockX(), context.blockZ()) ? 1.0D : 0.0D;
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
        return 0.0D;
    }

    @Override
    public double maxValue() {
        return 1.0D;
    }

    @Override
    public KeyDispatchDataCodec<? extends DensityFunction> codec() {
        return CODEC_HOLDER;
    }

    public static boolean isAbyssalRegion(int blockX, int blockZ) {
        if (!OOICommonConfig.enableAbyssalOceans || OOICommonConfig.abyssalOceanRarityPercent <= 0) {
            return false;
        }
        if (OOICommonConfig.abyssalOceanRarityPercent >= 100) {
            return true;
        }

        double threshold = 1.0D - OOICommonConfig.abyssalOceanRarityPercent / 100.0D;
        return regionalNoise(blockX, blockZ) >= threshold;
    }

    private static double regionalNoise(int blockX, int blockZ) {
        double x = blockX / 256.0D;
        double z = blockZ / 256.0D;
        int ix = fastFloor(x);
        int iz = fastFloor(z);
        double fx = smoothstep(x - ix);
        double fz = smoothstep(z - iz);

        double a = value(ix, iz);
        double b = value(ix + 1, iz);
        double c = value(ix, iz + 1);
        double d = value(ix + 1, iz + 1);

        return lerp(lerp(a, b, fx), lerp(c, d, fx), fz);
    }

    private static int fastFloor(double value) {
        int i = (int) value;
        return value < i ? i - 1 : i;
    }

    private static double value(int x, int z) {
        int hashed = hash(x, z, 0x4F3A2B1C);
        return (hashed & 0x7FFFFFFF) / (double) 0x7FFFFFFF;
    }

    private static double smoothstep(double value) {
        return value * value * (3.0D - 2.0D * value);
    }

    private static double lerp(double from, double to, double factor) {
        return from + (to - from) * factor;
    }

    private static int hash(int x, int z, int salt) {
        int value = x * 73428767 ^ z * 912931 ^ salt;
        value ^= value >>> 13;
        value *= 1274126177;
        value ^= value >>> 16;
        return value;
    }
}
