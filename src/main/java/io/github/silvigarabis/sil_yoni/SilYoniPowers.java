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

public class SilYoniPowers {
   private static void register(PowerFactory<?> powerFactory) {
      Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
   }
   private static void register(PowerFactorySupplier<?> factorySupplier) {
      register(factorySupplier.createFactory());
   }
   public static void register(){
      register(BeeRiderPower::createFactory);
   }
}
