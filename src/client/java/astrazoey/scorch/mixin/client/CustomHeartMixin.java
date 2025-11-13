package astrazoey.scorch.mixin.client;

import astrazoey.scorch.ClientCache;
import astrazoey.scorch.ScorchClient;
import com.llamalad7.mixinextras.expression.Expression;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.client.gl.RenderPipelines;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = InGameHud.class, priority = 100)
public class CustomHeartMixin {

    @Inject(method = "renderHealthBar", at = @At("HEAD"))
    private void capturePenalty(CallbackInfo ci, @Local(argsOnly = true) PlayerEntity player, @Share("penalty")LocalIntRef localIntRef) {
        localIntRef.set(ClientCache.getHealthPenalty());
    }

    @Expression("? >= @(0)")
    @ModifyExpressionValue(method = "renderHealthBar", at = @At("MIXINEXTRAS:EXPRESSION"))
    public int skipHeartRender(int original, @Share("penalty")LocalIntRef penalty, @Share("heartsStart") LocalIntRef heartsStart) {
        heartsStart.set(original);
        return original + penalty.get();
    }

    @Inject(method = "renderHealthBar", at = @At("RETURN"))
    private void renderBrokenHearts(DrawContext context, PlayerEntity player, int x, int y, int lines, int regeneratingHeartIndex, float maxHealth, int lastHealth, int health, int absorption, boolean blinking, CallbackInfo ci, @Share("penalty")LocalIntRef penalty, @Share("heartsStart") LocalIntRef heartsStart) {
        if (!(player instanceof ClientPlayerEntity)) {
            return;
        }

        int capturePenalty = penalty.get();

        if (capturePenalty <= 0) return;

        for (int i = heartsStart.get(); i < capturePenalty; i++) {
            int row = i / 10;
            int column = i % 10;

            int x2 = x + (column * 8);
            int y2 = y - (row * 10);

            context.drawTexture(RenderPipelines.GUI_TEXTURED, ScorchClient.BROKEN_HEART_TEXTURE, x2, y2, 0, 0, 9, 9, 9, 9);
        }
    }
}
