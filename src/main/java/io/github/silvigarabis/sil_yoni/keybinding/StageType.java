package io.github.silvigarabis.sil_yoni.keybinding;

public enum StageType {
    WaitPress,
    WaitRelease,
    PressHold,
    ReleaseHold,

    ActiveOnce,
    ActiveContinuousWhenHold,
    ActiveContinuousWhenRelease,

    WaitUntil,
    Completed,
    Reset
    ;
    public static final int DEFAULT_INTERVAL_WINDOW = 6;
    public static final int DEFAULT_LONG_PRESS_INTERVAL_WINDOW = 20;

    public StageSpec withInf(){
        return new StageSpec(this, Integer.MAX_VALUE);
    }
    public StageSpec withLongDef(){
        return new StageSpec(this, DEFAULT_LONG_PRESS_INTERVAL_WINDOW);
    }
    public StageSpec withDef(){
        return new StageSpec(this, DEFAULT_INTERVAL_WINDOW);
    }
    public StageSpec with(int timeTicks){
        return new StageSpec(this, timeTicks);
    }
    public StageSpec use(){
        return new StageSpec(this, -1);
    }
}
