package io.github.silvigarabis.sil_yoni.power.misc;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import io.github.silvigarabis.sil_yoni.mixin.PlayerEntityMixin;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class NoActualSleepPower extends Power {

    public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "misc/no_actual_sleep");

    public NoActualSleepPower(PowerType<?> type, LivingEntity entity){
        super(type, entity);
        if (entity instanceof PlayerEntity){
            super.setTicking();
        }
    }

    public static PowerFactory createFactory(){
        return new PowerFactory<>(
                ID,
                new SerializableData(),
                data -> (type, entity) -> new NoActualSleepPower(type, entity)
        ).allowCondition();
    }

    @Override
    public void tick() {
        if (entity instanceof PlayerEntityMixin mixinPlayer){
            mixinPlayer.sleepTimer(1);
        }
    }
}
