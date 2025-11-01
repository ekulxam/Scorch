package astrazoey.scorch.mixin.client;

import astrazoey.scorch.StriderRenderStateAccess;
import astrazoey.scorch.strider.StriderHairInterface;
import net.minecraft.client.render.entity.state.StriderEntityRenderState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin(StriderEntityRenderState.class)
public class StriderEntityRenderStateMixin implements StriderRenderStateAccess {
    @Unique
    public boolean sc$hasHair;
    @Unique
    public int sc$hairStyle;

    @Override
    public boolean sc$hasHair() {
        return sc$hasHair;
    }

    @Override
    public void sc$setHasHair(boolean value) {
        sc$hasHair = value;
    }

    @Override
    public int sc$getHairStyle() {
        return sc$hairStyle;
    }

    @Override
    public void sc$setHairStyle(int value) {
        sc$hairStyle = value;
    }
}
