package br.eti.mertz.machinelearning.bayes.crossvalidation;

/**
 * Created by jhonnymertz on 19/04/15.
 */
public class Outcome implements Execution{

    private float vp, vn, fp, fn;

    public Outcome(Integer vp, Integer vn, Integer fp, Integer fn) {
        this.vp = vp;
        this.vn = vn;
        this.fp = fp;
        this.fn = fn;
    }

    @Override
    public float getVp() {
        return vp;
    }

    @Override
    public float getVn() {
        return vn;
    }

    @Override
    public float getFp() {
        return fp;
    }

    @Override
    public float getFn() {
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
    public float getN() {
        return vp + vn + fp + fn;
    }

    protected void setVp(float vp) {
        this.vp = vp;
    }

    protected void setVn(float vn) {
        this.vn = vn;
    }

    protected void setFp(float fp) {
        this.fp = fp;
    }

    protected void setFn(float fn) {
        this.fn = fn;
    }
}
