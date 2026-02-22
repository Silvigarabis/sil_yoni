package io.github.silvigarabis.sil_yoni.mixin.client;

import com.mojang.authlib.GameProfile;
import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.silvigarabis.sil_yoni.power.misc.ReplaceSprintAbility;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin extends AbstractClientPlayerEntity {
    public ClientPlayerEntityMixin(ClientWorld world, GameProfile profile) {
        super(world, profile);
    }

    @Inject(method = "canSprint", at = @At(value = "RETURN"), cancellable = true)
    private void replaceSprintAbility(CallbackInfoReturnable<Boolean> cir) {
        boolean allowed = cir.getReturnValue();

        var abilityPowers = PowerHolderComponent.KEY.get(this).getPowers(ReplaceSprintAbility.class, true);
        if (!abilityPowers.isEmpty()){
            allowed = abilityPowers.stream().allMatch(Power::isActive);
        }

        cir.setReturnValue(allowed);
    }
}
