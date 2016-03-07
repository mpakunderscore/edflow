package engine;

import com.avaje.ebean.Ebean;
import models.Page;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import play.mvc.Controller;
import utils.Logs;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Crawler extends Controller {

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36";

    public static Page getPage(String url) {

        Logs.out(url);

        Page page = null;

        page = Ebean.find(Page.class).where().where().eq("url", url).findUnique();

        if (page != null)
            return page;

        Document pageDocument = Crawler.getPageDocument(url);

        String title = pageDocument.title();
        String text = pageDocument.body().text();
        String image = pageDocument.body().select("img").first().attr("src");
        List<String> categories = new ArrayList<>();

        page = new Page(url, title, text, image, String.join(",", categories));
        Ebean.save(page);

        return page;
    }

    public static Map<String, Integer> getWords(String[] textArray) {

        Map<String, Integer> words = new HashMap<>();

        for (String word : textArray) {

            word = word.toLowerCase();

            if (words.containsKey(word))
                words.put(word, (words.get(word) + 1));

            else
                words.put(word, 1);
        }

        return words;
    }

    public static Document getPageDocument(String url) {

        Document doc = null;

        try {

            Connection connection = Jsoup.connect(url);

            doc = connection.userAgent(Crawler.USER_AGENT).followRedirects(true).get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }

    public static Map<String, Integer> sortWords(Map<String, Integer> words) {

        LinkedHashMap<String, Integer> sortedWords = words
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return sortedWords;
    }

    public static Map<String, Integer> getSortedWords(Page page) {

        String text = page.text;

        int textLength = text.length();
        String[] textArray = text.split("\\s+|,\\s+|/.\\s+");
        int wordsCount = textArray.length;
        Map<String, Integer> words = Crawler.getWords(textArray);

        LinkedHashMap<String, Integer> sortedWords = (LinkedHashMap<String, Integer>) sortWords(words);

//        Logs.out(page.title);
//        Logs.first(sortedWords, 10);

        return sortedWords;
    }

    public static Map<String, Integer> getWordsIDF(List<Map<String, Integer>> pagesWords) {

        int pagesCount = pagesWords.size();

        Map<String, Integer> wordsFrequency = new HashMap<>();
        Map<String, Integer> wordsIDF = new HashMap<>();

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

            wordsIDF.put(name, (int) Math.log((double) pagesCount / (double) wordsFrequency.get(name)));
        }

        wordsIDF = Crawler.sortWords(wordsIDF);

        return wordsIDF;
    }
}
