package controllers;

import models.Page;
import play.mvc.Result;
import utils.Category;
import utils.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static play.libs.Json.toJson;
import static play.mvc.Results.ok;

/**
 * Created by pavelkuzmin on 08/02/16.
 */
public class Translate {

    public static Result getText(String categoryName, String language) {

        List<Page> texts = new ArrayList<>();
        texts.add(Wikipedia.getPage("EN", "Deep learning"));
        texts.add(Wikipedia.getPage("EN", "Connectionism"));

//        List<Map<String, String>> subCategories = new ArrayList<>();
//
//        Map<String, String> subCategoryMap = new HashMap<>();
//        subCategoryMap.put("title", "Text");
//        subCategories.add(subCategoryMap);
//
//        subCategoryMap = new HashMap<>();
//        subCategoryMap.put("title", "Video");
//        subCategories.add(subCategoryMap);

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Text"));
        categories.add(new Category("Video"));

        return ok(toJson(new Response(categories, texts)));
    }
}
