package io.github.silvigarabis.sil_yoni;

import net.minecraft.util.Identifier;
import net.minecraft.sound.SoundEvent;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;

public class SilYoniSounds {
    public static final SoundEvent FAIRY_FLY = create("entity.fairy.fly");
    public static final SoundEvent SPIRIT_OF_VOID_FLY = create("entity.spirit_of_void.fly");

    private static SoundEvent create(String name){
        Identifier id = SilYoniMod.identifier(name);
        SoundEvent event = SoundEvent.of(id);
        Registry.register(Registries.SOUND_EVENT, id, event);
        return event;
    }

    public static void register(){
        // cinit
    }
}