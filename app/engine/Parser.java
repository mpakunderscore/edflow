package engine;

import models.Page;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * Created by pavelkuzmin on 09/03/16.
 */
public class Parser {

    private final static Pattern wordPattern = Pattern.compile("[^\\s+\"\\d+(){}, –'\\-=_@:$;#%!<>&\\|\\*\\?\\[\\]\\.\\/\\+\\\\]{2,}");

    private final static boolean bigrams = true;

    public static Map<String, Integer> getWordsMap(String text) {

        Map<String, Integer> words = new HashMap<>();
        List<String> wordsList = new ArrayList<>();

        Matcher matcher = wordPattern.matcher(text);
        while (matcher.find()) {

            String word = matcher.group().toLowerCase();

            if (words.containsKey(word)) words.put(word, words.get(word) + 1);
            else words.put(word, 1);

            wordsList.add(word);
        }

        if (bigrams)
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

    public static Map<String, Integer> sortWords(Map<String, Integer> words) {

        LinkedHashMap<String, Integer> sortedWords = words
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return sortedWords;
    }

    public static Map<String, Double> sortWordsDouble(Map<String, Double> words) {

        LinkedHashMap<String, Double> sortedWords = words
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return sortedWords;
    }

    public static Map<String, Double> sortWordsDoubleBack(Map<String, Double> words) {

        LinkedHashMap<String, Double> sortedWords = words
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return sortedWords;
    }

    public static Map<String, Integer> getSortedWords(Page page) {

        String text = page.text;

//        int textLength = text.length();
//        String[] textArray = text.split("\\s+|,\\s+|/.\\s+");
//        int wordsCount = textArray.length;

        Map<String, Integer> words = getWordsMap(text);

        LinkedHashMap<String, Integer> sortedWords = (LinkedHashMap<String, Integer>) sortWords(words);

//        Logs.out(page.title);
//        Logs.first(sortedWords, 10);

        return sortedWords;
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

        wordsIDF = sortWordsDouble(wordsIDF);

        return wordsIDF;
    }
}
