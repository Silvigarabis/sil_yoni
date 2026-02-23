package io.github.silvigarabis.sil_yoni.keybinding;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.silvigarabis.sil_yoni.networking.ModC2SPackets;
import io.github.silvigarabis.sil_yoni.power.detection.Active;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.PacketByteBuf;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PowerActiveKeyListener {
    private static final HashMap<String, KeyBinding> keybindMap = new HashMap<>();
    private static final HashMap<String, InputProcessor> keybindProcessorMap = new HashMap<>();

    private static boolean initialized = false;
    public static void init(){
        ClientTickEvents.START_CLIENT_TICK.register((MinecraftClient mc) -> {
            if (!initialized) {
                initialized = true;
                init0(mc);
            }
            triggerTick(mc);
        });
    }
    private static void init0(MinecraftClient mc) {
        for (var keybind : mc.options.allKeys) {
            keybindMap.put(keybind.getTranslationKey(), keybind);
        }
    }

    static @Nullable KeyBinding getKeyBinding(String key) {
        return keybindMap.get(key);
    }

    private static List<Active> getActivePowers(ClientPlayerEntity player){
        List<Active> activePowers = new ArrayList<>();
        for (var power : PowerHolderComponent.KEY.get(player).getPowers()){
            if (power instanceof Active active){
                activePowers.add(active);
            }
        }
        return activePowers;
    }

    private static Map<String, KeyBinding> getUseKeys(@NotNull List<Active> activePowers){
        var useKeys = new HashMap<String, KeyBinding>();
        for (var power : activePowers){
            var keyConf = power.getUseKey();
            if (keyConf == null) continue;
            var keybind = getKeyBinding(keyConf.key());
            if (keybind == null) continue;
            useKeys.put(keyConf.key(), keybind);
        }
        return useKeys;
    }

    private static Map<String, List<TriggerPattern>> updateUseKeys(Map<String, KeyBinding> useKeys){
        useKeys.keySet().forEach(key -> keybindProcessorMap.computeIfAbsent(key, key1 -> new InputProcessor()));
        keybindProcessorMap.keySet().removeIf(key -> !useKeys.containsKey(key));

        var resultMap = new HashMap<String, List<TriggerPattern>>();

        for (var key : useKeys.keySet()){
            var keybind = useKeys.get(key);
            var inputProcessor = keybindProcessorMap.get(key);
            var result = inputProcessor.update(keybind.isPressed());
            resultMap.put(key, result);
        }

        return resultMap;
    }

    private static void triggerTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        var activePowers = getActivePowers(client.player);
        var usedKeys = getUseKeys(activePowers);
        var updatedKeyInfoMap = updateUseKeys(usedKeys);

        var powersToTrigger = new ArrayList<Active>();

        for (var power : activePowers) {
            var keyConf = power.getUseKey();
            if (keyConf == null) continue;

            // 获取这个能力对应的 TriggerPattern
            var triggerPattern = keyConf.trigger();

            // 检查 updatedKeyInfoMap 中这个 key 是否存在，并且包含对应的 TriggerPattern
            var triggeredPatterns = updatedKeyInfoMap.get(keyConf.key());
            if (triggeredPatterns != null && triggeredPatterns.contains(triggerPattern)) {
                powersToTrigger.add(power);
            }
        }

        if (!powersToTrigger.isEmpty()) {
            performActivePowers(powersToTrigger);
        }
    }

    private static void performActivePowers(@NotNull List<Active> powers) {
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        buffer.writeInt(powers.size());
        for (var active : powers) {
            buffer.writeIdentifier(active.getType().getIdentifier());
        }
        powers.forEach(Active::onUse);
        ClientPlayNetworking.send(ModC2SPackets.MULTIPLE_ACTIVE_SELF_POWER, buffer);
    }
}
