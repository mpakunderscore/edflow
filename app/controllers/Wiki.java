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

    private static final String CATEGORY_URL = ".wikipedia.org/wiki/Category:";
    private static final String PAGE_URL = ".wikipedia.org/wiki/";

    private static final String PROTOCOL = "https://";

    private static final String LANG = "en";
//    private static final String LANG = "ru";

//    private static final String MAIN_CATEGORY = "Статьи";
    private static final String MAIN_CATEGORY = "Main topic classifications";

    public static Result getCategoryTree(String categoryName) {

        if (Objects.equals(categoryName.toLowerCase(), "undefined"))
            categoryName = MAIN_CATEGORY;

        Document doc = getWikiDoc(categoryName);

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


    public static Document getWikiDoc(String categoryName) {

        Document doc = null;

        try {

            String connectUrl = PROTOCOL + LANG + CATEGORY_URL + categoryName;

            Connection connection = Jsoup.connect(connectUrl);

            doc = connection.userAgent(USER_AGENT).followRedirects(true).get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }
}
