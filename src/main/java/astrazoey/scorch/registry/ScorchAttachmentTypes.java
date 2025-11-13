package astrazoey.scorch.registry;

import astrazoey.scorch.Scorch;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.network.codec.PacketCodecs;

@SuppressWarnings("UnstableApiUsage")
public class ScorchAttachmentTypes {

    public static final AttachmentType<Boolean> STRIDER_HAIR_STATE = AttachmentRegistry.create(
            Scorch.id("hair_state"),
            builder -> builder
                    .initializer(() -> true)
                    .persistent(Codec.BOOL)
                    .syncWith(PacketCodecs.BOOLEAN, AttachmentSyncPredicate.all())
    );

    public static final AttachmentType<Integer> STRIDER_HAIR_STYLE = AttachmentRegistry.create(
            Scorch.id("hair_style"),
            builder -> builder
                    .initializer(() -> 0)
                    .persistent(Codec.INT)
                    .syncWith(PacketCodecs.VAR_INT, AttachmentSyncPredicate.all())
    );

    public static final AttachmentType<Integer> STRIDER_HAIR_GROWTH = AttachmentRegistry.create(
            Scorch.id("hair_growth"),
            builder -> builder
                    .initializer(() -> 0)
                    .persistent(Codec.INT)
                    .syncWith(PacketCodecs.VAR_INT, AttachmentSyncPredicate.all())
    );
}
