package astrazoey.scorch.mixin.client;


import net.minecraft.client.render.fog.DimensionOrBossFogModifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(DimensionOrBossFogModifier.class)
public abstract class NetherFogMixin {


    // Low order so other mods can override if needed

    @ModifyConstant(method = "applyStartEndModifier", constant = @Constant(floatValue = 192.0F), order = 1)
    public float changeThickFog(float constant) {
        return 3000f;
    }

    @ModifyConstant(method = "applyStartEndModifier", constant = @Constant(floatValue = 0.5F), order = 1)
    public float changeThickFog2(float constant) {
        return 2.0f;
    }


}