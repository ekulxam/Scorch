package astrazoey.scorch.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.render.fog.DimensionOrBossFogModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(value = DimensionOrBossFogModifier.class, priority = 500)
public abstract class NetherFogMixin {

    @ModifyExpressionValue(method = "applyStartEndModifier", at = @At(value = "CONSTANT", args = "floatValue=192.0"))
    public float changeThickFog(float original) {
        return 3000f;
    }

    @ModifyExpressionValue(method = "applyStartEndModifier", at = @At(value = "CONSTANT", args = "floatValue=0.5"))
    public float changeThickFog2(float original) {
        return 2.0f;
    }
}