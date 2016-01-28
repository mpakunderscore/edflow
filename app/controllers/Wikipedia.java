package controllers;

import com.avaje.ebean.Ebean;
import models.Page;
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
import utils.Response;

import static play.libs.Json.toJson;

public class Wikipedia extends Controller {

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


        String mainPageTitle = "";
        Elements categoryPageLinks = doc.body().select(".mainarticle b a");
        if (categoryPageLinks.size() > 0)
            mainPageTitle = categoryPageLinks.get(0).text();


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

        List<Page> pages = new ArrayList<>();

        for (Element link : pagesLinks) {

            String title = link.text();

//            Map<String, String> pageMap = new HashMap<>();
//            pageMap.put("title", title);
//            pageMap.put("image", "");
//            pageMap.put("description", "Culture (/ˈkʌltʃər/) is, in the words of E.B. Tylor, \"that complex whole which includes knowledge, belief, art, morals, law, custom and any other capabilities and habits acquired by man as a member of society.\"[1]Cambridge English Dictionary states that culture is, \"the way of life, especially the general customs and beliefs, of a particular group of people at a particular time.\"[2] Terror Management Theory posits that culture is a series of activities and worldviews that provide humans with the illusion of being individuals of value in a world meaning—raising themselves above the merely physical aspects of existence, in order to deny the animal insignificance and death that Homo Sapiens became aware of when they acquired a larger brain.[3]Culture");
            Page page = getPageMap(language, title);

            if (title.equals(mainPageTitle))
                page.setMain();

            pages.add(page);
        }

//        Map<String, List<Page>> out = new HashMap<>();
//        out.put("subCategories", subCategories);
//        out.put("pages", pages);

        Response response = new Response(subCategories, pages);

        return ok(toJson(response));
    }

    private static Page getPageMap(String language, String title) {

        System.out.println(title);

        Page page = null;

//        page = Ebean.find(Page.class).where().where().eq("title", title).findUnique();

        if (page != null)
            return page;

        String image = "";
        String description = "";

        Document mainPageDoc = getWikiDoc(language, "", title);

        Elements images = mainPageDoc.body().select("#mw-content-text img");
        if (images.size() > 1) {
            image = images.get(0).attr("src");
        }

        Elements pBlocks = mainPageDoc.body().select("#mw-content-text p");
        if (pBlocks.size() > 1) {

            //TODO
            description = "<p>" + pBlocks.get(0).html() + "</p><p>" + pBlocks.get(1).html() + "</p>";
        }

        page = new Page(title, description, image);
//        Ebean.save(page);

        return page;
    }


    public static Document getWikiDoc(String language, String categoryPrefix, String name) {

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
