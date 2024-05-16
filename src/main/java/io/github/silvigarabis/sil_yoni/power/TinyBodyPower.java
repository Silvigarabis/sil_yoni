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
import io.github.silvigarabis.sil_yoni.pehkui_modifier.TinyBodyModifier;

/*
scale multiply pehkui:height 0.25
scale multiply pehkui:width 0.25
scale multiply pehkui:block_reach 0.5
scale multiply pehkui:entity_reach 0.65
 */
public class TinyBodyPower extends Power {

   public static final Identifier ID = new Identifier(SilYoniMod.MOD_ID, "physique/tiny_body");
   private boolean shouldTick;

   public TinyBodyPower(PowerType<?> type, LivingEntity entity){
      super(type, entity);
   }

   public static PowerFactory createFactory(){
      return new PowerFactory<>(
         ID,
         new SerializableData(),
         data -> (type, entity) -> new TinyBodyPower(type, entity)
      ).allowCondition();
   }

   @Override
   public void onAdded(){
      for (var type : TinyBodyModifier.AffectedValues){
         type.getScaleData(this.entity)
            .getBaseValueModifiers()
            .add(TinyBodyModifier.INSTANCE);
      }
   }

   @Override
   public void onRespawn(){
      for (var type : TinyBodyModifier.AffectedValues){
         type.getScaleData(this.entity)
            .getBaseValueModifiers()
            .add(TinyBodyModifier.INSTANCE);
      }
   }

   @Override
   public void onRemoved(){
      for (var type : TinyBodyModifier.AffectedValues){
         type.getScaleData(this.entity)
            .getBaseValueModifiers()
            .remove(TinyBodyModifier.INSTANCE);
      }
   }
}