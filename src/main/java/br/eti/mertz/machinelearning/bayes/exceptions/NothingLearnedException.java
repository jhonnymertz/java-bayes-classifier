package br.eti.mertz.machinelearning.bayes.exceptions;

public class NothingLearnedException extends RuntimeException {

    public NothingLearnedException() {
        super("Nothing learned to classify");
    }
}
