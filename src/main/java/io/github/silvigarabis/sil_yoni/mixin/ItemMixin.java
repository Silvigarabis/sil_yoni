package io.github.silvigarabis.sil_yoni.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.silvigarabis.sil_yoni.power.modify.ModifyFoodEatTimePower;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(
            method = "getMaxUseTime(Lnet/minecraft/item/ItemStack;)I",
            at = @At("RETURN"),
            cancellable = true
    )
    public void modifyFoodEatTime(ItemStack stack, CallbackInfoReturnable<Integer> cir){
        if (!stack.isFood()) return;
        var user = stack.getHolder();
        if (user == null) return;

        int original = cir.getReturnValue();
        int modified = (int) PowerHolderComponent.modify(user, ModifyFoodEatTimePower.class, (double) original);

        modified = Math.max(modified, 1); // 低于 1 的值会导致无法吃下食物

        cir.setReturnValue(modified);
    }
}
