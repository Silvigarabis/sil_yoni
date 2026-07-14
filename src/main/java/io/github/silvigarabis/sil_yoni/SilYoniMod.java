package io.github.silvigarabis.sil_yoni;

import io.github.silvigarabis.sil_yoni.feature.FireBurnAbsorbFeature;
import io.github.silvigarabis.sil_yoni.networking.ModC2SPackets;
import io.github.silvigarabis.sil_yoni.power.misc.PehkuiValuePower;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.entity.event.v1.ServerEntityWorldChangeEvents;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SilYoniMod implements ModInitializer {
    // This logger is used to write text to the console and the log file.
    // It is considered best practice to use your mod id as the logger's name.
    // That way, it's clear which mod wrote info, warnings, and errors.
    private static final Logger LOGGER = LoggerFactory.getLogger("SIL YONI");
    public static final String MOD_ID = "sil_yoni";

    public static Identifier identifier(String id) {
        return Identifier.of(MOD_ID, id);
    }

    @Override
    public void onInitialize() {
        // This code runs as soon as Minecraft is in a mod-load-ready state.
        // However, some things (like resources) may still be uninitialized.
        // Proceed with mild caution.

        LOGGER.info("Initialize SilYoniMod... A mod just for me... to add some origin");

        SilYoniSounds.register();
        SilYoniPowers.register();
        ModC2SPackets.register();
        FireBurnAbsorbFeature.init();

        PehkuiValuePower.init();



        LOGGER.info("Done. Have a nice day.");
        
    }
}