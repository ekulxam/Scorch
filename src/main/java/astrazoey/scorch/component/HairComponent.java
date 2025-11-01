package astrazoey.scorch.component;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;


public record HairComponent(boolean hasHair, int style, int growth) {
    public static final Codec<HairComponent> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("hasHair").forGetter(HairComponent::hasHair),
            Codec.INT.fieldOf("style").forGetter(HairComponent::style),
            Codec.INT.fieldOf("growth").forGetter(HairComponent::growth)
    ).apply(instance, HairComponent::new));
}
