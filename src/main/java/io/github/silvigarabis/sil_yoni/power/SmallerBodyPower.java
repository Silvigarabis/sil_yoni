package io.github.silvigarabis.sil_yoni.power;

import io.github.apace100.calio.data.SerializableData;
import io.github.apace100.apoli.power.Power;
import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.factory.PowerFactory;

import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.api.ScaleData;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

import io.github.silvigarabis.sil_yoni.SilYoniMod;
import io.github.silvigarabis.sil_yoni.pehkui_modifier.SmallerBodyModifier;

/*
scale multiply pehkui:height 0.66
scale multiply pehkui:width 0.66
scale multiply pehkui:block_reach 0.7
 */
public class SmallerBodyPower extends Power {

   public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "physique/smaller_body");
   private boolean shouldTick;

   public SmallerBodyPower(PowerType<?> type, LivingEntity entity){
      super(type, entity);
   }

   public static PowerFactory createFactory(){
      return new PowerFactory<>(
         ID,
         new SerializableData(),
         data -> (type, entity) -> new SmallerBodyPower(type, entity)
      ).allowCondition();
   }

   @Override
   public void onAdded(){
      for (var type : SmallerBodyModifier.AffectedValues){
         type.getScaleData(this.entity)
            .getBaseValueModifiers()
            .add(SmallerBodyModifier.INSTANCE);
      }
   }

   @Override
   public void onRespawn(){
      for (var type : SmallerBodyModifier.AffectedValues){
         type.getScaleData(this.entity)
            .getBaseValueModifiers()
            .add(SmallerBodyModifier.INSTANCE);
      }
   }

   @Override
   public void onRemoved(){
      for (var type : SmallerBodyModifier.AffectedValues){
         type.getScaleData(this.entity)
            .getBaseValueModifiers()
            .remove(SmallerBodyModifier.INSTANCE);
      }
   }
}