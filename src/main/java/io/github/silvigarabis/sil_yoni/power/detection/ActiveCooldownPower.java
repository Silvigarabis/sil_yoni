package io.github.silvigarabis.sil_yoni.power.detection;

import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.CooldownPower;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.util.HudRender;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import io.github.silvigarabis.sil_yoni.data.DataTypes;
import io.github.silvigarabis.sil_yoni.data.Key;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;

import java.util.function.Consumer;

public class ActiveCooldownPower extends CooldownPower implements io.github.silvigarabis.sil_yoni.power.detection.Active {
    public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "detection/mutable_active_self");
    private final @Nullable Key key;
    private final Consumer<Entity> activeFunction;

    public ActiveCooldownPower(
            PowerType<?> type,
            LivingEntity entity,
            @Nullable Key key,
            int cooldownDuration,
            HudRender hudRender,
            Consumer<Entity> activeFunction
    ) {
        super(type, entity, cooldownDuration, hudRender);
        this.key = key;
        this.activeFunction = activeFunction;
    }

    @Override
    public @Nullable Key getUseKey() {
        return key;
    }

    @Override
    public void onUse() {
        if(canUse()) {
            this.activeFunction.accept(this.entity);
            super.use();
        }
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(ID,
                new SerializableData()
                        .add("entity_action", ApoliDataTypes.ENTITY_ACTION)
                        .add("cooldown", SerializableDataTypes.INT, 1)
                        .add("hud_render", ApoliDataTypes.HUD_RENDER, HudRender.DONT_RENDER)
                        .add("key", DataTypes.KEY_DATA_TYPE, null),
                data ->
                        (type, player) -> new ActiveCooldownPower(
                                type,
                                player,
                                data.get("key"),
                                data.getInt("cooldown"),
                                data.get("hud_render"),
                                data.get("entity_action")
                        ))
                .allowCondition();
    }
}
