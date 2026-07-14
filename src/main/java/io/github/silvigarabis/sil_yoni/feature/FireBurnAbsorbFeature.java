package io.github.silvigarabis.sil_yoni.feature;

import io.github.silvigarabis.sil_yoni.mixin.FireBlockInvoker;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Map;
import java.util.Set;

public class FireBurnAbsorbFeature {
    private final LivingEntity entity;
    private int activeTicks = 0;

    public FireBurnAbsorbFeature(LivingEntity entity) {
        this.entity = entity;
    }

    public static boolean callGuxiInactiveFireRemoved(BlockState state, ServerWorld world, BlockPos pos) {
        return world instanceof DataGuxiFireTracking data && data.silYoni$callGuxiInactiveFireRemoved(pos);
    }

    public static void trySpreadGuxiFire(FireBlock fireBlock, World world, BlockPos sourcePos, BlockPos targetPos) {
        if (world instanceof DataGuxiFireTracking data) {
            data.silYoni$trySpreadGuxiFire(fireBlock, sourcePos, targetPos);
        }
    }

    public static boolean isGuxiRemovingFire(ServerWorld world, BlockPos pos) {
        return world instanceof DataGuxiFireTracking data && data.silYoni$callGuxiLeavingFireRemoved(pos);
    }

    private boolean isActive() {
        return activeTicks > 0;
    }

    public void tickActive() {
        activeTicks++;
        if (activeTicks > 20) activeTicks = 20;

        BlockPos.Mutable mutable = new BlockPos.Mutable();
        var center = entity.getBlockPos();
        var world = entity.getWorld();

        for (int x = -4; x <= 4; x++) {
            for (int y = -4; y <= 4; y++) {
                for (int z = -4; z <= 4; z++) {
                    mutable.set(center, x, y, z);

                    BlockState state = entity.getWorld().getBlockState(mutable);
                    if (state.isOf(Blocks.FIRE)){
                        setGuxiFireOwner(world, mutable, this);
                    }
                }
            }
        }
    }

    public void tickInactive() {
        if (activeTicks > 0) activeTicks--;
    }

    public void inactiveImmediate() {
        this.activeTicks = 0;
    }

    public void onAdd(){

    }

    public void onLost(){
        this.inactiveImmediate();
    }

    public void onRemoved() {
        this.inactiveImmediate();
    }

    public static void setGuxiFireOwner(World world, BlockPos pos, FireBurnAbsorbFeature owner){
        if (world instanceof DataGuxiFireTracking data) {
            data.silYoni$setGuxiFireOwner(pos, owner);
        }
    }

    public static boolean isGuxiActiveFire(ServerWorld world, BlockPos pos){
        return world instanceof DataGuxiFireTracking data && data.silYoni$isGuxiActiveFire(pos);
    }

    public static void init(){
        ServerLivingEntityEvents.AFTER_DEATH.register(
                (entity, source) -> {
                    if (entity.getWorld() instanceof DataGuxiFireTracking data){
                        data.silYoni$removeFireOfOwner(entity);
                    }
                }
        );
    }

    public interface DataGuxiFireTracking {
        Map<BlockPos, FireBurnAbsorbFeature> sil_yoni$guxiFireTracking();
        Set<BlockPos> sil_yoni$leavingFireTracking();
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

        default boolean silYoni$callGuxiLeavingFireRemoved(BlockPos pos){
            return sil_yoni$leavingFireTracking().remove(pos);
        }

        default void silYoni$removeFireOfOwner(LivingEntity entity){
            for (var entry : sil_yoni$guxiFireTracking().entrySet()){
                if (entry.getValue().isOwned(entity)){
                    sil_yoni$leavingFireTracking().add(entry.getKey());
                }
            }
        }
    }

    private boolean isOwned(LivingEntity entity) {
        return this.entity.equals(entity);
    }
}
