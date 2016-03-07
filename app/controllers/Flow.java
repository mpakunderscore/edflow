package controllers;

import com.avaje.ebean.Ebean;
import engine.Crawler;
import models.Page;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Category;
import utils.Response;

import java.util.*;

import static play.libs.Json.toJson;
import static play.mvc.Results.ok;

/**
 * Created by pavelkuzmin on 08/02/16.
 */
public class Flow extends Controller {

    public static Result getFlow(String categoryName, String language) {

        List<Page> pages = Ebean.find(Page.class).findList();

        for (Page page : pages) {

            String text = page.text;

            int textLength = text.length();
            String[] textArray = text.split("\\s+");
            int wordsCount = textArray.length;
            Map<String, Integer> words = Crawler.getWords(textArray);

            System.out.println(page.title);
            System.out.println(textLength + " " + wordsCount + " " + words.size());
        }

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Recommend"));
        categories.add(new Category("Favorites"));
        categories.add(new Category("Domains"));

        return ok(toJson(new Response(categories, pages)));
    }


}
