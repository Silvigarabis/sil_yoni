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

/*
为蜜蜂添加一个骑乘动作（但是通过修改其他东西）
 */
@Mixin(BeeEntity.class)
public abstract class BeeRideableMixin extends AnimalEntity {
   @Unique
   private static final Logger LOGGER = LoggerFactory.getLogger("SIL YONI");

   @Override
   public ActionResult interactMob(PlayerEntity player, Hand hand){
      var itemStack = player.getStackInHand(hand);
      if (!itemStack.isEmpty()){
         return super.interactMob(player, hand);
      }
      if (this.getFirstPassenger() != null){
         return super.interactMob(player, hand);
      }

      BeeEntity entity = (BeeEntity)(Object)this;

      LOGGER.info("some one just interacting a bee");
      player.sendMessage(Text.literal("riding bee!"), false);

      var isServerSide = !entity.getWorld().isClient();
      if (isServerSide){
         player.setYaw(entity.getYaw());
         player.setPitch(entity.getPitch());
         player.startRiding(entity);
      }

      return ActionResult.success(isServerSide);
   }

   @Override
   public LivingEntity getControllingPassenger(){
      var passenger = this.getFirstPassenger();
      if (passenger instanceof LivingEntity){
         return (LivingEntity)passenger;
      }
      return super.getControllingPassenger();
   }

   public BeeRideableMixin(EntityType<? extends AnimalEntity> e, World w){
      super(e, w);
      throw new RuntimeException("no construct for mixin class");
   }
}