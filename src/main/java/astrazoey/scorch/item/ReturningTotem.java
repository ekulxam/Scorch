package astrazoey.scorch.item;

import astrazoey.scorch.network.ReturningTotemS2CPayload;
import astrazoey.scorch.registry.ScorchCriteria;
import astrazoey.scorch.registry.ScorchSounds;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.consume.UseAction;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Set;

public class ReturningTotem extends Item {
    private static final int USE_DURATION = 60;

    public ReturningTotem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return USE_DURATION;
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.BOW;
    }

    @Override
    public ActionResult use(World world, PlayerEntity player, Hand hand) {
        player.setCurrentHand(hand);
        return ActionResult.CONSUME;
    }

    @Override
    public boolean onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {

        if(!(user instanceof ServerPlayerEntity serverPlayer) || world.isClient()) return false;

        int useTime = this.getMaxUseTime(stack, user) - remainingUseTicks;

        if(useTime < 20) return false;

        // Get respawn info
        var respawnData = serverPlayer.getRespawn() != null ? serverPlayer.getRespawn().respawnData() : null;
        RegistryKey<World> dimensionKey = respawnData != null ? respawnData.getDimension() : World.OVERWORLD;

        MinecraftServer server = serverPlayer.getEntityWorld().getServer();

        ServerWorld targetWorld = server.getWorld(dimensionKey);
        if (targetWorld == null) return false;

        BlockPos spawnPos = respawnData != null && respawnData.getPos() != null
                ? respawnData.getPos()
                : targetWorld.getSpawnPoint().getPos();

        // Send a packet first to prevent bugs with mid-teleportation
        ReturningTotemS2CPayload returningPayload = new ReturningTotemS2CPayload(stack);
        ServerPlayNetworking.send(serverPlayer, returningPayload);

        // Grant Advancement
        ScorchCriteria.USE_RETURNING_TOTEM.trigger(serverPlayer);

        // Teleport
        serverPlayer.teleport(
                targetWorld,
                spawnPos.getX() + 0.5,
                spawnPos.getY() + 0.1,
                spawnPos.getZ() + 0.5,
                Set.of(),
                user.getYaw(),
                user.getPitch(),
                false
        );

        targetWorld.playSound(
                null,
                spawnPos,
                ScorchSounds.RETURN,
                SoundCategory.PLAYERS,
                1.0F,
                1.0F
        );

        if (!serverPlayer.isCreative()) stack.decrement(1);
        return true;
    }

}