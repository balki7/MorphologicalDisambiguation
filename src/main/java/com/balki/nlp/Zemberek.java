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
    
    public static Map<String, Set<String>> analyzeIG(String sentence) {
        logger.info("Will analyze : " + sentence);

        Map<String, Set<String>> igsMap = new LinkedHashMap<String, Set<String>>();
        SentenceAnalysis sentenceAnalysis = sentenceAnalyzer.analyze(sentence);
        sentenceAnalyzer.disambiguate(sentenceAnalysis);

        for (SentenceAnalysis.Entry entry : sentenceAnalysis) {
        	igsMap.put(entry.input, new HashSet<String>());
            for(WordAnalysis wordAnalysis : entry.parses){
            	igsMap.get(entry.input).add(wordAnalysis.formatOnlyIgs());
            }
        }

        return igsMap;
    }
}
