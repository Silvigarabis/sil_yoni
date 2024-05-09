package io.github.silvigarabis.sil_yoni;

import net.minecraft.util.Identifier;
import net.minecraft.sound.SoundEvent;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;

import java.util.Map;
import java.util.HashMap;

public class SilYoniSounds {
    public static void register(){
        addSoundEvent("entity.fairy.fly");
        addSoundEvent("entity.spirit_of_void.fly");
    }

    private static void addSoundEvent(String name){
        Identifier id = new Identifier(SilYoniMod.MOD_ID, name);
        SoundEvent event = SoundEvent.of(id);
        sounds.put(name, event);
        Registry.register(Registries.SOUND_EVENT, id, event);
    }

    public static SoundEvent get(String name){
        if (sounds.containsKey(name)){
            return sounds.get(name);
        }
        throw new NullPointerException("sound event " + name + " not found!");
    }

    private static final Map<String, SoundEvent> sounds = new HashMap<>();
}