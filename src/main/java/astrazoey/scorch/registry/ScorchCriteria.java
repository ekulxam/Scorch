package astrazoey.scorch.registry;

import astrazoey.scorch.Scorch;
import astrazoey.scorch.criterion.*;
import net.minecraft.advancement.criterion.Criteria;

public class ScorchCriteria {
    public static final ShootBlockCriterion SHOOT_BLOCK = Criteria.register(Scorch.MOD_ID + ":shoot_block", new ShootBlockCriterion());
    public static final WitherFireballCriterion WITHER_FIREBALL = Criteria.register(Scorch.MOD_ID + ":wither_fireball", new WitherFireballCriterion());
    public static final CureCurseCriterion CURE_CURSE = Criteria.register(Scorch.MOD_ID + ":cure_curse", new CureCurseCriterion());
    public static final UseReturningTotemCriterion USE_RETURNING_TOTEM = Criteria.register(Scorch.MOD_ID + ":use_returning_totem", new UseReturningTotemCriterion());
    public static final StyleStriderCriterion STYLE_STRIDER = Criteria.register(Scorch.MOD_ID + ":style_strider", new StyleStriderCriterion());
    public static final ShearStriderCriterion SHEAR_STRIDER = Criteria.register(Scorch.MOD_ID + ":shear_strider", new ShearStriderCriterion());

    public static void initialize() {
    }
}
