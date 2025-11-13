package astrazoey.scorch.registry;

import astrazoey.scorch.Scorch;
import astrazoey.scorch.item.ReturningTotem;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Rarity;

import java.util.function.Function;

public class ScorchItems {

    public static final Item RETURNING_TOTEM = register("returning_totem", ReturningTotem::new, new Item.Settings().maxCount(1).rarity(Rarity.UNCOMMON));
    public static final Item IRONSAND_CLUMP = register("ironsand_clump", Item::new, new Item.Settings());

    public static Item register(String name, Function<Item.Settings, Item> itemFactory, Item.Settings settings) {
        // Create the item key.
        RegistryKey<Item> itemKey = RegistryKey.of(RegistryKeys.ITEM, Scorch.id(name));

        // Create the item instance.
        Item item = itemFactory.apply(settings.registryKey(itemKey));

        // Register the item.
        Registry.register(Registries.ITEM, itemKey, item);

        return item;
    }

    public static void initialize() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.TOOLS)
                .register((itemGroup) -> itemGroup.add(ScorchItems.RETURNING_TOTEM));

        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
                .register((itemGroup) -> itemGroup.add(ScorchItems.IRONSAND_CLUMP));
    }
}
