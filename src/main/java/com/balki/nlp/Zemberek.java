package com.balki.nlp;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import zemberek.morphology.ambiguity.Z3MarkovModelDisambiguator;
import zemberek.morphology.analysis.SentenceAnalysis;
import zemberek.morphology.analysis.WordAnalysis;
import zemberek.morphology.analysis.tr.TurkishMorphology;
import zemberek.morphology.analysis.tr.TurkishSentenceAnalyzer;

import java.io.IOException;
import java.util.*;

public class Zemberek {
    private static Logger logger = LogManager.getLogger(Zemberek.class);
    private static TurkishMorphology morphology;
    private static TurkishSentenceAnalyzer sentenceAnalyzer;

    public static void init() throws IOException {
        morphology = TurkishMorphology.builder().addDefaultDictionaries().build();
        Z3MarkovModelDisambiguator disambiguator = new Z3MarkovModelDisambiguator();
        sentenceAnalyzer = new TurkishSentenceAnalyzer(morphology, disambiguator);
    }

    public static void analyzeIG(String sentence) {
        logger.info("Will analyze : " + sentence);

        Map<String, Set<String>> rootsMap = new HashMap<String, Set<String>>();
        SentenceAnalysis sentenceAnalysis = sentenceAnalyzer.analyze(sentence);
        sentenceAnalyzer.disambiguate(sentenceAnalysis);

        for (SentenceAnalysis.Entry entry : sentenceAnalysis) {
            rootsMap.put(entry.input, new HashSet<String>());
            for(WordAnalysis wordAnalysis : entry.parses){
                rootsMap.get(entry.input).add(wordAnalysis.toString());
            }
        }

        for(String word : rootsMap.keySet()){
            System.out.println("\nWord : " + word);
            if(rootsMap.get(word).isEmpty()){
                System.out.println("\tNo IG");
            }
            else if(rootsMap.get(word).size() == 1){
                Iterator<String> rootsIterator = rootsMap.get(word).iterator();
                System.out.println("\tIG : " + rootsIterator.next());
            }
            else {
                System.out.print("\tMultiple IGs : ");
                Iterator<String> rootsIterator = rootsMap.get(word).iterator();
                while(rootsIterator.hasNext()) {
                    System.out.print(rootsIterator.next() + " ");
                }
            }
        }
    }

    public static Map<String, Set<String>> analyzeRoot(String sentence) {
        logger.info("Will analyze : " + sentence);

        Map<String, Set<String>> rootsMap = new LinkedHashMap<String, Set<String>>();
        SentenceAnalysis sentenceAnalysis = sentenceAnalyzer.analyze(sentence);
        sentenceAnalyzer.disambiguate(sentenceAnalysis);

        for (SentenceAnalysis.Entry entry : sentenceAnalysis) {
            rootsMap.put(entry.input, new HashSet<String>());
            for(WordAnalysis wordAnalysis : entry.parses){
                rootsMap.get(entry.input).add(wordAnalysis.root);
            }
        }

        return rootsMap;
    }
}
