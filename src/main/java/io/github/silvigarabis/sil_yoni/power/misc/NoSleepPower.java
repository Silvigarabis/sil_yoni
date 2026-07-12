package io.github.silvigarabis.sil_yoni.power.misc;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class NoSleepPower extends Power {

    public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "misc/no_sleep");

    public NoSleepPower(PowerType<?> type, LivingEntity entity){
        super(type, entity);
    }

    public static PowerFactory createFactory(){
        return new PowerFactory<>(
                ID,
                new SerializableData(),
                data -> (type, entity) -> new NoSleepPower(type, entity)
        ).allowCondition();
    }
}
