package br.eti.mertz.machinelearning.bayes.exceptions;

/**
 * Created by jhonnymertz on 19/04/15.
 */
public class NothingLearnedException extends RuntimeException {

    public NothingLearnedException() {
        super("Nothing learned to classify");
    }
}
