package astrazoey.scorch.network;

import astrazoey.scorch.Scorch;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

public record CurseExposureS2CPayload(int curseExposure) implements CustomPayload {

    public static final Identifier CURSE_EXPOSURE_PAYLOAD_ID = Scorch.id("curse_exposure");
    public static final CustomPayload.Id<CurseExposureS2CPayload> ID = new CustomPayload.Id<>(CURSE_EXPOSURE_PAYLOAD_ID);
    public static final PacketCodec<RegistryByteBuf, CurseExposureS2CPayload> CODEC = PacketCodec.tuple(PacketCodecs.INTEGER, CurseExposureS2CPayload::curseExposure, CurseExposureS2CPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }
}
