package astrazoey.scorch.registry;

import astrazoey.scorch.Scorch;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;

public class ScorchDamageTypes {
    public static final RegistryKey<DamageType> CURSE_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Scorch.id("curse"));
}
