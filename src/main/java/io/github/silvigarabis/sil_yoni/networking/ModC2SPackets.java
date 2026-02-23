package io.github.silvigarabis.sil_yoni.networking;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import io.github.silvigarabis.sil_yoni.power.detection.Active;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class ModC2SPackets {
    public static final @NotNull Identifier MULTIPLE_ACTIVE_SELF_POWER = new Identifier(SilYoniMod.MOD_ID, "multiple_active_self_power");
    private static void useMultipleActivePowers(MinecraftServer minecraftServer, ServerPlayerEntity playerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        int count = packetByteBuf.readInt();
        Identifier[] powerIds = new Identifier[count];
        for(int i = 0; i < count; i++) {
            powerIds[i] = packetByteBuf.readIdentifier();
        }
        minecraftServer.execute(() -> {
            PowerHolderComponent component = PowerHolderComponent.KEY.get(playerEntity);
            for (Identifier id : powerIds) {
                PowerType<?> type = PowerTypeRegistry.get(id);
                Power power = component.getPower(type);
                if (power instanceof Active active) {
                    active.onUse();
                }
            }
        });
    }

    public static void register() {
        ServerPlayNetworking.registerGlobalReceiver(MULTIPLE_ACTIVE_SELF_POWER, ModC2SPackets::useMultipleActivePowers);
    }
}
