package engine.text;

import com.avaje.ebean.Ebean;
import models.Page;
import utils.Logs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;

public class Analyser {

    public static void processPages(List<Page> pages) {

        long time = System.currentTimeMillis();

        //TODO global words idf

            List<Map<String, Integer>> words = new ArrayList<>();

            for (Page page : pages) {

                Map<String, Integer> pageWords = Parser.getSortedWords(page);
                words.add(pageWords);
            }

            Map<String, Double> wordsIDF = Parser.getWordsIDF(words);

            Logs.debug(wordsIDF.size() + " IDF words");


        //TODO domain words idf
        Map<String, List<Map<String, Integer>>> domainsPagesWords = getDomainsPagesWords(pages);
        Map<String, Map<String, Double>> domainsWordsIDF = processDomains(domainsPagesWords);



        //TODO page words
        for (Page page : pages) {

            Map<String, Integer> pageWords = Parser.getSortedWords(page);

            Map<String, Double> processedPageWords = new HashMap<>();
            Map<String, Double> processedDomainPageWords = new HashMap<>();


            //processedPageWords
            for (Map.Entry<String, Integer> word : pageWords.entrySet()) {

                Double wordIDF = 1.0;
                if (wordsIDF.containsKey(word.getKey()))
                    wordIDF = wordsIDF.get(word.getKey());

                processedPageWords.put(word.getKey(), word.getValue() * wordIDF);
            }

            Map<String, Double> sortedProcessedPageWords = Parser.sortWordsDouble(processedPageWords);

            //processedDomainPageWords
            for (Map.Entry<String, Integer> word : pageWords.entrySet()) {

                Double domainWordIDF = 1.0;
                Map domainWordsIDF = domainsWordsIDF.get(getDomain(page.url));

                if (domainWordsIDF != null && domainWordsIDF.containsKey(word.getKey())) {
                    domainWordIDF = (Double) domainWordsIDF.get(word.getKey());
                }

                Double wordIDF = 1.0;
                if (wordsIDF.containsKey(word.getKey()))
                    wordIDF = wordsIDF.get(word.getKey());

                processedDomainPageWords.put(word.getKey(), word.getValue() * wordIDF * domainWordIDF);
            }

            Map<String, Double> sortedProcessedDomainPageWords = Parser.sortWordsDouble(processedDomainPageWords);

            Logs.debug(page.title);
            Logs.debug(page.url);
            Logs.debug(page.text.length() + " / " + pageWords.size());

            Logs.debug("Processed page words");
            Logs.first(sortedProcessedPageWords, 10);

            Logs.debug("With domain and, pages count: " + domainsPagesWords.get(getDomain(page.url)).size());
            Logs.first(sortedProcessedDomainPageWords, 10);


            page.categories = String.join(",", getLastTen(sortedProcessedDomainPageWords));
            page.text = checkText(page.text);
        }

        Logs.time("Process", time);

        pages.forEach(Ebean::save);
        Logs.time("Save", time);
    }

    private static String checkText(String text) {

        for (String word : text.split(" ")) {

            word = word.trim();

            if (word.length() > 15) {
                text = text.replace(word, "");
            }
        }

        return text;
    }

    private static List<String> getLastTen(Map<String, Double> sortedProcessedDomainPageWords) {

        List<String> categories = new ArrayList<>();

        int i = 0;
        for (Map.Entry word : sortedProcessedDomainPageWords.entrySet()) {

            categories.add((String) word.getKey());

            i++;
            if (i == 10)
                break;
        }

        return categories;
    }

    private static Map<String, Map<String, Double>> processDomains(Map<String, List<Map<String, Integer>>> domainsPagesWords) {

        Map<String, Map<String, Double>> domainsWordsIDF = new HashMap<>();

        for (Map.Entry domainPagesWords : domainsPagesWords.entrySet()) {

//            Logs.out(domainPagesWords.getKey() + " -> " + ((List) domainPagesWords.getValue()).size());

            int domainPagesSize = ((List<Map<String, Integer>>) domainPagesWords.getValue()).size();

            if (domainPagesSize > 1) {

                Map<String, Double> domainWordsIDF = Parser.getWordsIDF((List<Map<String, Integer>>) domainPagesWords.getValue());
                domainsWordsIDF.put((String) domainPagesWords.getKey(), domainWordsIDF);

                Map<String, Double> sortedProcessedDomainWords = Parser.sortWordsDoubleBack(domainWordsIDF);

                Logs.out("http://" + domainPagesWords.getKey() + " " + domainPagesSize);
                Logs.first(sortedProcessedDomainWords, 4);
            }
        }

        return domainsWordsIDF;
    }

    public static String getDomain(String url) {
        return url.replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];
    }

    public static Map<String,List<Map<String,Integer>>> getDomainsPagesWords(List<Page> pages) {

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

        return domainsPagesWords;
    }
}
