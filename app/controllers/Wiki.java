package controllers;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

import play.mvc.Controller;
import play.mvc.Result;

import static play.libs.Json.toJson;

public class Wiki extends Controller {

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36";

    private static final String CATEGORY_URL = ".wikipedia.org/wiki/";

    private static final String PROTOCOL = "https://";

    public static Result getCategoryTree(String categoryName, String language) {

        Map<String, String> mainCategories = new HashMap<>();
        mainCategories.put("EN", "Main topic classifications");
        mainCategories.put("RU", "Статьи");
        mainCategories.put("ZH", "頁面分類");
        mainCategories.put("ES", "Artículos");
        mainCategories.put("DE", "Sachsystematik");

        Map<String, String> categoriesPrefix = new HashMap<>();
        categoriesPrefix.put("EN", "Category:");
        categoriesPrefix.put("RU", "Category:");
        categoriesPrefix.put("ZH", "Category:");
        categoriesPrefix.put("ES", "Categoría:");
        categoriesPrefix.put("DE", "Kategorie:");

        String categoryPrefix = categoriesPrefix.get(language);

        if (Objects.equals(categoryName.toLowerCase(), "undefined")) {

            if (mainCategories.containsKey(language))
                categoryName = mainCategories.get(language);

            else
                categoryName = mainCategories.get("EN");
        }

        Document doc = getWikiDoc(language, categoryPrefix, categoryName);

        //categoryPage

//        Elements categoryPageLinks = doc.body().select("#catmore a");
//        String categoryPage = categoryPageLinks.get(0).text();
//        Map<String, String> categoryPageMap = new HashMap<>();
//        categoryPageMap.put("title", categoryPage);

        //subCategories

        Elements subCategoriesLinks = doc.body().select("#mw-subcategories ul a");

        List<Map<String, String>> subCategories = new ArrayList<>();

        for (Element link : subCategoriesLinks) {
            String subCategory = link.text();

            Map<String, String> subCategoryMap = new HashMap<>();
            subCategoryMap.put("title", subCategory);

            subCategories.add(subCategoryMap);
        }

        //pages

        Elements pagesLinks = doc.body().select("#mw-pages ul a");

        List<Map<String, String>> pages = new ArrayList<>();

        for (Element link : pagesLinks) {
            String title = link.text();
//            String img = getPageImg(title);
            String img = "";
            String description = "Description";

            Map<String, String> pageMap = new HashMap<>();
            pageMap.put("title", title);
            pageMap.put("img", img);
            pageMap.put("description", description);

            pages.add(pageMap);
        }

        Map<String, List<Map<String, String>>> out = new HashMap<>();
//        out.put("selectedCategories", selectedCategories);
        out.put("subCategories", subCategories);
        out.put("pages", pages);

        return ok(toJson(out));
    }


    public static Document getWikiDoc(String language, String categoryPrefix, String categoryName) {

        Document doc = null;

        try {

            String connectUrl = PROTOCOL + language.toLowerCase() + CATEGORY_URL + categoryPrefix + categoryName;

            Connection connection = Jsoup.connect(connectUrl);

            doc = connection.userAgent(USER_AGENT).followRedirects(true).get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }
}
