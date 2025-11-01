package astrazoey.scorch.mixin.client;

import astrazoey.scorch.ClientCache;
import astrazoey.scorch.Scorch;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;


@Mixin(InGameHud.class)
public class CustomHeartMixin {

    @Unique
    int capturePenalty = 0;

    @Inject(method = "renderHealthBar", at = @At("TAIL"))
    private void renderBrokenHearts(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci) {
        if(!(player instanceof ClientPlayerEntity)) {
            return;
        }

        capturePenalty = ClientCache.getHealthPenalty();

        if (capturePenalty <= 0) return;

        Identifier textureId = Identifier.of(Scorch.MOD_ID, "textures/gui/sprites/hud/heart/broken.png");

        for (int i = 0; i < capturePenalty; i++) {
            int row = i / 10;
            int column = i % 10;

            int x2 = x + (column * 8);
            int y2 = y - (row * 10);

            context.drawTexture(RenderPipelines.GUI_TEXTURED, textureId, x2, y2, 0, 0, 9, 9, 9, 9);
        }
    }

    @ModifyConstant(method = "renderHealthBar", constant = @Constant(expandZeroConditions = Constant.Condition.GREATER_THAN_OR_EQUAL_TO_ZERO))
    public int skipHeartRender(int constant) {
        return capturePenalty;
    }
}
