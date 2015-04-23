package br.eti.mertz.machinelearning.bayes.crossvalidation;

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
