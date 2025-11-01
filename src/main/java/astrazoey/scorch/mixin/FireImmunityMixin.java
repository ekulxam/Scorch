package astrazoey.scorch.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ItemEntity.class)
public abstract class FireImmunityMixin extends Entity {
    public FireImmunityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "isFireImmune", at = @At("HEAD"), cancellable = true)
    private void makeWitherSkullFireproof(CallbackInfoReturnable<Boolean> cir) {
        ItemEntity self = (ItemEntity) (Object) this;
        ItemStack stack = self.getStack();

        // Preserve vanilla logic but add extra fire immunity for wither skulls
        if (stack.isOf(Items.WITHER_SKELETON_SKULL) ||
                stack.isOf(Items.NETHER_STAR)) {
            cir.setReturnValue(true);
        }
    }
}
