package astrazoey.scorch.mixin;

import astrazoey.scorch.events.OnKilledByCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @Inject(method = "onKilledBy", at = @At(value="RETURN"))
    public void finishUsingEvent(LivingEntity adversary, CallbackInfo ci) {

        if(!(adversary instanceof PlayerEntity)) {
            return;
        }

        ActionResult result = OnKilledByCallback.EVENT.invoker().onKilledBy((PlayerEntity) adversary, (LivingEntity) (Object) this);

    }


}
