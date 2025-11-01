package astrazoey.scorch.mixin.client;

import astrazoey.scorch.Scorch;
import astrazoey.scorch.ScorchComponents;
import astrazoey.scorch.StriderRenderStateAccess;
import astrazoey.scorch.mixin.StriderEntityMixin;
import astrazoey.scorch.strider.StriderHairInterface;
import net.minecraft.client.render.entity.StriderEntityRenderer;
import net.minecraft.client.render.entity.state.StriderEntityRenderState;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(StriderEntityRenderer.class)
public class StriderEntityRendererMixin {


    @Unique
    private static final Identifier TEXTURE = Identifier.of("textures/entity/strider/strider.png");
    @Unique
    private static final Identifier COLD_TEXTURE = Identifier.of("textures/entity/strider/strider_cold.png");

    @Unique
    private static final Identifier TEXTURE_HAIRLESS = Identifier.of(Scorch.MOD_ID, "textures/entity/strider/strider_no_hair.png");
    @Unique
    private static final Identifier COLD_TEXTURE_HAIRLESS = Identifier.of(Scorch.MOD_ID,"textures/entity/strider/strider_cold_no_hair.png");

    @Unique
    private static final Identifier TEXTURE_HAIR_1 = Identifier.of(Scorch.MOD_ID,"textures/entity/strider/strider_hair_1.png");
    @Unique
    private static final Identifier COLD_TEXTURE_HAIR_1 = Identifier.of(Scorch.MOD_ID,"textures/entity/strider/strider_cold_hair_1.png");

    @Unique
    private static final Identifier TEXTURE_HAIR_2 = Identifier.of(Scorch.MOD_ID,"textures/entity/strider/strider_hair_2.png");
    @Unique
    private static final Identifier COLD_TEXTURE_HAIR_2 = Identifier.of(Scorch.MOD_ID,"textures/entity/strider/strider_cold_hair_2.png");

    @Unique
    private static final Identifier TEXTURE_HAIR_3 = Identifier.of(Scorch.MOD_ID,"textures/entity/strider/strider_hair_3.png");
    @Unique
    private static final Identifier COLD_TEXTURE_HAIR_3 = Identifier.of(Scorch.MOD_ID,"textures/entity/strider/strider_cold_hair_3.png");

    @Unique
    private static final Identifier TEXTURE_HAIR_4 = Identifier.of(Scorch.MOD_ID,"textures/entity/strider/strider_hair_4.png");
    @Unique
    private static final Identifier COLD_TEXTURE_HAIR_4 = Identifier.of(Scorch.MOD_ID,"textures/entity/strider/strider_cold_hair_4.png");

    @Unique
    private static final Identifier TEXTURE_HAIR_5 = Identifier.of(Scorch.MOD_ID,"textures/entity/strider/strider_hair_5.png");
    @Unique
    private static final Identifier COLD_TEXTURE_HAIR_5 = Identifier.of(Scorch.MOD_ID,"textures/entity/strider/strider_cold_hair_5.png");


    @Unique
    public boolean hasHair;
    @Unique
    public int hairStyle;

    @Inject(method = "updateRenderState*", at = @At("TAIL"))
    public void updateRenderState(StriderEntity striderEntity, StriderEntityRenderState striderEntityRenderState, float f, CallbackInfo ci) {
        StriderHairInterface entityAccess = (StriderHairInterface)(Object)striderEntity;
        StriderRenderStateAccess stateAccess = (StriderRenderStateAccess)(Object)striderEntityRenderState;

        stateAccess.sc$setHasHair(entityAccess.scorch$hasHair());
        stateAccess.sc$setHairStyle(entityAccess.scorch$getHairStyle());
    }


    @Inject(method = "getTexture*", at = @At("HEAD"), cancellable = true)
    public void getTexture(StriderEntityRenderState striderEntity, CallbackInfoReturnable<Identifier> cir) {

        StriderRenderStateAccess stateAccess = (StriderRenderStateAccess)(Object)striderEntity;

        hasHair = stateAccess.sc$hasHair();
        hairStyle = stateAccess.sc$getHairStyle();

        if(hasHair) {
            if(striderEntity.cold) {
                getHairTexture(cir, COLD_TEXTURE, COLD_TEXTURE_HAIR_1, COLD_TEXTURE_HAIR_2, COLD_TEXTURE_HAIR_3, COLD_TEXTURE_HAIR_4, COLD_TEXTURE_HAIR_5);
            } else {
                getHairTexture(cir, TEXTURE, TEXTURE_HAIR_1, TEXTURE_HAIR_2, TEXTURE_HAIR_3, TEXTURE_HAIR_4, TEXTURE_HAIR_5);
            }
        } else {
            if(striderEntity.cold) {
                cir.setReturnValue(COLD_TEXTURE_HAIRLESS);
            } else {
                cir.setReturnValue(TEXTURE_HAIRLESS);
            }
        }
    }

    @Unique
    private void getHairTexture(CallbackInfoReturnable<Identifier> cir, Identifier texture, Identifier textureHair1, Identifier textureHair2, Identifier textureHair3, Identifier textureHair4, Identifier textureHair5) {
        switch (hairStyle) {
            case 0:
                cir.setReturnValue(texture);
                break;
            case 1:
                cir.setReturnValue(textureHair1);
                break;
            case 2:
                cir.setReturnValue(textureHair2);
                break;
            case 3:
                cir.setReturnValue(textureHair3);
                break;
            case 4:
                cir.setReturnValue(textureHair4);
                break;
            case 5:
                cir.setReturnValue(textureHair5);
                break;
        }
    }
}
