package io.github.silvigarabis.sil_yoni.power.misc;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.data.ApoliDataTypes;
import io.github.apace100.apoli.power.CooldownPower;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.VariableIntPower;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;

public class MappingResourceRatioToFoodLevelPower extends Power {
    public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "misc/map_resource_ratio_to_food_level");

    private final PowerType<?> resourceType;

    public MappingResourceRatioToFoodLevelPower(
            PowerType<?> type,
            LivingEntity entity,
            PowerType<?> resourceType
    ) {
        super(type, entity);
        this.resourceType = resourceType;
        setTicking();
    }

    @Override
    public void tick() {
        if (!(entity instanceof PlayerEntity player)) return;

        int newFoodLevel = getVisualRatio(20);
        if (player.getHungerManager().getFoodLevel() != newFoodLevel) {
            player.getHungerManager().setFoodLevel(newFoodLevel);

            if (player instanceof ServerPlayerEntity serverPlayer) {
                serverPlayer.networkHandler.sendPacket(
                        new HealthUpdateS2CPacket(
                                player.getHealth(),
                                player.getHungerManager().getFoodLevel(),
                                player.getHungerManager().getSaturationLevel()
                        )
                );
            }
        }
    }

    private int getVisualRatio(int blocks) {
        PowerHolderComponent component = PowerHolderComponent.KEY.get(entity);
        Power p = component.getPower(resourceType);
        double ratio;
        if (p instanceof VariableIntPower vip) {
            ratio = (double) (vip.getValue() - vip.getMin()) / (vip.getMax() - vip.getMin());
        } else if (p instanceof CooldownPower cp) {
            ratio = cp.getProgress();
        } else {
            return 0;
        }
        int visual = (int) Math.ceil(ratio * blocks);
        return Math.max(0, Math.min(blocks, visual));
    }

    public static PowerFactory<Power> createFactory() {
        return new PowerFactory<>(ID,
                new SerializableData()
                        .add("resource", ApoliDataTypes.POWER_TYPE),
                data -> (powerType, livingEntity) -> new MappingResourceRatioToFoodLevelPower(
                        powerType,
                        livingEntity,
                        data.get("resource")
                )
        );
    }
}
