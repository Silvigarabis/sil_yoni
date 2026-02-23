package io.github.silvigarabis.sil_yoni.data;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.calio.data.SerializableDataType;
import io.github.apace100.calio.data.SerializableDataTypes;
import io.github.silvigarabis.sil_yoni.keybinding.TriggerPattern;

public class DataTypes {
    public static final SerializableDataType<TriggerPattern> TRIGGER_PATTERN_DATA_TYPE =
            new SerializableDataType<>(
                    TriggerPattern.class,
                    (buf, value) -> buf.writeString(value.name()),
                    buf -> TriggerPattern.valueOf(buf.readString(32767)),
                    json -> TriggerPattern.valueOf(json.getAsString())
            );
    public static final SerializableDataType<Key> KEY_DATA_TYPE = SerializableDataType.compound(
            Key.class,

            new SerializableData()
                    .add("key", SerializableDataTypes.STRING)
                    .add("trigger", TRIGGER_PATTERN_DATA_TYPE),

            (data) -> new Key(
                    data.getString("key"),
                    data.get("trigger")
            ),

            (serializableData, key) -> {
                SerializableData.Instance data = serializableData.new Instance();
                data.set("key", key.key());
                data.set("trigger", key.trigger());
                return data;
            }
    );
}
