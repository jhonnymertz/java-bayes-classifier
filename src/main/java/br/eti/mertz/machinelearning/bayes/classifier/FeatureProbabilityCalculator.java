package br.eti.mertz.machinelearning.bayes.classifier;

/**
 * Simple interface defining the method to calculate the feature probability.
 *
 * @param <T> The feature class.
 * @param <K> The category class.
 */
public interface FeatureProbabilityCalculator<T, K> {

    public float featureProbability(T feature, K category);
}
