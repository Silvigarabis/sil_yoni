package io.github.silvigarabis.sil_yoni.power.misc;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.CooldownPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.VariableIntPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

public class ConvertFoodToResourcePower extends Power {

    public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "misc/convert_food_to_resource");

    private final boolean shouldConvertFood;
    private final boolean shouldConvertSaturation;
    private final boolean shouldConvertExhaustion;
    private final boolean shouldAcceptFood;
    private final boolean shouldAcceptSaturation;
    private final boolean shouldAcceptExhaustion;
    private final @Nullable PowerType<?> resourceType;
    private final float resourceScale;

    public ConvertFoodToResourcePower(
            PowerType<?> type,
            LivingEntity entity,
            @Nullable PowerType<?> resourceType,
            float resourceScale,
            boolean shouldConvertFood, boolean shouldConvertSaturation, boolean shouldConvertExhaustion,
            boolean shouldAcceptFood, boolean shouldAcceptSaturation, boolean shouldAcceptExhaustion
    ) {

        super(type, entity);

        this.resourceType = resourceType;
        this.resourceScale = resourceScale;

        this.shouldConvertFood = shouldConvertFood;
        this.shouldConvertSaturation = shouldConvertSaturation;
        this.shouldConvertExhaustion = shouldConvertExhaustion;

        this.shouldAcceptSaturation = shouldAcceptSaturation;
        this.shouldAcceptFood = shouldAcceptFood;
        this.shouldAcceptExhaustion = shouldAcceptExhaustion;
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

    public void addExhaustionToResource(int exhaustionFood) {
        if (shouldConvertExhaustion) {
            addValueToResource(-exhaustionFood);
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
                        .add("convert_exhaustion", SerializableDataTypes.BOOLEAN, true)
                        .add("accept_saturation", SerializableDataTypes.BOOLEAN, false)
                        .add("accept_food", SerializableDataTypes.BOOLEAN, false)
                        .add("accept_exhaustion", SerializableDataTypes.BOOLEAN, false),
                data -> (powerType, livingEntity) -> new ConvertFoodToResourcePower(
                        powerType,
                        livingEntity,
                        data.get("resource"),
                        data.get("scale"),
                        data.get("convert_food"),
                        data.get("convert_saturation"),
                        data.get("convert_exhaustion"),
                        data.get("accept_food"),
                        data.get("accept_saturation"),
                        data.get("accept_saturation")
                )
        ).allowCondition();
    }

    public boolean shouldAcceptFood() {
        return shouldAcceptFood;
    }

    public boolean shouldAcceptSaturation() {
        return shouldAcceptSaturation;
    }

    public boolean shouldAcceptExhaustion() {
        return shouldAcceptExhaustion;
    }

}
