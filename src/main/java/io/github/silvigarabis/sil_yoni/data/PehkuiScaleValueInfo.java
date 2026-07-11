package io.github.silvigarabis.sil_yoni.data;

import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;
import virtuoel.pehkui.api.ScaleRegistries;
import virtuoel.pehkui.api.ScaleType;

import java.util.Objects;

public record PehkuiScaleValueInfo(@NotNull ScaleType type, @NotNull Identifier typeIdentifier, float baseValue) {
    public PehkuiScaleValueInfo(@NotNull ScaleType type, float baseValue) {
        this(type, ScaleRegistries.SCALE_TYPES.inverse().get(type), baseValue);
    }
    public PehkuiScaleValueInfo(@NotNull Identifier id, float baseValue) {
        this(Objects.requireNonNull(ScaleRegistries.SCALE_TYPES.get(id), () -> "no such scaleType:" + id), id, baseValue);
    }
    public PehkuiScaleValueInfo {
        Objects.requireNonNull(type, "type cannot be null");
        Objects.requireNonNull(typeIdentifier, "typeIdentifier cannot be null");
    }
}
