package astrazoey.scorch.mixin.client;

import astrazoey.scorch.CurseFogModifier;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.render.fog.FogRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(FogRenderer.class)
public class CurseFogMixin {
    @Shadow
    @Final
    private static List<FogModifier> FOG_MODIFIERS;

    @Inject(method = "<clinit>", at = @At("TAIL"))
    private static void onClassInit(CallbackInfo ci) {
        // add the modifier after vanilla ones are created
        FOG_MODIFIERS.add(3, new CurseFogModifier());
    }

}
