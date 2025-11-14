package astrazoey.scorch;

import astrazoey.scorch.block.PossessedChiseledIronstoneBlock;
import astrazoey.scorch.events.FinishUsingCallback;
import astrazoey.scorch.events.OnKilledByCallback;
import astrazoey.scorch.network.CurseActiveS2CPayload;
import astrazoey.scorch.network.CurseExposureS2CPayload;
import astrazoey.scorch.network.ReturningTotemS2CPayload;
import astrazoey.scorch.registry.ScorchAttachmentTypes;
import astrazoey.scorch.registry.ScorchBlocks;
import astrazoey.scorch.registry.ScorchCriteria;
import astrazoey.scorch.registry.ScorchItems;
import astrazoey.scorch.registry.ScorchSounds;
import astrazoey.scorch.world.gen.ScorchWorldGeneration;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.fabric.api.item.v1.DefaultItemComponentEvents;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.block.BlockState;
import net.minecraft.component.ComponentMap;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.DamageResistantComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.mob.WitherSkeletonEntity;
import net.minecraft.entity.passive.StriderEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stat;
import net.minecraft.stat.StatFormatter;
import net.minecraft.stat.Stats;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.function.Consumer;

public class Scorch implements ModInitializer {
    public static final String MOD_ID = "scorch";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    public static final Identifier CURSE_EXPOSURE = id("curse_exposure");
    public static Stat<Identifier> CURSE_EXPOSURE_STAT;

    public static final Identifier CURSE_ACTIVE = id("curse_active");
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
            if (player.isCreative()) {
                return;
            }
            if (!(world instanceof ServerWorld serverWorld)) {
                return;
            }
            triggerNearbyBlocks(serverWorld, pos);
        });

        OnKilledByCallback.EVENT.register((player, killedEntity) -> {
            if (player.isCreative()) {
                return;
            }
            if (player instanceof ServerPlayerEntity serverPlayer) {
                if (killedEntity instanceof WitherSkeletonEntity) {
                    triggerNearbyBlocks(serverPlayer.getEntityWorld(), player.getBlockPos());
                }
            }
        });

        UseEntityCallback.EVENT.register(Scorch::interactStrider);

        DefaultItemComponentEvents.MODIFY.register(context -> {
            Consumer<ComponentMap.Builder> fireImmunity = builder -> {
                // see Item.Settings#fireproof
                builder.add(DataComponentTypes.DAMAGE_RESISTANT, new DamageResistantComponent(DamageTypeTags.IS_FIRE));
            };
            context.modify(Items.NETHER_STAR, fireImmunity);
            context.modify(Items.WITHER_SKELETON_SKULL, fireImmunity);
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

                if (curseLevel > 0) {
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
        });

        // Payload for displaying the returning totem floating item effect
        PayloadTypeRegistry.playS2C().register(ReturningTotemS2CPayload.ID, ReturningTotemS2CPayload.CODEC);

    }

    // Wake up Nearby Possessed Chiseled Ironstone Blocks
    private static void triggerNearbyBlocks(ServerWorld world, BlockPos eventPos) {
        BlockPos.stream(eventPos.add(-5, -4, -5), eventPos.add(5, 4, 5)).forEach(pos -> {
            BlockState state = world.getBlockState(pos);
            if (state.getBlock() instanceof PossessedChiseledIronstoneBlock && !state.get(PossessedChiseledIronstoneBlock.ACTIVATED)) {
                PossessedChiseledIronstoneBlock.wakeUp(world, pos, state);
                world.setBlockState(pos, state.with(PossessedChiseledIronstoneBlock.ACTIVATED, true));
            }
        });
    }

    @SuppressWarnings("UnstableApiUsage")
    private static ActionResult interactStrider(PlayerEntity player, World world, Hand hand, Entity entity, EntityHitResult hitResult) {
        if (!(world instanceof ServerWorld serverWorld)) {
            return ActionResult.PASS;
        }

        if (!(entity instanceof StriderEntity strider)) {
            return ActionResult.PASS;
        }

        if (player.isSpectator()) {
            return ActionResult.PASS;
        }

        ItemStack stack = player.getStackInHand(hand);
        if (stack.isEmpty()) {
            return ActionResult.PASS;
        }

        if (strider.getAttachedOrCreate(ScorchAttachmentTypes.STRIDER_HAIR_STATE) && stack.isOf(Items.SHEARS)) {
            strider.dropItem(serverWorld, Items.STRING);

            strider.setAttached(ScorchAttachmentTypes.STRIDER_HAIR_STATE, false);

            stack.damage(1, player, hand);

            world.playSound(null, strider.getBlockPos(), ScorchSounds.SHEAR_STRIDER, SoundCategory.PLAYERS, 1.0F, 1.0F);

            ScorchCriteria.SHEAR_STRIDER.trigger((ServerPlayerEntity) player);
            return ActionResult.SUCCESS_SERVER;
        }

        if (strider.getAttachedOrCreate(ScorchAttachmentTypes.STRIDER_HAIR_STATE) && player.getStackInHand(hand).isOf(Items.MAGMA_CREAM)) {
            int updatedHairStyle = (strider.getAttachedOrCreate(ScorchAttachmentTypes.STRIDER_HAIR_STYLE) + 1) % 5;
            strider.setAttached(ScorchAttachmentTypes.STRIDER_HAIR_STYLE, updatedHairStyle);

            stack.decrementUnlessCreative(1, player);

            world.playSound(null, strider.getBlockPos(), ScorchSounds.APPLY_MAGMA, SoundCategory.PLAYERS, 1.0F, 1.0F);

            ScorchCriteria.STYLE_STRIDER.trigger((ServerPlayerEntity) player);
            return ActionResult.SUCCESS_SERVER;
        }

        if (!strider.getAttachedOrCreate(ScorchAttachmentTypes.STRIDER_HAIR_STATE) && player.getStackInHand(hand).isOf(Items.WARPED_ROOTS)) {
            strider.setAttached(ScorchAttachmentTypes.STRIDER_HAIR_GROWTH, strider.getAttachedOrCreate(ScorchAttachmentTypes.STRIDER_HAIR_GROWTH) + 1200);

            stack.decrementUnlessCreative(1, player);

            world.playSound(null, strider.getBlockPos(), SoundEvents.ENTITY_STRIDER_EAT, SoundCategory.NEUTRAL, 1.0F, 1.0F);
            return ActionResult.SUCCESS_SERVER;
        }

        return ActionResult.PASS;
    }
    
    public static Identifier id(String path) {
        return Identifier.of(Scorch.MOD_ID, path);
    }
}