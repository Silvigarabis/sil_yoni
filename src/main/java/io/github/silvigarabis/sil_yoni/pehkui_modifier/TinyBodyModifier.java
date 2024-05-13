package io.github.silvigarabis.sil_yoni.pehkui_modifier;

import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.api.ScaleData;

import java.util.Map;
import java.util.HashMap;

public class TinyBodyModifier extends ScaleModifier {
   private TinyBodyModifier(){
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
      AffectedValueMultipliers.put(ScaleTypes.BASE, 0.25f);
      AffectedValueMultipliers.put(ScaleTypes.HEIGHT, 0.25f);
      AffectedValueMultipliers.put(ScaleTypes.WIDTH, 0.25f);
      AffectedValueMultipliers.put(ScaleTypes.ENTITY_REACH, 0.65f);
      AffectedValueMultipliers.put(ScaleTypes.BLOCK_REACH, 0.5f);
      AffectedValueMultipliers.put(ScaleTypes.MOTION, 0.3f);
      AffectedValueMultipliers.put(ScaleTypes.VISIBILITY, 0.35f);
      AffectedValueMultipliers.put(ScaleTypes.KNOCKBACK, 4.0f);
   }

   @Override
   public float modifyScale(ScaleData scaleData, float modifiedScale, float Tickdelta){
      if (AffectedValueMultipliers.containsKey(scaleData.getScaleType())){
         return modifiedScale * AffectedValueMultipliers.get(scaleData.getScaleType());
      } else {
         return modifiedScale;
      }
   }

   public static final TinyBodyModifier INSTANCE = new TinyBodyModifier();
}
