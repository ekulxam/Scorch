package astrazoey.scorch.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.ShapeContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;

import java.util.stream.Stream;

public class BurialUrnBlock extends Block {
    public static final BooleanProperty NATURAL = Properties.NATURAL;
    public static final VoxelShape OUTLINE_SHAPE = Stream.of(
            Block.createCuboidShape(5, 2, 5, 11, 4, 11),
            Block.createCuboidShape(4, 0, 4, 12, 2, 12),
            Block.createCuboidShape(4, 4, 4, 12, 10, 12),
            Block.createCuboidShape(5, 10, 5, 11, 12, 11)
    ).reduce(VoxelShapes::union).get();

    public BurialUrnBlock(Settings settings) {
        super(settings);
        this.setDefaultState(this.stateManager.getDefaultState().with(NATURAL, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(NATURAL);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState state, BlockView view, BlockPos pos, ShapeContext context) {
        return OUTLINE_SHAPE; // cached
    }
}
