package br.eti.mertz.machinelearning.bayes.validation;

import java.util.List;

/**
 * Created by jhonnymertz on 19/04/15.
 */
public class Executions extends ExecutionResult{

    private List<ExecutionResult> executions;

    public Executions(Integer vp, Integer vn, Integer fp, Integer fn) {
        super(vp, vn, fp, fn);
    }

    @Override
    public Integer getVp() {
        return null;
    }

    @Override
    public Integer getVn() {
        return null;
    }

    @Override
    public Integer getFp() {
        return null;
    }

    @Override
    public Integer getFn() {
        return null;
    }

    @Override
    public float getAccuracy() {
        return 0;
    }

    @Override
    public float getError() {
        return 0;
    }

    @Override
    public float getRecall() {
        return 0;
    }

    @Override
    public float getTFP() {
        return 0;
    }

    @Override
    public float getTFN() {
        return 0;
    }

    @Override
    public float getFMeasure() {
        return 0;
    }

    @Override
    public Integer getN() {
        return null;
    }
}
