package io.github.silvigarabis.sil_yoni.power.misc;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import io.github.silvigarabis.sil_yoni.data.DataTypes;
import io.github.silvigarabis.sil_yoni.data.PehkuiScaleValueInfo;
import io.github.silvigarabis.sil_yoni.util.ReflectionUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import virtuoel.pehkui.api.ScaleType;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

public class PehkuiValuePower extends Power {
    public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "misc/pehkui_value");

    private final List<PehkuiScaleValueInfo> baseValueInfoList;
    private final List<PehkuiScaleValueInfo> targetValueInfoList;
    private final List<PehkuiScaleValueInfo> setValueInfoList;

    public PehkuiValuePower(PowerType<?> type, LivingEntity entity,
                            List<PehkuiScaleValueInfo> baseValueInfoList,
                            List<PehkuiScaleValueInfo> targetValueInfoList,
                            List<PehkuiScaleValueInfo> setValueInfoList
    ) {
        super(type, entity);
        this.baseValueInfoList = baseValueInfoList;
        this.targetValueInfoList = targetValueInfoList;
        this.setValueInfoList = setValueInfoList;
    }

    private void doSetup(){
        if (!ReflectionUtil.hasPehkui()) return;
        for (var info : baseValueInfoList) {
            info.type().getScaleData(this.entity).setBaseScale(info.baseValue());
        }
        for (var info : targetValueInfoList) {
            info.type().getScaleData(this.entity).setTargetScale(info.baseValue());
        }
        for (var info : setValueInfoList) {
            info.type().getScaleData(this.entity).setScale(info.baseValue());
        }
    }
    private void doSetupDown() {
        if (!ReflectionUtil.hasPehkui()) return;
        var scaleTypesAffected = new HashSet<ScaleType>();

        for (var info : baseValueInfoList) {
            scaleTypesAffected.add(info.type());
        }
        for (var info : targetValueInfoList) {
            scaleTypesAffected.add(info.type());
        }
        for (var info : setValueInfoList) {
            scaleTypesAffected.add(info.type());
        }
        for (var type : scaleTypesAffected) {
            type.getScaleData(this.entity).setScale(type.getDefaultBaseScale());
        }
    }

    @Override
    public void onAdded(){
        doSetup();
    }

    @Override
    public void onRespawn(){
        doSetup();
    }

    @Override
    public void onRemoved(){
        doSetupDown();
    }

    public static void applyAfterDimensionChanged(Entity entity){
        for (var power : PowerHolderComponent.getPowers(entity, PehkuiValuePower.class)){
            power.doSetup();
        }
    }

    public static PowerFactory<Power> createFactory() {
        return new PowerFactory<>(ID,
                new SerializableData()
                        .add("start", DataTypes.BACKWARDS_COMPATIBLE_PEHKUI_BASE_VALUE_INFO_LIST, Collections.emptyList())
                        .add("end", DataTypes.BACKWARDS_COMPATIBLE_PEHKUI_BASE_VALUE_INFO_LIST, Collections.emptyList())
                        .add("set", DataTypes.BACKWARDS_COMPATIBLE_PEHKUI_BASE_VALUE_INFO_LIST, Collections.emptyList()),
                data -> (powerType, livingEntity) -> new PehkuiValuePower(
                        powerType,
                        livingEntity,
                        data.get("start"),
                        data.get("end"),
                        data.get("set")
                )
        );
    }
}
