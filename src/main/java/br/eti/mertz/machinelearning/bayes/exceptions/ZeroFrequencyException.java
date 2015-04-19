package br.eti.mertz.machinelearning.bayes.exceptions;

/**
 * Created by jhonnymertz on 19/04/15.
 */
public class ZeroFrequencyException extends RuntimeException {

    public ZeroFrequencyException() {
        super("Attribute does not occur in one of the classes");
    }
}
