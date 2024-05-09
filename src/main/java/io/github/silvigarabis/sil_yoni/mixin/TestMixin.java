package io.github.silvigarabis.sil_yoni.mixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.objectweb.asm.Opcodes;

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

@Mixin(Entity.class)
public abstract class TestMixin {
   @Unique
   private static final Logger LOGGER = LoggerFactory.getLogger("SIL YONI");
   @Inject(
      method = "setVelocity",
      at = @At("HEAD")
   )
   public void printStackTraceWhenChangeVelocity(CallbackInfo info){
      if (((Entity)(Object)this).getFirstPassenger() != null && !((Entity)(Object)this).getWorld().isClient()){
         // new Exception("changed velocity").printStackTrace();
      }
   }
   @Inject(
      method = "updateVelocity",
      at = @At("HEAD")
   )
   public void printSomethingWhenUpdateVelocity(float speed, Vec3d movementInput, CallbackInfo info){
      if (((Entity)(Object)this).hasPassengers()){
          LOGGER.info("updateVelocity on {} speed {} input {}", ((Entity)(Object)this).getWorld().isClient() ? "client" : "server", speed, movementInput.toString());
      }
   }
   @Inject(
      method = "updateVelocity",
      at = @At("RETURN")
   )
   public void printVelocityAfterUpdateVelocity(float speed, Vec3d movementInput, CallbackInfo info){
      if (((Entity)(Object)this).hasPassengers()){
          LOGGER.info("updateVelocity on {} as {}", ((Entity)(Object)this).getWorld().isClient() ? "client" : "server", ((Entity)(Object)this).getVelocity().toString());
      }
   }
}