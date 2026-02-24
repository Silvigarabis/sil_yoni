package io.github.silvigarabis.sil_yoni.keybinding;

import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

import static io.github.silvigarabis.sil_yoni.keybinding.StageType.*;

public enum TriggerPattern {
    SINGLE_CLICK_INTERVAL(
            WaitPress.withInf(),
            WaitRelease.withDef(),
            ReleaseHold.withDef(),
            ActiveOnce.use()
    ),
    DOUBLE_CLICK_INTERVAL(
            WaitPress.withInf(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            WaitRelease.withDef(),
            ReleaseHold.withDef(),
            ActiveOnce.use()
    ),
    TRIPLE_CLICK_INTERVAL(
            WaitPress.withInf(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            WaitRelease.withDef(),
            ReleaseHold.withDef(),
            ActiveOnce.use()
    ),

    SINGLE_CLICK_IMMEDIATE(
            WaitPress.withInf(),
            WaitRelease.withDef(),
            ActiveOnce.use(),
            Reset.use()
    ),
    DOUBLE_CLICK_IMMEDIATE(
            WaitPress.withInf(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            WaitRelease.withDef(),
            ActiveOnce.use(),
            Reset.use()
    ),
    TRIPLE_CLICK_IMMEDIATE(
            WaitPress.withInf(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            WaitRelease.withDef(),
            ActiveOnce.use(),
            Reset.use()
    ),

    SINGLE_PRESS_CLICK(
            WaitPress.withInf(),
            ActiveOnce.use(),
            WaitRelease.withDef(),
            Reset.use()
    ),

    DOUBLE_PRESS_CLICK(
            WaitPress.withInf(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            ActiveOnce.use(),
            WaitRelease.withDef(),
            Reset.use()
    ),

    TRIPLE_PRESS_CLICK(
            WaitPress.withInf(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            ActiveOnce.use(),
            WaitRelease.withDef(),
            Reset.use()
    ),

    SINGLE_PRESS_HOLD_ALWAYS_CONTINUOUS(
            WaitPress.withInf(),
            ActiveContinuousWhenHold.use(),
            Reset.use()
    ),

    SINGLE_PRESS_HOLD(
            WaitPress.withInf(),
            PressHold.withLongDef(),
            ActiveOnce.use()
    ),

    LONG_PRESS_3s(
            WaitPress.withInf(),
            PressHold.with(20 * 3),
            ActiveOnce.use()
    ),
    LONG_PRESS_10s(
            WaitPress.withInf(),
            PressHold.with(20 * 10),
            ActiveOnce.use()
    ),
    ;

    public @Unmodifiable List<StageSpec> stages() {
        return stages;
    }

    private final List<StageSpec> stages;

    TriggerPattern(StageSpec... stages) {
        this.stages = List.of(stages);
    }
}
