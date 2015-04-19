package br.eti.mertz.machinelearning.bayes;

import br.eti.mertz.machinelearning.bayes.classifier.BayesClassifier;
import br.eti.mertz.machinelearning.bayes.classifier.Classification;
import br.eti.mertz.machinelearning.bayes.classifier.Classifier;
import br.eti.mertz.machinelearning.bayes.dataset.Dataset;
import br.eti.mertz.machinelearning.bayes.validation.ExecutionResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.SortedSet;

/**
 * Created by jhonnymertz on 16/04/15.
 */
public class Main {

    static final Logger LOG = LoggerFactory.getLogger(Main.class);

    public static void main(String args[]) {
        final Classifier<String, String> bayes =
                new BayesClassifier<String, String>();

        bayes.setMemoryCapacity(1000000);

        Dataset dataset = new Dataset();

        try {
            List<String> negativeTexts = dataset.index(new File("spec/IMDB/neg"));
            List<String> positiveTexts = dataset.index(new File("spec/IMDB/pos"));

            // defined by teacher
            int start = 10;
            int end = 24009;

            //10 fold cv => 24009 - 10 = 23999 => 10 folds * 2399 examples
            //first folds ends in 2399 + 10 = 2409
            int folds = 10;

            LOG.info("Starting {}-fold-cv ...", folds);
            //executions
            for (int exec = 1; exec <= folds; exec++) {

                bayes.reset();

                LOG.info("Training {} of {} folds", exec, folds);

                //train

                for (int i = 0; i < negativeTexts.size(); i++) {
                    if (i >= start &&
                            (i > (exec) * (end / folds)) ||
                            (i <= (exec - 1) * (end / folds))) {
                        bayes.learn("negative", Arrays.asList(negativeTexts.get(i).split("\\s")));
                        LOG.debug("{} of {} {} texts learned", i, negativeTexts.size(), "negative");
                    }
                }

                for (int i = 0; i < positiveTexts.size(); i++) {
                    if (i >= start &&
                            (i > (exec) * (end / folds)) ||
                            (i <= (exec - 1) * (end / folds))) {
                        bayes.learn("positive", Arrays.asList(positiveTexts.get(i).split("\\s")));
                        LOG.debug("{} of {} {} texts learned", i, positiveTexts.size(), "positive");
                    }
                }


                //test

                int fp = 0, fn = 0, vp = 0, vn = 0;

                BayesClassifier bayesClassifier = ((BayesClassifier<String, String>) bayes);

                for (String text : negativeTexts.subList(
                        ((exec - 1) * (end / folds)) == 0 ? start : ((exec - 1) * (end / folds)),
                        (exec) * (end / folds))) {
                    SortedSet<Classification<String, String>> classification = (SortedSet<Classification<String, String>>) bayesClassifier.classifyDetailed(
                            Arrays.asList(text.split("\\s")));

                    if (classification.last().getCategory().equals("negative"))
                        vn++;
                    else fp++;
                }

                for (String text : positiveTexts.subList(
                        ((exec - 1) * (end / folds)) == 0 ? start : ((exec - 1) * (end / folds)),
                        (exec) * (end / folds))) {

                    Classification classification = bayesClassifier.classify(
                            Arrays.asList(text.split("\\s")));

                    if (classification.getCategory().equals("positive"))
                        vp++;
                    else fn++;
                }


                ExecutionResult execution = new ExecutionResult(vp, vn, fp, fn);

                LOG.debug("Collision matrix:\n    Positive Negative \nPos {}     {} \nNeg {}     {}", vp, fn, fp, vn);
                LOG.debug("Accuracy of execution number {}: {}", exec, execution.getAccuracy());
                LOG.debug("Error of execution number {}: {}", exec, execution.getError());



            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
