package io.github.silvigarabis.sil_yoni.power;

import io.github.apace100.apoli.Apoli;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.CooldownPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.VariableIntPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import net.minecraft.entity.LivingEntity;
import org.jetbrains.annotations.Nullable;

public class ConvertFoodToResourcePower extends Power {

    public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "convert_food_to_resource");

    private final boolean shouldConvertFood;
    private final boolean shouldConvertSaturation;
    private final boolean shouldAcceptFood;
    private final boolean shouldAcceptSaturation;
    private final @Nullable PowerType<?> resourceType;
    private final float resourceScale;

    public ConvertFoodToResource(
            PowerType<?> type,
            LivingEntity entity,
            @Nullable PowerType<?> resourceType,
            float resourceScale,
            boolean shouldConvertFood, boolean shouldConvertSaturation,
            boolean shouldAcceptFood, boolean shouldAcceptSaturation
            ) {

        super(type, entity);

        this.resourceType = resourceType;
        this.resourceScale = resourceScale;

        this.shouldConvertFood = shouldConvertFood;
        this.shouldConvertSaturation = shouldConvertSaturation;

        this.shouldAcceptSaturation = shouldAcceptSaturation;
        this.shouldAcceptFood = shouldAcceptFood;
    }

    public void addFoodToResource(int food){
        if (shouldConvertFood) {
            addValueToResource(food);
        }
    }

    public void addSaturationToResource(float saturation) {
        if (shouldConvertSaturation) {
            addValueToResource(saturation);
        }
    }

    private void addValueToResource(double addValue) {
        PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
        Power p = component.getPower(resourceType);
        if (p instanceof VariableIntPower vip) {
            int newValue = vip.getValue();
            newValue += (int) (addValue * resourceScale);
            vip.setValue(newValue);
            PowerHolderComponent.syncPower(entity, resourceType);
        } else if (p instanceof CooldownPower cp) {
            cp.modify((int) (addValue * resourceScale));
            PowerHolderComponent.syncPower(entity, resourceType);
        }
    }

    public static PowerFactory<Power> createFactory() {
        return new PowerFactory<>(ID,
                new SerializableData()
                        .add("resource", ApoliDataTypes.POWER_TYPE, null)
                        .add("scale", SerializableDataTypes.FLOAT, 1f)
                        .add("convert_saturation", SerializableDataTypes.BOOLEAN, true)
                        .add("convert_food", SerializableDataTypes.BOOLEAN, true)
                        .add("accept_saturation", SerializableDataTypes.BOOLEAN, false)
                        .add("accept_food", SerializableDataTypes.BOOLEAN, false),
                data -> (powerType, livingEntity) -> new ConvertFoodToResource(
                        powerType,
                        livingEntity,
                        data.get("resource"),
                        data.get("scale"),
                        data.get("convert_food"),
                        data.get("convert_saturation"),
                        data.get("accept_food"),
                        data.get("accept_saturation")
                )
        ).allowCondition();
    }

    public boolean shouldConvertFood() {
        return shouldConvertFood;
    }

    public boolean shouldConvertSaturation() {
        return shouldConvertSaturation;
    }

    public boolean shouldAcceptFood() {
        return shouldAcceptFood;
    }

    public boolean shouldAcceptSaturation() {
        return shouldAcceptSaturation;
    }
}
