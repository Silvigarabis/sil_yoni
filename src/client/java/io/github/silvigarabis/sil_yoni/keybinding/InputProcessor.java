package io.github.silvigarabis.sil_yoni.keybinding;

import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputProcessor {
    private final Map<TriggerPattern, TriggerPatternRunner> patternRunnerMap = new HashMap<>();
    private final List<TriggerPattern> disusedPatterns = new ArrayList<>();

    private int releaseDuration = 0;
    private int longPressDuration = 0;
    private boolean lastPressed = false;

    public @Unmodifiable List<TriggerPattern> update(boolean pressed) {
        List<TriggerPattern> result = new ArrayList<>();

        boolean justPressed = pressed && !lastPressed;
        boolean justReleased = !pressed && lastPressed;
        if (pressed && !justPressed) {
            longPressDuration++;
        }
        if (!pressed && !justReleased) {
            releaseDuration++;
        }

        if (TriggerPattern.values().length == disusedPatterns.size()) {
            // 所有模式都被淘汰了，恢复默认
            disusedPatterns.clear();
        }

        int steppedPatternCounter = 0;

        for (var pattern : TriggerPattern.values()) {
            if (disusedPatterns.contains(pattern)) continue;

            var runner = patternRunnerMap.computeIfAbsent(pattern, TriggerPatternRunner::new);
            var testResult = runner.test(pressed, justPressed, justReleased, longPressDuration, releaseDuration);
            boolean hasNext = runner.hasNext();

            if (runner.curStageSeq() > 0){
                steppedPatternCounter++;
            }

            if (testResult == TriggerPatternRunner.TestResult.WAITING) {
                continue;
            }

            if (testResult == TriggerPatternRunner.TestResult.COMPLETED) {
                if (hasNext) {
                    runner.next();
                } else {
                    result.add(pattern);
                }
            } else if (testResult == TriggerPatternRunner.TestResult.FAILURE) {
                runner.reset();
                // 阶段失败，淘汰该模式
                disusedPatterns.add(pattern);
            }
        }

        // 在本 tick 没有任何模式处于进行中的阶段，直接清空淘汰模式列表
        if (steppedPatternCounter == 0){
            disusedPatterns.clear();
        }

        lastPressed = pressed;
        if (pressed) {
            releaseDuration = 0;
        } else {
            longPressDuration = 0;
        }

        return List.copyOf(result);
    }
}
