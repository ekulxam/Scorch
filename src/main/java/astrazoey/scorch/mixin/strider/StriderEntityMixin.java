package astrazoey.scorch.mixin.strider;

import astrazoey.scorch.strider.StriderHairInterface;
import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.*;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.storage.ReadView;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

import static astrazoey.scorch.registry.ScorchAttachmentTypes.STRIDER_HAIR_GROWTH;
import static astrazoey.scorch.registry.ScorchAttachmentTypes.STRIDER_HAIR_STATE;
import static astrazoey.scorch.registry.ScorchAttachmentTypes.STRIDER_HAIR_STYLE;

@SuppressWarnings("UnstableApiUsage")
@Mixin(StriderEntity.class)
public abstract class StriderEntityMixin extends AnimalEntityMixin implements Shearable, StriderHairInterface {

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

    @Override
    protected void readOldHairData(ReadView view, CallbackInfo ci) {
        if (!view.contains(AttachmentTarget.NBT_ATTACHMENT_KEY)) {
            return;
        }

        boolean oldHasHair = view.contains("hasHair");
        boolean oldHairStyle = view.contains("hairStyle");
        boolean oldHairGrowth = view.contains("hairGrowth");

        if (!oldHasHair && !oldHairStyle && !oldHairGrowth) {
            return;
        }

        Map<AttachmentType<?>, Object> map = view.read(AttachmentTarget.NBT_ATTACHMENT_KEY, AttachmentSerializingImplAccessor.scorch$getCodec()).filter(m -> !m.isEmpty()).orElse(null);

        if (map == null) {
            scorch$readOldData(view, oldHasHair, oldHairStyle, oldHairGrowth);
            return;
        }

        if (map.containsKey(STRIDER_HAIR_STATE)) {
            oldHasHair = false;
        }
        if (map.containsKey(STRIDER_HAIR_STYLE)) {
            oldHairStyle = false;
        }
        if (map.containsKey(STRIDER_HAIR_GROWTH)) {
            oldHairGrowth = false;
        }

        scorch$readOldData(view, oldHasHair, oldHairStyle, oldHairGrowth);
    }

    @Unique
    private void scorch$readOldData(ReadView view, boolean oldHasHair, boolean oldHairStyle, boolean oldHairGrowth) {
        if (oldHasHair) {
            this.setAttached(STRIDER_HAIR_STATE, view.getBoolean("hasHair", true));
        }
        if (oldHairStyle) {
            this.setAttached(STRIDER_HAIR_STYLE, view.getInt("hairStyle", 0));
        }
        if (oldHairGrowth) {
            this.setAttached(STRIDER_HAIR_GROWTH, view.getInt("hairGrowth", 0));
        }
    }
}