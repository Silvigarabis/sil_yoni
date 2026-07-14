package io.github.silvigarabis.sil_yoni.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.FireBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(FireBlock.class)
public interface FireBlockInvoker {

    @Invoker("getSpreadChance")
    int silYoni$getSpreadChance(BlockState state);

    @Invoker("getStateForPosition")
    BlockState silYoni$getStateForPosition(BlockView world, BlockPos pos);
}
