package io.github.silvigarabis.sil_yoni.feature;

import io.github.silvigarabis.sil_yoni.mixin.FireBlockInvoker;
import io.github.silvigarabis.sil_yoni.power.origin_spec.guxi.FireBurnAbsorbPower;
import io.github.silvigarabis.sil_yoni.util.TwoDimRotateUpDownScanner;
import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FireBlock;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.Set;

public class FireBurnAbsorbFeature {

    static final Logger LOGGER = LogManager.getLogger("silYoniGuxiFire");

    private final LivingEntity entity;
    private int activeTicks = 0;
    private int activeTicksTimer = 0;


    public FireBurnAbsorbFeature(LivingEntity entity) {
        this.entity = entity;
    }

    public static boolean callGuxiInactiveFireRemoved(BlockState state, ServerWorld world, BlockPos pos) {
        return ((DataGuxiFireTracking)world).silYoni$callGuxiInactiveFireRemoved(pos);
    }

    public static void trySpreadGuxiFire(FireBlock fireBlock, World world, BlockPos sourcePos, BlockPos targetPos, Random random) {
        ((DataGuxiFireTracking)world).silYoni$trySpreadGuxiFire(fireBlock, sourcePos, targetPos, random);
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
        activeTicksTimer++;

        if (activeTicks > 20) activeTicks = 20;

        var world = entity.getWorld();
        var center = entity.getBlockPos();

        if (activeTicksTimer % 20 == 1) {
            BlockPos.Mutable mutable = new BlockPos.Mutable();
            for (int x = -4; x <= 4; x++) {
                for (int y = -4; y <= 4; y++) {
                    for (int z = -4; z <= 4; z++) {
                        mutable.set(center, x, y, z);

                        BlockState state = entity.getWorld().getBlockState(mutable);
                        if (state.isOf(Blocks.FIRE)) {
                            if (tryBecameNewGuxiFireOwner(world, mutable, this)) {
                                LOGGER.trace("[ACTIVE]: {}", mutable);
                            }
                        }
                    }
                }
            }
        }

        if (activeTicksTimer % 5 == 1) {
            for (var pos : TwoDimRotateUpDownScanner.with(center, 8)) {
                BlockState state = world.getBlockState(pos);
                if (!state.isOf(Blocks.FIRE)) {
                    if (tryLintGuxiFire((FireBlock) Blocks.FIRE, world, pos, this)) {
                        break;
                    }
                }
            }
        }
    }

    public void tickInactive() {
        if (activeTicks > 0) activeTicks--;
        activeTicksTimer = 0;
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
        default void silYoni$trySpreadGuxiFire(FireBlock fireBlock, BlockPos sourcePos, BlockPos targetPos, Random random) {
            var sourceOwner = silYoni$getGuxiFireOwner(sourcePos);
            var targetOwner = silYoni$getGuxiFireOwner(targetPos);
            assert sourceOwner != null;
            if (targetOwner != null) return;

            // TODO: 尝试限制传播范围 guxi fire

            // 我们会使用传播几率作为要添加到GUXI上的能量
            BlockState state = ((World)this).getBlockState(targetPos);
            int spreadChance = ((FireBlockInvoker)fireBlock).silYoni$getSpreadChance(state);
            if (sourceOwner.startFireSpread(spreadChance, sourcePos, targetPos, state, random)){
                silYoni$tryBecameNewGuxiFireOwner(targetPos, sourceOwner);
                ((World) this).setBlockState(targetPos, ((FireBlockInvoker) fireBlock).silYoni$getStateForPosition((World) this, targetPos), FireBlock.NOTIFY_ALL);
                LOGGER.trace("[SPREAD]: {}", targetPos);
            }
        }

        default boolean silYoni$callGuxiInactiveFireRemoved(BlockPos pos){
            var owner = silYoni$getGuxiFireOwner(pos);
            if (owner != null && !owner.isActive()){
                LOGGER.trace("[INACTIVE]: {}", pos);
                return true;
            }
            return false;
        }

        default boolean silYoni$callGuxiLeavingFireRemoved(BlockPos pos){
            var removed = sil_yoni$leavingFireTracking().contains(pos);
            if (removed){
                LOGGER.trace("[LEAVING]: {}", pos);
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
            int burnChance = ((FireBlockInvoker)fireBlock).silYoni$getBurnChance((World)this, pos);
            if (burnChance > 0 && silYoni$tryBecameNewGuxiFireOwner(pos, owner)){
                LOGGER.trace("[BURN]: {}", pos);
                ((World) this).setBlockState(pos, ((FireBlockInvoker) fireBlock).silYoni$getStateForPosition((World) this, pos), FireBlock.NOTIFY_ALL);
                return true;
            }
            return false;
        }
    }

    private boolean startFireSpread(int spreadChance, BlockPos sourcePos, BlockPos targetPos, BlockState state, Random random) {
        if (this.entity instanceof PlayerEntity player){
            int fuelValue = FireBurnAbsorbPower.getSpreadEnergy(state);
            if (spreadChance > 0 && fuelValue > 0 && random.nextInt(10 + fuelValue) < 10){
                player.getHungerManager().add(fuelValue, 0);
                return true;
            }
        } else {
            return spreadChance > 0;
        }
        return false;
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
