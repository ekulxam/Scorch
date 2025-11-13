package astrazoey.scorch.world;

import astrazoey.scorch.Scorch;
import astrazoey.scorch.registry.ScorchBlocks;
import net.minecraft.registry.Registerable;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.structure.rule.RuleTest;
import net.minecraft.structure.rule.TagMatchRuleTest;
import net.minecraft.util.Identifier;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.FeatureConfig;
import net.minecraft.world.gen.feature.OreFeatureConfig;

import java.util.List;

public class ScorchConfiguredFeatures {

    public static final RegistryKey<ConfiguredFeature<?, ?>> PYRACK_ORE_LARGE_KEY = registerKey("pyrack_ore_large");
    public static final RegistryKey<ConfiguredFeature<?, ?>> PYRACK_ORE_SMALL_KEY = registerKey("pyrack_ore_small");

    public static void bootstrap(Registerable<ConfiguredFeature<?, ?>> context) {
        RuleTest netherReplaceable = new TagMatchRuleTest(BlockTags.BASE_STONE_NETHER);

        List<OreFeatureConfig.Target> smallPyrackOres = List.of(OreFeatureConfig.createTarget(netherReplaceable, ScorchBlocks.PYRACK.getDefaultState()));
        List<OreFeatureConfig.Target> largePyrackOres = List.of(OreFeatureConfig.createTarget(netherReplaceable, ScorchBlocks.PYRACK.getDefaultState()));

        register(context, PYRACK_ORE_SMALL_KEY, Feature.ORE, new OreFeatureConfig(smallPyrackOres, 16));
        register(context, PYRACK_ORE_LARGE_KEY, Feature.ORE, new OreFeatureConfig(largePyrackOres, 32));
    }

    public static RegistryKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return RegistryKey.of(RegistryKeys.CONFIGURED_FEATURE, Scorch.id(name));
    }

    private static <FC extends FeatureConfig, F extends Feature<FC>> void register(Registerable<ConfiguredFeature<?, ?>> context,
                                                                                   RegistryKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }

}
