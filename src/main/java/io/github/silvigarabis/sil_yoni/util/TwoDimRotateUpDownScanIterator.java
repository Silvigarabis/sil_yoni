package io.github.silvigarabis.sil_yoni.util;

import java.util.Iterator;

public class TwoDimRotateUpDownScanIterator implements Iterator<int[]>, Iterable<int[]> {
    private final int[] result = new int[3];

    private final int[] center;
    private final int[] yList;
    private final Iterator<int[]> twoDimRotateIterator;
    private int[] twoDimPos = null;
    private int yListIterIndex = 0;

    TwoDimRotateUpDownScanIterator(int[] center, int radius, int hRadius){
        if (center == null) {
            center = new int[]{0, 0, 0};
        }
        this.center = center;

        this.yList = new int[hRadius * 2 + 1];
        int i = 0;
        for (int j = 0; j <= hRadius; j++) {
            yList[i++] = j;
            if (j == 0) continue;
            yList[i++] = -j;
        }

        this.twoDimRotateIterator = new TwoDimRotateScanIterator(radius * 2 + 1, Direction2D.EAST, 1);
    }

    TwoDimRotateUpDownScanIterator(int[] center, int radius){
        this(center, radius, radius);
    }

    TwoDimRotateUpDownScanIterator(int radius, int hRadius){
        this(null, radius, hRadius);
    }

    TwoDimRotateUpDownScanIterator(int radius){
        this(radius, radius);
    }

    @Override
    public boolean hasNext() {
        return yListIterIndex < yList.length || this.twoDimRotateIterator.hasNext();
    }

    @Override
    public int[] next() {
        if (yListIterIndex >= yList.length) {
            twoDimPos = null;
        }
        if (twoDimPos == null){
            twoDimPos = twoDimRotateIterator.next();
            yListIterIndex = 0;
        }

        result[0] = center[0] + twoDimPos[0];
        result[1] = center[1] + yList[yListIterIndex];
        result[2] = center[2] + twoDimPos[1];

        yListIterIndex++;

        return result;
    }

    @Override
    public Iterator<int[]> iterator() {
        return this;
    }

}
