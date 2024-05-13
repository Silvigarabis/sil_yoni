package io.github.silvigarabis.sil_yoni;

import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleRegistries;

import net.minecraft.util.Identifier;

import io.github.silvigarabis.sil_yoni.pehkui_modifier.*;

public class SilYoniPehkuiRegistries {
   public static final Identifier MODIFIER_WEAKENED_BODY = new Identifier(SilYoniMod.MOD_ID, "weakened_body");
   public static final Identifier MODIFIER_SMALLER_BODY = new Identifier(SilYoniMod.MOD_ID, "smaller_body");
   public static final Identifier MODIFIER_TINY_BODY = new Identifier(SilYoniMod.MOD_ID, "tiny_body");

   protected static void registerAll(){
      registerModifiers();
   }
   private static void registerModifiers(){
      ScaleRegistries.register(ScaleRegistries.SCALE_MODIFIERS, MODIFIER_WEAKENED_BODY, WeakenedBodyModifier.INSTANCE);
      ScaleRegistries.register(ScaleRegistries.SCALE_MODIFIERS, MODIFIER_SMALLER_BODY, SmallerBodyModifier.INSTANCE);
      ScaleRegistries.register(ScaleRegistries.SCALE_MODIFIERS, MODIFIER_TINY_BODY, TinyBodyModifier.INSTANCE);
   }
}
