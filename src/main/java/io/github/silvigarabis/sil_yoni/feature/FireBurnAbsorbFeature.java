package io.github.silvigarabis.sil_yoni.feature;

import io.github.silvigarabis.sil_yoni.mixin.FireBlockInvoker;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;

public class FireBurnAbsorbFeature {

    static final Logger LOGGER = LogManager.getLogger("silYoniGuxiFire");

    private final LivingEntity entity;
    private int activeTicks = 0;

    public FireBurnAbsorbFeature(LivingEntity entity) {
        this.entity = entity;
    }

    public static boolean callGuxiInactiveFireRemoved(BlockState state, ServerWorld world, BlockPos pos) {
        return ((DataGuxiFireTracking)world).silYoni$callGuxiInactiveFireRemoved(pos);
    }

    public static void trySpreadGuxiFire(FireBlock fireBlock, World world, BlockPos sourcePos, BlockPos targetPos) {
        ((DataGuxiFireTracking)world).silYoni$trySpreadGuxiFire(fireBlock, sourcePos, targetPos);
    }

    public static boolean callGuxiRemovingFireRemoved(ServerWorld world, BlockPos pos) {
        return ((DataGuxiFireTracking)world).silYoni$callGuxiLeavingFireRemoved(pos);
    }

    public static boolean isGuxiFire(ServerWorld world, BlockPos pos) {
        return ((DataGuxiFireTracking)world).sil_yoni$guxiFireTracking().containsKey(pos)
                || ((DataGuxiFireTracking)world).sil_yoni$leavingFireTracking().contains(pos);
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
                        if (tryBecameNewGuxiFireOwner(world, mutable, this)){
                            LOGGER.info("[ACTIVE]: {}", mutable);
                        }
                    }
                }
            }
        }

        for (var pos : BlockPos.iterateInSquare(center, 8, Direction.UP, Direction.EAST)) {
            BlockState state = world.getBlockState(pos);
            if (!state.isOf(Blocks.FIRE)) {
                if (tryLintGuxiFire((FireBlock) Blocks.FIRE, world, pos, this))
                    break;
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

    public static boolean tryBecameNewGuxiFireOwner(World world, BlockPos pos, FireBurnAbsorbFeature owner){
        return ((DataGuxiFireTracking)world).silYoni$tryBecameNewGuxiFireOwner(pos, owner);
    }

    public static boolean tryLintGuxiFire(FireBlock fireBlock, World world, BlockPos pos, FireBurnAbsorbFeature owner) {
        return ((DataGuxiFireTracking)world).silYoni$tryLintGuxiFire(fireBlock, pos, owner);
    }

    public static boolean isGuxiActiveFire(ServerWorld world, BlockPos pos){
        return ((DataGuxiFireTracking)world).silYoni$isGuxiActiveFire(pos);
    }

    public static void init(){
        ServerLivingEntityEvents.AFTER_DEATH.register(
                (entity, source) -> {
                    if (!entity.getWorld().isClient)
                        ((DataGuxiFireTracking)entity.getWorld()).silYoni$removeFireOfOwner(entity);
                }
        );
        ServerTickEvents.END_WORLD_TICK.register(FireBurnAbsorbFeature::guxiRemovedFire);
    }

    public interface DataGuxiFireTracking {
        Map<BlockPos, FireBurnAbsorbFeature> sil_yoni$guxiFireTracking();
        Set<BlockPos> sil_yoni$leavingFireTracking();
        default boolean silYoni$tryBecameNewGuxiFireOwner(BlockPos pos, FireBurnAbsorbFeature owner){
            return null == sil_yoni$guxiFireTracking().putIfAbsent(pos.toImmutable(), owner);
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
            // TODO: 尝试限制传播范围 guxi fire

            // 我们也许会使用传播几率作为要添加到GUXI上的能量
            int spreadChance = ((FireBlockInvoker)fireBlock).silYoni$getSpreadChance(((World)this).getBlockState(targetPos));
            if (spreadChance > 0 && silYoni$tryBecameNewGuxiFireOwner(targetPos, sourceOwner)) {
                ((World) this).setBlockState(targetPos, ((FireBlockInvoker) fireBlock).silYoni$getStateForPosition((World) this, targetPos), FireBlock.NOTIFY_ALL);
                LOGGER.info("[SPREAD]: {}", targetPos);
            }
        }

        default boolean silYoni$callGuxiInactiveFireRemoved(BlockPos pos){
            var owner = silYoni$getGuxiFireOwner(pos);
            if (owner != null && !owner.isActive()){
                LOGGER.info("[INACTIVE]: {}", pos);
                return true;
            }
            return false;
        }

        default boolean silYoni$callGuxiLeavingFireRemoved(BlockPos pos){
            var removed = sil_yoni$leavingFireTracking().contains(pos);
            if (removed){
                LOGGER.info("[LEAVING]: {}", pos);
            }
            return removed;
        }

        default void silYoni$removeFireOfOwner(LivingEntity entity){
            for (var entry : sil_yoni$guxiFireTracking().entrySet()){
                if (entry.getValue().isOwned(entity)){
                    sil_yoni$leavingFireTracking().add(entry.getKey());
                }
            }
        }

        default boolean silYoni$tryLintGuxiFire(FireBlock fireBlock, BlockPos pos, FireBurnAbsorbFeature owner){
            // 我们也许会使用传播几率作为要添加到GUXI上的能量
            int burnChance = ((FireBlockInvoker)fireBlock).silYoni$getBurnChance((World)this, pos);
            if (burnChance > 0 && silYoni$tryBecameNewGuxiFireOwner(pos, owner)){
                LOGGER.info("[BURN]: {}", pos);
                ((World) this).setBlockState(pos, ((FireBlockInvoker) fireBlock).silYoni$getStateForPosition((World) this, pos), FireBlock.NOTIFY_ALL);
                return true;
            }
            return false;
        }
    }

    private boolean isOwned(LivingEntity entity) {
        return this.entity.equals(entity);
    }

    static void guxiRemovedFire(ServerWorld world){
        var data = (DataGuxiFireTracking)world;
        int allowedCounts = 300;

        var it1 = data.sil_yoni$guxiFireTracking().keySet().iterator();
        for (; allowedCounts > 0 && it1.hasNext(); allowedCounts--) {
            var pos = it1.next();
            if (!world.getBlockState(pos).isOf(Blocks.FIRE)) {
                it1.remove();
            }
        }

        var it2 = data.sil_yoni$leavingFireTracking().iterator();
        for (; allowedCounts > 0 && it2.hasNext(); allowedCounts--) {
            var pos = it2.next();
            if (!world.getBlockState(pos).isOf(Blocks.FIRE)) {
                it2.remove();
            }
        }
    }
}
