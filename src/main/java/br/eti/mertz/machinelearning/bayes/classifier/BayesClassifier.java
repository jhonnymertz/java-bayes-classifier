package br.eti.mertz.machinelearning.bayes.classifier;

import br.eti.mertz.machinelearning.bayes.exceptions.NothingLearnedException;
import br.eti.mertz.machinelearning.bayes.exceptions.ZeroFrequencyException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.Collection;
import java.util.Comparator;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * features: classify(feat1,...,featN) = argmax(P(cat)*PROD(P(featI|cat)
 * @see http://en.wikipedia.org/wiki/Naive_Bayes_classifier
 */
public class BayesClassifier<T, K> extends Classifier<T, K> {

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

    private BigDecimal categoryProbability(Collection<T> features, K category) {
        return featuresProbabilityProduct(features, category)
                .multiply(
                        new BigDecimal(this.categoryCount(category))
                                .divide(new BigDecimal(this.getCategoriesTotal()), MathContext.DECIMAL128));
    }

    private SortedSet<Classification<T, K>> categoryProbabilities(
            Collection<T> features) {

        SortedSet<Classification<T, K>> probabilities =
                new TreeSet<Classification<T, K>>(
                        new Comparator<Classification<T, K>>() {

                            @Override
                            public int compare(Classification<T, K> o1,
                                               Classification<T, K> o2) {
                                int toReturn = o1.getProbability().compareTo(o2.getProbability());
                                if ((toReturn == 0)
                                        && !o1.getCategory().equals(o2.getCategory())) {
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

    @Override
    public Classification<T, K> classify(Collection<T> features) {
        SortedSet<Classification<T, K>> probabilities =
                this.categoryProbabilities(features);

        return probabilities.last();
    }

    public Collection<Classification<T, K>> classifyDetailed(
            Collection<T> features) {
        return this.categoryProbabilities(features);
    }

}
