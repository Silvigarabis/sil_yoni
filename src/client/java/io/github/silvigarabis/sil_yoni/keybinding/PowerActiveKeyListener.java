package io.github.silvigarabis.sil_yoni.keybinding;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.silvigarabis.sil_yoni.networking.ModC2SPackets;
import io.github.silvigarabis.sil_yoni.detection.Active;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class PowerActiveKeyListener {
    // 用于测试效果的，由于目前暂时还没设置某种显示机制
//    private static final String _testKey = "key.origins.primary_active";
    private static final String _testKey = null;

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

    private static Set<String> getUseKeys(@NotNull List<Active> activePowers){
        var useKeys = new HashSet<String>();
        for (var power : activePowers){
            for (var k : power.getUseKeys()){
                var keybind = getKeyBinding(k.key());
                if (keybind == null) continue;
                useKeys.add(k.key());
            }
        }
        return useKeys;
    }

    private static Map<String, Set<TriggerPattern>> updateUseKeys(Set<String> useKeys){
        // 为所有活跃的键位监听创建处理器，并移除不再使用的处理器
        useKeys.forEach(key -> keybindProcessorMap.computeIfAbsent(key, key1 -> new InputProcessor()));
        keybindProcessorMap.keySet().retainAll(useKeys);

        var resultMap = new HashMap<String, Set<TriggerPattern>>();

        // 更新所有处理器
        for (var keyProcEntry : keybindProcessorMap.entrySet()) {
            var key = keyProcEntry.getKey();
            var keybind = getKeyBinding(key);

            if (keybind == null) throw new IllegalStateException("no key binding for " + key);

            var inputProcessor = keyProcEntry.getValue();
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

        if (_testKey != null) {
            usedKeys.add(_testKey);
        }

        var updatedKeyInfoMap = updateUseKeys(usedKeys);

        var powersToTrigger = new ArrayList<Active>();

        for (var power : activePowers) {
            if (power.matchAnyUseKey(updatedKeyInfoMap)) {
                powersToTrigger.add(power);
            }
        }

        if (_testKey != null) {
            var _testKey_result = updatedKeyInfoMap.get(_testKey);
            if (_testKey_result != null && !_testKey_result.isEmpty())
                client.player.sendMessage(Text.of("%s: %s".formatted(_testKey, _testKey_result)));
        }

        if (!powersToTrigger.isEmpty()) {
            performActivePowers(powersToTrigger);
        }
    }

    private static void performActivePowers(@NotNull List<Active> powers) {
        PacketByteBuf buffer = new PacketByteBuf(Unpooled.buffer());
        buffer.writeInt(powers.size());
        for (var active : powers) {
            buffer.writeIdentifier(active.getActiveIdentifier());
        }
        powers.forEach(Active::onUse);
        ClientPlayNetworking.send(ModC2SPackets.MULTIPLE_ACTIVE_SELF_POWER, buffer);
    }
}
