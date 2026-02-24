package io.github.silvigarabis.sil_yoni.power.modify;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.ValueModifyingPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

public class ModifyFoodEatTimePower extends ValueModifyingPower {
    public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "modify/food_eat_time");

    public ModifyFoodEatTimePower(PowerType<?> type, LivingEntity entity) {
        super(type, entity);
    }

    public static PowerFactory createFactory(){
        return ValueModifyingPower.createValueModifyingFactory(
                ModifyFoodEatTimePower::new,
                ID
        );
    }
}
