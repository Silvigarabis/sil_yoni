package io.github.silvigarabis.sil_yoni.util;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.Entity;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeRegistry;
import io.github.apace100.apoli.component.PowerHolderComponent;

import net.minecraft.util.Identifier;

public class ApoliPowerHelper {
   public static boolean hasPower(LivingEntity entity, PowerType type){
      return PowerHolderComponent.KEY.get(entity)
          .getPowers().stream()
          .anyMatch(t -> t.getType().equals(type));
   }
   public static boolean hasPower(LivingEntity entity, Identifier type){
      return PowerHolderComponent.KEY.get(entity)
          .getPowers().stream()
          .anyMatch(t -> t.getType().getIdentifier().equals(type));
   }
   public static boolean hasPower(Entity entity, PowerType type){
      return entity instanceof LivingEntity lEntity && hasPower(lEntity, type);
   }
   public static boolean hasPower(Entity entity, Identifier type){
      return entity instanceof LivingEntity lEntity && hasPower(lEntity, type);
   }
}
