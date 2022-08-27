package de.ambertation.lib.math.sdf;

import de.ambertation.lib.math.Float3;
import de.ambertation.lib.math.Transform;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

public class SDFUnion extends SDFBinaryOperation {
    public static final Codec<SDFUnion> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Transform.CODEC.fieldOf("transform").orElse(Transform.IDENTITY).forGetter(o -> o.transform),
                    SDF.CODEC.fieldOf("sdf_a").forGetter(b -> b.getFirst()),
                    SDF.CODEC.fieldOf("sdf_b").forGetter(b -> b.getSecond())
            )
            .apply(instance, SDFUnion::new)
    );

    public static final KeyDispatchDataCodec<SDFUnion> CODEC = KeyDispatchDataCodec.of(DIRECT_CODEC);

    @Override
    public KeyDispatchDataCodec<? extends SDF> codec() {
        return CODEC;
    }


    //-------------------------------------------------------------------------------
    public SDFUnion(Transform t, SDF a, SDF b) {
        super(t, a, b);
    }

    public SDFUnion(SDF a, SDF b) {
        this(Transform.IDENTITY, a, b);
    }

    @Override
    public double dist(Float3 pos) {
        return Math.min(getFirst().dist(pos), getSecond().dist(pos));
    }

    @Override
    public void dist(EvaluationData d, Float3 pos) {
        getFirst().dist(d, pos);
        final double d0 = d.dist;
        final SDF s0 = d.source();

        getSecond().dist(d, pos);
        if (d0 < d.dist) {
            d.dist = d0;
            d.source = s0;
        }
    }

    @Override
    public String toString() {
        return "(" + getFirst() + " | " + getSecond() + ")" + " [" + graphIndex + "]";
    }


}
