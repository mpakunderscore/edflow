package engine;

import models.Page;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Category;
import utils.Logs;
import utils.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 07/03/16.
 */
public class API extends Controller {

    public static Result getPage(String url) {

        Page page = Crawler.getPage(url);

        Crawler.getSortedWords(page);

        return ok(toJson(page));
    }

    public static Result getSettings() {

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Process"));
        categories.add(new Category("Debug"));
        categories.add(new Category("Logs"));


        return ok(toJson(new Response(categories, new ArrayList<Object>())));
    }
}
