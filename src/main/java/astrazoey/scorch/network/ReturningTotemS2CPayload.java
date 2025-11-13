package astrazoey.scorch.network;

import astrazoey.scorch.Scorch;
import net.minecraft.item.ItemStack;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record ReturningTotemS2CPayload(ItemStack stack) implements CustomPayload {

    public static final Identifier RETURNING_TOTEM_PAYLOAD_ID = Scorch.id("returning_totem");
    public static final CustomPayload.Id<ReturningTotemS2CPayload> ID = new CustomPayload.Id<>(RETURNING_TOTEM_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, ReturningTotemS2CPayload> CODEC = PacketCodec.tuple(ItemStack.PACKET_CODEC, ReturningTotemS2CPayload::stack, ReturningTotemS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
