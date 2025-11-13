package astrazoey.scorch.mixin;

import net.fabricmc.fabric.api.attachment.v1.AttachmentTarget;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.storage.ReadView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnimalEntity.class)
public class AnimalEntityMixin {

    @SuppressWarnings("UnstableApiUsage")
    @Inject(method = "readCustomData", at = @At("RETURN"))
    private void readOldHairData(ReadView view, CallbackInfo ci) {
        if (!view.contains(AttachmentTarget.NBT_ATTACHMENT_KEY)) {
            return;
        }


    }
}
