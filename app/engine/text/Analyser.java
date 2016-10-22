package engine.text;

import com.avaje.ebean.Ebean;
import models.Page;
import utils.Logs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Analyser {

    public static void processPages(List<Page> pages) {

        long time = System.currentTimeMillis();

        //global words idf TODO LANGUAGE
        Map<String, Map<String, Double>> wordsIDF = Words.getWordsIDFFromPages(pages);

        //domain words idf
        Map<String, List<Map<String, Integer>>> domainsPagesWords = getDomainsPagesWords(pages);
        Map<String, Map<String, Double>> domainsWordsIDF = processDomainsIDF(domainsPagesWords);

        //page words
        for (Page page : pages) {
            processPage(page, wordsIDF.get(page.language), domainsWordsIDF);
        }

        Logs.time("Process Analyser", time);

        pages.forEach(Ebean::update);
        Logs.time("Save", time);
    }

    private static Map<String, Map<String, Double>> processDomainsIDF(Map<String, List<Map<String, Integer>>> domainsPagesWords) {

        Map<String, Map<String, Double>> domainsWordsIDF = new HashMap<>();

        for (Map.Entry domainPagesWords : domainsPagesWords.entrySet()) {

//            Logs.out(domainPagesWords.getKey() + " -> " + ((List) domainPagesWords.getValue()).size());

            int domainPagesSize = ((List<Map<String, Integer>>) domainPagesWords.getValue()).size();

            if (domainPagesSize > 1) {

                Map<String, Double> domainWordsIDF = Words.getWordsIDF((List<Map<String, Integer>>) domainPagesWords.getValue());
                domainsWordsIDF.put((String) domainPagesWords.getKey(), domainWordsIDF);

                Map<String, Double> sortedProcessedDomainWords = Utils.sortWordsDoubleBack(domainWordsIDF);

                Logs.out("http://" + domainPagesWords.getKey() + " " + domainPagesSize);
                Logs.first(sortedProcessedDomainWords, 5);
            }
        }

        return domainsWordsIDF;
    }

    public static Map<String, List<Map<String, Integer>>> getDomainsPagesWords(List<Page> pages) {

        Map<String, List<Map<String, Integer>>> domainsPagesWords = new HashMap<>();

        for (Page page : pages) {

            String domainName = Utils.getDomain(page.url);
            Map<String, Integer> pageWords = Words.getSortedWords(page);

            if (domainsPagesWords.containsKey(domainName))
                domainsPagesWords.get(domainName).add(pageWords);

            else {

                List<Map<String, Integer>> domainPagesWords = new ArrayList<>();
                domainPagesWords.add(pageWords);
                domainsPagesWords.put(domainName, domainPagesWords);
            }
        }

        return domainsPagesWords;
    }

    public static void processPage(Page page, Map<String, Double> wordsIDF, Map<String, Map<String, Double>> domainsWordsIDF) {

        Map<String, Integer> pageWords = Words.getSortedWords(page);
        Map<String, Double> processedPageWords = new HashMap<>();
        Map<String, Double> processedDomainPageWords = new HashMap<>();


        //processedPageWords
        for (Map.Entry<String, Integer> word : pageWords.entrySet()) {

            Double wordIDF = 1.0;
            if (wordsIDF.containsKey(word.getKey()))
                wordIDF = wordsIDF.get(word.getKey());

            double value = word.getValue() * wordIDF;
            if (value > 1.0)
                processedPageWords.put(word.getKey(), value);
        }

        Map<String, Double> sortedProcessedPageWords = Utils.sortWordsDouble(processedPageWords);

        //processedDomainPageWords
        for (Map.Entry<String, Integer> word : pageWords.entrySet()) {

            Double domainWordIDF = 1.0;
            Map domainWordsIDF = domainsWordsIDF.get(Utils.getDomain(page.url));

            if (domainWordsIDF != null && domainWordsIDF.containsKey(word.getKey())) {
                domainWordIDF = (Double) domainWordsIDF.get(word.getKey());
            }

            Double wordIDF = 1.0;
            if (wordsIDF.containsKey(word.getKey()))
                wordIDF = wordsIDF.get(word.getKey());

            double value = word.getValue() * wordIDF * domainWordIDF;
            if (value > 1.0)
                processedDomainPageWords.put(word.getKey(), value);
        }

        Map<String, Double> sortedProcessedDomainPageWords = Utils.sortWordsDouble(processedDomainPageWords);

        Logs.debug(page.title);
        Logs.debug(page.url);
        Logs.debug(page.text.length() + " / " +
                    pageWords.size() + " / " +
                    sortedProcessedPageWords.size() + " / " +
                    sortedProcessedDomainPageWords.size());

        Logs.debug("Processed page words");
        Logs.first(sortedProcessedPageWords, 10);

        Logs.debug("Processed page words with domain");
        Logs.first(sortedProcessedDomainPageWords, 10);

        page.categories = String.join(",", Utils.getLastTen(sortedProcessedDomainPageWords));
        page.text = Utils.checkText(page.text);
    }
}
