package io.github.silvigarabis.sil_yoni.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.silvigarabis.sil_yoni.power.ConvertFoodToResource;
import net.minecraft.entity.player.HungerManager;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(HungerManager.class)
public abstract class HungerManagerMixin {
    @Unique
    private PlayerEntity player;

    @ModifyVariable(method = "add(IF)V", at = @At(value = "HEAD"), argsOnly = true)
    public int onAddFood(int food){
        if (player != null) {
            List<ConvertFoodToResource> mfps = PowerHolderComponent.getPowers(player, ConvertFoodToResource.class);

            boolean acceptFood = mfps.stream().anyMatch(ConvertFoodToResource::shouldAcceptFood);

            int finalFood = food;
            mfps.forEach(p -> p.addFoodToResource(finalFood));

            if (!acceptFood) food = 0;
        }
        return food;
    }

    @ModifyVariable(method = "add(IF)V", at = @At(value = "HEAD"), argsOnly = true)
    public float onAddSaturation(float saturation) {
        if (player != null) {
            List<ConvertFoodToResource> mfps = PowerHolderComponent.getPowers(player, ConvertFoodToResource.class);

            boolean acceptSaturation = mfps.stream().anyMatch(ConvertFoodToResource::shouldAcceptSaturation);

            float finalSaturation = saturation;
            mfps.forEach(p -> p.addSaturationToResource(finalSaturation));

            if (!acceptSaturation) saturation = 0f;
        }
        return saturation;
    }

    @Inject(method = "update", at = @At("HEAD"))
    private void cachePlayer(PlayerEntity player, CallbackInfo ci) {
        this.player = player;
    }
}
