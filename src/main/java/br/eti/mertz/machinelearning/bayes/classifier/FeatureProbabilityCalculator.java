package br.eti.mertz.machinelearning.bayes.classifier;

public interface FeatureProbabilityCalculator<T, K> {

    public float featureProbability(T feature, K category);
}
