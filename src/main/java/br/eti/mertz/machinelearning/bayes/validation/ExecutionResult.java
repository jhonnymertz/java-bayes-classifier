package br.eti.mertz.machinelearning.bayes.validation;

/**
 * Created by jhonnymertz on 19/04/15.
 */
public class ExecutionResult implements Execution{

    private Integer vp, vn, fp, fn;

    public ExecutionResult(Integer vp, Integer vn, Integer fp, Integer fn) {
        this.vp = vp;
        this.vn = vn;
        this.fp = fp;
        this.fn = fn;
    }

    @Override
    public Integer getVp() {
        return vp;
    }

    @Override
    public Integer getVn() {
        return vn;
    }

    @Override
    public Integer getFp() {
        return fp;
    }

    @Override
    public Integer getFn() {
        return fn;
    }

    @Override
    public float getAccuracy(){
        return ((float)(vp + vn) / (float) getN());
    }

    @Override
    public float getError(){
        return ((float)(fp + fn) / (float) getN());
    }

    @Override
    public float getRecall(){
        return ((float) vp / (float) (vp + fn));
    }

    @Override
    public float getTFP(){
        return ((float) fp / (float) (fp + vn));
    }

    @Override
    public float getTFN(){
        return ((float) fn / (float) (vp + fn));
    }

    @Override
    public float getFMeasure(){
        return ((2 * getRecall() * getAccuracy()) / (getRecall() + getAccuracy()));
    }

    @Override
    public Integer getN() {
        return vp + vn + fp + fn;
    }
}
