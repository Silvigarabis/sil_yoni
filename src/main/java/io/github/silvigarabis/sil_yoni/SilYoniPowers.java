package io.github.silvigarabis.sil_yoni;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.PowerFactorySupplier;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.silvigarabis.sil_yoni.power.BeeRiderPower;
import io.github.silvigarabis.sil_yoni.power.FlightAbilityPower;
import io.github.silvigarabis.sil_yoni.power.detection.ActiveCooldownPower;
import io.github.silvigarabis.sil_yoni.power.detection.TogglePower;
import io.github.silvigarabis.sil_yoni.power.misc.*;
import io.github.silvigarabis.sil_yoni.power.modify.ModifyFoodEatTimePower;
import net.minecraft.registry.Registry;

public class SilYoniPowers {
   public static final PowerType<?> NO_BREATH = new PowerTypeReference<>(NoBreathPower.ID);
   public static final PowerType<?> FLIGHT_ABILITY = new PowerTypeReference<>(FlightAbilityPower.ID);
   public static final PowerType<?> BEE_RIDER = new PowerTypeReference<>(BeeRiderPower.ID);

   private static void register(PowerFactory<?> powerFactory) {
      Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
   }
   private static void register(PowerFactorySupplier<?> factorySupplier) {
      register(factorySupplier.createFactory());
   }
   public static void register(){
      register(NoBreathPower::createFactory);
      register(FlightAbilityPower::createFactory);
      register(BeeRiderPower::createFactory);
      register(ConvertFoodToResourcePower::createFactory);
      register(StaticHungerPower::createFactory);
      register(MappingResourceRatioToFoodLevelPower::createFactory);
      register(ReplaceSprintAbility::createFactory);
      register(ActiveCooldownPower::createFactory);
      register(TogglePower::createFactory);
      register(ModifyFoodEatTimePower::createFactory);
      register(PehkuiValuePower::createFactory);
   }
}
