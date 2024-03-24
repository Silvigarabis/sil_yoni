package io.github.silvigarabis.sil_yoni.mixin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.world.World;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.ActionResult;

@Mixin(BeeEntity.class)
public abstract class BeeRideableMixin extends AnimalEntity {
   @Unique
   private static final Logger LOGGER = LoggerFactory.getLogger("SIL YONI");

   /*//@Inject(at = @At("HEAD"), method = "Lnet/minecraft/entity/passive/AnimalEntity;interactMob")
   @Inject(at = @At("HEAD"), method = "interactMob")
   private void onInteractMobBee(PlayerEntity player, Hand hand, CallbackInfo info){
      LOGGER.info("some one just interacting a bee");
      player.sendMessage(Text.literal("some one just interacting a bee"), false);
   }*/

   /*
    hope there's no conflict with other mods
    */
   @Override
   public ActionResult interactMob(PlayerEntity player, Hand hand) {
      LOGGER.info("some one interact on a bee");
      player.sendMessage(Text.literal("you just interact on a bee!"), false);
      return super.interactMob(player, hand);
   }

   public BeeRideableMixin(EntityType<? extends AnimalEntity> et, World w){
      super(et, w);
      throw new RuntimeException("mixin class cannot be construct");
   }
}
