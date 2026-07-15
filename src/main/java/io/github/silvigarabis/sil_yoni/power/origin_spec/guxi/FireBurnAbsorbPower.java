package io.github.silvigarabis.sil_yoni.power.origin_spec.guxi;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import io.github.silvigarabis.sil_yoni.data.DataTypes;
import io.github.silvigarabis.sil_yoni.data.Key;
import io.github.silvigarabis.sil_yoni.detection.PowerActive;
import io.github.silvigarabis.sil_yoni.feature.FireBurnAbsorbFeature;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.state.property.Properties;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class FireBurnAbsorbPower extends Power implements PowerActive {

    public static final Identifier ID = SilYoniMod.identifier("origin_spec/guxi/energy/absorb_fire_burn");
    private final List<Key> triggerKeys;
    private final FireBurnAbsorbFeature feat;

    public FireBurnAbsorbPower(PowerType<?> type, LivingEntity entity, List<Key> triggerKeys) {
        super(type, entity);
        this.triggerKeys = triggerKeys;
        setTicking(true);
        this.feat = new FireBurnAbsorbFeature(entity);
    }

    private int activeTicks = 0;

    @Override
    public void onUse() {
        activeTicks = 20;
    }

    @Override
    public void tick() {
        if (activeTicks > 0 && super.isActive()) {
            activeTicks--;
            this.feat.tickActive();
        } else {
            this.feat.tickInactive();
        }
    }

    @Override
    public void onLost() {
        this.feat.onLost();
    }

    @Override
    public void onRemoved() {
        this.feat.onRemoved();
    }

    @Override
    public void onAdded() {
        this.feat.onAdd();
    }

    @Override
    public @NotNull List<Key> getUseKeys() {
        return triggerKeys;
    }

    public static PowerFactory createFactory(){
        return new PowerFactory<>(ID,
                new SerializableData()
                        .add("trigger", DataTypes.BACKWARDS_COMPATIBLE_KEY_LIST, Collections.emptyList()),
                data ->
                        (type, player) -> new FireBurnAbsorbPower(
                                type,
                                player,
                                data.get("trigger")
                        ))
                .allowCondition();
    }

    private static final Map<Block, Integer> HEAT_VALUES = Map.<Block, Integer>ofEntries(

            // ===== 木材 =====
            Map.entry(Blocks.OAK_LOG, 60),
            Map.entry(Blocks.SPRUCE_LOG, 60),
            Map.entry(Blocks.BIRCH_LOG, 60),
            Map.entry(Blocks.JUNGLE_LOG, 60),
            Map.entry(Blocks.ACACIA_LOG, 60),
            Map.entry(Blocks.CHERRY_LOG, 60),
            Map.entry(Blocks.DARK_OAK_LOG, 60),
            Map.entry(Blocks.MANGROVE_LOG, 60),

            Map.entry(Blocks.OAK_WOOD, 60),
            Map.entry(Blocks.SPRUCE_WOOD, 60),
            Map.entry(Blocks.BIRCH_WOOD, 60),
            Map.entry(Blocks.JUNGLE_WOOD, 60),
            Map.entry(Blocks.ACACIA_WOOD, 60),
            Map.entry(Blocks.CHERRY_WOOD, 60),
            Map.entry(Blocks.DARK_OAK_WOOD, 60),
            Map.entry(Blocks.MANGROVE_WOOD, 60),

            Map.entry(Blocks.STRIPPED_OAK_LOG, 60),
            Map.entry(Blocks.STRIPPED_SPRUCE_LOG, 60),
            Map.entry(Blocks.STRIPPED_BIRCH_LOG, 60),
            Map.entry(Blocks.STRIPPED_JUNGLE_LOG, 60),
            Map.entry(Blocks.STRIPPED_ACACIA_LOG, 60),
            Map.entry(Blocks.STRIPPED_CHERRY_LOG, 60),
            Map.entry(Blocks.STRIPPED_DARK_OAK_LOG, 60),
            Map.entry(Blocks.STRIPPED_MANGROVE_LOG, 60),

            Map.entry(Blocks.STRIPPED_OAK_WOOD, 60),
            Map.entry(Blocks.STRIPPED_SPRUCE_WOOD, 60),
            Map.entry(Blocks.STRIPPED_BIRCH_WOOD, 60),
            Map.entry(Blocks.STRIPPED_JUNGLE_WOOD, 60),
            Map.entry(Blocks.STRIPPED_ACACIA_WOOD, 60),
            Map.entry(Blocks.STRIPPED_CHERRY_WOOD, 60),
            Map.entry(Blocks.STRIPPED_DARK_OAK_WOOD, 60),
            Map.entry(Blocks.STRIPPED_MANGROVE_WOOD, 60),

            // ===== 树叶 =====
            Map.entry(Blocks.OAK_LEAVES, 2),
            Map.entry(Blocks.SPRUCE_LEAVES, 2),
            Map.entry(Blocks.BIRCH_LEAVES, 2),
            Map.entry(Blocks.JUNGLE_LEAVES, 2),
            Map.entry(Blocks.ACACIA_LEAVES, 2),
            Map.entry(Blocks.CHERRY_LEAVES, 2),
            Map.entry(Blocks.DARK_OAK_LEAVES, 2),
            Map.entry(Blocks.MANGROVE_LEAVES, 2),
            Map.entry(Blocks.AZALEA_LEAVES, 2),
            Map.entry(Blocks.FLOWERING_AZALEA_LEAVES, 2),

            // ===== 羊毛 =====
            Map.entry(Blocks.WHITE_WOOL, 8),
            Map.entry(Blocks.ORANGE_WOOL, 8),
            Map.entry(Blocks.MAGENTA_WOOL, 8),
            Map.entry(Blocks.LIGHT_BLUE_WOOL, 8),
            Map.entry(Blocks.YELLOW_WOOL, 8),
            Map.entry(Blocks.LIME_WOOL, 8),
            Map.entry(Blocks.PINK_WOOL, 8),
            Map.entry(Blocks.GRAY_WOOL, 8),
            Map.entry(Blocks.LIGHT_GRAY_WOOL, 8),
            Map.entry(Blocks.CYAN_WOOL, 8),
            Map.entry(Blocks.PURPLE_WOOL, 8),
            Map.entry(Blocks.BLUE_WOOL, 8),
            Map.entry(Blocks.BROWN_WOOL, 8),
            Map.entry(Blocks.GREEN_WOOL, 8),
            Map.entry(Blocks.RED_WOOL, 8),
            Map.entry(Blocks.BLACK_WOOL, 8),

            // ===== 常见植物 =====
            Map.entry(Blocks.GRASS, 1),
            Map.entry(Blocks.FERN, 1),
            Map.entry(Blocks.TALL_GRASS, 1),
            Map.entry(Blocks.LARGE_FERN, 1),
            Map.entry(Blocks.VINE, 1),
            Map.entry(Blocks.SWEET_BERRY_BUSH, 1),

            // ===== 特殊植物 =====
            Map.entry(Blocks.BAMBOO, 25),
            Map.entry(Blocks.BAMBOO_BLOCK, 60),
            Map.entry(Blocks.STRIPPED_BAMBOO_BLOCK, 60),
            Map.entry(Blocks.HAY_BLOCK, 80),
            Map.entry(Blocks.DRIED_KELP_BLOCK, 80),

            // ===== 根 =====
            Map.entry(Blocks.MANGROVE_ROOTS, 50),
            Map.entry(Blocks.HANGING_ROOTS, 15),

            // ===== 资源方块 =====
            Map.entry(Blocks.COAL_BLOCK, 500),

            // ===== 特殊 =====
            Map.entry(Blocks.BOOKSHELF, 30),
            Map.entry(Blocks.BEE_NEST, 30),
            Map.entry(Blocks.BEEHIVE, 30),
            Map.entry(Blocks.TNT, 100)
    );

    public static int getSpreadEnergy(BlockState state) {
        boolean isWaterLogger = state.contains(Properties.WATERLOGGED) && state.get(Properties.WATERLOGGED);

        Integer fuelHeatValue = HEAT_VALUES.get(state.getBlock());
        if (fuelHeatValue == null) {
            fuelHeatValue = 0;
        }

        if (isWaterLogger) {
            fuelHeatValue -= 100;
        }

        return Math.max(0, fuelHeatValue);
    }
}
