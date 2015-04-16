package br.eti.mertz.machinelearning.bayes;

import br.eti.mertz.machinelearning.bayes.classifier.BayesClassifier;
import br.eti.mertz.machinelearning.bayes.classifier.Classifier;
import br.eti.mertz.machinelearning.bayes.dataset.Dataset;
import br.eti.mertz.machinelearning.bayes.filter.Filter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Created by jhonnymertz on 16/04/15.
 */
public class Main {

    static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String args[]){
        final Classifier<String, String> bayes =
                new BayesClassifier<String, String>();

        bayes.setMemoryCapacity(100000);

        Dataset dataset = new Dataset();

        try {
            dataset.index(new File("spec/IMDB/neg"), "negative", bayes);
            dataset.index(new File("spec/IMDB/pos"), "positive", bayes);

            System.out.println(((BayesClassifier<String, String>) bayes).classify(
                    Arrays.asList(Filter.filter("Story of a man who has unnatural feelings for a pig. Starts out with a opening scene that is a terrific example of absurd comedy. A formal orchestra audience is turned into an insane, violent mob by the crazy chantings of it's singers. Unfortunately it stays absurd the WHOLE time with no general narrative eventually making it just too off putting. Even those from the era should be turned off. The cryptic dialogue would make Shakespeare seem easy to a third grader. On a technical level it's better than you might think with some good cinematography by future great Vilmos Zsigmond. Future stars Sally Kirkland and Frederic Forrest can be seen briefly.").split("\\s"))));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
