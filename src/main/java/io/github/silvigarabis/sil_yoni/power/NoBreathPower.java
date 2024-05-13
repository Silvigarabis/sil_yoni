package io.github.silvigarabis.sil_yoni.power;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;

import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;

import io.github.silvigarabis.sil_yoni.SilYoniMod;

public class NoBreathPower extends Power {

   public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "misc/no_breath");

   public NoBreathPower(PowerType<?> type, LivingEntity entity){
      super(type, entity);
   }

   public static PowerFactory createFactory(){
      return new PowerFactory<>(
         ID,
         new SerializableData(),
         data -> (type, entity) -> new NoBreathPower(type, entity)
      ).allowCondition();
   }

   @Override
   public boolean shouldTick(){
      return this.isActive();
   }

   @Override
   public void tick(){
      this.entity.setAir(200);
   }
}
