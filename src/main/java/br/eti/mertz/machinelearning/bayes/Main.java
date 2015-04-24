package br.eti.mertz.machinelearning.bayes;

import br.eti.mertz.machinelearning.bayes.classifier.BayesClassifier;
import br.eti.mertz.machinelearning.bayes.classifier.Classification;
import br.eti.mertz.machinelearning.bayes.crossvalidation.ExecutionConfig;
import br.eti.mertz.machinelearning.bayes.crossvalidation.Executions;
import br.eti.mertz.machinelearning.bayes.crossvalidation.Outcome;
import br.eti.mertz.machinelearning.bayes.dataset.Dataset;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

@Slf4j
public class Main {

    private static List<String> testSubList;

    public static void main(String args[]) {

        final BayesClassifier bayes = ((BayesClassifier<String, String>) new BayesClassifier<String, String>());

        try {

            // defined by teacher
            //10 fold cv => 24009 - 10 = 23999 => 10 folds * 2399 examples
            //first folds ends in 2399 + 10 = 2409
            ExecutionConfig config = ExecutionConfig.builder().start(10).end(24009).folds(10).build();

            List<String> negativeTexts = Dataset.index(new File("spec/IMDB/neg"), config);
            List<String> positiveTexts = Dataset.index(new File("spec/IMDB/pos"), config);

            log.info("Starting {}-fold-cv ...", config.folds());

            Executions executions = new Executions();

            for (int executionNumber = 1; executionNumber <= config.folds(); executionNumber++) {

                List<String> positiveTestTexts = getTestSubList(positiveTexts, executionNumber, config);
                List<String> positiveTrainTexts = new ArrayList<String>(positiveTexts);
                positiveTrainTexts.removeAll(positiveTestTexts);

                List<String> negativeTestTexts = getTestSubList(negativeTexts, executionNumber, config);
                List<String> negativeTrainTexts = new ArrayList<String>(negativeTexts);
                negativeTrainTexts.removeAll(negativeTestTexts);

                HashMap<String, List<String>> trainTexts = new HashMap<>();

                trainTexts.put("negative", negativeTrainTexts);
                trainTexts.put("positive", positiveTrainTexts);

                bayes.reset();

                log.info("Training {} of {} folds", executionNumber, config.folds());
                trainTexts.forEach((String category, List<String> texts) -> {
                    texts.forEach((text) -> bayes.learn(category, Arrays.asList(text.split("\\s"))));
                });

                //test
                log.info("Testing {} of {} folds", executionNumber, config.folds());
                Outcome execution = new Outcome();

                negativeTestTexts.forEach((String text) -> {
                    Classification classification = bayes.classify(
                            Arrays.asList(text.split("\\s")));
                    if (isItRight(classification, "negative"))
                        execution.incVn();
                    else execution.incFp();
                });

                positiveTestTexts.forEach((String text) -> {
                    Classification classification = bayes.classify(
                            Arrays.asList(text.split("\\s")));
                    if (isItRight(classification, "positive"))
                        execution.incVp();
                    else execution.incFn();
                });

                executions.add(execution);

                showOutcome(execution, executionNumber);
            }

            showCrossValidation(executions);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void showOutcome(Outcome execution, int executionNumber) {
        log.debug("Collision matrix:\n    Positive Negative \nPos {}     {} \nNeg {}     {}", execution.getVp(), execution.getFn(), execution.getFp(), execution.getVn());
        log.debug("Accuracy of execution number {}: {}", executionNumber, execution.getAccuracy());
        log.debug("TVP (Recall) of execution number {}: {}", executionNumber, execution.getRecall());
        log.debug("TFP of execution number {}: {}", executionNumber, execution.getTFP());
        log.debug("Medida-F of execution number {}: {}", executionNumber, execution.getFMeasure());
    }

    private static void showCrossValidation(Executions executions) {
        log.info("Mean Collision matrix:\n    Positive Negative \nPos {}     {} \nNeg {}     {}", executions.getVp(), executions.getFn(), executions.getFp(), executions.getVn());
        log.info("Accuracy: {}", executions.getAccuracy());
        log.info("TVP (Recall): {}", executions.getRecall());
        log.info("TFP: {}", executions.getTFP());
        log.info("Medida-F: {}", executions.getFMeasure());
    }

    private static boolean isItRight(Classification classification, String category) {
        return classification.getCategory().equals(category);
    }

    private static List<String> getTestSubList(List<String> list, int exec, ExecutionConfig config) {
        return list.subList(
                (exec - 1) * config.getFoldSize(),
                (exec * config.getFoldSize()) - 1);
    }
}
