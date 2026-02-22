package io.github.silvigarabis.sil_yoni.power.detection;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.util.HudRender;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import java.util.function.Consumer;

public class ActiveCooldownPower extends io.github.apace100.apoli.power.ActiveCooldownPower {
    public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "detection/mutable_active_self");

    public ActiveCooldownPower(PowerType<?> type, LivingEntity entity, int cooldownDuration, HudRender hudRender, Consumer<Entity> activeFunction) {
        super(type, entity, cooldownDuration, hudRender, activeFunction);
    }
}
