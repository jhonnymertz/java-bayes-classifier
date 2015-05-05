package br.eti.mertz.machinelearning.bayes.classifier;

import br.eti.mertz.machinelearning.bayes.exceptions.NothingLearnedException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class BayesClassifier<T, K> extends Classifier<T, K> {

    /**
     * Calculates the product of all feature probabilities: PROD(P(featI|cat)
     *
     * BigDecimal to greater accuracy ad avoid rouding
     * primitive types such float and double can truncate
     *
     * @param features The set of features to use.
     * @param category The category to test for.
     * @return The product of all feature probabilities.
     */
    private BigDecimal featuresProbabilityProduct(Collection<T> features,
                                                  K category) {
        BigDecimal product = new BigDecimal(0);
        for (T feature : features)
            product = product.add(new BigDecimal(Math.log(this.featureProbability(feature, category))));
        return product;
    }

    /**
     * Calculates the feature probability
     * To avoid the zero frequency problem, it's used laplacian smoothing
     *
     * @param feature The feature to use.
     * @param category The category to test for.
     * @return feature probability.
     */
    @Override
    public float featureProbability(T feature, K category) {
        return ((float) this.featureOccurrencesInCategory(feature, category) + 1)
                / (float) (this.categoryOccurrencesCount(category) + this.vocabularyCount());
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
                .add(new BigDecimal(Math.log(0.5))); //due to dataset distribution, 50% positives e 50% negatives
    }

    /**
     * Retrieves a sorted <code>Set</code> of probabilities that the given set
     * of features is classified as the available categories.
     *
     * @param features The set of features to use.
     * @return A sorted <code>Set</code> of category-probability-entries.
     */
    private List<Classification<T, K>> categoryProbabilities(
            Collection<T> features) {

        List<Classification<T, K>> probabilities = new ArrayList<>();

        this.getCategories().forEach(category ->
            probabilities.add(new Classification<T, K>(
                    features, category,
                    this.categoryProbability(features, category))));

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
        List<Classification<T, K>> probabilities =
                this.categoryProbabilities(features);

        return probabilities.stream().max((o1, o2) -> {
            int toReturn = o1.getProbability().compareTo(o2.getProbability());
            if ((toReturn == 0)
                    && !o1.getCategory().equals(o2.getCategory())) {
                toReturn = -1;
            }
            return toReturn;
        }).get();
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
