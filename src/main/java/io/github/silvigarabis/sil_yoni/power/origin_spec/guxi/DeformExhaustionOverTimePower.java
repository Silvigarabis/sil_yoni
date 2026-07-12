package io.github.silvigarabis.sil_yoni.power.origin_spec.guxi;

import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.calio.data.SerializableData;
import io.github.silvigarabis.sil_yoni.SilYoniMod;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import java.util.LinkedHashMap;

public class DeformExhaustionOverTimePower extends Power {

   public static final Identifier ID = SilYoniMod.identifier("origin_spec/guxi/deform_exhaustion_over_time");

   public DeformExhaustionOverTimePower(
           PowerType<?> type,
           LivingEntity entity
   ) {
      super(type, entity);
      if (entity instanceof PlayerEntity) {
         setTicking();
      }
   }

   public static PowerFactory createFactory() {
      return new PowerFactory<>(
              ID,
              new SerializableData(),
              data -> (type, entity) -> new DeformExhaustionOverTimePower(
                      type,
                      entity
              )
      ).allowCondition();
   }

   private static final LinkedHashMap<Double, Float> EXHAUSTIONS = new LinkedHashMap<>();
   static {
      EXHAUSTIONS.put(57d, 38f);
      EXHAUSTIONS.put(50d, 25f);
      EXHAUSTIONS.put(40d, 13.33f);
      EXHAUSTIONS.put(33d, 8.25f);
      EXHAUSTIONS.put(24d, 4.8f);
      EXHAUSTIONS.put(10d, 1.11f);
      EXHAUSTIONS.put(0d, 1f);
   }

   private static final int INTERVAL = 4;

   @Override
   public void tick() {
      if (!(entity instanceof PlayerEntity player)) {
         return;
      }

      double speed = player.getVelocity().length() * 20;
      float exhaustion = 0;
      for (var entry : EXHAUSTIONS.entrySet()) {
         if (speed > entry.getKey()) {
            exhaustion = entry.getValue();
            break;
         }
      }

      if (exhaustion > 0.0F) {
         player.addExhaustion(exhaustion / 20);
      }
   }
}
