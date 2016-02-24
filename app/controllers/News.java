package controllers;

import models.Page;
import play.mvc.Result;
import utils.Category;
import utils.Response;

import java.util.ArrayList;
import java.util.List;

import static play.libs.Json.toJson;
import static play.mvc.Results.ok;

/**
 * Created by pavelkuzmin on 08/02/16.
 */
public class News {

    public static Result getNews(String categoryName, String language) {

        List<Page> texts = new ArrayList<>();
        texts.add(Wikipedia.getPage("EN", "Infinity"));

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Local"));

        return ok(toJson(new Response(categories, texts)));
    }
}
