package astrazoey.scorch;

import astrazoey.scorch.datagen.ScorchRegistryDataGenerator;
import astrazoey.scorch.world.ScorchConfiguredFeatures;
import astrazoey.scorch.world.ScorchPlacedFeatures;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.minecraft.registry.RegistryBuilder;
import net.minecraft.registry.RegistryKeys;

public class ScorchDataGenerator implements DataGeneratorEntrypoint {
    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        pack.addProvider(ScorchRegistryDataGenerator::new);
        System.out.println("Scorch Data Generator Initialized!");
    }

    @Override
    public void buildRegistry(RegistryBuilder registryBuilder) {
        registryBuilder.addRegistry(RegistryKeys.CONFIGURED_FEATURE, ScorchConfiguredFeatures::bootstrap);
        registryBuilder.addRegistry(RegistryKeys.PLACED_FEATURE, ScorchPlacedFeatures::bootstrap);
    }
}
