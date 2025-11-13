package astrazoey.scorch.block;

import astrazoey.scorch.registry.ScorchBlocks;
import astrazoey.scorch.registry.ScorchCriteria;
import astrazoey.scorch.registry.ScorchSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class PyrackBlock extends Block {
    public static final BooleanProperty ACTIVATED = BooleanProperty.of("activated");

    public PyrackBlock(Settings settings) {
        super(settings);

        setDefaultState(this.stateManager.getDefaultState().with(ACTIVATED, false));
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
    }

    // are you sure all of these are only called on server
    protected ActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        if (!stack.isOf(Items.FLINT_AND_STEEL) && !stack.isOf(Items.FIRE_CHARGE)) {
            return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
        } else {
            world.setBlockState(pos, state.with(ACTIVATED, true));
            world.scheduleBlockTick(pos, this, 3);

            Item item = stack.getItem();
            if (stack.isOf(Items.FLINT_AND_STEEL)) {
                stack.damage(1, player, hand.getEquipmentSlot());
            } else {
                stack.decrementUnlessCreative(1, player);
            }
            player.incrementStat(Stats.USED.getOrCreateStat(item));

            world.playSound(player, pos, ScorchSounds.PYRACK_IGNITES, SoundCategory.PLAYERS, 1.0f, 1.0f);

            return ActionResult.SUCCESS;
        }
    }

    public void onProjectileHit(World world, BlockState state, BlockHitResult hit, ProjectileEntity projectile) {
        if (!world.isClient()) {
            BlockPos pos = hit.getBlockPos();
            Entity entity = projectile.getOwner();

            if (projectile.isOnFire() && projectile.canModifyAt((ServerWorld) world, pos)) {
                world.setBlockState(pos, state.with(ACTIVATED, true));
                world.scheduleBlockTick(pos, this, 1);

                if (entity instanceof ServerPlayerEntity) {
                    if (projectile instanceof ArrowEntity) {
                        ScorchCriteria.SHOOT_BLOCK.trigger((ServerPlayerEntity) entity);
                    }
                }
            }
        }
    }

    public void onDestroyedByExplosion(ServerWorld world, BlockPos pos, Explosion explosion) {
        //world.setBlockState(pos, ScorchBlocks.PYRACK.getDefaultState());
        BlockState state = ScorchBlocks.PYRACK.getDefaultState();
        world.setBlockState(pos, state.with(ACTIVATED, true));
        // Random delay from 2 to 4 ticks to prevent explosions from happening all at once
        int delay = (int) ((Math.random() * (4 - 2)) + 2);
        world.scheduleBlockTick(pos, this, delay);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        // Detonation
        detonate(world, pos);
    }

    public void detonate(ServerWorld world, BlockPos pos) {
        world.removeBlock(pos, false);
        world.createExplosion(null, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, 1.2F, World.ExplosionSourceType.BLOCK);
    }

    protected void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        super.onStacksDropped(state, world, pos, tool, dropExperience);
        if (dropExperience) {
            this.dropExperienceWhenMined(world, pos, tool, UniformIntProvider.create(1, 5));
        }
    }
}
