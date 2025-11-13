package astrazoey.scorch.registry;

import astrazoey.scorch.Scorch;
import astrazoey.scorch.block.*;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.*;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.BlockSoundGroup;

import java.util.function.Function;

public class ScorchBlocks {

    @SuppressWarnings("SameParameterValue")
    private static Block register(String name, Function<AbstractBlock.Settings, Block> blockFactory, AbstractBlock.Settings settings, boolean shouldRegisterItem) {
        // Create a registry key for the block
        RegistryKey<Block> blockKey = keyOfBlock(name);
        // Create the block instance
        Block block = blockFactory.apply(settings.registryKey(blockKey));

        // Sometimes, you may not want to register an item for the block.
        // Eg: if it's a technical block like `minecraft:moving_piston` or `minecraft:end_gateway`
        if (shouldRegisterItem) {
            // Items need to be registered with a different type of registry key, but the ID
            // can be the same.
            RegistryKey<Item> itemKey = keyOfItem(name);

            BlockItem blockItem = new BlockItem(block, new Item.Settings().registryKey(itemKey).useBlockPrefixedTranslationKey());
            Registry.register(Registries.ITEM, itemKey, blockItem);
        }

        return Registry.register(Registries.BLOCK, blockKey, block);
    }

    private static RegistryKey<Block> keyOfBlock(String name) {
        return RegistryKey.of(RegistryKeys.BLOCK, Scorch.id(name));
    }

    private static RegistryKey<Item> keyOfItem(String name) {
        return RegistryKey.of(RegistryKeys.ITEM, Scorch.id(name));
    }

    public static final Block PYRACK = register(
            "pyrack",
            PyrackBlock::new,
            AbstractBlock.Settings.create().sounds(BlockSoundGroup.NETHERRACK).hardness(3.0f).requiresTool().strength(3.0f),
            true
    );

    public static final Block IGNISTONE = register(
            "ignistone",
            IgnistoneBlock::new,
            AbstractBlock.Settings.create().sounds(BlockSoundGroup.NETHERRACK).hardness(6.0f).requiresTool().strength(3.0f),
            true
    );

    public static final Block IRONSAND = register(
            "ironsand",
            IronsandBlock::new,
            AbstractBlock.Settings.create().sounds(BlockSoundGroup.SOUL_SOIL).hardness(0.5f).requiresTool().strength(0.8f),
            true
    );

    public static final Block IRONSTONE = register(
            "ironstone",
            Block::new,
            AbstractBlock.Settings.create().sounds(BlockSoundGroup.IRON).hardness(5f).requiresTool().strength(6.0f),
            true
    );

    public static final Block CHISELED_IRONSTONE = register(
            "chiseled_ironstone",
            Block::new,
            AbstractBlock.Settings.create().sounds(BlockSoundGroup.IRON).hardness(5f).requiresTool().strength(6.0f),
            true
    );

    public static final Block POSSESSED_CHISELED_IRONSTONE = register(
            "possessed_chiseled_ironstone",
            PossessedChiseledIronstoneBlock::new,
            AbstractBlock.Settings.create().sounds(BlockSoundGroup.IRON).hardness(5f).requiresTool().strength(12.0f),
            true
    );

    public static final Block IRONSTONE_SLAB = register(
            "ironstone_slab",
            SlabBlock::new,
            AbstractBlock.Settings.create().sounds(BlockSoundGroup.IRON).hardness(5f).requiresTool().strength(6.0f),
            true
    );

    public static final Block IRONSTONE_STAIRS = register(
            "ironstone_stairs",
            settings -> new StairsBlock(IRONSTONE.getDefaultState(), settings),
            AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.IRON)
                    .hardness(5f)
                    .requiresTool()
                    .strength(6.0f),
            true
    );

    public static final Block IRONSTONE_WALL = register(
            "ironstone_wall",
            WallBlock::new,
            AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.IRON)
                    .hardness(5f)
                    .requiresTool()
                    .strength(6.0f),
            true
    );

    public static final Block BURIAL_URN = register(
            "burial_urn",
            BurialUrnBlock::new,
            AbstractBlock.Settings.create()
                    .sounds(BlockSoundGroup.IRON)
                    .hardness(2f)
                    .strength(0.3f),
            true
    );


    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> itemGroup.add(ScorchBlocks.PYRACK));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> itemGroup.add(ScorchBlocks.IGNISTONE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.NATURAL).register((itemGroup) -> itemGroup.add(ScorchBlocks.IRONSAND));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register((itemGroup) -> itemGroup.add(ScorchBlocks.IRONSTONE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register((itemGroup) -> itemGroup.add(ScorchBlocks.CHISELED_IRONSTONE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register((itemGroup) -> itemGroup.add(ScorchBlocks.POSSESSED_CHISELED_IRONSTONE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((itemGroup) -> itemGroup.add(ScorchBlocks.POSSESSED_CHISELED_IRONSTONE));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register((itemGroup) -> itemGroup.add(ScorchBlocks.IRONSTONE_STAIRS));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register((itemGroup) -> itemGroup.add(ScorchBlocks.IRONSTONE_SLAB));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register((itemGroup) -> itemGroup.add(ScorchBlocks.IRONSTONE_WALL));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.BUILDING_BLOCKS).register((itemGroup) -> itemGroup.add(ScorchBlocks.BURIAL_URN));
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((itemGroup) -> itemGroup.add(ScorchBlocks.BURIAL_URN));
    }
}
