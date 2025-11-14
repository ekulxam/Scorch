package astrazoey.scorch.mixin.strider;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.impl.attachment.AttachmentSerializingImpl;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.IdentityHashMap;

@SuppressWarnings("UnstableApiUsage")
@Mixin(value = AttachmentSerializingImpl.class, remap = false)
public interface AttachmentSerializingImplAccessor {

    @Accessor("CODEC")
    static Codec<IdentityHashMap<AttachmentType<?>, Object>> scorch$getCodec() {
        throw new UnsupportedOperationException("Mixin accessor");
    }
}
