package io.github.silvigarabis.sil_yoni.mixin;

import net.minecraft.server.MinecraftServer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class ExampleMixin {
   private static final Logger LOGGER = LoggerFactory.getLogger("SIL YONI");

   @Inject(at = @At("HEAD"), method = "loadWorld")
   private void worldLoading(CallbackInfo info) {
      LOGGER.info("loading world...");
   }
}
