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
public class Flow {

    public static Result getPages(String categoryName, String language) {

        List<Page> texts = new ArrayList<>();
        texts.add(Wikipedia.getPage("EN", "Deep learning"));
        texts.add(Wikipedia.getPage("EN", "Connectionism"));

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Recommend"));
        categories.add(new Category("Favorites"));
        categories.add(new Category("Domains"));

        return ok(toJson(new Response(categories, texts)));
    }
}
