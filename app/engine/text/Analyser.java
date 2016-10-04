package engine.text;

import models.Page;
import utils.Logs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analyser {

    public static void processPages(List<Page> pages) {

        long time = System.currentTimeMillis();

        //global idf

        List<Map<String, Integer>> words = new ArrayList<>();

        for (Page page : pages) {

            Map<String, Integer> pageWords = Parser.getSortedWords(page);
            words.add(pageWords);
        }

        Map<String, Double> wordsIDF = Parser.getWordsIDF(words);

        Logs.debug(wordsIDF.size() + " IDF words");



        //domain idf

        Map<String, List<Map<String, Integer>>> domainsPagesWords = new HashMap<>();

        for (Page page : pages) {

            String domainName = getDomain(page.url);
            Map<String, Integer> pageWords = Parser.getSortedWords(page);

            if (domainsPagesWords.containsKey(domainName))
                domainsPagesWords.get(domainName).add(pageWords);

            else {
                List<Map<String, Integer>> domainPagesWords = new ArrayList<>();
                domainPagesWords.add(pageWords);
                domainsPagesWords.put(domainName, domainPagesWords);
            }
        }

        Map<String, Map<String, Double>> domainsWordsIDF = new HashMap<>();

        Logs.debug("Domain words");

        for (Map.Entry domainPagesWords : domainsPagesWords.entrySet()) {

            Map<String, Double> domainWordsIDF = Parser.getWordsIDF((List<Map<String, Integer>>) domainPagesWords.getValue());
            domainsWordsIDF.put((String) domainPagesWords.getKey(), domainWordsIDF);

            Map<String, Double> sortedProcessedDomainWords = Parser.sortWordsDoubleBack(domainWordsIDF);

            Logs.out("http://" + (String) domainPagesWords.getKey() + " " + ((List<Map<String,Integer>>) domainPagesWords.getValue()).size());
            Logs.first(sortedProcessedDomainWords, 4);
        }

        //page words

        for (Page page : pages) {

            Map<String, Integer> pageWords = Parser.getSortedWords(page);
            Map<String, Double> processedPageWords = new HashMap<>();
            Map<String, Double> processedDomainPageWords = new HashMap<>();

            for (Map.Entry<String, Integer> word : pageWords.entrySet()) {

                processedPageWords.put(word.getKey(), word.getValue() * wordsIDF.get(word.getKey()));
            }

            Map<String, Double> sortedProcessedPageWords = Parser.sortWordsDouble(processedPageWords);

            for (Map.Entry<String, Integer> word : pageWords.entrySet()) {

                processedDomainPageWords.put(word.getKey(), word.getValue() * wordsIDF.get(word.getKey()) * domainsWordsIDF.get(getDomain(page.url)).get(word.getKey()));
            }

            Map<String, Double> sortedProcessedDomainPageWords = Parser.sortWordsDouble(processedDomainPageWords);

//            Logs.debug(page.title + " " + page.url);
//            Logs.first(sortedProcessedPageWords, 10);
//            Logs.first(sortedProcessedDomainPageWords, 10);
//            Logs.out("--");
        }

        Logs.time(time);
    }

    public static String getDomain(String url) {
        return url.replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];
    }
}
