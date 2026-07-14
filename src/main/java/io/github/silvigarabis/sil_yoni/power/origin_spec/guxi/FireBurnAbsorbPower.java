package io.github.silvigarabis.sil_yoni.power.origin_spec.guxi;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import io.github.silvigarabis.sil_yoni.data.DataTypes;
import io.github.silvigarabis.sil_yoni.data.Key;
import io.github.silvigarabis.sil_yoni.detection.PowerActive;
import io.github.silvigarabis.sil_yoni.feature.FireBurnAbsorbFeature;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class FireBurnAbsorbPower extends Power implements PowerActive {

    public static final Identifier ID = SilYoniMod.identifier("origin_spec/guxi/energy/absorb_fire_burn");
    private final List<Key> triggerKeys;
    private final FireBurnAbsorbFeature feat;

    public FireBurnAbsorbPower(PowerType<?> type, LivingEntity entity, List<Key> triggerKeys) {
        super(type, entity);
        this.triggerKeys = triggerKeys;
        setTicking(true);
        this.feat = new FireBurnAbsorbFeature(entity);
    }

    private int activeTicks = 0;

    @Override
    public void onUse() {
        activeTicks += 200;
        if (activeTicks > 1200) {
            activeTicks = 1200;
        }
    }

    @Override
    public void tick() {
        if (activeTicks > 0 && super.isActive()) {
            activeTicks--;
            this.feat.tickActive();
        } else {
            this.feat.tickInactive();
        }
    }

    @Override
    public void onLost() {
        this.feat.inactiveImmediate();
    }

    @Override
    public @NotNull List<Key> getUseKeys() {
        return triggerKeys;
    }

    public static PowerFactory createFactory(){
        return new PowerFactory<>(ID,
                new SerializableData()
                        .add("trigger", DataTypes.BACKWARDS_COMPATIBLE_KEY_LIST, Collections.emptyList()),
                data ->
                        (type, player) -> new FireBurnAbsorbPower(
                                type,
                                player,
                                data.get("trigger")
                        ))
                .allowCondition();
    }
}
