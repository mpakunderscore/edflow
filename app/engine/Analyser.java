package engine;

import models.Page;
import utils.Logs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analyser {

    public static void processPages(List<Page> pages) {

        long time = System.currentTimeMillis();

        List<Map<String, Integer>> words = new ArrayList<>();

        for (Page page : pages) {

            Map<String, Integer> pageWords = Parser.getSortedWords(page);
            words.add(pageWords);
        }

        Map<String, Double> wordsIDF = Parser.getWordsIDF(words);

        for (Page page : pages) {

            Map<String, Integer> pageWords = Parser.getSortedWords(page);
            Map<String, Double> processedPageWords = new HashMap<>();

            for (Map.Entry<String, Integer> word : pageWords.entrySet()) {

                processedPageWords.put(word.getKey(), word.getValue() * wordsIDF.get(word.getKey()));
            }

            Map<String, Double> sortedProcessedPageWords = Parser.sortWordsDouble(processedPageWords);

//            sortedProcessedPageWords.keySet().forEach();

//            page.categories =

            Logs.out(page.title + " " + page.url);
            Logs.first(sortedProcessedPageWords, 10);
        }

        Logs.time(time);
    }
}
