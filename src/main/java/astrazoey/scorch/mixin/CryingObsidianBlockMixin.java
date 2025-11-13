package astrazoey.scorch.mixin;

import astrazoey.scorch.registry.ScorchBlocks;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.CryingObsidianBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CryingObsidianBlock.class)
public abstract class CryingObsidianBlockMixin extends AbstractBlockMixin {

    // concerning because you're modifying a vanilla block but okay
    @Override
    protected void cryingTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        int result = MathHelper.nextInt(random, 0, 99);

        if (result >= 10) {
            return;
        }

        BlockPos.Mutable checkedPos = pos.mutableCopy();

        checkedPos = checkedPos.move(0,-1,0);

        for (int i = 1; i <= 10; i++) {
            BlockState checkedBlock = world.getBlockState(checkedPos);

            if (checkedBlock.isAir()) {
                checkedPos = checkedPos.move(0,-1,0);
                continue;
            }

            if (checkedBlock.isOf(Blocks.LAVA)) {
                if (checkedBlock.getFluidState().isStill()) {
                    world.setBlockState(checkedPos, ScorchBlocks.IGNISTONE.getDefaultState());
                } else {
                    world.setBlockState(checkedPos, Blocks.NETHERRACK.getDefaultState());
                }
            }
            break;
        }
    }
}