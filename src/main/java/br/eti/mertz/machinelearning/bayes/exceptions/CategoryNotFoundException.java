package br.eti.mertz.machinelearning.bayes.exceptions;

/**
 * Created by jhonnymertz on 19/04/15.
 */
public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException() {
        super("Category doesn't exists");
    }
}
