package astrazoey.scorch.mixin.strider;

import astrazoey.scorch.strider.StriderHairInterface;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static astrazoey.scorch.registry.ScorchAttachmentTypes.STRIDER_HAIR_GROWTH;
import static astrazoey.scorch.registry.ScorchAttachmentTypes.STRIDER_HAIR_STATE;
import static astrazoey.scorch.registry.ScorchAttachmentTypes.STRIDER_HAIR_STYLE;

@SuppressWarnings("UnstableApiUsage")
@Mixin(StriderEntity.class)
public abstract class StriderEntityMixin extends AnimalEntity implements Shearable, StriderHairInterface {

    @Shadow
    public abstract boolean isCold();

    public StriderEntityMixin(EntityType<? extends AnimalEntity> entityType, World world, SaddledComponent saddledComponent) {
        super(entityType, world);
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