package br.eti.mertz.machinelearning.bayes.validation;

/**
 * Created by jhonnymertz on 20/04/15.
 */
public interface Execution {

    Integer getVp();

    Integer getVn();

    Integer getFp();

    Integer getFn();

    float getAccuracy();

    float getError();

    float getRecall();

    float getTFP();

    float getTFN();

    float getFMeasure();

    Integer getN();
}
