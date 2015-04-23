package br.eti.mertz.machinelearning.bayes.dataset;

import br.eti.mertz.machinelearning.bayes.ExecutionConfig;
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
import java.util.List;

public class Dataset {

    static final Logger LOG = LoggerFactory.getLogger(Dataset.class);

    public static List<String> index(File directory, ExecutionConfig config) throws IOException {

        List<String> filesRead = new ArrayList<String>();

        if (directory.isDirectory()) {

            int length = config.getAmountFiles();
            LOG.info("Files to index in {}: {}", directory.getAbsolutePath(), length);

            File[] files = directory.listFiles();

            for(int i = config.start(); i <= config.end(); i++){
                FileInputStream f = new FileInputStream(files[i]);
                FileChannel ch = f.getChannel();
                MappedByteBuffer mbb = ch.map(FileChannel.MapMode.READ_ONLY, 0L, ch.size());
                while (mbb.hasRemaining()) {
                    CharBuffer cb = Charset.forName("UTF-8").decode(mbb);
                    filesRead.add(filter(cb.toString()));
                }
                f.close();

                LOG.debug("{} indexed. Status: {} files indexed of {} to train", files[i].getName(), i, length);
            }
        }

        return filesRead;
    }

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
