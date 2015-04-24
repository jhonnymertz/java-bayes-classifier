package br.eti.mertz.machinelearning.bayes.exceptions;

public class NothingLearnedException extends RuntimeException {

    public NothingLearnedException() {
        super("Can't classify anything. Nothing learned.");
    }
}
