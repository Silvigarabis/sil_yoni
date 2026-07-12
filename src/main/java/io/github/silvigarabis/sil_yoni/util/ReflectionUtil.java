package io.github.silvigarabis.sil_yoni.util;

public class ReflectionUtil {
    private static final boolean hasPehkui;
    static {
        boolean checkHasPehkui;
        try {
            Class.forName("virtuoel.pehkui.api.ScaleType");
            checkHasPehkui = true;
        } catch (ClassNotFoundException e) {
            checkHasPehkui = false;
        }
        hasPehkui = checkHasPehkui;
    }

    public static boolean hasPehkui() {
        return hasPehkui;
    }
}
