package astrazoey.scorch;

import net.minecraft.block.enums.CameraSubmersionType;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.client.render.fog.FogData;
import net.minecraft.client.render.fog.FogModifier;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.Nullable;

public class CurseFogModifier extends FogModifier {

    public int getFogColor(ClientWorld world, Camera camera, int viewDistance, float skyDarkness) {
        return 0x95fdfb;
    }

    public void applyStartEndModifier(FogData data, Entity cameraEntity, BlockPos cameraPos, ClientWorld world, float viewDistance, RenderTickCounter tickCounter) {
        if (cameraEntity.isSpectator()) {
            data.environmentalStart = -8.0F;
            data.environmentalEnd = viewDistance * 0.5F;
        } else {
            data.environmentalStart = (float) -ClientCache.getCurseActive() / 40;
            data.environmentalEnd = viewDistance / ((float) ClientCache.getCurseActive() / 20);
        }

        data.skyEnd = data.environmentalEnd;
        data.cloudEnd = data.environmentalEnd;
    }

    public boolean shouldApply(@Nullable CameraSubmersionType submersionType, Entity cameraEntity) {
        return ClientCache.getCurseActive() > 0;
    }
}
