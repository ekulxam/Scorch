package astrazoey.scorch.mixin;

import astrazoey.scorch.CurseHelper;
import astrazoey.scorch.Scorch;
import astrazoey.scorch.network.CurseActiveS2CPayload;
import astrazoey.scorch.network.CurseExposureS2CPayload;
import astrazoey.scorch.registry.ScorchDamageTypes;
import com.mojang.authlib.GameProfile;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.ServerStatHandler;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayerEntity.class)
public abstract class PlayerCurseMixin extends PlayerEntity {

    @Unique
    private static final int scorch$CURSE_REDUCTION_TIME = 15;

    @Unique
    private static final int scorch$CURSE_SAFE_TIME = 1;

    @Shadow
    public abstract ServerStatHandler getStatHandler();

    @Shadow
    public abstract ServerWorld getEntityWorld();

    @Unique
    private int scorch$tickCounter = 0;
    @Unique
    private int scorch$safeCounter = 0;

    @Unique
    private int curseDisplayed = 0;

    public PlayerCurseMixin(World world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void tickCurse(CallbackInfo ci) {
        scorch$tickCurse();
        scorch$curseEffect();
    }

    @Unique
    private void scorch$tickCurse() {
        scorch$tickCounter++;

        if (scorch$tickCounter >= (scorch$CURSE_REDUCTION_TIME * 20)) {
            scorch$tickCounter = 0;

            int current = this.getStatHandler().getStat(Scorch.CURSE_EXPOSURE_STAT);

            if (current > 0) {
                this.increaseStat(Scorch.CURSE_EXPOSURE_STAT, -1);
            }

            if (current > 500) {
                this.getStatHandler().setStat(this, Scorch.CURSE_EXPOSURE_STAT, 500);
            }
        }

        scorch$safeCounter++;

        if (scorch$safeCounter >= (scorch$CURSE_SAFE_TIME * 20)) {
            scorch$safeCounter = 0;
            int active = this.getStatHandler().getStat(Scorch.CURSE_ACTIVE_STAT);

            if (active > 0) {
                this.increaseStat(Scorch.CURSE_ACTIVE_STAT, -1);
            }
        }
    }

    @Unique
    private void scorch$curseEffect() {
        int healthPenalty = CurseHelper.getPenalty(this.getStatHandler().getStat(Scorch.CURSE_EXPOSURE_STAT), this.getMaxHealth());

        // Send Curse to the Client
        // This is to display on the health bar
        CurseExposureS2CPayload payload = new CurseExposureS2CPayload(healthPenalty);
        ServerPlayNetworking.send((ServerPlayerEntity) (Object) this, payload);

        if (this.getHealth() - (healthPenalty * 2) <= 0) {
            ServerWorld serverWorld = this.getEntityWorld();
            DamageSource curseDamage = new DamageSource(
                    serverWorld.getRegistryManager()
                            .getOrThrow(RegistryKeys.DAMAGE_TYPE)
                            .getEntry(ScorchDamageTypes.CURSE_DAMAGE.getValue()).get()
            );

            this.damage(serverWorld, curseDamage, Float.MAX_VALUE);
        }

        curseDisplayed = MathHelper.lerp(0.05F, curseDisplayed, this.getStatHandler().getStat(Scorch.CURSE_ACTIVE_STAT) * 40);

        CurseActiveS2CPayload activePayload = new CurseActiveS2CPayload(curseDisplayed);
        //noinspection DataFlowIssue
        ServerPlayNetworking.send((ServerPlayerEntity) (Object) this, activePayload);
    }
}
