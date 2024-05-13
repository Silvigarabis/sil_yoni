package io.github.silvigarabis.sil_yoni.pehkui_modifier;

import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.api.ScaleData;

public class TinyBodyModifier extends ScaleModifier {
   private TinyBodyModifier(){
      super(2695f);
   }

   @Override
   public float modifyScale(ScaleData scaleData, float modifiedScale, float Tickdelta){
      if (scaleData.getScaleType() == ScaleTypes.BLOCK_REACH){
         return modifiedScale * 0.5f;
      } else if (scaleData.getScaleType() == ScaleTypes.ENTITY_REACH){
         return modifiedScale * 0.65f;
      } else {
         return modifiedScale * 0.25f;
      }
   }

   public static final TinyBodyModifier INSTANCE = new TinyBodyModifier();
}
