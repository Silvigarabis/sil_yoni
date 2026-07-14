package io.github.silvigarabis.sil_yoni.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import io.github.silvigarabis.sil_yoni.feature.FireBurnAbsorbFeature;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FireBlock.class)
public abstract class FireBlockMixin {
//    @Override
//    public void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
//        world.scheduleBlockTick(pos, this, getFireTickDelay(world.random));
//        if (world.getGameRules().getBoolean(GameRules.DO_FIRE_TICK)) {
//            if (!state.canPlaceAt(world, pos)) {
    @ModifyExpressionValue(
            method = "scheduledTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;canPlaceAt(Lnet/minecraft/world/WorldView;Lnet/minecraft/util/math/BlockPos;)Z",
                    ordinal = 0
            )
    )
    private boolean silYoni$guxiRemovingFire(boolean original, BlockState state, ServerWorld world, BlockPos pos, Random random) {
        return original & !FireBurnAbsorbFeature.callGuxiRemovingFireRemoved(world, pos) & !FireBurnAbsorbFeature.callGuxiInactiveFireRemoved(state, world, pos);
    }
//                world.removeBlock(pos, false);
    @Inject(
            method = "scheduledTick",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;removeBlock(Lnet/minecraft/util/math/BlockPos;Z)Z", shift = At.Shift.AFTER)
    )
    void silYoni$guxiRemovedFire(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci){
        ((FireBurnAbsorbFeature.DataGuxiFireTracking)world).sil_yoni$leavingFireTracking().remove(pos);
        ((FireBurnAbsorbFeature.DataGuxiFireTracking)world).sil_yoni$guxiFireTracking().remove(pos);
    }
//            }
//
//            BlockState blockState = world.getBlockState(pos.down());
//            boolean bl = blockState.isIn(world.getDimension().infiniburn());

    @ModifyExpressionValue(
            method = "scheduledTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/BlockState;isIn(Lnet/minecraft/registry/tag/TagKey;)Z",
                    ordinal = 0
            ),
            slice = @Slice(
                    from = @At(
                            value = "INVOKE",
                            target = "Lnet/minecraft/world/dimension/DimensionType;infiniburn()Lnet/minecraft/registry/tag/TagKey;"
                    )
            )
    )
    private boolean silYoni$guxiInfiniteFire(boolean original, BlockState state, ServerWorld world, BlockPos pos, Random random) {
        return original || FireBurnAbsorbFeature.isGuxiActiveFire(world, pos);
    }

//            int i = state.get(AGE);
//            if (!bl && world.isRaining() && this.isRainingAround(world, pos) && random.nextFloat() < 0.2F + i * 0.03F) {
//                world.removeBlock(pos, false);
//            } else {
//                int j = Math.min(15, i + random.nextInt(3) / 2);
//                if (i != j) {
//                    state = state.with(AGE, j);
//                    world.setBlockState(pos, state, Block.NO_REDRAW);
//                }
//
//                if (!bl) {
//                    if (!this.areBlocksAroundFlammable(world, pos)) {
//                        BlockPos blockPos = pos.down();
//                        if (!world.getBlockState(blockPos).isSideSolidFullSquare(world, blockPos, Direction.UP) || i > 3) {
//                            world.removeBlock(pos, false);
//                        }
//
//                        return;
//                    }
//
//                    if (i == 15 && random.nextInt(4) == 0 && !this.isFlammable(world.getBlockState(pos.down()))) {
//                        world.removeBlock(pos, false);
//                        return;
//                    }
//                }
//
//                boolean bl2 = world.getBiome(pos).isIn(BiomeTags.INCREASED_FIRE_BURNOUT);
//                int k = bl2 ? -50 : 0;
    @WrapOperation(
            method = "scheduledTick",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/block/FireBlock;trySpreadingFire(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;ILnet/minecraft/util/math/random/Random;I)V"
            ),
            require = 6
    )
    private void silYoni$trySpreadingFire(
            FireBlock $instance,
            World $instance$world,
            BlockPos $instance$pos,
            int $instance$_factor,
            Random $instance$_random,
            int $instance$_age,

            Operation<Void> original,

            // scheduledTick 的参数
            BlockState $method_state,
            ServerWorld $method_world,
            BlockPos $method_pos,
            Random $method_random
    ) {
        if (!FireBurnAbsorbFeature.isGuxiActiveFire($method_world, $method_pos)) {
            original.call($instance, $instance$world, $instance$pos, $instance$_factor, $instance$_random, $instance$_age);
            return;
        }

        FireBurnAbsorbFeature.trySpreadGuxiFire($instance, $instance$world, $method_pos, $instance$pos);
    }
//                this.trySpreadingFire(world, pos.east(), 300 + k, random, i);
//                this.trySpreadingFire(world, pos.west(), 300 + k, random, i);
//                this.trySpreadingFire(world, pos.down(), 250 + k, random, i);
//                this.trySpreadingFire(world, pos.up(), 250 + k, random, i);
//                this.trySpreadingFire(world, pos.north(), 300 + k, random, i);
//                this.trySpreadingFire(world, pos.south(), 300 + k, random, i);
    @Inject(
            method = "scheduledTick",
            at = @At(
                    value = "NEW",
                    target = "net/minecraft/util/math/BlockPos$Mutable"
            ),
            cancellable = true
    )
    private void silYoni$skipAirSpread(
            BlockState state,
            ServerWorld world,
            BlockPos pos,
            Random random,
            CallbackInfo ci
    ) {
        if (FireBurnAbsorbFeature.isGuxiActiveFire(world, pos)) {
            ci.cancel();
        }
    }
//                BlockPos.Mutable mutable = new BlockPos.Mutable();
//
//                for (int l = -1; l <= 1; l++) {
//                    for (int m = -1; m <= 1; m++) {
//                        for (int n = -1; n <= 4; n++) {
//                            if (l != 0 || n != 0 || m != 0) {
//                                int o = 100;
//                                if (n > 1) {
//                                    o += (n - 1) * 100;
//                                }
//
//                                mutable.set(pos, l, n, m);
//                                int p = this.getBurnChance(world, mutable);
//                                if (p > 0) {
//                                    int q = (p + 40 + world.getDifficulty().getId() * 7) / (i + 30);
//                                    if (bl2) {
//                                        q /= 2;
//                                    }
//
//                                    if (q > 0 && random.nextInt(o) <= q && (!world.isRaining() || !this.isRainingAround(world, mutable))) {
//                                        int r = Math.min(15, i + random.nextInt(5) / 4);
//                                        world.setBlockState(mutable, this.getStateWithAge(world, mutable, r), Block.NOTIFY_ALL);
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//    }
}
