package io.github.silvigarabis.sil_yoni;

import io.github.apace100.apoli.power.factory.PowerFactory;
import io.github.apace100.apoli.power.factory.PowerFactorySupplier;
import io.github.apace100.apoli.registry.ApoliRegistries;
import io.github.silvigarabis.sil_yoni.power.ConvertFoodToResource;
import net.minecraft.registry.Registry;

public class PowerFactories {
    public static void register() {
        register(ConvertFoodToResource::createFactory);
    }
    private static void register(PowerFactory<?> powerFactory) {
        Registry.register(ApoliRegistries.POWER_FACTORY, powerFactory.getSerializerId(), powerFactory);
    }

    private static void register(PowerFactorySupplier<?> factorySupplier) {
        register(factorySupplier.createFactory());
    }
}
