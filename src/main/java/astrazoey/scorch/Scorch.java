package astrazoey.scorch;

import astrazoey.scorch.block.PossessedChiseledIronstoneBlock;
import astrazoey.scorch.events.FinishUsingCallback;
import astrazoey.scorch.events.OnKilledByCallback;
import astrazoey.scorch.network.CurseActiveS2CPayload;
import astrazoey.scorch.network.CurseExposureS2CPayload;
import astrazoey.scorch.network.ReturningTotemS2CPayload;
import astrazoey.scorch.registry.ScorchBlocks;
import astrazoey.scorch.registry.ScorchCriteria;
import astrazoey.scorch.registry.ScorchItems;
import astrazoey.scorch.registry.ScorchSounds;
import astrazoey.scorch.world.gen.ScorchWorldGeneration;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Scorch implements ModInitializer {
    public static final String MOD_ID = "scorch";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Identifier CURSE_EXPOSURE = Identifier.of(MOD_ID, "curse_exposure");
    public static Stat<Identifier> CURSE_EXPOSURE_STAT;

    public static final Identifier CURSE_ACTIVE = Identifier.of(MOD_ID, "curse_active");
    public static Stat<Identifier> CURSE_ACTIVE_STAT;

    @Override
    public void onInitialize() {
        ScorchBlocks.initialize();
        ScorchSounds.initialize();
        ScorchItems.initialize();
        //ScorchComponents.initialize();
        ScorchCriteria.initialize();
        ScorchWorldGeneration.generateScorchWorldGen();


        PlayerBlockBreakEvents.AFTER.register((world, player, pos, state, entity) -> {
            if(player.isCreative()) {
                return;
            }
            triggerNearbyBlocks(world, pos);
        });

        OnKilledByCallback.EVENT.register((player, killedEntity) -> {
            if(player.isCreative()) {
                return ActionResult.PASS;
            }
            if(player instanceof ServerPlayerEntity serverPlayer) {
                if(killedEntity instanceof WitherSkeletonEntity) {
                    triggerNearbyBlocks(player.getEntityWorld(), player.getBlockPos());
                }
            }
            return ActionResult.PASS;
        });

        Registry.register(Registries.CUSTOM_STAT, "curse_exposure", CURSE_EXPOSURE);
        Stats.CUSTOM.getOrCreateStat(CURSE_EXPOSURE, StatFormatter.DEFAULT);
        CURSE_EXPOSURE_STAT = Stats.CUSTOM.getOrCreateStat(CURSE_EXPOSURE);
        PayloadTypeRegistry.playS2C().register(CurseExposureS2CPayload.ID, CurseExposureS2CPayload.CODEC);

        Registry.register(Registries.CUSTOM_STAT, "curse_active", CURSE_ACTIVE);
        Stats.CUSTOM.getOrCreateStat(CURSE_ACTIVE, StatFormatter.DEFAULT);
        CURSE_ACTIVE_STAT = Stats.CUSTOM.getOrCreateStat(CURSE_ACTIVE);
        PayloadTypeRegistry.playS2C().register(CurseActiveS2CPayload.ID, CurseActiveS2CPayload.CODEC);

        // Reduce Curse when eating Golden Apple
        FinishUsingCallback.EVENT.register((player, stack) -> {
            if (player instanceof ServerPlayerEntity serverPlayer) {

                int curseLevel = serverPlayer.getStatHandler().getStat(Scorch.CURSE_EXPOSURE_STAT);

                if(curseLevel > 0) {
                    if (stack.getItem() == Items.GOLDEN_APPLE) {
                        player.increaseStat(CURSE_EXPOSURE_STAT, -15);
                        ScorchCriteria.CURE_CURSE.trigger(serverPlayer);
                    }

                    if (stack.getItem() == Items.ENCHANTED_GOLDEN_APPLE) {
                        player.increaseStat(CURSE_EXPOSURE_STAT, -100);
                        ScorchCriteria.CURE_CURSE.trigger(serverPlayer);
                    }
                }

                if (curseLevel < 0) {
                    player.resetStat(Scorch.CURSE_EXPOSURE_STAT);
                }
            }
            return ActionResult.PASS;
        });

        // Payload for displaying the returning totem floating item effect
        PayloadTypeRegistry.playS2C().register(ReturningTotemS2CPayload.ID, ReturningTotemS2CPayload.CODEC);



    }

    // Wake up Nearby Possessed Chiseled Ironstone Blocks
    private static void triggerNearbyBlocks(World world, BlockPos eventPos) {
        BlockPos.stream(eventPos.add(-5, -4, -5), eventPos.add(5, 4, 5)).forEach(pos -> {
            if(world instanceof ServerWorld) {
                BlockState state = world.getBlockState(pos);
                if (state.getBlock() instanceof PossessedChiseledIronstoneBlock && !state.get(PossessedChiseledIronstoneBlock.ACTIVATED)) {
                    PossessedChiseledIronstoneBlock.wakeUp((ServerWorld) world, pos, state);
                    world.setBlockState(pos, state.with(PossessedChiseledIronstoneBlock.ACTIVATED, true));
                }
            }
        });
    }
}