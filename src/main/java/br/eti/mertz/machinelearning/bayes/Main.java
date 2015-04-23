package br.eti.mertz.machinelearning.bayes;

import br.eti.mertz.machinelearning.bayes.classifier.BayesClassifier;
import br.eti.mertz.machinelearning.bayes.classifier.Classification;
import br.eti.mertz.machinelearning.bayes.crossvalidation.Executions;
import br.eti.mertz.machinelearning.bayes.crossvalidation.Outcome;
import br.eti.mertz.machinelearning.bayes.dataset.Dataset;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

                List<String> positiveTextsTest = getTestSubList(positiveTexts, executionNumber, config);
                List<String> positiveTextsTrain = new ArrayList<String>(positiveTexts);
                positiveTextsTrain.removeAll(positiveTextsTest);

                List<String> negativeTextsTest = getTestSubList(negativeTexts, executionNumber, config);
                List<String> negativeTextsTrain = new ArrayList<String>(negativeTexts);
                negativeTextsTrain.removeAll(negativeTextsTest);

                bayes.reset();



                //train
                log.info("Training {} of {} folds", executionNumber, config.folds());
                for (String text : negativeTextsTrain)
                        bayes.learn("negative", Arrays.asList(text.split("\\s")));
                for (String text : positiveTextsTrain)
                        bayes.learn("positive", Arrays.asList(text.split("\\s")));



                //test
                log.info("Testing {} of {} folds", executionNumber, config.folds());
                Outcome execution = new Outcome();
                for (String text : negativeTextsTest) {
                    Classification classification = bayes.classify(
                            Arrays.asList(text.split("\\s")));
                    if (isItRight(classification, "negative"))
                        execution.incVn();
                    else execution.incFp();
                }
                for (String text : positiveTextsTest) {
                    Classification classification = bayes.classify(
                            Arrays.asList(text.split("\\s")));
                    if (isItRight(classification, "positive"))
                        execution.incVp();
                    else execution.incFn();
                }

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
        log.debug("Error of execution number {}: {}", executionNumber, execution.getError());
    }

    private static void showCrossValidation(Executions executions) {
        log.info("Mean Collision matrix:\n    Positive Negative \nPos {}     {} \nNeg {}     {}", executions.getVp(), executions.getFn(), executions.getFp(), executions.getVn());
        log.info("Accuracy of execution number: {}", executions.getAccuracy());
        log.info("TVP (Recall): {}", executions.getRecall());
        log.info("TFP: {}", executions.getTFP());
        log.info("Medida-F: {}", executions.getFMeasure());
    }

    private static boolean isItRight(Classification classification, String category) {
        return classification.getCategory().equals(category);
    }

    public static List<String> getTestSubList(List<String> list, int exec, ExecutionConfig config) {
        return list.subList(
                (exec - 1) * config.getFoldSize(),
                (exec * config.getFoldSize()) - 1);
    }
}
