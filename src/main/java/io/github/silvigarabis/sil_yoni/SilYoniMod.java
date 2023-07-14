package com.example;

import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import net.minecraft.util.Identifier;
import net.minecraft.sound.SoundEvent;
import net.minecraft.registry.Registry;
import net.minecraft.registry.Registries;

public class SilYoniMod implements ModInitializer {
	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
    public static final Logger LOGGER = LoggerFactory.getLogger("SIL YONI");

    public static final Identifier SOUND_ENTITY_FAIRY_FLY_ID = new Identifier("sil_yoni:entity.fairy.fly");
    public static final SoundEvent SOUND_ENTITY_FAIRY_FLY_EVENT = SoundEvent.of(SOUND_ENTITY_FAIRY_FLY_ID);
    
	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.

		LOGGER.info("Initialize SilYoniMod...");
		
		Registry.register(Registries.SOUND_EVENT, SOUND_ENTITY_FAIRY_FLY_ID, SOUND_ENTITY_FAIRY_FLY_EVENT);
		
		LOGGER.info("Done.");
		
	}
}