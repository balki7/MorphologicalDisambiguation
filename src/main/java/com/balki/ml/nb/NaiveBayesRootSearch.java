package com.balki.ml.nb;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.datavec.api.util.ClassPathResource;

import com.balki.App;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class NaiveBayesRootSearch {
    private static Logger logger = LogManager.getLogger(NaiveBayesRootSearch.class);

    private static final String SINGLE_ROOTS_FILE_NAME = "roots.txt";

    private static Classifier<String, String> bayes;

    private static void init(){
        bayes = new BayesClassifier<String, String>();
        bayes.setMemoryCapacity(50000);
    }

    public static void learn(){
        init();

        logger.info("Learn started.");
        List<String> lines = readFile();
        for(String line : lines){
            String[] detail = line.split(",");
            bayes.learn(detail[1], Arrays.asList(detail[0].toLowerCase()));
        }
        logger.info("Learn completed.");
    }

    public static String search(String word){
        App.appendReport("Searching result in most used IGs by NB : " + word);
        logger.debug("\n\tSearching : " + word);
    	String result = bayes.classify(Arrays.asList(word.toLowerCase())).getCategory();
        logger.debug("\n\tNaive Bayes Most Used Result : " + result);
        App.appendReport("IG result by NB : " + result);
        return result;
    }

    private static List<String> readFile(){
        List<String> lines = new ArrayList<String>();
        String filePath = null;
        try {
			filePath = new ClassPathResource(SINGLE_ROOTS_FILE_NAME).getFile().getAbsolutePath();
		} catch (FileNotFoundException e) {
			logger.error("File read error", e);
			return lines;
		}
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String sCurrentLine;
            while ((sCurrentLine = br.readLine()) != null) {
                lines.add(sCurrentLine);
            }

        } catch (IOException e) {
            logger.error("Read file error", e);
        }

        return lines;
    }
}
