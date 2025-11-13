package astrazoey.scorch;

import astrazoey.scorch.network.CurseActiveS2CPayload;
import astrazoey.scorch.network.CurseExposureS2CPayload;
import astrazoey.scorch.network.ReturningTotemS2CPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.RenderStateDataKey;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;

public class ScorchClient implements ClientModInitializer {

    public static final RenderStateDataKey<Boolean> STRIDER_HAIR = RenderStateDataKey.create(() -> Scorch.id("strider_hair").toString());
    public static final RenderStateDataKey<Integer> STRIDER_HAIR_STYLE = RenderStateDataKey.create(() -> Scorch.id("strider_hair_style").toString());

    @SuppressWarnings("resource")
    @Override
	public void onInitializeClient() {
        ClientPlayNetworking.registerGlobalReceiver(CurseExposureS2CPayload.ID, (payload, context) -> {
            ClientWorld world = context.client().world;

            if (world == null) {
                return;
            }

            int healthPenalty = payload.curseExposure();
            ClientCache.setHealthPenalty(healthPenalty);
        });

        ClientPlayNetworking.registerGlobalReceiver(CurseActiveS2CPayload.ID, (payload, context) -> {
            ClientWorld world = context.client().world;

            if (world == null) {
                return;
            }

            int curseActive = payload.curseActive();
            ClientCache.setCurseActive(curseActive);
        });

        ClientPlayNetworking.registerGlobalReceiver(ReturningTotemS2CPayload.ID, (payload, context) -> {
            MinecraftClient client = context.client();

            ItemStack totemStack = payload.stack();

            client.gameRenderer.showFloatingItem(totemStack);

            ClientPlayerEntity user = context.player();

            if (user == null) {
                return; // asserts don't work in production
            }

            for (int i = 0; i < 64; i++) {
                // the world is basically guaranteed to not be null here if the player is not null
                //noinspection DataFlowIssue
                double dx = user.getX() + (client.world.random.nextDouble() - 0.5) * 2.0;
                double dy = user.getBodyY(0.5);
                double dz = user.getZ() + (client.world.random.nextDouble() - 0.5) * 2.0;
                client.world.addParticleClient(ParticleTypes.SOUL_FIRE_FLAME, dx, dy, dz, 0.0, 0.0, 0.0);
            }
        });

	}
}