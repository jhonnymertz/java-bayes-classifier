package br.eti.mertz.machinelearning.bayes.crossvalidation;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;

public class Executions extends Outcome {

    private List<Outcome> outcomes;

    public Executions(){
        super();
        outcomes = new ArrayList<Outcome>();
    }

    public Executions(List<Outcome> outcomes){
        super();
        this.outcomes = outcomes;
        updateMeans();
    }

    private void updateMeans(){
        try {
            setMeanField("Fn");
            setMeanField("Fp");
            setMeanField("Vn");
            setMeanField("Vp");
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    private void setMeanField(String field)
            throws NoSuchMethodException, InvocationTargetException, IllegalAccessException{
        float meanField = 0;
        for(Outcome er : outcomes) {
            meanField += Float.valueOf(er.getClass().getMethod("get" + field).invoke(er).toString());
        }

        this.getClass().getMethod("set" + field, float.class).invoke(this, meanField / outcomes.size());
    }

    public void add(Outcome er){
        outcomes.add(er);
        updateMeans();
    }
}
