package controllers;

import com.avaje.ebean.Ebean;
import models.Flow;
import models.Page;
import play.mvc.Controller;
import play.mvc.Result;
import utils.response.Category;
import utils.response.Response;

import java.util.*;
import java.util.concurrent.TimeUnit;

import static play.libs.Json.toJson;

public class Content extends Controller {

    public static Result getPages(String categoryName, String language) {

        List<Page> pages = Ebean.find(Page.class).findList();

        List<Category> subCategories = new ArrayList<>();

        Response response = new Response(subCategories, pages);
        return ok(toJson(response));
    }

    public static Result getMain(String language) {

        List<Page> pages = Ebean.find(Page.class).orderBy("time desc").findList().subList(0, 30);

        List<Category> subCategories = new ArrayList<>();

        Response response = new Response(subCategories, pages);

        return ok(toJson(response));
    }

    public static Result getFlows(String language) {

        List<Flow> pages = Ebean.find(Flow.class).orderBy("time desc").findList();

        List<Category> subCategories = new ArrayList<>();

        Response response = new Response(subCategories, pages);
        return ok(toJson(response));
    }

    public static Result getMine(String language) throws InterruptedException {

        TimeUnit.SECONDS.sleep(3);

        List<Page> pages = Ebean.find(Page.class).orderBy("time").findList();

        List<Category> subCategories = new ArrayList<>();

        Response response = new Response(subCategories, pages);
        return ok(toJson(response));
    }

}
