package astrazoey.scorch.mixin.client;

import astrazoey.scorch.CurseFogModifier;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.render.fog.FogRenderer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.List;

// better to specify priority so you always know in what order other mods' fog modifiers will be applied with yours
@Mixin(value = FogRenderer.class, priority = 1500)
public class CurseFogMixin {
    @Shadow
    @Final
    private static List<FogModifier> FOG_MODIFIERS;

    static {
        // add the modifier after vanilla ones are created
        FOG_MODIFIERS.add(3, new CurseFogModifier());
    }
}
