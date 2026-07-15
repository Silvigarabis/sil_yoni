package io.github.silvigarabis.sil_yoni.keybinding;

import org.jetbrains.annotations.Unmodifiable;

import java.util.List;

import static io.github.silvigarabis.sil_yoni.keybinding.StageType.*;

public enum TriggerPattern {
    SINGLE_CLICK_INTERVAL(
            "sil_yoni.gui.badge.active.single-click-interval",
            "sil_yoni.gui.badge.toggle.single-click-interval",
            WaitPress.withInf(),
            WaitRelease.withDef(),
            ReleaseHold.withDef(),
            ActiveOnce.use()
    ),
    DOUBLE_CLICK_INTERVAL(
            "sil_yoni.gui.badge.active.double-click-interval",
            "sil_yoni.gui.badge.toggle.double-click-interval",
            WaitPress.withInf(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            WaitRelease.withDef(),
            ReleaseHold.withDef(),
            ActiveOnce.use()
    ),
    TRIPLE_CLICK_INTERVAL(
            "sil_yoni.gui.badge.active.triple-click-interval",
            "sil_yoni.gui.badge.toggle.triple-click-interval",
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
            "sil_yoni.gui.badge.active.single-click-immediate",
            "sil_yoni.gui.badge.toggle.single-click-immediate",
            WaitPress.withInf(),
            WaitRelease.withDef(),
            ActiveOnce.use(),
            Reset.use()
    ),
    DOUBLE_CLICK_IMMEDIATE(
            "sil_yoni.gui.badge.active.double-click-immediate",
            "sil_yoni.gui.badge.toggle.double-click-immediate",
            WaitPress.withInf(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            WaitRelease.withDef(),
            ActiveOnce.use(),
            Reset.use()
    ),
    TRIPLE_CLICK_IMMEDIATE(
            "sil_yoni.gui.badge.active.triple-click-immediate",
            "sil_yoni.gui.badge.toggle.triple-click-immediate",
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
            "sil_yoni.gui.badge.active.single-press-click",
            "sil_yoni.gui.badge.toggle.single-press-click",
            WaitPress.withInf(),
            ActiveOnce.use(),
            WaitRelease.withDef(),
            Reset.use()
    ),

    DOUBLE_PRESS_CLICK(
            "sil_yoni.gui.badge.active.double-press-click",
            "sil_yoni.gui.badge.toggle.double-press-click",
            WaitPress.withInf(),
            WaitRelease.withDef(),
            WaitPress.withDef(),
            ActiveOnce.use(),
            WaitRelease.withDef(),
            Reset.use()
    ),

    TRIPLE_PRESS_CLICK(
            "sil_yoni.gui.badge.active.triple-press-click",
            "sil_yoni.gui.badge.toggle.triple-press-click",
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
            "sil_yoni.gui.badge.active.single-press-hold-always-continuous",
            "sil_yoni.gui.badge.toggle.single-press-hold-always-continuous",
            WaitPress.withInf(),
            ActiveContinuousWhenHold.use(),
            Reset.use()
    ),

    SINGLE_PRESS_HOLD(
            "sil_yoni.gui.badge.active.single-press-hold",
            "sil_yoni.gui.badge.toggle.single-press-hold",
            WaitPress.withInf(),
            PressHold.withLongDef(),
            ActiveOnce.use()
    ),

    LONG_PRESS_3s(
            "sil_yoni.gui.badge.active.long-press-3s",
            "sil_yoni.gui.badge.toggle.long-press-3s",
            WaitPress.withInf(),
            PressHold.with(20 * 3),
            ActiveOnce.use()
    ),
    LONG_PRESS_10s(
            "sil_yoni.gui.badge.active.long-press-10s",
            "sil_yoni.gui.badge.toggle.long-press-10s",
            WaitPress.withInf(),
            PressHold.with(20 * 10),
            ActiveOnce.use()
    ),

    LONG_PRESS_3s_ALWAYS_CONTINUOUS(
            "sil_yoni.gui.badge.active.long-press-3s-always-continuous",
            "sil_yoni.gui.badge.toggle.long-press-3s-always-continuous",
            WaitPress.withInf(),
            PressHold.with(20 * 3),
            ActiveContinuousWhenHold.use(),
            Reset.use()
    ),
    LONG_PRESS_10s_ALWAYS_CONTINUOUS(
            "sil_yoni.gui.badge.active.long-press-10s-always-continuous",
            "sil_yoni.gui.badge.toggle.long-press-10s-always-continuous",
            WaitPress.withInf(),
            PressHold.with(20 * 10),
            ActiveContinuousWhenHold.use(),
            Reset.use()
    ),

    ;

    public @Unmodifiable List<StageSpec> stages() {
        return stages;
    }

    public String translationKey(){
        return translationKey;
    }

    public String toggleTranslationKey(){
        return toggleTranslationKey;
    }

    private final List<StageSpec> stages;
    private final String translationKey;
    private final String toggleTranslationKey;


    TriggerPattern(String translationKey, String toggleTranslationKey, StageSpec... stages) {
        this.translationKey = translationKey;
        this.toggleTranslationKey = toggleTranslationKey;
        this.stages = List.of(stages);
    }
}
