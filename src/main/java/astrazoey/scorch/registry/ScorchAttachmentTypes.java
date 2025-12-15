package astrazoey.scorch.registry;

import astrazoey.scorch.Scorch;
import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.entity.data.TrackedDataHandler;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;

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

    public static TrackedDataHandler<Integer> HAIR_STYLE = registerFabricTrackedData(Scorch.id("hair_style"), TrackedDataHandlerRegistry.INTEGER);

    public static <T> TrackedDataHandler<T> registerFabricTrackedData(Identifier id, TrackedDataHandler<T> trackedDataHandler) {
        FabricTrackedDataRegistry.register(id, trackedDataHandler);
        return (TrackedDataHandler<T>) FabricTrackedDataRegistry.get(id);
    }
}
