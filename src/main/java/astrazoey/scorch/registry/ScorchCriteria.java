package astrazoey.scorch.registry;

import astrazoey.scorch.Scorch;
import astrazoey.scorch.criterion.*;
import net.minecraft.advancement.criterion.Criterion;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ScorchCriteria {
    public static final GenericPlayerCriterion SHOOT_BLOCK = register("shoot_block", new GenericPlayerCriterion());
    public static final GenericPlayerCriterion WITHER_FIREBALL = register("wither_fireball", new GenericPlayerCriterion());
    public static final GenericPlayerCriterion CURE_CURSE = register("cure_curse", new GenericPlayerCriterion());
    public static final GenericPlayerCriterion USE_RETURNING_TOTEM = register("use_returning_totem", new GenericPlayerCriterion());
    public static final GenericPlayerCriterion STYLE_STRIDER = register("style_strider", new GenericPlayerCriterion());
    public static final GenericPlayerCriterion SHEAR_STRIDER = register("shear_strider", new GenericPlayerCriterion());

    private static <T extends Criterion<?>> T register(String name, T criterion) {
        return register(Scorch.id(name), criterion);
    }

    private static <T extends Criterion<?>> T register(Identifier id, T criterion) {
        return Registry.register(Registries.CRITERION, id, criterion);
    }

    public static void initialize() {
    }
}
