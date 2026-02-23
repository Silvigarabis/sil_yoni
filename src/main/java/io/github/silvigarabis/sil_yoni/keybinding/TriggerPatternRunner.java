package io.github.silvigarabis.sil_yoni.keybinding;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TriggerPatternRunner {
    public enum TestResult {
        WAITING, COMPLETED, FAILURE
    }

    private @Nullable StageSpec curStage;

    private int curStageSeq;
    private int nextStageSeq;

    private int stageTicks;
    private @NotNull final TriggerPattern pattern;

    public TriggerPatternRunner(@NotNull TriggerPattern pattern) {
        this.pattern = pattern;
        reset();
    }

    public void reset(){
        curStage = null;
        curStageSeq = -1;
        nextStageSeq = 0;
        stageTicks = -1;
    }

    public void next() {
        if (hasNext()) {
            curStageSeq = nextStageSeq;
            this.curStage = pattern.stages().get(curStageSeq);
            this.nextStageSeq++;
            this.stageTicks = -1;
        }
    }

    public boolean hasNext() {
        return pattern.stages().size() > nextStageSeq;
    }

    public @NotNull TestResult test(
            boolean pressed,
            boolean justPressed,
            boolean justReleased,
            int longPressDuration,
            int releaseDuration
    ) {
        if (pattern.stages().isEmpty()) {
            return TestResult.WAITING;
        }
        if (curStage == null) {
            next();
        }

        stageTicks++;
        @NotNull TestResult result = switch (curStage.mode()) {
            case WaitPress -> {
                if (releaseDuration >= curStage.timeTicks()){
                    yield TestResult.FAILURE;
                }
                if (justPressed){
                    yield TestResult.COMPLETED;
                }
                yield TestResult.WAITING;
            }
            case WaitRelease -> {
                if (longPressDuration >= curStage.timeTicks()){
                    yield TestResult.FAILURE;
                }
                if (justReleased){
                    yield TestResult.COMPLETED;
                }
                yield TestResult.WAITING;
            }
            case PressHold -> {
                if (longPressDuration >= curStage.timeTicks()){
                    yield TestResult.COMPLETED;
                }
                if (!pressed){
                    yield TestResult.FAILURE;
                }
                yield TestResult.WAITING;
            }
            case ReleaseHold -> {
                if (releaseDuration >= curStage.timeTicks()){
                    yield TestResult.COMPLETED;
                }
                if (pressed){
                    yield TestResult.FAILURE;
                }
                yield TestResult.WAITING;
            }
            case ActiveOnce -> {
                if (stageTicks == 0){
                    yield TestResult.COMPLETED;
                }
                if (!pressed){
                    yield TestResult.FAILURE;
                }
                yield TestResult.WAITING;
            }
            case ActiveContinuousWhenHold -> {
                if (pressed){
                    yield TestResult.COMPLETED;
                } else {
                    yield TestResult.FAILURE;
                }
            }
        };

        return result;
    }

    public int curStageSeq() {
        return curStageSeq;
    }

}
