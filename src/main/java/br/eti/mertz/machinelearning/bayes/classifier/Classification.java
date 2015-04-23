package br.eti.mertz.machinelearning.bayes.classifier;

import java.math.BigDecimal;
import java.util.Collection;

public class Classification<T, K> {

    private Collection<T> featureset;

    private K category;

    private BigDecimal probability;

    public Classification(Collection<T> featureset, K category) {
        this(featureset, category, new BigDecimal(1));
    }

    public Classification(Collection<T> featureset, K category,
                          BigDecimal probability) {
        this.featureset = featureset;
        this.category = category;
        this.probability = probability;
    }

    public Collection<T> getFeatureset() {
        return featureset;
    }

    public BigDecimal getProbability() {
        return this.probability;
    }

    public K getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Classification [category=" + this.category
                + ", probability=" + this.probability
                + ", featureset=" + this.featureset
                + "]";
    }

}
