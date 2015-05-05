package br.eti.mertz.machinelearning.bayes.classifier;

import java.util.*;

public abstract class Classifier<T, K> {

    private Dictionary<K, Dictionary<T, Integer>> featureCountPerCategory;

    private Dictionary<T, Integer> featureOccurrencesCount;

    private Dictionary<K, Integer> categoryOccurrencesCount;

    public Classifier() {
        this.reset();
    }

    /**
     * Resets the <i>learned</i> feature and category counts.
     */
    public void reset() {
        this.featureCountPerCategory =
                new Hashtable<K, Dictionary<T, Integer>>();
        this.featureOccurrencesCount =
                new Hashtable<T, Integer>();
        this.categoryOccurrencesCount =
                new Hashtable<K, Integer>();
    }

    /**
     * Returns a <code>Set</code> of features the classifier knows about.
     *
     * @return The <code>Set</code> of features the classifier knows about.
     */
    public Set<T> getFeatures() {
        return ((Hashtable<T, Integer>) this.featureOccurrencesCount).keySet();
    }

    /**
     * Returns a <code>Set</code> of categories the classifier knows about.
     *
     * @return The <code>Set</code> of categories the classifier knows about.
     */
    public Set<K> getCategories() {
        return ((Hashtable<K, Integer>) this.categoryOccurrencesCount).keySet();
    }

    /**
     * Retrieves the number of occurrences of the given feature in the given
     * category.
     *
     * @param feature  The feature, which count to retrieve.
     * @param category The category, which the feature occurred in.
     * @return The number of occurrences of the feature in the category.
     */
    public int featureOccurrencesInCategory(T feature, K category) {
        Dictionary<T, Integer> features =
                this.featureCountPerCategory.get(category);
        if (features == null)
            return 0;
        Integer count = features.get(feature);
        return (count == null) ? 0 : count.intValue();
    }

    public int vocabularyCount() {
        return featureOccurrencesCount.size();
    }

    /**
     * Retrieves the number of occurrences of the given category.
     *
     * @param category The category, which count should be retrieved.
     * @return The number of occurrences.
     */
    public int categoryOccurrencesCount(K category) {
        Integer count = this.categoryOccurrencesCount.get(category);
        return (count == null) ? 0 : count.intValue();
    }

    /**
     * Retrieves the total number of examples of any category that the classifier learned.
     *
     * @return The total category count.
     */
    public int totalFeaturesLearnedCount() {
        int toReturn = 0;
        for (Enumeration<Integer> e = this.categoryOccurrencesCount.elements(); e.hasMoreElements(); ) {
            toReturn += e.nextElement();
        }
        return toReturn;
    }

    /**
     * Increments the count of a given feature in the given category.  This is
     * equal to telling the classifier, that this feature has occurred in this
     * category.
     *
     * @param feature  The feature, which count to increase.
     * @param category The category the feature occurred in.
     */
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

        Integer totalCount = this.featureOccurrencesCount.get(feature);
        if (totalCount == null) {
            this.featureOccurrencesCount.put(feature, 0);
            totalCount = this.featureOccurrencesCount.get(feature);
        }
        this.featureOccurrencesCount.put(feature, ++totalCount);
    }

    /**
     * Increments the count of a given category.  This is equal to telling the
     * classifier, that this category has occurred once more.
     *
     * @param category The category, which count to increase.
     */
    public void incrementCategory(K category) {
        Integer count = this.categoryOccurrencesCount.get(category);
        if (count == null) {
            this.categoryOccurrencesCount.put(category, 0);
            count = this.categoryOccurrencesCount.get(category);
        }
        this.categoryOccurrencesCount.put(category, ++count);
    }

    public void learn(K category, Collection<T> features) {
        Classification<T, K> classification = new Classification<T, K>(features, category);

        classification.getFeatures()
                .forEach(feature -> this.incrementFeature(feature, classification.getCategory()));

        this.incrementCategory(classification.getCategory());
    }

    public abstract float featureProbability(T feature, K category);

    public abstract Classification<T, K> classify(Collection<T> features);

}
