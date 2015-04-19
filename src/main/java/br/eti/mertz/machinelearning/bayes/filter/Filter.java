package br.eti.mertz.machinelearning.bayes.filter;

import org.jsoup.Jsoup;

/**
 * Created by jhonnymertz on 16/04/15.
 */
public class Filter {

    public static String filter(String s) {
        return Jsoup.parse(s).text()
                .toLowerCase()
                .replaceAll("[^a-z]", " ")
                .replaceAll("\\s+", " ").trim()
                .replaceAll("\\b\\w{1,4}\\b\\s?", "")
                //.replaceAll("\\.", "")
                //.replaceAll("^\\s*[\\da-zA-Z][\\da-zA-Z\\s]*$", "")
                //.replaceAll("[^\\w ]+$", "")
                ;
    }
}
