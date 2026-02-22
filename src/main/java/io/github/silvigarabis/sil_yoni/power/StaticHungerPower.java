package io.github.silvigarabis.sil_yoni.power;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class StaticHungerPower extends Power {

    public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "misc/static_hunger");
    private final boolean staticFoodLevel;
    private final boolean staticSaturationLevel;
    private final int foodLevel;
    private final float saturationLevel;

    public StaticHungerPower(PowerType<Power> type, LivingEntity entity, boolean staticFoodLevel, boolean staticSaturationLevel, int foodLevel, float saturationLevel) {
        super(type, entity);
        setTicking();
        this.staticFoodLevel = staticFoodLevel;
        this.staticSaturationLevel = staticSaturationLevel;
        this.foodLevel = foodLevel;
        this.saturationLevel = saturationLevel;
    }

    public static PowerFactory<Power> createFactory(){
        return new PowerFactory<>(
                ID,
                new SerializableData()
                        .add("static_food_level", SerializableDataTypes.BOOLEAN, false)
                        .add("static_saturation_level", SerializableDataTypes.BOOLEAN, false)
                        .add("food_level", SerializableDataTypes.INT, 0)
                        .add("saturation_level", SerializableDataTypes.FLOAT, 0f)
                ,
                data -> (type, entity) -> new StaticHungerPower(
                        type,
                        entity,
                        data.getBoolean("static_food_level"),
                        data.getBoolean("static_saturation_level"),
                        data.getInt("food_level"),
                        data.getFloat("saturation_level")
                )
        ).allowCondition();
    }

    public void update(PlayerEntity player) {
        if (staticFoodLevel) {
            player.getHungerManager().setFoodLevel(foodLevel);
        }
        if (staticSaturationLevel) {
            player.getHungerManager().setSaturationLevel(saturationLevel);
        }
    }

    @Override
    public boolean shouldTick() {
        return super.shouldTick() && entity instanceof PlayerEntity;
    }
}
