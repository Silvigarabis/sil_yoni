package io.github.silvigarabis.sil_yoni.pehkui_modifier;

import virtuoel.pehkui.api.ScaleModifier;
import virtuoel.pehkui.api.ScaleTypes;
import virtuoel.pehkui.api.ScaleData;

public class SmallerBodyModifier extends ScaleModifier {
   private SmallerBodyModifier(){
      super(2695f);
   }

   @Override
   public float modifyScale(ScaleData scaleData, float modifiedScale, float Tickdelta){
      return modifiedScale * ((scaleData.getScaleType() == ScaleTypes.BLOCK_REACH) ? 0.7f : 0.66f);
   }

   public static final SmallerBodyModifier INSTANCE = new SmallerBodyModifier();
}
