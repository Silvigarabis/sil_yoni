package io.github.silvigarabis.sil_yoni.util;

public enum Direction2D {
    EAST(1, 0),
    SOUTH(0, 1),
    WEST(-1, 0),
    NORTH(0, -1);

    final int dx;
    final int dz;

    Direction2D(int dx, int dz) {
        this.dx = dx;
        this.dz = dz;
    }

    Direction2D next(int step) {
        int next = Math.floorMod(ordinal() + step, 4);
        return values()[next];
    }

    Direction2D next() {
        return next(1);
    }
}
