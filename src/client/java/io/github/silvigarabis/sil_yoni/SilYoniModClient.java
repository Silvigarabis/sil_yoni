package io.github.silvigarabis.sil_yoni;

import io.github.silvigarabis.sil_yoni.keybinding.PowerActiveKeyListener;
import net.fabricmc.api.ClientModInitializer;

public class SilYoniModClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        PowerActiveKeyListener.init();
        BadgeManager.init();
    }
}
