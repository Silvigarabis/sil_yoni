package io.github.silvigarabis.sil_yoni.feature;

import io.github.silvigarabis.sil_yoni.SilYoniPowers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;

public class BeeRidingFeature {
   public static boolean canRideBee(Entity entity, PlayerEntity player){
      return SilYoniPowers.BEE_RIDER.isActive(player);
   }
}
