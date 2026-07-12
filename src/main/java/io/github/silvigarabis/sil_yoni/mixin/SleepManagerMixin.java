package io.github.silvigarabis.sil_yoni.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import io.github.silvigarabis.sil_yoni.power.misc.NoSleepPower;
import io.github.silvigarabis.sil_yoni.util.ApoliPowerHelper;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.SleepManager;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Collections;
import java.util.List;

@Mixin(SleepManager.class)
public class SleepManagerMixin {

    @Unique
    List<ServerPlayerEntity> serverPlayers = null;

    @Inject(
            method="canResetTime(ILjava/util/List;)Z",
            at = @At("HEAD")
    )
    void sil_yoni$captureCanResetTimeArguments(int percentage, List<ServerPlayerEntity> players, CallbackInfoReturnable<Boolean> cir) {
        serverPlayers = players;
    }

    @ModifyExpressionValue(
        method = "getNightSkippingRequirement",
        at = @At(
                value = "FIELD",
                target = "Lnet/minecraft/server/world/SleepManager;total:I",
                opcode = Opcodes.GETFIELD)
    )
    int sil_yoni$modifyTotal(int total){
        List<ServerPlayerEntity> noSleepPlayers;
        if (serverPlayers == null) {
            noSleepPlayers = Collections.emptyList();
        } else {
            noSleepPlayers = serverPlayers.stream().filter(player -> ApoliPowerHelper.hasPower(player, NoSleepPower.ID)).toList();
        }
        int newTotal = total - noSleepPlayers.size();
        return newTotal;
    }
}