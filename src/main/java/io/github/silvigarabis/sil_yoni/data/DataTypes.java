package io.github.silvigarabis.sil_yoni.data;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import io.github.silvigarabis.sil_yoni.keybinding.TriggerPattern;
import io.github.silvigarabis.sil_yoni.util.ReflectionUtil;

import java.util.List;
import java.util.Set;

public class DataTypes {
    public static final SerializableDataType<TriggerPattern> TRIGGER_PATTERN_DATA_TYPE =
            SerializableDataType.enumValue(TriggerPattern.class);
    public static final SerializableDataType<List<TriggerPattern>> BACKWARDS_COMPATIBLE_TRIGGER_PATTERN_DATA_TYPE_LIST =
            singleOrList(TRIGGER_PATTERN_DATA_TYPE);

    public static final SerializableDataType<Key> KEY_DATA_TYPE = SerializableDataType.compound(
            Key.class,

            new SerializableData()
                    .add("key", SerializableDataTypes.STRING)
                    .add("trigger", BACKWARDS_COMPATIBLE_TRIGGER_PATTERN_DATA_TYPE_LIST),

            (data) -> new Key(
                    data.getString("key"),
                    Set.copyOf(data.get("trigger"))
            ),

            (serializableData, key) -> {
                SerializableData.Instance data = serializableData.new Instance();
                data.set("key", key.key());
                data.set("trigger", List.copyOf(key.triggers()));
                return data;
            }
    );

    public static final SerializableDataType<PehkuiScaleValueInfo> PEHKUI_BASE_VALUE_INFO_DATA_TYPE = SerializableDataType.compound(
            PehkuiScaleValueInfo.class,

            new SerializableData()
                    .add("scale-type", SerializableDataTypes.IDENTIFIER)
                    .add("value", SerializableDataTypes.FLOAT),

            (data) -> {
                if (!ReflectionUtil.hasPehkui()) return PehkuiScaleValueInfo.NO_PEHKUI;

                return new PehkuiScaleValueInfo(
                        data.getId("scale-type"),
                        data.getFloat("value")
                );
            },

            (serializableData, info) -> {
                SerializableData.Instance data = serializableData.new Instance();
                if (ReflectionUtil.hasPehkui()) {
                    data.set("scale-type", info.typeIdentifier());
                    data.set("value", info.baseValue());
                } else {
                    data.set("scale-type", SilYoniMod.identifier("placeholder-no-pehkui"));
                    data.set("value", 0f);
                }
                return data;
            }
    );

    public static final SerializableDataType<List<Key>> BACKWARDS_COMPATIBLE_KEY_LIST =
            singleOrList(KEY_DATA_TYPE);

    public static final SerializableDataType<List<PehkuiScaleValueInfo>> BACKWARDS_COMPATIBLE_PEHKUI_BASE_VALUE_INFO_LIST =
            singleOrList(PEHKUI_BASE_VALUE_INFO_DATA_TYPE);

    @SuppressWarnings("unchecked")
    public static <T> SerializableDataType<List<T>> singleOrList(SerializableDataType<T> dataType) {
        var listType = SerializableDataType.list(dataType);
        return new SerializableDataType<>(
                (Class<List<T>>)(Object)List.class,
                listType::send,
                listType::receive,
                jsonElement -> {
                    if (jsonElement == null || jsonElement.isJsonNull()) {
                        return List.of();
                    } else if (jsonElement.isJsonArray()) {
                        return listType.read(jsonElement);
                    } else {
                        return List.of(dataType.read(jsonElement));
                    }
                }
        );
    }
}
