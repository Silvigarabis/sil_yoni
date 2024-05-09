package io.github.silvigarabis.sil_yoni.feature;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;

import io.github.silvigarabis.sil_yoni.power.BeeRiderPower;
import io.github.silvigarabis.sil_yoni.util.ApoliPowerHelper;

public class BeeRidingFeature {
   private static final Logger LOGGER = LoggerFactory.getLogger("SIL YONI");

   public static boolean enforced = false;

   public boolean enforced(){
      return enforced;
   }

   public static void setEnforced(boolean state){
      enforced = state;
   }

   public static boolean getEnforced(){
      return enforced;
   }

   public static boolean canRideBee(Entity entity, PlayerEntity player){
      if (enforced){
         return true;
      } else if (ApoliPowerHelper.hasPower(player, BeeRiderPower.ID)){
         return true;
      } else {
         return false;
      }
   }
}
