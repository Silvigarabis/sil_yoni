package io.github.silvigarabis.sil_yoni.pehkui_modifier;

import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleType;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.api.ScaleData;

import java.util.Map;
import java.util.HashMap;

public class WeakenedBodyModifier extends ScaleModifier {
   private WeakenedBodyModifier(){
      super(2695f);
   }

   public static final ScaleType[] AffectedValues = new ScaleType[]{
      ScaleTypes.ATTACK_SPEED,
      ScaleTypes.MINING_SPEED,
      ScaleTypes.MOTION
   };

   private static Map<ScaleType, Float> AffectedValueMultipliers = new HashMap<>();
   static {
      AffectedValueMultipliers.put(ScaleTypes.ATTACK_SPEED, 0.5f);
      AffectedValueMultipliers.put(ScaleTypes.MINING_SPEED, 0.5f);
      AffectedValueMultipliers.put(ScaleTypes.MOTION, 0.85f);
   }

   @Override
   public float modifyScale(ScaleData scaleData, float modifiedScale, float Tickdelta){
      if (AffectedValueMultipliers.containsKey(scaleData.getScaleType())){
         return modifiedScale * AffectedValueMultipliers.get(scaleData.getScaleType());
      } else {
         return modifiedScale;
      }
   }

   public static final WeakenedBodyModifier INSTANCE = new WeakenedBodyModifier();
}
