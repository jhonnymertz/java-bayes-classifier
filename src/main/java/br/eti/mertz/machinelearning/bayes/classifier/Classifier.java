package br.eti.mertz.machinelearning.bayes.classifier;

import br.eti.mertz.machinelearning.bayes.exceptions.CategoryNotFoundException;

import java.util.*;

public abstract class Classifier<T, K> implements FeatureProbabilityCalculator<T, K> {

    private Dictionary<K, Dictionary<T, Integer>> featureCountPerCategory;

    private Dictionary<T, Integer> totalFeatureCount;

    private Dictionary<K, Integer> totalCategoryCount;

    public Classifier() {
        this.reset();
    }


    public void reset() {
        this.featureCountPerCategory =
                new Hashtable<K, Dictionary<T, Integer>>();
        this.totalFeatureCount =
                new Hashtable<T, Integer>();
        this.totalCategoryCount =
                new Hashtable<K, Integer>();
    }

    public Set<T> getFeatures() {
        return ((Hashtable<T, Integer>) this.totalFeatureCount).keySet();
    }

    public Set<K> getCategories() {
        return ((Hashtable<K, Integer>) this.totalCategoryCount).keySet();
    }

    public int getCategoriesTotal() {
        int toReturn = 0;
        for (Enumeration<Integer> e = this.totalCategoryCount.elements();
             e.hasMoreElements(); ) {
            toReturn += e.nextElement();
        }
        return toReturn;
    }

    public void incrementFeature(T feature, K category) {
        Dictionary<T, Integer> features =
                this.featureCountPerCategory.get(category);
        if (features == null) {
            this.featureCountPerCategory.put(category,
                    new Hashtable<T, Integer>());
            features = this.featureCountPerCategory.get(category);
        }
        Integer count = features.get(feature);
        if (count == null) {
            features.put(feature, 0);
            count = features.get(feature);
        }
        features.put(feature, ++count);

        Integer totalCount = this.totalFeatureCount.get(feature);
        if (totalCount == null) {
            this.totalFeatureCount.put(feature, 0);
            totalCount = this.totalFeatureCount.get(feature);
        }
        this.totalFeatureCount.put(feature, ++totalCount);
    }

    public void incrementCategory(K category) {
        Integer count = this.totalCategoryCount.get(category);
        if (count == null) {
            this.totalCategoryCount.put(category, 0);
            count = this.totalCategoryCount.get(category);
        }
        this.totalCategoryCount.put(category, ++count);
    }

    public int featureCount(T feature, K category) {
        Dictionary<T, Integer> features =
                this.featureCountPerCategory.get(category);
        if (features == null)
            return 0;
        Integer count = features.get(feature);
        return (count == null) ? 0 : count.intValue();
    }

    public int categoryCount(K category) {
        Integer count = this.totalCategoryCount.get(category);
        return (count == null) ? 0 : count.intValue();
    }

    @Override
    public float featureProbability(T feature, K category) {
        if (this.categoryCount(category) == 0)
            throw new CategoryNotFoundException();

        return (float) this.featureCount(feature, category)
                / (float) this.categoryCount(category);
    }

    public float featureProbability(T feature, K category, FeatureProbabilityCalculator calculator) {
        if (this.categoryCount(category) == 0)
            throw new CategoryNotFoundException();

        return calculator.featureProbability(feature, category);
    }

    public void learn(K category, Collection<T> features) {
        this.learn(new Classification<T, K>(features, category));
    }

    public void learn(Classification<T, K> classification) {
        for (T feature : classification.getFeatureset())
            this.incrementFeature(feature, classification.getCategory());

        this.incrementCategory(classification.getCategory());
    }

    public abstract Classification<T, K> classify(Collection<T> features);

}
