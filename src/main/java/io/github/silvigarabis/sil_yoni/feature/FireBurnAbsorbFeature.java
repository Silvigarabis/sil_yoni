package io.github.silvigarabis.sil_yoni.feature;

import io.github.silvigarabis.sil_yoni.mixin.FireBlockInvoker;
import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;

public class FireBurnAbsorbFeature {
    private int activeTicks = 0;

    public FireBurnAbsorbFeature(LivingEntity entity) {
        // 每个 guxi 都会创建一个对应的实例
    }

    public static boolean callGuxiInactiveFireRemoved(BlockState state, ServerWorld world, BlockPos pos) {
        return world instanceof DataGuxiFireTracking data && data.silYoni$callGuxiInactiveFireRemoved(pos);
    }

    public static void trySpreadGuxiFire(FireBlock fireBlock, World world, BlockPos sourcePos, BlockPos targetPos) {
        if (world instanceof DataGuxiFireTracking data) {
            data.silYoni$trySpreadGuxiFire(fireBlock, sourcePos, targetPos);
        }
    }

    private boolean isActive() {
        return activeTicks > 0;
    }

    public void tickActive() {
        activeTicks++;
        if (activeTicks > 40) activeTicks = 40;
    }

    public void tickInactive() {
        if (activeTicks > 0) activeTicks--;
    }

    public void inactiveImmediate() {
        this.activeTicks = 0;
    }

    public void onAdd(){

    }

    public void onRemove(){
        this.inactiveImmediate();
    }

    public static boolean isGuxiActiveFire(ServerWorld world, BlockPos pos){
        return world instanceof DataGuxiFireTracking data && data.silYoni$isGuxiActiveFire(pos);
    }

    public interface DataGuxiFireTracking {
        Map<BlockPos, FireBurnAbsorbFeature> sil_yoni$guxiFireTracking();
        default void silYoni$setGuxiFireOwner(BlockPos pos, FireBurnAbsorbFeature owner){
            sil_yoni$guxiFireTracking().put(pos, owner);
        }
        default boolean silYoni$isGuxiActiveFire(BlockPos pos){
            var owner = sil_yoni$guxiFireTracking().get(pos);
            return owner != null && owner.isActive();
        }
        default FireBurnAbsorbFeature silYoni$getGuxiFireOwner(BlockPos pos){
            return sil_yoni$guxiFireTracking().get(pos);
        }
        default void silYoni$trySpreadGuxiFire(FireBlock fireBlock, BlockPos sourcePos, BlockPos targetPos) {
            var sourceOwner = silYoni$getGuxiFireOwner(sourcePos);
            var targetOwner = silYoni$getGuxiFireOwner(targetPos);
            assert sourceOwner != null;
            if (targetOwner != null) return;

            // TODO: 尝试传播 guxi fire

            // 我们也许会使用传播几率作为要添加到GUXI上的能量
            int spreadChance = ((FireBlockInvoker)fireBlock).silYoni$getSpreadChance(((World)this).getBlockState(targetPos));

            ((World)this).setBlockState(targetPos, ((FireBlockInvoker)fireBlock).silYoni$getStateForPosition((World)this, targetPos), FireBlock.NOTIFY_ALL);
            silYoni$setGuxiFireOwner(targetPos, sourceOwner);
        }

        default boolean silYoni$callGuxiInactiveFireRemoved(BlockPos pos){
            var owner = silYoni$getGuxiFireOwner(pos);
            if (owner != null && !owner.isActive()){
                return sil_yoni$guxiFireTracking().remove(pos) != null;
            }
            return false;
        }
    }
}
