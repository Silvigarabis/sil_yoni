package io.github.silvigarabis.sil_yoni.mixin;

import io.github.silvigarabis.sil_yoni.feature.FireBurnAbsorbFeature;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(World.class)
public class WorldMixin {
    @Inject(
            method = "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;II)Z",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;onBlockChanged(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;)V", shift = At.Shift.AFTER)
    )
    void onChanged(BlockPos pos, BlockState state, int flags, int maxUpdateDepth, CallbackInfoReturnable<Boolean> cir){
        if (((Object)this) instanceof ServerWorld serverWorld && !((World)(Object)this).getBlockState(pos).isOf(Blocks.FIRE)){
            ((FireBurnAbsorbFeature.DataGuxiFireTracking)serverWorld).sil_yoni$leavingFireTracking().remove(pos);
            ((FireBurnAbsorbFeature.DataGuxiFireTracking)serverWorld).sil_yoni$guxiFireTracking().remove(pos);
        }
    }
}
