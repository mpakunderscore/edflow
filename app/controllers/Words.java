package controllers;

import models.Page;
import models.Word;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.mvc.Result;
import utils.Category;
import utils.Response;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

import static play.libs.Json.toJson;
import static play.mvc.Results.ok;

/**
 * Created by pavelkuzmin on 08/02/16.
 */
public class Words {

    public static Result getWords(String categoryName, String language) {

        List<Word> words = new ArrayList<>();
        words.add(new Word("Word", "Слово"));
        words.add(new Word("One", "Раз"));
        words.add(new Word("Two", "Два"));
        words.add(new Word("Three", "Три"));
        words.add(new Word("Newspaper", "Четыре"));
        words.add(new Word("Five", "Пять"));
        words.add(new Word("Six", "Шесть"));

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Thousand"));
        categories.add(new Category("Known"));
        categories.add(new Category("Learn"));

        return ok(toJson(new Response(categories, words)));
    }
}
