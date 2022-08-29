package de.ambertation.lib.math.sdf.shapes;

import de.ambertation.lib.math.Float3;
import de.ambertation.lib.math.Transform;
import de.ambertation.lib.math.sdf.SDF;
import de.ambertation.lib.math.sdf.interfaces.Rotatable;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.util.KeyDispatchDataCodec;

// https://iquilezles.org/articles/distfunctions/
public class Cylinder extends BaseShape implements Rotatable {
    public static final Transform DEFAULT_TRANSFORM = Transform.of(Float3.of(0, 0, 0), Float3.of(5, 8, 5));
    public static final Codec<Cylinder> DIRECT_CODEC = RecordCodecBuilder.create(instance -> instance
            .group(
                    Transform.CODEC.fieldOf("transform").orElse(Transform.IDENTITY).forGetter(o -> o.transform),
                    Codec.INT.fieldOf("material").orElse(0).forGetter(BaseShape::getMaterialIndex)
            )
            .apply(instance, Cylinder::new)
    );

    public static final KeyDispatchDataCodec<Cylinder> CODEC = KeyDispatchDataCodec.of(DIRECT_CODEC);

    @Override
    public KeyDispatchDataCodec<? extends SDF> codec() {
        return CODEC;
    }


    //-------------------------------------------------------------------------------
    public Cylinder(Transform t, int matIndex) {
        super(t, matIndex);
    }

    public Cylinder(Transform t) {
        this(t, 0);
    }


    public Cylinder(Float3 center, double height, double radius) {
        this(Transform.of(center, Float3.of(2 * radius, height, 2 * radius)), 0);
    }

    public double getHeight() {
        return transform.size.y;
    }


    public double getRadius() {
        return Math.abs(Math.min(transform.size.x, transform.size.z) / 2);
    }


    @Override
    public double dist(Float3 p) {
        p = getParentTransformMatrix().inverted().transform(p);
        p = p.sub(transform.center).unRotate(transform.rotation).div(transform.size);
//        Float2 d = Float2.of(p.xz().length(), p.y).abs().sub(Float2.of(.5, .5));
//        return Math.min(d.maxComp(), 0.0) + d.max(0.0).length();
        return Math.max(p.xz().length() - 0.5, Math.abs(p.y) - 0.5);
    }

    @Override
    public Transform defaultTransform() {
        return DEFAULT_TRANSFORM;
    }
}