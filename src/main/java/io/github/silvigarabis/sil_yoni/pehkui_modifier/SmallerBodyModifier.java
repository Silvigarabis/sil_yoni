package io.github.silvigarabis.sil_yoni.pehkui_modifier;

import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleData;

import java.util.Map;
import java.util.HashMap;

public class SmallerBodyModifier extends ScaleModifier {
   private SmallerBodyModifier(){
      super(2695f);
   }

   public static final ScaleType[] AffectedValues = new ScaleType[]{
      ScaleTypes.HEIGHT,
      ScaleTypes.WIDTH,
      ScaleTypes.ENTITY_REACH,
      ScaleTypes.BLOCK_REACH,
      ScaleTypes.MOTION,
      ScaleTypes.VISIBILITY,
      ScaleTypes.KNOCKBACK
   };

   private static Map<ScaleType, Float> AffectedValueMultipliers = new HashMap<>();
   static {
      AffectedValueMultipliers.put(ScaleTypes.BASE, 0.66f);
      AffectedValueMultipliers.put(ScaleTypes.HEIGHT, 0.66f);
      AffectedValueMultipliers.put(ScaleTypes.WIDTH, 0.66f);
      AffectedValueMultipliers.put(ScaleTypes.BLOCK_REACH, 0.7f);
      AffectedValueMultipliers.put(ScaleTypes.MOTION, 0.66f);
      AffectedValueMultipliers.put(ScaleTypes.VISIBILITY, 0.66f);
      AffectedValueMultipliers.put(ScaleTypes.KNOCKBACK, 1.66f);
   }

   @Override
   public float modifyScale(ScaleData scaleData, float modifiedScale, float Tickdelta){
      if (AffectedValueMultipliers.containsKey(scaleData.getScaleType())){
         return modifiedScale * AffectedValueMultipliers.get(scaleData.getScaleType());
      } else {
         return modifiedScale;
      }
   }

   public static final SmallerBodyModifier INSTANCE = new SmallerBodyModifier();
}
