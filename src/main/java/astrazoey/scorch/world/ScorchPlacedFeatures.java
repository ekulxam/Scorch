package astrazoey.scorch.world;

import astrazoey.scorch.Scorch;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.YOffset;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.PlacedFeature;
import net.minecraft.world.gen.placementmodifier.HeightRangePlacementModifier;
import net.minecraft.world.gen.placementmodifier.PlacementModifier;

import java.util.List;

public class ScorchPlacedFeatures {

    public static final RegistryKey<PlacedFeature> PYRACK_SMALL_PLACED_KEY = registerKey("pyrack_small_placed");
    public static final RegistryKey<PlacedFeature> PYRACK_LARGE_PLACED_KEY = registerKey("pyrack_large_placed");




    public static void bootstrap(Registerable<PlacedFeature> context) {
        var configuredFeatures = context.getRegistryLookup(RegistryKeys.CONFIGURED_FEATURE);

        register(context, PYRACK_SMALL_PLACED_KEY, configuredFeatures.getOrThrow(ScorchConfiguredFeatures.PYRACK_ORE_SMALL_KEY),
                ScorchOrePlacement.modifiersWithCount(34,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(30), YOffset.fixed(230)))
                );
        register(context, PYRACK_LARGE_PLACED_KEY, configuredFeatures.getOrThrow(ScorchConfiguredFeatures.PYRACK_ORE_LARGE_KEY),
                ScorchOrePlacement.modifiersWithCount(8,
                        HeightRangePlacementModifier.uniform(YOffset.fixed(-32), YOffset.fixed(20)))
                );



    }

    public static RegistryKey<PlacedFeature> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.PLACED_FEATURE, Identifier.of(Scorch.MOD_ID, name));
    }

    private static void register(Registerable<PlacedFeature> context,
                                 RegistryKey<PlacedFeature> key,
                                 RegistryEntry<ConfiguredFeature<?,?>> configuration,
                                 List<PlacementModifier> modifiers) {
        context.register(key, new PlacedFeature(configuration, List.copyOf(modifiers)));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(
            Registerable<PlacedFeature> context,
            RegistryKey<PlacedFeature> key,
            RegistryEntry<ConfiguredFeature<?, ?>> configuration,
            PlacementModifier... modifiers) {
        register(context, key, configuration, List.of(modifiers));
    }


}
