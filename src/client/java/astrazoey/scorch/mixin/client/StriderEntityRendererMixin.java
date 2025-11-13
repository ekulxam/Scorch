package astrazoey.scorch.mixin.client;

import astrazoey.scorch.Scorch;
import astrazoey.scorch.strider.StriderHairInterface;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.client.render.entity.StriderEntityRenderer;
import net.minecraft.client.render.entity.state.StriderEntityRenderState;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import static astrazoey.scorch.ScorchClient.STRIDER_HAIR;
import static astrazoey.scorch.ScorchClient.STRIDER_HAIR_STYLE;

@Mixin(StriderEntityRenderer.class)
public class StriderEntityRendererMixin {

    /*
    These should all be prefixed but they're all private so it should be fine
    Consider putting this into an enum or other data structure so this code is cleaner
     */
    @Unique
    private static final Identifier TEXTURE = Identifier.of("textures/entity/strider/strider.png");
    @Unique
    private static final Identifier COLD_TEXTURE = Identifier.of("textures/entity/strider/strider_cold.png");

    @Unique
    private static final Identifier TEXTURE_HAIRLESS = Scorch.id("textures/entity/strider/strider_no_hair.png");
    @Unique
    private static final Identifier COLD_TEXTURE_HAIRLESS = Scorch.id("textures/entity/strider/strider_cold_no_hair.png");

    @Unique
    private static final Identifier TEXTURE_HAIR_1 = Scorch.id("textures/entity/strider/strider_hair_1.png");
    @Unique
    private static final Identifier COLD_TEXTURE_HAIR_1 = Scorch.id("textures/entity/strider/strider_cold_hair_1.png");

    @Unique
    private static final Identifier TEXTURE_HAIR_2 = Scorch.id("textures/entity/strider/strider_hair_2.png");
    @Unique
    private static final Identifier COLD_TEXTURE_HAIR_2 = Scorch.id("textures/entity/strider/strider_cold_hair_2.png");

    @Unique
    private static final Identifier TEXTURE_HAIR_3 = Scorch.id("textures/entity/strider/strider_hair_3.png");
    @Unique
    private static final Identifier COLD_TEXTURE_HAIR_3 = Scorch.id("textures/entity/strider/strider_cold_hair_3.png");

    @Unique
    private static final Identifier TEXTURE_HAIR_4 = Scorch.id("textures/entity/strider/strider_hair_4.png");
    @Unique
    private static final Identifier COLD_TEXTURE_HAIR_4 = Scorch.id("textures/entity/strider/strider_cold_hair_4.png");

    @Unique
    private static final Identifier TEXTURE_HAIR_5 = Scorch.id("textures/entity/strider/strider_hair_5.png");
    @Unique
    private static final Identifier COLD_TEXTURE_HAIR_5 = Scorch.id("textures/entity/strider/strider_cold_hair_5.png");

    @Inject(method = "updateRenderState(Lnet/minecraft/entity/passive/StriderEntity;Lnet/minecraft/client/render/entity/state/StriderEntityRenderState;F)V", at = @At("RETURN"))
    public void updateRenderState(StriderEntity striderEntity, StriderEntityRenderState renderState, float tickProgress, CallbackInfo ci) {
        StriderHairInterface entityAccess = (StriderHairInterface) striderEntity;

        renderState.setData(STRIDER_HAIR, entityAccess.scorch$hasHair());
        renderState.setData(STRIDER_HAIR_STYLE, entityAccess.scorch$getHairStyle());
    }

    @ModifyReturnValue(method = "getTexture(Lnet/minecraft/client/render/entity/state/StriderEntityRenderState;)Lnet/minecraft/util/Identifier;", at = @At("RETURN"))
    public Identifier getTexture(Identifier original, StriderEntityRenderState renderState) {
        final boolean hasHair = renderState.getDataOrDefault(STRIDER_HAIR, false);
        final int hairStyle = renderState.getDataOrDefault(STRIDER_HAIR_STYLE, 0);

        if (hasHair) {
            if (renderState.cold) {
                return getHairTexture(hairStyle, COLD_TEXTURE, COLD_TEXTURE_HAIR_1, COLD_TEXTURE_HAIR_2, COLD_TEXTURE_HAIR_3, COLD_TEXTURE_HAIR_4, COLD_TEXTURE_HAIR_5);
            } else {
                return getHairTexture(hairStyle, TEXTURE, TEXTURE_HAIR_1, TEXTURE_HAIR_2, TEXTURE_HAIR_3, TEXTURE_HAIR_4, TEXTURE_HAIR_5);
            }
        }

        if (renderState.cold) {
            return COLD_TEXTURE_HAIRLESS;
        } else {
            return TEXTURE_HAIRLESS;
        }
    }

    @Unique
    private Identifier getHairTexture(int hairStyle, Identifier texture, Identifier textureHair1, Identifier textureHair2, Identifier textureHair3, Identifier textureHair4, Identifier textureHair5) {
        return switch (hairStyle) {
            case 1 -> textureHair1;
            case 2 -> textureHair2;
            case 3 -> textureHair3;
            case 4 -> textureHair4;
            case 5 -> textureHair5;
            default -> texture; // also case 0
        };
    }
}
