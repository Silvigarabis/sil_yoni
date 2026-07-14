package io.github.silvigarabis.sil_yoni.detection;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.origins.Origins;
import io.github.silvigarabis.sil_yoni.data.Key;
import io.github.silvigarabis.sil_yoni.keybinding.TriggerPattern;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Active {
    /**
     * 在通过按键激活时会调用此方法
     */
    void onUse();
    @NotNull List<Key> getUseKeys();
    Identifier getActiveIdentifier();

    default boolean matchAnyUseKey(Map<String, Set<TriggerPattern>> triggerMap) {
        for (var k : getUseKeys()) {
            var triggers = triggerMap.get(k.key());
            if (triggers == null || triggers.isEmpty()) continue;

            for (var pattern : k.triggers()) {
                if (triggers.contains(pattern)) {
                    return true;
                }
            }
        }
        return false;
    }

    default Identifier getBadgeSpriteFor(TriggerPattern pattern) {
        return Origins.identifier("textures/gui/badge/active.png");
    }

    default String getTranslateFor(TriggerPattern pattern) {
        return pattern.translationKey();
    }
}
