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

    private static final String WIKI_URL = ".wikipedia.org/wiki/";

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

        //TODO
        if (!mainCategories.containsKey(language))
            language = "EN";

        String categoryPrefix = categoriesPrefix.get(language);

        if (Objects.equals(categoryName.toLowerCase(), "undefined")) {

            if (mainCategories.containsKey(language))
                categoryName = mainCategories.get(language);

            else
                categoryName = mainCategories.get("EN");
        }

        Document doc = getWikiDoc(language, categoryPrefix, categoryName);


        //MAIN PAGE


//        List<Map<String, String>> mainPage = new ArrayList<>();

        String mainPageTitle = "";
        Elements categoryPageLinks = doc.body().select(".mainarticle b a");
        if (categoryPageLinks.size() > 0) {

            mainPageTitle = categoryPageLinks.get(0).text();

//            Map<String, String> categoryPageMap = getPageMap(language, mainPageTitle);
//            mainPage.add(categoryPageMap);
        }


        //SUB CATEGORIES


        Elements subCategoriesLinks = doc.body().select("#mw-subcategories ul a");

        List<Map<String, String>> subCategories = new ArrayList<>();

        for (Element link : subCategoriesLinks) {
            String subCategory = link.text();

            Map<String, String> subCategoryMap = new HashMap<>();
            subCategoryMap.put("title", subCategory);

            subCategories.add(subCategoryMap);
        }


        //PAGES


        Elements pagesLinks = doc.body().select("#mw-pages ul a");

        List<Map<String, String>> pages = new ArrayList<>();

        for (Element link : pagesLinks) {

            String title = link.text();

            Map<String, String> pageMap = new HashMap<>();
            pageMap.put("title", title);
            pageMap.put("image", "");
            pageMap.put("description", "Culture (/ˈkʌltʃər/) is, in the words of E.B. Tylor, \"that complex whole which includes knowledge, belief, art, morals, law, custom and any other capabilities and habits acquired by man as a member of society.\"[1]Cambridge English Dictionary states that culture is, \"the way of life, especially the general customs and beliefs, of a particular group of people at a particular time.\"[2] Terror Management Theory posits that culture is a series of activities and worldviews that provide humans with the illusion of being individuals of value in a world meaning—raising themselves above the merely physical aspects of existence, in order to deny the animal insignificance and death that Homo Sapiens became aware of when they acquired a larger brain.[3]Culture");
//            Map<String, String> pageMap = getPageMap(language, title);

            if (title.equals(mainPageTitle))
                pageMap.put("main", "true");

            pages.add(pageMap);
        }

        Map<String, List<Map<String, String>>> out = new HashMap<>();
//        out.put("selectedCategories", selectedCategories);
        out.put("subCategories", subCategories);
        out.put("pages", pages);

        return ok(toJson(out));
    }

    private static Map<String, String> getPageMap(String language, String title) {

        String image = "";
        String description = "";

        Document mainPageDoc = getWikiDoc(language, "", title);

        Elements images = mainPageDoc.body().select("#mw-content-text img");
        if (images.size() > 1) {
            image = images.get(0).attr("src");
        }

        Elements pBlocks = mainPageDoc.body().select("#mw-content-text p");
        if (pBlocks.size() > 1) {
            description = pBlocks.get(0).text() + pBlocks.get(1).text();
        }

        Map<String, String> pageMap = new HashMap<>();
        pageMap.put("title", title);
        pageMap.put("image", image);
        pageMap.put("description", description);

        return pageMap;
    }


    public static Document getWikiDoc(String language, String categoryPrefix, String name) {

        System.out.println(name);

        Document doc = null;

        try {

            String connectUrl = PROTOCOL + language.toLowerCase() + WIKI_URL + categoryPrefix + name;

            Connection connection = Jsoup.connect(connectUrl);

            doc = connection.userAgent(USER_AGENT).followRedirects(true).get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }
}
