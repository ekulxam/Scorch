package astrazoey.scorch.registry;

import astrazoey.scorch.Scorch;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;

public class ScorchSounds {

    public static final SoundEvent IGNISTONE_DROPS_LAVA = registerSound("ignistone_drops_lava");
    public static final SoundEvent PYRACK_IGNITES = registerSound("pyrack_ignites");
    public static final SoundEvent CURSE_FIRST_TIME = registerSound("curse_first_time");
    public static final SoundEvent CURSE = registerSound("curse");
    public static final SoundEvent CURSE_ON_BREAK = registerSound("curse_on_break");
    public static final SoundEvent SHEAR_STRIDER = registerSound("shear_strider");
    public static final SoundEvent APPLY_MAGMA = registerSound("apply_magma");
    public static final SoundEvent RETURN = registerSound("return");

    private static SoundEvent registerSound(String id) {
        Identifier identifier = Scorch.id(id);
        return Registry.register(Registries.SOUND_EVENT, identifier, SoundEvent.of(identifier));
    }

    public static void initialize() {

    }
}
