package io.github.silvigarabis.sil_yoni.detection;

import io.github.apace100.apoli.power.PowerType;
import net.minecraft.util.Identifier;

public interface PowerActive extends Active {
    PowerType<?> getType();

    default Identifier getActiveIdentifier(){
        return getType().getIdentifier();
    }
}
