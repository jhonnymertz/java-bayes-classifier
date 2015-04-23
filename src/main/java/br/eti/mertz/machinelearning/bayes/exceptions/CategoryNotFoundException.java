package br.eti.mertz.machinelearning.bayes.exceptions;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException() {
        super("Category doesn't exists");
    }
}
