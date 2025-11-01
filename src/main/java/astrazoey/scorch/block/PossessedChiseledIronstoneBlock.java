package astrazoey.scorch.block;

import astrazoey.scorch.Scorch;
import astrazoey.scorch.registry.ScorchSounds;
import net.minecraft.block.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.BooleanProperty;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

import java.util.List;


public class PossessedChiseledIronstoneBlock extends Block {



    public PossessedChiseledIronstoneBlock(Settings settings) {
        super(settings);
        setDefaultState(getDefaultState().with(ACTIVATED, false));
    }

    public static final BooleanProperty ACTIVATED = BooleanProperty.of("activated");
    final int curseCheckTime = 20;
    final int curseCheckRadius = 7;
    final int wakeCurseAmount = 5;

    final int curseExposureBreakAmount = 12;

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
        builder.add(ACTIVATED);
    }

    @Override
    public void randomDisplayTick(BlockState state, World world, BlockPos pos, Random random) {
        if (state.get(ACTIVATED)) {
            for (int i = 0; i < 3; i++) {
                double x = pos.getX() + 3 + (random.nextDouble() - 3);
                double y = pos.getY() + 0.5 + random.nextDouble() * 2;
                double z = pos.getZ() + 3 + (random.nextDouble() - 3);
                world.addParticleClient(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0.0, 0.01, 0.0);
            }
        }
    }

    public void onBlockAdded(BlockState state, World world, BlockPos pos, BlockState oldState, boolean notify) {
        if (!world.isClient()) {
            world.scheduleBlockTick(pos, this, curseCheckTime);
        }
    }

    // Add curse to nearby players when broken
    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        BlockState result = super.onBreak(world, pos, state, player);
        playDestroyEffects(world, pos);
        return result;
    }

    @Override
    public void onDestroyedByExplosion(ServerWorld world, BlockPos pos, Explosion explosion) {
        playDestroyEffects(world, pos);
    }

    public void playDestroyEffects(World world, BlockPos pos) {
        if (!world.isClient()) {
            List<PlayerEntity> nearbyPlayers = world.getEntitiesByClass(
                    PlayerEntity.class,
                    new Box(pos).expand(curseCheckRadius),
                    p -> true
            );

            for (PlayerEntity nearbyPlayer : nearbyPlayers) {
                if(!nearbyPlayer.isCreative()) {
                    nearbyPlayer.increaseStat(Scorch.CURSE_EXPOSURE_STAT, curseExposureBreakAmount);
                }
            }
        }

        Random random = world.getRandom();
        for (int i = 0; i < 16; i++) {
            double x = pos.getX() + 0.6 + (random.nextDouble() - 0.6);
            double y = pos.getY() + 0.6 + (random.nextDouble() - 0.6);
            double z = pos.getZ() + 0.6 + (random.nextDouble() - 0.6);
            world.addParticleClient(ParticleTypes.SOUL_FIRE_FLAME, x, y, z, 0.0, 0.01, 0.0);
        }

        world.playSound(null, pos, ScorchSounds.CURSE_ON_BREAK, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
    }

    protected void onStateReplaced(BlockState state, ServerWorld world, BlockPos pos, boolean moved) {
        tryCursePlayer(state, world, pos);
    }

    @Override
    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        tryCursePlayer(state, world, pos);
    }

    public void tryCursePlayer(BlockState state, ServerWorld world, BlockPos pos) {
        if(state.get(ACTIVATED)) {
            checkForCursedPlayers(world, pos, true);
            world.scheduleBlockTick(pos, this, curseCheckTime);
        }
    }

    @Override
    protected boolean hasRandomTicks(BlockState state) {
        return true;
    }

    @Override
    protected void randomTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
        if(!checkForCursedPlayers(world, pos, false)) {
            world.setBlockState(pos, state.with(ACTIVATED, false), 3);
        } else {
            if(!state.get(ACTIVATED)) {
                wakeUp(world, pos, state);
            }
        }
    }

    private boolean checkForCursedPlayers(ServerWorld world, BlockPos pos, boolean addCurseToPlayer) {

        boolean foundCursedPlayer = false;

        List<PlayerEntity> players = world.getEntitiesByClass(
                PlayerEntity.class,
                new Box(pos).expand(curseCheckRadius),
                player -> true
        );

        if (!players.isEmpty()) {
            for (PlayerEntity player : players) {
                ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;

                if(addCurseToPlayer && serverPlayer.getStatHandler().getStat(Scorch.CURSE_EXPOSURE_STAT) == 0) {
                    serverPlayer.increaseStat(Scorch.CURSE_EXPOSURE_STAT, 1);
                    if(serverPlayer.getStatHandler().getStat(Scorch.CURSE_ACTIVE_STAT) < 2) {
                        serverPlayer.increaseStat(Scorch.CURSE_ACTIVE_STAT, 2);
                    }
                }

                if (serverPlayer.getStatHandler().getStat(Scorch.CURSE_EXPOSURE_STAT) > 0) {
                    serverPlayer.increaseStat(Scorch.CURSE_EXPOSURE_STAT, 1);
                    world.playSound(null, pos, ScorchSounds.CURSE, SoundCategory.BLOCKS, 1.0F, 0.9F + world.random.nextFloat() * 0.2F);
                    if(serverPlayer.getStatHandler().getStat(Scorch.CURSE_ACTIVE_STAT) < 2) {
                        serverPlayer.increaseStat(Scorch.CURSE_ACTIVE_STAT, 2);
                    }
                }

                if(serverPlayer.getStatHandler().getStat(Scorch.CURSE_EXPOSURE_STAT) >= wakeCurseAmount) {
                    foundCursedPlayer = true;
                }
            }
        }
        return foundCursedPlayer;
    }


    public static void wakeUp(ServerWorld world, BlockPos pos, BlockState state) {
        world.playSound(null, pos, ScorchSounds.CURSE_FIRST_TIME, SoundCategory.BLOCKS, 1.0F, 0.8F + world.random.nextFloat() * 0.4F);
        world.setBlockState(pos, state.with(ACTIVATED, true), 3);
    }

    protected void onStacksDropped(BlockState state, ServerWorld world, BlockPos pos, ItemStack tool, boolean dropExperience) {
        super.onStacksDropped(state, world, pos, tool, dropExperience);
        if (dropExperience) {
            this.dropExperienceWhenMined(world, pos, tool, UniformIntProvider.create(10, 18));
        }

    }

}
