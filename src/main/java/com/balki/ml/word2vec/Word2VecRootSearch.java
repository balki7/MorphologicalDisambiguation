package com.balki.ml.word2vec;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.deeplearning4j.models.word2vec.Word2Vec;
import org.deeplearning4j.text.sentenceiterator.BasicLineIterator;
import org.deeplearning4j.text.sentenceiterator.SentenceIterator;
import org.deeplearning4j.text.tokenization.tokenizer.preprocessor.CommonPreprocessor;
import org.deeplearning4j.text.tokenization.tokenizerfactory.DefaultTokenizerFactory;
import org.deeplearning4j.text.tokenization.tokenizerfactory.TokenizerFactory;

import com.balki.App;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class Word2VecRootSearch {
	private static Logger logger = LogManager.getLogger(Word2VecRootSearch.class);

	private static final String SINGLE_ROOTS_FILE_NAME = "/Users/balki/eclipse-workspace/MorphologicalDisambiguation/gazete-root.txt";

	private static Word2Vec vec;

	public static void learn() {
		logger.debug("Learn started.");
		List<String> lines = readFile();
		StringBuffer sb = new StringBuffer();
		for (String line : lines) {
			sb.append("\n");
			sb.append(line);
		}

		logger.debug("Load & Vectorize Sentences....");
		InputStream stream = new ByteArrayInputStream(sb.toString().getBytes(StandardCharsets.UTF_8));
		SentenceIterator iter = new BasicLineIterator(stream);
		TokenizerFactory t = new DefaultTokenizerFactory();
		t.setTokenPreProcessor(new CommonPreprocessor());

		logger.debug("Building model....");
		vec = new Word2Vec.Builder().minWordFrequency(3).iterations(1).layerSize(100).seed(42).windowSize(3)
				.iterate(iter).tokenizerFactory(t).build();

		logger.debug("Fitting Word2Vec model....");
		vec.fit();

		logger.debug("Writing word vectors to text file....");

		logger.debug("Learn completed.");
	}

	public static String search(String word, Set<String> mayBeList) {
        App.appendReport("Searching result in root word2vec : " + word);
		logger.debug("Word : " + word + " Search List : " + mayBeList);
		String result = null;
		double maxResult = 0;
		for (String mayBe : mayBeList) {
			double similarity = vec.similarity(word, mayBe);
			logger.debug("Similarity : " + word + " - " + mayBe + " : " + similarity);
	        App.appendReport("Root similarity by Word2Vec : " + word + " - " + mayBe + " : " + similarity);
			if (similarity > maxResult) {
				maxResult = similarity;
				result = mayBe;
			}
		}
		logger.debug("Word2Vec Result : " + result);
        App.appendReport("Root result by Word2Vec : " + result);
		return result;
	}

	private static List<String> readFile() {
		List<String> lines = new ArrayList<String>();
		try (BufferedReader br = new BufferedReader(new FileReader(SINGLE_ROOTS_FILE_NAME))) {
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
