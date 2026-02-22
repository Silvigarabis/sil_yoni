package io.github.silvigarabis.sil_yoni.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.silvigarabis.sil_yoni.power.misc.ConvertFoodToResourcePower;
import io.github.silvigarabis.sil_yoni.power.misc.StaticHungerPower;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.packet.s2c.play.HealthUpdateS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HungerManager.class)
public class HungerManagerMixin {
    @Unique
    private PlayerEntity player;
    @Unique
    private List<ConvertFoodToResourcePower> convertFoodToResourcePowers;
    @Unique
    private boolean acceptSaturation = true;
    @Unique
    private boolean acceptFood = true;
    @Unique
    private boolean acceptExhaustion = true;
    @Unique
    private @Nullable StaticHungerPower staticsHungerPower;
    @Unique
    private boolean customModifyApplied = false;

    @Unique
    private void updateData() {
        if (player == null) return;
        staticsHungerPower = PowerHolderComponent.getPowers(player, StaticHungerPower.class).stream().findFirst().orElse(null);
        convertFoodToResourcePowers = PowerHolderComponent.getPowers(player, ConvertFoodToResourcePower.class);
        acceptSaturation= convertFoodToResourcePowers.stream().anyMatch(ConvertFoodToResourcePower::shouldAcceptSaturation);
        acceptFood = convertFoodToResourcePowers.stream().anyMatch(ConvertFoodToResourcePower::shouldAcceptFood);
        acceptExhaustion = convertFoodToResourcePowers.stream().anyMatch(ConvertFoodToResourcePower::shouldAcceptExhaustion);
        customModifyApplied = false;
    }

    @ModifyVariable(method = "add(IF)V", at = @At(value = "HEAD"), argsOnly = true)
    public int onAddFood(int food){
        if (player != null) {
            updateData();

            int finalFood = food;
            convertFoodToResourcePowers.forEach(p -> p.addFoodToResource(finalFood));

            if (!acceptFood) {
                food = 0;
                customModifyApplied = true;
            } else if (staticsHungerPower != null){
                food = 0;
            }
        }
        return food;
    }

    @ModifyVariable(method = "add(IF)V", at = @At(value = "HEAD"), argsOnly = true)
    public float onAddSaturation(float saturation) {
        if (player != null) {
            float finalSaturation = saturation;
            convertFoodToResourcePowers.forEach(p -> p.addSaturationToResource(finalSaturation));

            if (!acceptSaturation) {
                saturation = 0f;
                customModifyApplied = true;
            } else if (staticsHungerPower != null) {
                saturation = 0f;
            }
        }
        return saturation;
    }

    @Inject(method = "add(IF)V", at = @At("RETURN"))
    public void afterAdd(int food, float saturationModifier, CallbackInfo ci){
        if (staticsHungerPower != null) {
            staticsHungerPower.update(player);
            customModifyApplied = true;
        }
        sendUpdate((HungerManager)(Object) this, player);
    }

    @Unique
    private void sendUpdate(HungerManager instance, PlayerEntity player) {
        if (customModifyApplied){
            customModifyApplied = false;
            ((ServerPlayerEntity) player).networkHandler.sendPacket(
                    new HealthUpdateS2CPacket(
                            player.getHealth(),
                            instance.getFoodLevel(),
                            instance.getSaturationLevel()
                    )
            );
        }
    }

    @Inject(method = "update", at = @At("HEAD"))
    private void cachePlayer(PlayerEntity player, CallbackInfo ci) {
        this.player = player;
        updateData();
    }

    @Inject(
            method = "update",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/HungerManager;exhaustion:F",
                    opcode = Opcodes.PUTFIELD,
                    shift = At.Shift.AFTER,
                    ordinal = 0
            )
    )
    private void onExhaustionTriggered(PlayerEntity player, CallbackInfo ci) {
        // 注入点：
        // if (this.exhaustion > 4.0F) {
        //    this.exhaustion -= 4.0F;
        for (var p : convertFoodToResourcePowers){
            p.addExhaustionToResource(1);
        }
    }

    @Redirect(
            method = "update",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/HungerManager;foodLevel:I",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 0
            )
    )
    private void redirectExhaustFood(HungerManager instance, int value, PlayerEntity player) {
        if (acceptExhaustion){
            instance.setFoodLevel(value);
        }
    }

    @Redirect(
            method = "update",
            at = @At(
                    value = "FIELD",
                    target = "Lnet/minecraft/entity/player/HungerManager;saturationLevel:F",
                    opcode = Opcodes.PUTFIELD,
                    ordinal = 0
            )
    )
    private void redirectExhaustSaturation(HungerManager instance, float value, PlayerEntity player) {
        if (acceptExhaustion){
            instance.setSaturationLevel(value);
        }
    }

    @Inject(method = "update", at = @At("RETURN"))
    private void staticsHunger(PlayerEntity player, CallbackInfo ci){
        if (staticsHungerPower != null){
            staticsHungerPower.update(player);
        }
    }
}
