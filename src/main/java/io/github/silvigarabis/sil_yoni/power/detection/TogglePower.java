package io.github.silvigarabis.sil_yoni.power.detection;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.apace100.origins.Origins;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import io.github.silvigarabis.sil_yoni.data.DataTypes;
import io.github.silvigarabis.sil_yoni.data.Key;
import io.github.silvigarabis.sil_yoni.detection.PowerActive;
import io.github.silvigarabis.sil_yoni.keybinding.TriggerPattern;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtByte;
import net.minecraft.nbt.NbtElement;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Collections;
import java.util.List;

public class TogglePower extends Power implements PowerActive {
    public static final Identifier ID = SilYoniMod.identifier("detection/mutable_toggle");
    private final List<Key> keys;

    private boolean isActive;
    private final boolean shouldRetainState;

    public TogglePower(PowerType<?> type, LivingEntity entity,
                       @NotNull List<Key> keys,
                       boolean activeByDefault, boolean shouldRetainState) {
        super(type, entity);
        this.keys = List.copyOf(keys);
        this.shouldRetainState = shouldRetainState;
        this.isActive = activeByDefault;
    }

    @Override
    public boolean shouldTick() {
        return !shouldRetainState && !this.conditions.isEmpty();
    }

    @Override
    public boolean shouldTickWhenInactive() {
        return true;
    }

    @Override
    public void tick() {
        if(!super.isActive() && this.isActive) {
            this.isActive = false;
            PowerHolderComponent.syncPower(entity, this.type);
        }
    }

    @Override
    public void onUse() {
        this.isActive = !this.isActive;
        PowerHolderComponent.syncPower(entity, this.type);
    }

    @Override
    public @NotNull List<Key> getUseKeys() {
        return keys;
    }

    public boolean isActive() {
        return this.isActive && super.isActive();
    }

    @Override
    public NbtElement toTag() {
        return NbtByte.of(isActive);
    }

    @Override
    public void fromTag(NbtElement tag) {
        isActive = ((NbtByte)tag).byteValue() > 0;
    }

    @Override
    public Identifier getBadgeSpriteFor(TriggerPattern pattern) {
        return Origins.identifier("textures/gui/badge/toggle.png");
    }

    @Override
    public String getTranslateFor(TriggerPattern pattern) {
        return pattern.toggleTranslationKey();
    }

    public static PowerFactory createFactory() {
        return new PowerFactory<>(ID,
                new SerializableData()
                        .add("active_by_default", SerializableDataTypes.BOOLEAN, true)
                        .add("retain_state", SerializableDataTypes.BOOLEAN, true)
                        .add("key", DataTypes.BACKWARDS_COMPATIBLE_KEY_LIST, Collections.emptyList()),
                data ->
                        (type, player) -> new TogglePower(
                                type,
                                player,
                                data.get("key"),
                                data.getBoolean("active_by_default"),
                                data.getBoolean("retain_state")
                        )
        ).allowCondition();
    }
}
