package br.eti.mertz.machinelearning.bayes.exceptions;

public class ZeroFrequencyException extends RuntimeException {

    public ZeroFrequencyException() {
        super("Attribute does not occur in one of the classes");
    }
}
