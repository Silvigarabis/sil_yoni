package io.github.silvigarabis.sil_yoni.util;

import net.minecraft.util.math.BlockPos;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

public class TwoDimRotateUpDownScanner {
    private static class Iterator implements java.util.Iterator<BlockPos.Mutable> {
        private final TwoDimRotateUpDownScanIterator it;
        private final BlockPos.Mutable pos =  new BlockPos.Mutable();

        public Iterator(int radius, int hRadius, BlockPos center) {
            this.it = new TwoDimRotateUpDownScanIterator(
                    center == null
                            ? null
                            : new int[]{center.getX(), center.getY(), center.getZ()},
                    radius,
                    hRadius
            );
        }

        @Override
        public boolean hasNext() {
            return it.hasNext();
        }

        @Override
        public BlockPos.Mutable next() {
            var result = it.next();
            pos.set(result[0], result[1], result[2]);
            return pos;
        }
    }
    public static Iterable<BlockPos.Mutable> with(BlockPos center, int radius, int hRadius){
        return () -> new Iterator(radius, hRadius, center);
    }
    public static Iterable<BlockPos.Mutable> with(BlockPos center, int radius){
        return with(center, radius, radius);
    }
    public static Iterable<BlockPos.Mutable> with(int radius, int hRadius){
        return with(null, radius, hRadius);
    }
    public static Iterable<BlockPos.Mutable> with(int radius){
        return with(radius, radius);
    }
}
