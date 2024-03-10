package io.github.silvigarabis.sil_yoni.mixin;

import net.minecraft.entity.passive.BeeEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BeeEntity.class)
public class BeeRideableMixin {
   private static final Logger LOGGER = LoggerFactory.getLogger("SIL YONI");

   @Inject(at = @At("HEAD"), method = "interactMob")
   private void onInteractMobBee(CallbackInfo info){
      LOGGER.info("some one interact on a bee");
   }
}
