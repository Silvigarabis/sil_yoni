package io.github.silvigarabis.sil_yoni.power;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import io.github.silvigarabis.sil_yoni.SilYoniMod;

public class FlightAbilityPower extends Power {

   public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "flight_ability");
   private boolean shouldTick;

   public FlightAbilityPower(PowerType<?> type, LivingEntity entity){
      super(type, entity);
      // because I don't know how to make other mob to fly
      shouldTick = entity instanceof PlayerEntity;
   }

   public static PowerFactory createFactory(){
      return new PowerFactory<>(
         ID,
         new SerializableData(),
         data -> (type, entity) -> new FlightAbilityPower(type, entity)
      ).allowCondition();
   }

   @Override
   public boolean shouldTick(){
      return shouldTick;
   }

   @Override
   public void tick(){
   }
}
