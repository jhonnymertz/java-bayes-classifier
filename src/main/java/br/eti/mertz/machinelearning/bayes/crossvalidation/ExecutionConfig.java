package br.eti.mertz.machinelearning.bayes.crossvalidation;

import lombok.Builder;
import lombok.Getter;
import lombok.experimental.Accessors;

@Builder
@Accessors(fluent = true)
@Getter
public class ExecutionConfig {

    private int start, end, folds;

    public boolean isInsideFold(int i, int exec) {
        return (i >= start &&
                (i > (exec) * (end / folds)) ||
                (i <= (exec - 1) * (end / folds)));
    }

    public int getFoldSize() {
        return (end - start) / folds;
    }

    public int getAmountFiles() {
        return end - start;
    }


}
