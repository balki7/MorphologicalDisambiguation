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

public class NaiveBayesIGSearch {
	private static Logger logger = LogManager.getLogger(NaiveBayesIGSearch.class);

	private static final String SINGLE_IGS_FILE_NAME = "igs.txt";

	private static Classifier<String, String> bayes;

	private static void init() {
		bayes = new BayesClassifier<String, String>();
		bayes.setMemoryCapacity(50000);
	}

	public static void learn() {
		init();

		logger.info("Learn started.");
		List<String> lines = readFile();
		String previousRecord = null;
		for (String line : lines) {
			String[] detail = line.split("\\s");
			try {
				String currentRecord = detail[1];
				currentRecord = currentRecord.replaceAll("^[A-Za-zĞÜŞİÇÖğüşiçö]+[+]{1}","");
				if(previousRecord != null) {
					bayes.learn(currentRecord, Arrays.asList(previousRecord, detail[0].toLowerCase()));
				}
				previousRecord = currentRecord;
			} catch (Exception e) {
				logger.error("Ignored NB learn for : " + line, e);
			}
		}
		logger.info("Learn completed.");
	}

	public static String search(String word, String previousPos) {
		App.appendReport("Searching result in most used IGs by NB : " + word);
		logger.debug("\n\tSearching : " + word);
		String result = bayes.classify(Arrays.asList(word.toLowerCase(), previousPos)).getCategory();
		logger.debug("\n\tNaive Bayes Most Used Result : " + result);
		App.appendReport("IG result by NB : " + result.split(":")[0]);
		return result;
	}

	private static List<String> readFile() {
		List<String> lines = new ArrayList<String>();
		String filePath = null;
		try {
			filePath = new ClassPathResource(SINGLE_IGS_FILE_NAME).getFile().getAbsolutePath();
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
