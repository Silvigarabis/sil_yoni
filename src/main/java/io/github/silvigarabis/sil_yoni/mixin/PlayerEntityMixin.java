package io.github.silvigarabis.sil_yoni.mixin;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(PlayerEntity.class)
public interface PlayerEntityMixin {
    @Accessor("sleepTimer")
    int sleepTimer();
    @Accessor("sleepTimer")
    void sleepTimer(int value);
}
