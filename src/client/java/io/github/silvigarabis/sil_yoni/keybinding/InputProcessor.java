package io.github.silvigarabis.sil_yoni.keybinding;

import org.jetbrains.annotations.Unmodifiable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InputProcessor {
    private final Map<TriggerPattern, TriggerPatternRunner> patternRunnerMap = new HashMap<>();

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

        for (var state : TriggerPattern.values()) {
            var runner = patternRunnerMap.computeIfAbsent(state, TriggerPatternRunner::new);
            var testResult = runner.test(pressed, justPressed, justReleased, longPressDuration, releaseDuration);
            boolean hasNext = runner.hasNext();

            if (testResult == TriggerPatternRunner.TestResult.WAITING) {
                continue;
            }

            if (testResult == TriggerPatternRunner.TestResult.COMPLETED) {
                if (hasNext) {
                    runner.next();
                } else {
                    result.add(state);
                }
            } else if (testResult == TriggerPatternRunner.TestResult.FAILURE) {
                runner.reset();
            }
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
