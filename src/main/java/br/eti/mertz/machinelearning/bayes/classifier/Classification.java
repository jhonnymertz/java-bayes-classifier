package br.eti.mertz.machinelearning.bayes.classifier;

import java.math.BigDecimal;
import java.util.Collection;

public class Classification<T, K> {

    private Collection<T> features;

    private K category;

    private BigDecimal probability;

    public Classification(Collection<T> features, K category) {
        this(features, category, new BigDecimal(1));
    }

    public Classification(Collection<T> features, K category,
                          BigDecimal probability) {
        this.features = features;
        this.category = category;
        this.probability = probability;
    }

    public Collection<T> getFeatures() {
        return features;
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
                + ", features=" + this.features
                + "]";
    }

}
