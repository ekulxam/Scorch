package astrazoey.scorch.registry;

import astrazoey.scorch.Scorch;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;

public class ScorchDamageTypes {
    public static final RegistryKey<DamageType> CURSE_DAMAGE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(Scorch.MOD_ID, "curse"));


}
