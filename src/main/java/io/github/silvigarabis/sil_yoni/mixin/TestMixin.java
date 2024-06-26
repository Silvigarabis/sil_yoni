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
import net.minecraft.entity.player.PlayerEntity;
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

@Mixin(PlayerEntity.class)
public abstract class TestMixin {
   @Unique
   private static final Logger LOGGER = LoggerFactory.getLogger("SIL YONI");
   @Unique
   private PlayerEntity self;

   @Inject(
      method = "<init>",
      at = @At("RETURN")
   )
   public void setSelf(CallbackInfo info){
      self = (PlayerEntity)(Object)this;
   }

   @Inject(
      method = "tick",
      at = @At("HEAD")
   )
   public void printSome(CallbackInfo info){
      LOGGER.info("some data on {}: forwardSpeed: {}, sidewaySpeed: {}, upwardSpeed: {}", self.getWorld().isClient() ? "client" : "server", self.forwardSpeed, self.sidewaysSpeed, self.upwardSpeed);
   }
}
