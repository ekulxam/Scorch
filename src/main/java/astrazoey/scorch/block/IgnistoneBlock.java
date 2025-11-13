package astrazoey.scorch.block;

import astrazoey.scorch.registry.ScorchSounds;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.explosion.Explosion;

public class IgnistoneBlock extends Block {
    public IgnistoneBlock(Settings settings) {
        super(settings);
    }

    @Override
    public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
        if (!world.isClient()) {
            ItemStack stack = player.getMainHandStack();

            RegistryEntry<Enchantment> enchantment = player.getRegistryManager().getOrThrow(RegistryKeys.ENCHANTMENT).getOrThrow(Enchantments.SILK_TOUCH);
            boolean hasSilkTouch = EnchantmentHelper.getLevel(enchantment, stack) > 0;

            if (!hasSilkTouch && !player.isCreative()) {
                placeLava(world, pos);
            }
        }
        return super.onBreak(world, pos, state, player);
    }

    @Override
    public void onDestroyedByExplosion(ServerWorld world, BlockPos blockPos, Explosion explosion) {
        placeLava(world, blockPos);
    }

    public void placeLava(World world, BlockPos pos) {
        world.setBlockState(pos, Blocks.LAVA.getDefaultState());
        world.playSound(null, pos, ScorchSounds.IGNISTONE_DROPS_LAVA, SoundCategory.BLOCKS, 1.0F, 0.75F);
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.LAVA, pos.getX(), pos.getY(), pos.getZ(), 5,  0.1d, 0.1d, 0.1d, 0.2d);
            serverWorld.spawnParticles(ParticleTypes.SMOKE, pos.getX(), pos.getY(), pos.getZ(), 5,  0.1d, 0.1d, 0.1d, 0.2d);
        }
    }
}
