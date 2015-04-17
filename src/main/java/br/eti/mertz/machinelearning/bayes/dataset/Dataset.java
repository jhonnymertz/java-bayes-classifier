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

    public List<String> index(File file) throws IOException {

        List<String> filesRead = new ArrayList<String>();

        if(file.isDirectory()) {
            File[] files = file.listFiles();
            int length = files.length;
            LOG.info("Files to index in {}: {}", file.getAbsolutePath(), length);

            for(int i = 0; i < length; i++) {
                    FileInputStream f = new FileInputStream(files[i]);
                    FileChannel ch = f.getChannel();
                    MappedByteBuffer mbb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, ch.size());
                    while (mbb.hasRemaining()) {
                        CharBuffer cb = Charset.forName("UTF-8").decode(mbb);
                        filesRead.add(Filter.filter(cb.toString()));
                    }
                    f.close();

                    LOG.debug("{} files indexed of {} to train", i, length);
                }
        }

        return filesRead;
    }

}
