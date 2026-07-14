package io.github.silvigarabis.sil_yoni.mixin;

import io.github.silvigarabis.sil_yoni.feature.FireBurnAbsorbFeature;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Mixin(ServerWorld.class)
public class ServerWorldMixin implements FireBurnAbsorbFeature.DataGuxiFireTracking {
    @Unique
    private final Set<BlockPos> sil_yoni$leavingFireTracking = new HashSet<>();

    @Unique
    private final Map<BlockPos, FireBurnAbsorbFeature> sil_yoni$guxiFireTracking = new HashMap<>();

    public Map<BlockPos, FireBurnAbsorbFeature> sil_yoni$guxiFireTracking() {
        return sil_yoni$guxiFireTracking;
    }

    @Override
    public Set<BlockPos> sil_yoni$leavingFireTracking() {
        return sil_yoni$leavingFireTracking;
    }
}
