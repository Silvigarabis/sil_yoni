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
import io.github.silvigarabis.sil_yoni.pehkui_modifier.WeakenedBodyModifier;

/*
scale multiply pehkui:height 0.66
scale multiply pehkui:width 0.66
scale multiply pehkui:block_reach 0.7
 */
public class WeakenedOnlyBodyPower extends Power {

   public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "physique/weakened_body");
   private boolean shouldTick;

   public WeakenedOnlyBodyPower(PowerType<?> type, LivingEntity entity){
      super(type, entity);
   }

   public static PowerFactory createFactory(){
      return new PowerFactory<>(
         ID,
         new SerializableData(),
         data -> (type, entity) -> new WeakenedOnlyBodyPower(type, entity)
      ).allowCondition();
   }

   @Override
   public void onGained(){
      for (var type : WeakenedBodyModifier.AffectedValues){
         if (type == ScaleTypes.HEALTH){
            continue;
         }
         type.getScaleData(this.entity)
            .getBaseValueModifiers()
            .add(WeakenedBodyModifier.INSTANCE);
      }
   }

   @Override
   public void onLost(){
      for (var type : WeakenedBodyModifier.AffectedValues){
         if (type == ScaleTypes.HEALTH){
            continue;
         }
         type.getScaleData(this.entity)
            .getBaseValueModifiers()
            .remove(WeakenedBodyModifier.INSTANCE);
      }
   }
}