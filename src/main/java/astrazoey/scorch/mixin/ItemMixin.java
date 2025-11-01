package astrazoey.scorch.mixin;

import astrazoey.scorch.events.FinishUsingCallback;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {

    @Inject(method = "finishUsing", at = @At(value="HEAD"))
    public void finishUsingEvent(ItemStack stack, World world, LivingEntity user, CallbackInfoReturnable<ItemStack> cir) {

        if(!(user instanceof PlayerEntity)) {
            return;
        }

        ActionResult result = FinishUsingCallback.EVENT.invoker().finishUsing((PlayerEntity) user, stack);

    }

}
