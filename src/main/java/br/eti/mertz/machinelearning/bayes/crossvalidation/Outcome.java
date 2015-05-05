package br.eti.mertz.machinelearning.bayes.crossvalidation;

import lombok.Data;

@Data
public class Outcome implements Execution {

    private float vp, vn, fp, fn;

    public Outcome() {
        this(0, 0, 0, 0);
    }

    public Outcome(Integer vp, Integer vn, Integer fp, Integer fn) {
        this.vp = vp;
        this.vn = vn;
        this.fp = fp;
        this.fn = fn;
    }

    @Override
    public float getAccuracy() {
        return ((float) (vp + vn) / (float) getN());
    }

    @Override
    public float getError() {
        return ((float) (fp + fn) / (float) getN());
    }

    @Override
    public float getRecall() {
        return ((float) vp / (float) (vp + fn));
    }

    @Override
    public float getTFP() {
        return ((float) fp / (float) (fp + vn));
    }

    @Override
    public float getTFN() {
        return ((float) fn / (float) (vp + fn));
    }

    @Override
    public float getFMeasure() {
        return ((2 * getRecall() * getAccuracy()) / (getRecall() + getAccuracy()));
    }

    @Override
    public float getN() {
        return vp + vn + fp + fn;
    }

    public float incVp() {
        return ++vp;
    }

    public float incVn() {
        return ++vn;
    }

    public float incFp() {
        return ++fp;
    }

    public float incFn() {
        return ++fn;
    }
}
