package io.github.silvigarabis.sil_yoni;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.Map;
import java.util.HashMap;
import net.minecraft.util.Identifier;
import net.minecraft.sound.SoundEvent;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;

public class SilYoniMod implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    private static final Logger LOGGER = LoggerFactory.getLogger("SIL YONI");

    // new sounds to be registering
    private static final Map<Identifier, SoundEvent> soundRegistries = new HashMap<>();

    private static void addSound(String name){
        Identifier id = new Identifier(name);
        SoundEvent eventRegistry = SoundEvent.of(id);
        soundRegistries.put(id, eventRegistry);
    }

    private static void printSystemEnv(){
        // debug only
        var envs = System.getenv();
        for (var name: envs.keySet()){
            LOGGER.info(name + "=" + envs.get(name));
        }
    }

    private void registerSoundEvents(){
        for (Map.Entry<Identifier, SoundEvent> entry : soundRegistries.entrySet()){
            Registry.register(
                Registries.SOUND_EVENT,
                entry.getKey(),
                entry.getValue()
            );
        }
        
    }

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Initialize SilYoniMod... A mod just for me... to add some origin");
        
        registerSoundEvents();
        
        LOGGER.info("Done. Have a nice day.");
        
    }

    static {
        addSound("sil_yoni:entity.fairy.fly");
        addSound("sil_yoni:entity.spirit_of_void.fly");
    }

}