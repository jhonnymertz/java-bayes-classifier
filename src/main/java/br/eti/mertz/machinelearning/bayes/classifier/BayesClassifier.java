package br.eti.mertz.machinelearning.bayes.classifier;

import br.eti.mertz.machinelearning.bayes.exceptions.NothingLearnedException;
import br.eti.mertz.machinelearning.bayes.exceptions.ZeroFrequencyException;

import java.math.BigDecimal;
import java.math.MathContext;
import java.util.*;

public class BayesClassifier<T, K> extends Classifier<T, K> {

    //BigDecimal para maior precisão e evitar arredondamentos
    //a utilização de tipos primitivos como float e double pode truncar o resultado
    private BigDecimal featuresProbabilityProduct(Collection<T> features,
                                                  K category) {
        BigDecimal product = new BigDecimal(0);
        for (T feature : features)
            product = product.add(new BigDecimal(Math.log(this.featureProbability(feature, category))));
        return product;
    }

    @Override
    public float featureProbability(T feature, K category) {
        //problema da frequência 0: adicionar 1 ao contador de ocorrencias da característica na categoria (estimador de laplace)
        return ((float) this.featureCount(feature, category) + 1)
                / (float) this.categoryCount(category);
    }

    /**
     * Cálculo da função de classificação
     * @param features lista de palavras
     * @param category categoria
     * @return
     */
    private BigDecimal categoryProbability(Collection<T> features, K category) {
        return featuresProbabilityProduct(features, category)
                .add(
                        new BigDecimal(Math.log(0.5))); //devido a característica da distribuição dos exemplos, 50% positivos e 50% negativos
    }

    /**
     * Probabilidade de classificação do texto diante de todas as categorias
     * @param features lista de palavras do texto
     * @return lista com todas as classificações possíveis e sua probabilidade de ocorrência
     */
    private List<Classification<T, K>> categoryProbabilities(
            Collection<T> features) {

        List<Classification<T, K>> probabilities = new ArrayList<>();

        this.getCategories().forEach(category -> {
            probabilities.add(new Classification<T, K>(
                    features, category,
                    this.categoryProbability(features, category)));
        });

        if (probabilities.size() == 0)
            throw new NothingLearnedException();

        return probabilities;
    }

    /**
     * Implementação do método de classificação
     * @param features lista de palavras presentes no texto
     * @return classificação do texto, i. e., a categoria com a maior probabilidade com base na ocorrência das palavras
     */
    @Override
    public Classification<T, K> classify(Collection<T> features) {
        List<Classification<T, K>> probabilities =
                this.categoryProbabilities(features);

        return probabilities.stream().max((o1, o2) -> {
            int toReturn = o1.getProbability().compareTo(o2.getProbability());
            if ((toReturn == 0)
                    && !o1.getCategory().equals(o2.getCategory())) {

                if (o1.getProbability().compareTo(new BigDecimal(0)) == 0)
                    throw new ZeroFrequencyException();

                toReturn = -1;
            }
            return toReturn;
        }).get();
    }

    /**
     * Classificação detalhada do texto
     * @param features lista de palavras do texto
     * @return probabilidade de classificação do texto diante de todas as categorias
     */
    public Collection<Classification<T, K>> classifyDetailed(
            Collection<T> features) {
        return this.categoryProbabilities(features);
    }

}
