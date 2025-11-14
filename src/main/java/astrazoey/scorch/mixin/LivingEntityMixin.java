package astrazoey.scorch.mixin;

import astrazoey.scorch.events.OnKilledByCallback;
import com.llamalad7.mixinextras.injector.wrapmethod.WrapMethod;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "onKilledBy", at = @At(value="RETURN"))
    public void finishUsingEvent(LivingEntity adversary, CallbackInfo ci) {
        if (!(adversary instanceof PlayerEntity player)) {
            return;
        }

        OnKilledByCallback.EVENT.invoker().onKilledBy(player, (LivingEntity) (Object) this);
    }

    @WrapMethod(method = "onDeath")
    protected void wrapOnDeath(DamageSource damageSource, Operation<Void> original) {
        original.call(damageSource);
    }
}
