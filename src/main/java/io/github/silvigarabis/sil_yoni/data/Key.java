package io.github.silvigarabis.sil_yoni.data;

import io.github.silvigarabis.sil_yoni.keybinding.TriggerPattern;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.util.Set;

public record Key(@NotNull String key, @NotNull @Unmodifiable Set<@NotNull TriggerPattern> triggers) {
    public Key {
        triggers = Set.copyOf(triggers);
    }
}
