package astrazoey.scorch.mixin;

import astrazoey.scorch.registry.ScorchBlocks;
import net.minecraft.block.*;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Random;

@Mixin(CryingObsidianBlock.class)
public class CryingObsidianBlockMixin extends Block {

    public CryingObsidianBlockMixin(Settings settings) {
        super(settings);
    }


    @Override
    public boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, net.minecraft.util.math.random.Random random) {
        Random r = new Random();
        int result = r.nextInt(100-1);

        if(result < 10) {
            BlockPos.Mutable checkedPos = pos.mutableCopy();

            checkedPos = checkedPos.move(0,-1,0);

            for (int i = 1; i <= 10; i++) {
                BlockState checkedBlock = world.getBlockState(checkedPos);
                if (checkedBlock.isAir()) {
                    checkedPos = checkedPos.move(0,-1,0);
                } else {
                    if(checkedBlock.isOf(Blocks.LAVA)) {
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
    }
}