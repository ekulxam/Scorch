package astrazoey.scorch;

import com.mojang.serialization.Codec;
import net.minecraft.component.ComponentType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ScorchComponents {

    public static final ComponentType<Boolean> STRIDER_HAIR_STATE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Scorch.id("strider_hair_state"),
            ComponentType.<Boolean>builder().codec(Codec.BOOL).build()
    );

    public static final ComponentType<Integer> STRIDER_HAIR_GROWTH = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Scorch.id("strider_hair_growth"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    public static final ComponentType<Integer> STRIDER_HAIR_STYLE = Registry.register(
            Registries.DATA_COMPONENT_TYPE,
            Scorch.id("strider_hair_style"),
            ComponentType.<Integer>builder().codec(Codec.INT).build()
    );

    protected static void initialize() {
        Scorch.LOGGER.info("Registering {} components", Scorch.MOD_ID);
    }


}
