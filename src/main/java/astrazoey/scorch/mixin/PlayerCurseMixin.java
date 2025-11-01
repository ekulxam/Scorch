package astrazoey.scorch.mixin;


import astrazoey.scorch.CurseHelper;
import astrazoey.scorch.Scorch;
import astrazoey.scorch.network.CurseActiveS2CPayload;
import astrazoey.scorch.network.CurseExposureS2CPayload;
import astrazoey.scorch.registry.ScorchDamageTypes;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public class PlayerCurseMixin {

    @Unique
    int tickCounter = 0;
    @Unique
    int safeCounter = 0;

    @Unique
    final int curseReductionTime = 15;

    @Unique
    final int curseSafeTime = 1;

    @Unique
    int curseDisplayed = 0;

    @Inject(method = "tick", at = @At(value="TAIL"))
    public void tickCurse(CallbackInfo ci) {
        if((Object) this instanceof ServerPlayerEntity player) {
            tickCounter++;
            if (tickCounter >= (curseReductionTime * 20)) {
                tickCounter = 0;

                int current = player.getStatHandler().getStat(Scorch.CURSE_EXPOSURE_STAT);
                if (current > 0) {
                    player.increaseStat(Scorch.CURSE_EXPOSURE_STAT, -1);
                }

                if (current > 500) {
                    player.getStatHandler().setStat(player, Scorch.CURSE_EXPOSURE_STAT, 500);
                }


            }

            safeCounter++;
            if(safeCounter >= (curseSafeTime * 20)) {
                safeCounter = 0;
                int active = player.getStatHandler().getStat(Scorch.CURSE_ACTIVE_STAT);
                if (active > 0) {
                    player.increaseStat(Scorch.CURSE_ACTIVE_STAT, -1);
                }
            }


        }
    }

    @Inject(method = "tick", at = @At(value="TAIL"))
    public void curseEffect(CallbackInfo ci) {
        if((Object) this instanceof ServerPlayerEntity player) {
            int healthPenalty = CurseHelper.getPenalty(player.getStatHandler().getStat(Scorch.CURSE_EXPOSURE_STAT), player.getMaxHealth());

            // Send Curse to the Client
            // This is to display on the health bar
            CurseExposureS2CPayload payload = new CurseExposureS2CPayload(healthPenalty);
            ServerPlayNetworking.send(player, payload);

            if (player.getHealth() - (healthPenalty*2) <= 0) {
                DamageSource curseDamage = new DamageSource(
                        player.getEntityWorld().getRegistryManager()
                                .getOrThrow(RegistryKeys.DAMAGE_TYPE)
                                .getEntry(ScorchDamageTypes.CURSE_DAMAGE.getValue()).get()
                );

                player.damage(player.getEntityWorld(), curseDamage, Float.MAX_VALUE);
            }

            curseDisplayed = MathHelper.lerp(0.05F, curseDisplayed, player.getStatHandler().getStat(Scorch.CURSE_ACTIVE_STAT) * 40);

            CurseActiveS2CPayload activePayload = new CurseActiveS2CPayload(curseDisplayed);
            ServerPlayNetworking.send(player, activePayload);


        }


    }


}
