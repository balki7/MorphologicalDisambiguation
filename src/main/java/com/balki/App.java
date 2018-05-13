package com.balki;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.balki.gui.AppGUI;
import com.balki.ml.nb.NaiveBayesIGSearch;
import com.balki.ml.nb.NaiveBayesRootSearch;
import com.balki.ml.word2vec.Word2VecRootSearch;
import com.balki.nlp.Zemberek;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

public class App {
	private static Logger logger = LogManager.getLogger(App.class);
	private static AppGUI gui;

	public static void main(String[] args) {
		gui = new AppGUI();
		gui.show();
		appendReport("Please wait...");

		try {
			Zemberek.init();
		} catch (IOException e) {
			logger.error("Zemberek init error.", e);
		}

		appendReport("Naive Bayes learn for roots started.");
		NaiveBayesRootSearch.learn();
		appendReport("Naive Bayes learn for roots completed.");
		appendReport("Naive Bayes learn for IGs started.");
		NaiveBayesIGSearch.learn();
		appendReport("Naive Bayes learn for IGs completed.");
		
		appendReport("Word2Vec learn for roots started.");
		Word2VecRootSearch.learn();
		appendReport("Word2Vec learn for roots completed.");
		
		start();
	}

	private static void start() {
		appendReport("Ready to use.");
		gui.ready();
		Scanner scanner = new Scanner(System.in);
		appendReport("Enter a sentence");
		analyze(scanner.nextLine());
		start();
	}

	public static void analyze(String sentence) {
		appendReport("Analyzing Root");
		Map<String, Set<String>> rootsMap = Zemberek.analyzeRoot(sentence);

		int index = 0;
		gui.appendRootResult("Root Analysis Result\n--------------------");
		for (String word : rootsMap.keySet()) {
			appendReport("Word : " + word);
			if (rootsMap.get(word).isEmpty()) {
				appendReport("\tNo Roots");
				gui.appendRootResult(word + " : " + "Not Found");
			} else if (rootsMap.get(word).size() == 1) {
				Iterator<String> rootsIterator = rootsMap.get(word).iterator();
				String root = rootsIterator.next();
				appendReport("\tRoot : " + root);
				gui.appendRootResult(word + " : " + root);
			} else {
				appendReport("\tMultiple Roots : ");
				Iterator<String> rootsIterator = rootsMap.get(word).iterator();
				while (rootsIterator.hasNext()) {
					appendReport("\t\t" + rootsIterator.next() + " ");
				}

				if (rootsMap.keySet().size() == 1) {
					String result = NaiveBayesRootSearch.search(word);
					if (result == null) {
						result = Word2VecRootSearch.search(rootsMap.keySet().toArray()[index].toString(),
								rootsMap.get(word));
					}
					appendReport("\tRoot : " + result);
					gui.appendRootResult(word + " : " + result);
				} else {
					String result = Word2VecRootSearch.search(rootsMap.keySet().toArray()[index].toString(),
							rootsMap.get(word));
					if (result == null) {
						result = NaiveBayesRootSearch.search(word);
					}
					appendReport("\tRoot : " + result);
					gui.appendRootResult(word + " : " + result);
				}
			}
			index++;
		}
	}
	
	public static void appendReport(String text) {
		gui.appendReport(text);
	}
}
