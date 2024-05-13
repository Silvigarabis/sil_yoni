package io.github.silvigarabis.sil_yoni;

import io.github.apace100.apoli.power.PowerType;
import io.github.apace100.apoli.power.PowerTypeReference;
import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.PowerFactorySupplier;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.apace100.calio.data.SerializableData;

import io.github.silvigarabis.sil_yoni.power.*;

import net.minecraft.util.Identifier;
import net.minecraft.registry.Registry;

@SuppressWarnings("unchecked")
public class SilYoniPowers {
   public static final PowerType<?> NO_BREATH = new PowerTypeReference<>(NoBreathPower.ID);
   public static final PowerType<?> TINY_BODY = new PowerTypeReference<>(TinyBodyPower.ID);
   public static final PowerType<?> SMALLER_BODY = new PowerTypeReference<>(SmallerBodyPower.ID);
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
      register(SmallerBodyPower::createFactory);
      register(TinyBodyPower::createFactory);
      register(WeakenedOnlyBodyPower::createFactory);
   }
}
