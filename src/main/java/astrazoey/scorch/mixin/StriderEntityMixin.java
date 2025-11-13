package astrazoey.scorch.mixin;

import astrazoey.scorch.registry.ScorchCriteria;
import astrazoey.scorch.registry.ScorchSounds;
import astrazoey.scorch.strider.StriderHairInterface;
import astrazoey.scorch.strider.StriderInteractInterface;
import net.minecraft.entity.*;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.storage.ReadView;
import net.minecraft.storage.WriteView;
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

@Mixin(StriderEntity.class)
public abstract class StriderEntityMixin extends AnimalEntity implements Shearable, StriderHairInterface, StriderInteractInterface {

    @Shadow
    public abstract boolean isCold();

    @Unique
    private static TrackedData<Boolean> HAIR_STATE;

    @Unique
    private static TrackedData<Integer> HAIR_STYLE;

    @Unique
    private static TrackedData<Integer> HAIR_GROWTH;



    public StriderEntityMixin(EntityType<? extends AnimalEntity> entityType, World world, SaddledComponent saddledComponent) {
        super(entityType, world);


    }

    @Inject(method = "initDataTracker", at = @At(value="TAIL"))
    public void initDataTracker(DataTracker.Builder builder, CallbackInfo ci) {
        builder.add(HAIR_STATE, true);
        builder.add(HAIR_STYLE, 0);
        builder.add(HAIR_GROWTH, 0);
    }

    public void writeData(WriteView view) {
        view.putBoolean("hasHair", scorch$hasHair());
        view.putInt("hairStyle", scorch$getHairStyle());
        view.putInt("hairGrowth", scorch$getHairGrowth());
        super.writeData(view);
    }

    public void readData(ReadView view) {
        super.readData(view);
        setHasHair(view.getBoolean("hasHair", true));
        setHairStyle(view.getInt("hairStyle", 0));
        setHairGrowth(view.getInt("hairGrowth", 0));
    }



    @Inject(method = "interactMob", at = @At(value="HEAD"), cancellable = true)
    public void shearStrider(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (dataTracker.get(HAIR_STATE) && player.getStackInHand(hand).getItem() == Items.SHEARS) {
            World world = player.getEntityWorld();

            if (world instanceof ServerWorld) {
                ItemEntity stringDrop = new ItemEntity(world, this.getX(), this.getY(), this.getZ(), new ItemStack(Items.STRING));
                world.spawnEntity(stringDrop);

                setHasHair(false);

                player.getStackInHand(hand).damage(1, player, hand);

                world.playSound(null, this.getBlockPos(), ScorchSounds.SHEAR_STRIDER, SoundCategory.PLAYERS, 1.0F, 1.0F);

                ScorchCriteria.SHEAR_STRIDER.trigger((ServerPlayerEntity) player);
            }
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "interactMob", at = @At(value="HEAD"), cancellable = true)
    public void creamStrider(PlayerEntity player, Hand hand, CallbackInfoReturnable<ActionResult> cir) {
        if (dataTracker.get(HAIR_STATE) && player.getStackInHand(hand).getItem() == Items.MAGMA_CREAM) {
            World world = player.getEntityWorld();

            if (world instanceof ServerWorld) {
                setHairStyle((dataTracker.get(HAIR_STYLE)) +1);
                if (dataTracker.get(HAIR_STYLE) > 4) {
                    setHairStyle(0);
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
        if (!dataTracker.get(HAIR_STATE) && player.getStackInHand(hand).getItem() == Items.WARPED_ROOTS) {
            World world = player.getEntityWorld();

            if (world instanceof ServerWorld) {
                dataTracker.set(HAIR_GROWTH, dataTracker.get(HAIR_GROWTH)+1200);

                player.getStackInHand(hand).decrement(1);

                world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_STRIDER_EAT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            }
            cir.setReturnValue(ActionResult.SUCCESS);
        }
    }

    @Inject(method = "tick", at = @At(value="TAIL"))
    public void growHair(CallbackInfo ci) {
        if (!dataTracker.get(HAIR_STATE) && !this.isCold()) {
            dataTracker.set(HAIR_GROWTH, dataTracker.get(HAIR_GROWTH)+1);
            if (dataTracker.get(HAIR_GROWTH) >= 6000) {
                dataTracker.set(HAIR_GROWTH, 0);
                dataTracker.set(HAIR_STATE, true);
            }
        }
    }

    @Unique
    public void setHasHair(boolean hasHair) {
        this.dataTracker.set(HAIR_STATE, hasHair);
    }

    public boolean scorch$hasHair() {

        return this.dataTracker.get(HAIR_STATE);
    }

    @Unique
    public void setHairStyle(int hairStyle) {this.dataTracker.set(HAIR_STYLE, hairStyle);}

    @Unique
    public int scorch$getHairStyle() {return this.dataTracker.get(HAIR_STYLE);}

    @Unique
    public void setHairGrowth(int hairGrowth) {this.dataTracker.set(HAIR_GROWTH, hairGrowth);}

    @Unique
    public int scorch$getHairGrowth() {return this.dataTracker.get(HAIR_GROWTH);}


    static {
        HAIR_STATE = DataTracker.registerData(StriderEntityMixin.class, TrackedDataHandlerRegistry.BOOLEAN);
        HAIR_GROWTH = DataTracker.registerData(StriderEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
        HAIR_STYLE = DataTracker.registerData(StriderEntityMixin.class, TrackedDataHandlerRegistry.INTEGER);
    }

}