package controllers;

import com.avaje.ebean.Ebean;
import engine.Crawler;
import engine.type.HTML;
import models.Page;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.*;

import play.mvc.Controller;
import play.mvc.Result;
import utils.Category;
import utils.Response;

import static play.libs.Json.toJson;

public class Wikipedia extends Controller {

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

        String url = getWikiUrl(language, categoryPrefix, categoryName);

        Document pageDocument = HTML.getPageDocument(url);


        //MAIN PAGE


        String mainPageTitle = "";
        Elements categoryPageLinks = pageDocument.body().select(".mainarticle b a");
        if (categoryPageLinks.size() > 0)
            mainPageTitle = categoryPageLinks.get(0).text();


        //SUB CATEGORIES


        Elements subCategoriesLinks = pageDocument.body().select("#mw-subcategories ul a");

        List<Category> subCategories = new ArrayList<>();

        for (Element link : subCategoriesLinks) {
            String subCategory = link.text();

//            Map<String, String> subCategoryMap = new HashMap<>();
//            subCategoryMap.put("title", );

            subCategories.add(new Category(subCategory));
        }


        //PAGES


        Elements pagesLinks = pageDocument.body().select("#mw-pages ul a");

        List<Page> pages = new ArrayList<>();

        for (Element link : pagesLinks) {

            String title = link.text();

//            Map<String, String> pageMap = new HashMap<>();
//            pageMap.put("title", title);
//            pageMap.put("image", "");
//            pageMap.put("description", "Culture (/ˈkʌltʃər/) is, in the words of E.B. Tylor, \"that complex whole which includes knowledge, belief, art, morals, law, custom and any other capabilities and habits acquired by man as a member of society.\"[1]Cambridge English Dictionary states that culture is, \"the way of life, especially the general customs and beliefs, of a particular group of people at a particular time.\"[2] Terror Management Theory posits that culture is a series of activities and worldviews that provide humans with the illusion of being individuals of value in a world meaning—raising themselves above the merely physical aspects of existence, in order to deny the animal insignificance and death that Homo Sapiens became aware of when they acquired a larger brain.[3]Culture");

            Page page;

            try {
                page = getPage(language, title);
            } catch (Exception e) {
                continue;
            }

            pages.add(page);
        }

//        Map<String, List<Page>> out = new HashMap<>();
//        out.put("subCategories", subCategories);
//        out.put("pages", pages);

        Response response = new Response(subCategories, pages);

        return ok(toJson(response));
    }

    public static Page getPage(String language, String title) {

        String url = getWikiUrl(language, "", title);

//        System.out.println(url);

        Page page = null;

        page = Ebean.find(Page.class).where().where().eq("url", url).findUnique();

        if (page != null)
            return page;

        String image = "";
        String description = "";
        String text = "";

        List<String> categories = new ArrayList<>();

        Document pageDocument = HTML.getPageDocument(url);

        Elements images = pageDocument.body().select("#mw-content-text img");
        if (images.size() > 1) {
            image = images.get(0).attr("src");
        }

        Elements pBlocks = pageDocument.body().select("#mw-content-text p");
        if (pBlocks.size() > 1) {

            //TODO
            description = "<p>" + pBlocks.get(0).html() + "</p><p>" + pBlocks.get(1).html() + "</p>";
        }

        Elements htmlText = pageDocument.body().select("#mw-content-text");
        for (int i = 0; i < htmlText.size(); i++) {

            //TODO
            text += htmlText.get(i).text();
        }

        Elements catLinks = pageDocument.body().select("#mw-normal-catlinks ul a");
        for (int i = 0; i < catLinks.size(); i++) {
            categories.add(catLinks.get(i).text());
        }

        page = new Page(url, title, text, image, String.join(",", categories));

        Ebean.save(page);

        return page;
    }


    public static String getWikiUrl(String language, String categoryPrefix, String name) {

        return PROTOCOL + language.toLowerCase() + WIKI_URL + categoryPrefix + name;
    }
}
