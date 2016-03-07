package controllers;

import com.avaje.ebean.Ebean;
import engine.Crawler;
import models.Page;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Category;
import utils.Logs;
import utils.Response;

import java.util.*;

import static play.libs.Json.toJson;
import static play.mvc.Results.ok;

/**
 * Created by pavelkuzmin on 08/02/16.
 */
public class Flow extends Controller {

    public static Result getFlow(String categoryName, String language) {

        List<Page> pages = Ebean.find(Page.class).orderBy("time desc").findList();

        processPages(pages);

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Recommend"));
        categories.add(new Category("Favorites"));
        categories.add(new Category("Domains"));

        return ok(toJson(new Response(categories, pages)));
    }

    private static void processPages(List<Page> pages) {

        long time = System.currentTimeMillis();

        List<Map<String, Integer>> words = new ArrayList<>();

        for (Page page : pages) {

            Map<String, Integer> pageWords = Crawler.getSortedWords(page);
            words.add(pageWords);
        }

        Map<String, Double> wordsIDF = Crawler.getWordsIDF(words);

        for (Page page : pages) {

            Map<String, Integer> pageWords = Crawler.getSortedWords(page);
            Map<String, Double> processedPageWords = new HashMap<>();

            for (Map.Entry<String, Integer> word : pageWords.entrySet()) {

                processedPageWords.put(word.getKey(), word.getValue() * wordsIDF.get(word.getKey()));
            }

            Map<String, Double> sortedProcessedPageWords = Crawler.sortWordsDouble(processedPageWords);

//            sortedProcessedPageWords.keySet().forEach();

//            page.categories =

            Logs.out(page.title + " " + page.url);
            Logs.first(sortedProcessedPageWords, 10);
        }

        Logs.time(time);
    }
}
