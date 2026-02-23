package io.github.silvigarabis.sil_yoni.keybinding;

import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

import static io.github.silvigarabis.sil_yoni.keybinding.StageType.*;

public enum TriggerPattern {
    ACTIVE_SINGLE_CLICK(
            WaitPress.withInf(),
            WaitRelease.withDef(),
            ReleaseHold.withDef(),
            ActiveOnce.use()
    ),
    ACTIVE_DOUBLE_CLICK(
            WaitPress.withInf(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            WaitRelease.withDef(),
            ReleaseHold.withDef(),
            ActiveOnce.use()
    ),
    ACTIVE_TRIPLE_CLICK(
            WaitPress.withInf(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            WaitRelease.withDef(),
            ReleaseHold.withDef(),
            ActiveOnce.use()
    ),
    ACTIVE_SHOT_HOLD_ONCE(
            WaitPress.withInf(),
            WaitRelease.withDef(),
            ActiveOnce.use()
    ),
    ACTIVE_HOLD_ONCE(
            WaitPress.withInf(),
            PressHold.withLongDef(),
            ActiveOnce.use()
    ),
    ACTIVE_DOUBLE_CLICK_HOLD_ONCE(
            WaitPress.withInf(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            PressHold.withLongDef(),
            ActiveOnce.use()
    ),
    ACTIVE_SHOT_HOLD(
            WaitPress.withInf(),
            ActiveContinuousWhenHold.use()
    ),
    ACTIVE_HOLD(
            WaitPress.withInf(),
            PressHold.withLongDef(),
            ActiveContinuousWhenHold.use()
    ),
    ACTIVE_DOUBLE_CLICK_HOLD(
            WaitPress.withInf(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            PressHold.withLongDef(),
            ActiveContinuousWhenHold.use()
    ),
    ACTIVE_10s_LONG_HOLD_ONCE(
            WaitPress.withInf(),
            PressHold.with(20 * 10),
            ActiveOnce.use()
    );

    public @Unmodifiable List<StageSpec> stages() {
        return stages;
    }

    private final List<StageSpec> stages;

    TriggerPattern(StageSpec... stages) {
        this.stages = List.of(stages);
    }
}
