package io.github.silvigarabis.sil_yoni.mixin;

import io.github.apace100.apoli.component.PowerHolderComponent;
import io.github.silvigarabis.sil_yoni.power.modify.ModifyFoodEatTimePower;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Item.class)
public class ItemMixin {
    @Unique
    private PlayerEntity user;

    @Inject(
            method = "use",
            at = @At("HEAD")
    )
    public void captureUsePlayer(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir){
        this.user = user;
    }

    @Inject(
            method = "use",
            at = @At("RETURN")
    )
    public void cleanCaptureUsePlayer(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir){
        this.user = null;
    }

    @Inject(
            method = "getMaxUseTime(Lnet/minecraft/item/ItemStack;)I",
            at = @At("RETURN"),
            cancellable = true
    )
    public void modifyFoodEatTime(ItemStack stack, CallbackInfoReturnable<Integer> cir){
        if (user == null) return;
        if (!stack.isFood()) return;

        int original = cir.getReturnValue();
        int modified = (int) PowerHolderComponent.modify(user, ModifyFoodEatTimePower.class, (double) original);

        modified = Math.max(modified, 1);

        cir.setReturnValue(modified);
    }
}
