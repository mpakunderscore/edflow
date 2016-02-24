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
public class Book {

    public static Result getPages(String categoryName, String language) {

        List<Page> texts = new ArrayList<>();
        texts.add(Wikipedia.getPage("EN", "Consciousness"));
        texts.add(Wikipedia.getPage("EN", "Probability"));
        texts.add(Wikipedia.getPage("EN", "Artificial neural network"));
        texts.add(Wikipedia.getPage("EN", "Brain"));
        texts.add(Wikipedia.getPage("EN", "Infinity"));

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Thousand"));

        return ok(toJson(new Response(categories, texts)));
    }
}
