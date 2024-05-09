package io.github.silvigarabis.sil_yoni.power;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import io.github.silvigarabis.sil_yoni.SilYoniMod;

public class BeeRiderPower extends Power {

   public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "bee_rider");

   public BeeRiderPower(PowerType<?> type, LivingEntity entity){
      super(type, entity);
   }

   public static PowerFactory createFactory(){
      return new PowerFactory<>(
         ID,
         new SerializableData(),
         data -> (type, entity) -> new BeeRiderPower(type, entity)
      ).allowCondition();
   }
}
