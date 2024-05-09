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

import io.github.silvigarabis.sil_yoni.feature.BeeRidingFeature;

/*
 * 为蜜蜂添加一个骑乘动作
 */
 
@Mixin(BeeEntity.class)
public abstract class BeeRideableMixin extends AnimalEntity {
   @Unique
   private static final Logger LOGGER = LoggerFactory.getLogger("SIL YONI");

   @Override
   public ActionResult interactMob(PlayerEntity player, Hand hand){
      if (!this.isBreedingItem(player.getStackInHand(hand)) && !this.hasPassengers() && !player.shouldCancelInteraction()) {
         if (!this.getWorld().isClient && BeeRidingFeature.canRideBee(this, player)){
            player.setYaw(this.getYaw());
            player.setPitch(this.getPitch());
            player.startRiding(this);

            player.sendMessage(Text.literal("riding bee!"), false);
         }
         return ActionResult.success(this.getWorld().isClient);
      }
      return super.interactMob(player, hand);
   }

   @Override
   public LivingEntity getControllingPassenger(){
      var passenger = this.getFirstPassenger();
      if (passenger instanceof LivingEntity){
         return (LivingEntity)passenger;
      }
      return super.getControllingPassenger();
   }

   @Override
   protected void tickControlled(PlayerEntity controllingPlayer, Vec3d movementInput) {
      this.getWorld().getProfiler().push("BeeEntity.tickControlled()");
      this.setRotation(controllingPlayer.getYaw(), controllingPlayer.getPitch() * 0.5f);
      this.bodyYaw = this.headYaw = this.getYaw();
      this.prevYaw = this.headYaw;
      LOGGER.info("manually travel for BeeEntity.tickControlled() and mob {}, actual movement speed: {}, fake speed: 0.02", this.toString(), this.getMovementSpeed());
      this.updateVelocity(0.02f, this.getControlledMovementInput(controllingPlayer, movementInput));
      this.getWorld().getProfiler().pop();
   }

   @Override
   protected Vec3d getControlledMovementInput(PlayerEntity controllingPlayer, Vec3d movementInput) {
      LOGGER.info("controllingPlayer.forwardSpeed: {}", controllingPlayer.forwardSpeed);

      double forwardSpeed = Math.signum(controllingPlayer.forwardSpeed);
      double sidewaysSpeed = Math.signum(controllingPlayer.sidewaysSpeed);
      double upwardSpeed = 0.0;
      if (sidewaysSpeed != 0 || forwardSpeed != 0){
         upwardSpeed = -0.75 * Math.sin(Math.PI / 180 * controllingPlayer.getPitch());
      }

      Vec3d input = new Vec3d(sidewaysSpeed, upwardSpeed, forwardSpeed);
      return input.normalize();
   }

   @Inject(
      method = "tickMovement",
      at = @At(
         value = "INVOKE",
         target = "Lnet/minecraft/entity/passive/AnimalEntity;tickMovement()V",
         shift = At.Shift.AFTER
      ),
      cancellable = true
   )
   public void injectTickMovement(CallbackInfo info){
      // if there is an passenger, do not tick other things
      if (this.hasPassengers()){
         info.cancel();
      }
   }

   public BeeRideableMixin(EntityType<? extends AnimalEntity> e, World w){
      super(e, w);
      throw new RuntimeException("no construct for mixin class");
   }
}
