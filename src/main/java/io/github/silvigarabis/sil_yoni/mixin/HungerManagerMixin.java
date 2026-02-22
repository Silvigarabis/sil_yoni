package io.github.silvigarabis.sil_yoni.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.silvigarabis.sil_yoni.power.ConvertFoodToResourcePower;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
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
public abstract class HungerManagerMixin {
    @Unique
    private PlayerEntity player;
    @Unique
    private List<ConvertFoodToResourcePower> mfps;
    @Unique
    private boolean acceptSaturation = true;
    @Unique
    private boolean acceptFood = true;
    @Unique
    private boolean acceptExhaustion = true;

    @Inject(method = "update", at = @At("HEAD"))
    private void cachePlayer(PlayerEntity player, CallbackInfo ci) {
        this.player = player;
    }

    @Unique
    private void updateData() {
        if (player == null) return;
        mfps = PowerHolderComponent.getPowers(player, ConvertFoodToResourcePower.class);
        acceptSaturation= mfps.stream().anyMatch(ConvertFoodToResourcePower::shouldAcceptSaturation);
        acceptFood = mfps.stream().anyMatch(ConvertFoodToResourcePower::shouldAcceptFood);
        acceptExhaustion = mfps.stream().anyMatch(ConvertFoodToResourcePower::shouldAcceptExhaustion);
    }

    @ModifyVariable(method = "add(IF)V", at = @At(value = "HEAD"), argsOnly = true)
    public int onAddFood(int food){
        if (player != null) {
            updateData();

            int finalFood = food;
            mfps.forEach(p -> p.addFoodToResource(finalFood));

            if (!acceptFood) food = 0;
        }
        return food;
    }

    @ModifyVariable(method = "add(IF)V", at = @At(value = "HEAD"), argsOnly = true)
    public float onAddSaturation(float saturation) {
        if (player != null) {
            updateData();

            float finalSaturation = saturation;
            mfps.forEach(p -> p.addSaturationToResource(finalSaturation));

            if (!acceptSaturation) saturation = 0f;
        }
        return saturation;
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
        updateData();

        // 注入点：
        // if (this.exhaustion > 4.0F) {
        //    this.exhaustion -= 4.0F;
        mfps.forEach(p -> p.addExhaustionToResource(-1));
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
        updateData();

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
        updateData();

        if (acceptExhaustion){
            instance.setSaturationLevel(value);
        }
    }
}
