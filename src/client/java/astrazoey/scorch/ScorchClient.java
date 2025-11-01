package astrazoey.scorch;

import astrazoey.scorch.network.CurseActiveS2CPayload;
import astrazoey.scorch.network.CurseExposureS2CPayload;
import astrazoey.scorch.network.ReturningTotemS2CPayload;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;


public class ScorchClient implements ClientModInitializer {
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
            MinecraftClient client = MinecraftClient.getInstance();

            ItemStack totemStack = payload.stack();
            PlayerEntity user = client.player;

            client.gameRenderer.showFloatingItem(totemStack);

            if (client.world != null) {
                for (int i = 0; i < 64; i++) {
                    assert user != null;
                    double dx = user.getX() + (client.world.random.nextDouble() - 0.5) * 2.0;
                    double dy = user.getBodyY(0.5);
                    double dz = user.getZ() + (client.world.random.nextDouble() - 0.5) * 2.0;
                    client.world.addParticleClient(ParticleTypes.SOUL_FIRE_FLAME, dx, dy, dz, 0.0, 0.0, 0.0);
                }
            }
        });



	}
}