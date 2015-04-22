package br.eti.mertz.machinelearning.bayes.crossvalidation;

/**
 * Created by jhonnymertz on 20/04/15.
 */
public interface Execution {

    float getVp();

    float getVn();

    float getFp();

    float getFn();

    float getAccuracy();

    float getError();

    float getRecall();

    float getTFP();

    float getTFN();

    float getFMeasure();

    float getN();
}
