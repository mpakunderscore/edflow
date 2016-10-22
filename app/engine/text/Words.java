package engine.text;

import models.Page;
import utils.Logs;
import utils.Settings;

import java.util.*;
import java.util.regex.Matcher;

/**
 * Created by pavelkuzmin on 09/03/16.
 */
public class Words {

    public static Map<String, Integer> getSortedWords(Page page) {

        Map<String, Integer> words = getWordsMap(page);

        LinkedHashMap<String, Integer> sortedWords = (LinkedHashMap<String, Integer>) Utils.sortWords(words);

        return sortedWords;
    }

    public static Map<String, Integer> getWordsMap(Page page) {

        Map<String, Integer> words = new HashMap<>();
        List<String> wordsList = new ArrayList<>();

        Matcher matcher = Settings.wordPattern.matcher(page.text);
        while (matcher.find()) {

            String word = matcher.group().toLowerCase().trim();

            if (word.length() == 0)
                continue;

//            if (word.length() > 15) {
//                page.text = page.text.replace(word, "");
//                continue;
//            }

            if (words.containsKey(word))
                words.put(word, words.get(word) + 1);

            else
                words.put(word, 1);

            wordsList.add(word);
        }

        if (Settings.bigrams)
            setBigrams(wordsList, words);

        return words;
    }

    private static void setBigrams(List<String> wordsList, Map<String, Integer> words) {

        Map<String, Integer> bigrams = new HashMap<>();

        for (int i = 0; i < wordsList.size() - 1; i++) {

            String bigram = wordsList.get(i) + " " + wordsList.get(i + 1);

            if (bigrams.containsKey(bigram)) bigrams.put(bigram, bigrams.get(bigram) + 1);
            else bigrams.put(bigram, 1);
        }

        words.putAll(bigrams);
    }

    public static Map<String, Map<String, Double>> getWordsIDFFromPages(List<Page> pages) {

        //TODO LANGUAGE

        Map<String, List<Map<String, Integer>>> languageWords = new HashMap<>();

        Map<String, Map<String, Double>> languageWordsIDF = new HashMap<>();

        List<String> languages = new ArrayList<>();


        for (Page page : pages) {

            List<Map<String, Integer>> words = languageWords.get(page.language);

            if (words == null) {
                words = new ArrayList<>();
                languages.add(page.language);
            }

            Map<String, Integer> pageWords = Words.getSortedWords(page);
            words.add(pageWords);

            languageWords.put(page.language, words);
        }

        for (String language : languages) {

            Map<String, Double> wordsIDF = Words.getWordsIDF(languageWords.get(language));
            Logs.debug(wordsIDF.size() + " IDF words [" + language + "]");

            languageWordsIDF.put(language, wordsIDF);
        }

        return languageWordsIDF;
    }

    public static Map<String, Double> getWordsIDF(List<Map<String, Integer>> pagesWords) {

        int pagesCount = pagesWords.size();

        Map<String, Integer> wordsFrequency = new HashMap<>();
        Map<String, Double> wordsIDF = new HashMap<>();

//        Map<String, Integer> count = processTokens(pagesList, true);

        for (Map<String, Integer> pageWords : pagesWords) {

            for (Map.Entry<String, Integer> word : pageWords.entrySet()) {

                String name = word.getKey();

                if (wordsFrequency.containsKey(name))
                    wordsFrequency.put(name, (wordsFrequency.get(name) + 1));

                else
                    wordsFrequency.put(name, 1);
            }
        }

        for (Map.Entry<String, Integer> word : wordsFrequency.entrySet()) {

            String name = word.getKey();

            wordsIDF.put(name, Math.log((double) pagesCount / (double) wordsFrequency.get(name)));
        }

        wordsIDF = Utils.sortWordsDouble(wordsIDF);

        return wordsIDF;
    }
}
