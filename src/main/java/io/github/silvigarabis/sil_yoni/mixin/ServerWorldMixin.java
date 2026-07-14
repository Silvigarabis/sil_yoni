package io.github.silvigarabis.sil_yoni.mixin;

import io.github.silvigarabis.sil_yoni.feature.FireBurnAbsorbFeature;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin implements FireBurnAbsorbFeature.DataGuxiFireTracking {
    @Unique
    private final Map<BlockPos, FireBurnAbsorbFeature> sil_yoni$guxiFireTracking = new HashMap<>();

    public Map<BlockPos, FireBurnAbsorbFeature> sil_yoni$guxiFireTracking() {
        return sil_yoni$guxiFireTracking;
    }
}
