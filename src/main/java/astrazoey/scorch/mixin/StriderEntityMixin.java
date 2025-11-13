package astrazoey.scorch.mixin;

import astrazoey.scorch.registry.ScorchCriteria;
import astrazoey.scorch.registry.ScorchSounds;
import astrazoey.scorch.strider.StriderHairInterface;
import astrazoey.scorch.strider.StriderInteractInterface;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import static astrazoey.scorch.registry.ScorchAttachmentTypes.STRIDER_HAIR_GROWTH;
import static astrazoey.scorch.registry.ScorchAttachmentTypes.STRIDER_HAIR_STATE;
import static astrazoey.scorch.registry.ScorchAttachmentTypes.STRIDER_HAIR_STYLE;

@SuppressWarnings("UnstableApiUsage")
@Mixin(StriderEntity.class)
public abstract class StriderEntityMixin extends AnimalEntity implements Shearable, StriderHairInterface, StriderInteractInterface {

    @Shadow
    public abstract boolean isCold();

    public StriderEntityMixin(EntityType<? extends AnimalEntity> entityType, World world, SaddledComponent saddledComponent) {
        super(entityType, world);
    }

    @Inject(method = )

    public void readData(ReadView view) {
        super.readData(view);
        scorch$setHasHair(view.getBoolean("hasHair", true));
        scorch$setHairStyle(view.getInt("hairStyle", 0));
        scorch$setHairGrowth(view.getInt("hairGrowth", 0));
    }

    @Inject(method = "interactMob", at = @At(value="HEAD"), cancellable = true)
    public void shearStrider(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (this.getAttachedOrCreate(STRIDER_HAIR_STATE) && player.getStackInHand(hand).isOf(Items.SHEARS)) {
            World world = player.getEntityWorld();

            if (world instanceof ServerWorld serverWorld) {
                this.dropItem(serverWorld, Items.STRING);

                scorch$setHasHair(false);

                player.getStackInHand(hand).damage(1, player, hand);

                world.playSound(null, this.getBlockPos(), ScorchSounds.SHEAR_STRIDER, SoundCategory.PLAYERS, 1.0F, 1.0F);

                ScorchCriteria.SHEAR_STRIDER.trigger((ServerPlayerEntity) player);
            }
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "interactMob", at = @At(value="HEAD"), cancellable = true)
    public void creamStrider(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (dataTracker.get(HAIR_STATE) && player.getStackInHand(hand).isOf(Items.MAGMA_CREAM)) {
            World world = player.getEntityWorld();

            if (world instanceof ServerWorld) {
                scorch$setHairStyle((dataTracker.get(HAIR_STYLE)) +1);
                if (dataTracker.get(HAIR_STYLE) > 4) {
                    scorch$setHairStyle(0);
                }

                player.getStackInHand(hand).decrement(1);

                world.playSound(null, this.getBlockPos(), ScorchSounds.APPLY_MAGMA, SoundCategory.PLAYERS, 1.0F, 1.0F);

                ScorchCriteria.STYLE_STRIDER.trigger((ServerPlayerEntity) player);
            }
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "interactMob", at = @At(value="HEAD"), cancellable = true)
    public void feedStrider(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (!dataTracker.get(HAIR_STATE) && player.getStackInHand(hand).isOf(Items.WARPED_ROOTS)) {
            World world = player.getEntityWorld();

            if (world instanceof ServerWorld) {
                dataTracker.set(HAIR_GROWTH, dataTracker.get(HAIR_GROWTH)+1200);

                player.getStackInHand(hand).decrement(1);

                world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_STRIDER_EAT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "tick", at = @At("RETURN"))
    public void growHair(CallbackInfo ci) {
        if (this.getEntityWorld().isClient()) {
            return;
        }

        if (this.getAttachedOrCreate(STRIDER_HAIR_STATE) || this.isCold()) {
            return;
        }

        this.setAttached(STRIDER_HAIR_GROWTH, this.getAttachedOrCreate(STRIDER_HAIR_GROWTH) + 1);
        if (this.getAttachedOrCreate(STRIDER_HAIR_GROWTH) >= 6000) {
            this.setAttached(STRIDER_HAIR_GROWTH, 0);
            this.setAttached(STRIDER_HAIR_STATE, true);
        }
    }

    @Override
    public boolean scorch$hasHair() {
        return this.getAttachedOrElse(STRIDER_HAIR_STATE, true);
    }

    @Override
    public int scorch$getHairStyle() {
        return this.getAttachedOrElse(STRIDER_HAIR_STYLE, 0);
    }
}