package br.eti.mertz.machinelearning.bayes.classifier;

import br.eti.mertz.machinelearning.bayes.exceptions.NothingLearnedException;
import br.eti.mertz.machinelearning.bayes.exceptions.ZeroFrequencyException;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A concrete implementation of the abstract br.eti.mertz.machinelearning.bayes.classifier.Classifier class.  The Bayes
 * classifier implements a naive Bayes approach to classifying a given set of
 * features: classify(feat1,...,featN) = argmax(P(cat)*PROD(P(featI|cat)
 *
 * @param <T> The feature class.
 * @param <K> The category class.
 * @see http://en.wikipedia.org/wiki/Naive_Bayes_classifier
 */
public class BayesClassifier<T, K> extends Classifier<T, K> {

    /**
     * Calculates the product of all feature probabilities: PROD(P(featI|cat)
     *
     * @param features The set of features to use.
     * @param category The category to test for.
     * @return The product of all feature probabilities.
     */
    private BigDecimal featuresProbabilityProduct(Collection<T> features,
                                                  K category) {
        BigDecimal product = new BigDecimal(1);
        for (T feature : features)
            product = product.multiply(new BigDecimal(this.featureProbability(feature, category)));
        return product;
    }

    @Override
    public float featureProbability(T feature, K category) {
        return ((float) this.featureCount(feature, category) + 1)
                / (float) this.categoryCount(category);
    }

    /**
     * Calculates the probability that the features can be classified as the
     * category given.
     *
     * @param features The set of features to use.
     * @param category The category to test for.
     * @return The probability that the features can be classified as the
     * category.
     */
    private BigDecimal categoryProbability(Collection<T> features, K category) {
        return featuresProbabilityProduct(features, category)
                .multiply(
                        new BigDecimal(this.categoryCount(category))
                                .divide(new BigDecimal(this.getCategoriesTotal())));
    }

    /**
     * Retrieves a sorted <code>Set</code> of probabilities that the given set
     * of features is classified as the available categories.
     *
     * @param features The set of features to use.
     * @return A sorted <code>Set</code> of category-probability-entries.
     */
    private SortedSet<Classification<T, K>> categoryProbabilities(
            Collection<T> features) {

        /*
         * Sort the set according to the possibilities. Because we have to sort
         * by the mapped value and not by the mapped key, we can not use a
         * sorted tree (TreeMap) and we have to use a set-entry approach to
         * achieve the desired functionality. A custom comparator is therefore
         * needed.
         */
        SortedSet<Classification<T, K>> probabilities =
                new TreeSet<Classification<T, K>>(
                        new Comparator<Classification<T, K>>() {

                            @Override
                            public int compare(Classification<T, K> o1,
                                               Classification<T, K> o2) {
                                int toReturn = o1.getProbability().compareTo(o2.getProbability());
                                if ((toReturn == 0)
                                        && !o1.getCategory().equals(o2.getCategory())) {
                                    //TODO verify if it works
                                    if (o1.getProbability().compareTo(new BigDecimal(0)) == 0)
                                        throw new ZeroFrequencyException();

                                    toReturn = -1;
                                }
                                return toReturn;
                            }
                        });

        for (K category : this.getCategories())
            probabilities.add(new Classification<T, K>(
                    features, category,
                    this.categoryProbability(features, category)));

        if (probabilities.size() == 0)
            throw new NothingLearnedException();

        return probabilities;
    }

    /**
     * Classifies the given set of features.
     *
     * @return The category the set of features is classified as.
     */
    @Override
    public Classification<T, K> classify(Collection<T> features) {
        SortedSet<Classification<T, K>> probabilities =
                this.categoryProbabilities(features);

        return probabilities.last();
    }

    /**
     * Classifies the given set of features. and return the full details of the
     * classification.
     *
     * @return The set of categories the set of features is classified as.
     */
    public Collection<Classification<T, K>> classifyDetailed(
            Collection<T> features) {
        return this.categoryProbabilities(features);
    }

}
