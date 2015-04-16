package br.eti.mertz.machinelearning.bayes.dataset;

import br.eti.mertz.machinelearning.bayes.classifier.BayesClassifier;
import br.eti.mertz.machinelearning.bayes.classifier.Classifier;
import br.eti.mertz.machinelearning.bayes.filter.Filter;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by jhonnymertz on 15/04/15.
 */
public class Dataset {

    static final Logger LOG = LoggerFactory.getLogger(Dataset.class);

    public List<String> index(File file, String category, Classifier<String, String> bayes) throws IOException {

        if(file.isDirectory()) {
            File[] files = file.listFiles();
            int length = files.length;
            LOG.debug("Files to index in {}: {} with category: {}", file.getAbsolutePath(), length, category);

            int start = 10;
            int end = 24009;

            //10 fold cv => 24009 - 10 = 23999 => 10 folds * 2399 examples
            //first folds ends in 2399 + 10 = 2409
            int folds = 10;

            //executions
            for(int exec = 1; exec <= folds; exec++){

                //train
                for(int i = start; i < end; i++) {

                    if((i < (exec - 1) * (end / folds)) &&
                       (i > (exec) * (end / folds))) {


                        FileInputStream f = new FileInputStream(files[i]);
                        FileChannel ch = f.getChannel();
                        MappedByteBuffer mbb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, ch.size());
                        while (mbb.hasRemaining()) {
                            CharBuffer cb = Charset.forName("UTF-8").decode(mbb);
                            bayes.learn(category, Arrays.asList(Filter.filter(cb.toString()).split("\\s")));
                        }
                        f.close();

                        //String fileText = FileUtils.readFileToString(files[i]);

                        LOG.debug("{} files indexed of {} from {} category to train", i, length, category);
                    }
                }


                List<String> test = new ArrayList<String>();

                //test
                for(int i = ((exec - 1) * (end / folds)) == 0 ? start : ((exec - 1) * (end / folds));
                    i <= ((exec) * (end / folds));
                    i++) {

                    FileInputStream f = new FileInputStream(files[i]);
                    FileChannel ch = f.getChannel();
                    MappedByteBuffer mbb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, ch.size());
                    while (mbb.hasRemaining()) {
                        CharBuffer cb = Charset.forName("UTF-8").decode(mbb);
                        test.add(Filter.filter(cb.toString()));
                    }
                    f.close();

                    LOG.debug("{} files indexed of {} from {} category to test", i, length, category);
                }
            }
        }
        else {
            LOG.debug("File to index: {}/{}", file.getAbsolutePath(), file.getName());
            String fileText = FileUtils.readFileToString(file);
            bayes.learn(category, Arrays.asList(fileText.split("\\s")));
        }

    }

}
