package io.github.silvigarabis.sil_yoni.util;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class TwoDimRotateScanIterator implements Iterator<int[]>, Iterable<int[]> {
    private int side = 4;
    private int rotateStep = 1;
    private Direction2D direction = Direction2D.EAST;
    private final int[] result = new int[2];

    public TwoDimRotateScanIterator(int side, Direction2D direction, int rotateStep) {
        this.side = side;
        this.direction = direction;
        if (rotateStep != -1 && rotateStep != 1) {
            throw new IllegalArgumentException("rotate steps must be either -1 or 1");
        }
        this.rotateStep = rotateStep;
    }

    private int r = 0;
    private int x = 0;
    private int z = 0;
    private int s = 0;
    private int turns = 0;

    @Override
    public boolean hasNext() {
        if (r == 0) return true;
        if (r >= side) {
            return s + 1 < side;
        }
        return true;
    }

    /**
     * @implNote 注意：next() 返回的是同一个可复用数组。
     */
    @Override
    public int[] next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }

        if (r > 0) {
            x += direction.dx;
            z += direction.dz;

            s++;
        }

        result[0] = x;
        result[1] = z;

        if (r == 0) {
            r++;
        } else if (s == r) {
            s = 0;
            direction = direction.next(rotateStep);
            turns = (turns + 1) & 3;
            if (turns % 2 == 0) {
                r++;
            }
        }

        return result;
    }

    @Override
    public Iterator<int[]> iterator() {
        return this;
    }
}
