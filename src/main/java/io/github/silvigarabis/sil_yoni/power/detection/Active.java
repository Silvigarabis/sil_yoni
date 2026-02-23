package io.github.silvigarabis.sil_yoni.power.detection;

import io.github.apace100.apoli.power.PowerType;
import io.github.silvigarabis.sil_yoni.data.Key;
import org.jetbrains.annotations.Nullable;

public interface Active {
    /**
     * 在通过按键激活时会调用此方法
     */
    void onUse();
    @Nullable Key getUseKey();
    PowerType<?> getType();
}
