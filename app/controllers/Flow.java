package controllers;

import com.avaje.ebean.Ebean;
import engine.Analyser;
import engine.Crawler;
import models.Page;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Category;
import utils.Logs;
import utils.Response;

import java.util.*;

import static play.libs.Json.toJson;
import static play.mvc.Results.ok;

/**
 * Created by pavelkuzmin on 08/02/16.
 */
public class Flow extends Controller {

    public static Result getFlow(String categoryName, String language) {

        long time = System.currentTimeMillis();

        List<Page> pages = Ebean.find(Page.class).orderBy("time desc").findList();

        Logs.time(time);

        Analyser.processPages(pages);

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Recommend"));
        categories.add(new Category("Favorites"));
        categories.add(new Category("Domains"));

        return ok(toJson(new Response(categories, pages)));
    }
}
