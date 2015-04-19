package br.eti.mertz.machinelearning.bayes.validation;

/**
 * Created by jhonnymertz on 19/04/15.
 */
public class ExecutionResult {

    private Integer vp, vn, fp, fn;

    public ExecutionResult(Integer vp, Integer vn, Integer fp, Integer fn) {
        this.vp = vp;
        this.vn = vn;
        this.fp = fp;
        this.fn = fn;
    }

    public float getAccuracy(){
        return ((float)(vp + vn) / (float) getN());
    }

    public float getError(){
        return ((float)(fp + fn) / (float) getN());
    }

    public Integer getN() {
        return vp + vn + fp + fn;
    }
}
