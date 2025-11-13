package astrazoey.scorch.network;

import astrazoey.scorch.Scorch;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record CurseActiveS2CPayload(int curseActive) implements CustomPayload {

    public static final Identifier CURSE_ACTIVE_PAYLOAD_ID = Scorch.id("curse_active");
    public static final Id<CurseActiveS2CPayload> ID = new Id<>(CURSE_ACTIVE_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, CurseActiveS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, CurseActiveS2CPayload::curseActive, CurseActiveS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
