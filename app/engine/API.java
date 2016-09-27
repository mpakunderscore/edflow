package engine;

import models.Page;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.mvc.Controller;
import play.mvc.Result;
import utils.Category;
import utils.Logs;
import utils.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static play.libs.Json.toJson;

/**
 * Created by pavelkuzmin on 07/03/16.
 */
public class API extends Controller {

    public static Result getPage(String url) {

        Logs.out(url);

        Long time = System.currentTimeMillis();

        Runnable task = () -> {

            Page page = Crawler.getPage(url);

            if (page != null)
                Logs.debug("Page ok");

//            try {
//                TimeUnit.SECONDS.sleep(5);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }

//            Logs.out("3: " + (System.currentTimeMillis() - time));
        };

//        Logs.out("1: " + (System.currentTimeMillis() - time));

        Thread thread = new Thread(task);
        thread.start();

//        Logs.out("2: " + (System.currentTimeMillis() - time));

        return ok();
    }

    public static Result getSettings() {

        List<Category> categories = new ArrayList<>();
        categories.add(new Category("Process"));
        categories.add(new Category("Debug"));
        categories.add(new Category("Logs"));


        return ok(toJson(new Response(categories, new ArrayList<Object>())));
    }

    public static Result randomData() {

        //check sites

//        String url = "https://www.reddit.com/r/MachineLearning/top/?sort=top&t=year";

        String url = "https://www.reddit.com/r/CompressiveSensing/top/?sort=top&t=year";

        Document document = Crawler.getPageDocument(url);

        Elements elements = document.body().select(".title");

        for (int i = 0; i < elements.size(); i++) {

            String link = elements.get(i).attr("data-href-url");
            if (link.length() != 0 && !link.endsWith(".jpg") && !link.endsWith(".png")) {
//                Logs.out(link);

                getPage(link);
            }
        }
        //run get page

        return ok();
    }

//    public static Result randomData() {
//
//        //check sites
//
////        String url = "https://www.reddit.com/r/MachineLearning/top/?sort=top&t=year";
//
//        String url = "https://www.reddit.com/r/CompressiveSensing/top/?sort=top&t=year";
//
//        Document document = Crawler.getPageDocument(url);
//
//        Elements elements = document.body().select(".title");
//
//        for (int i = 0; i < elements.size(); i++) {
//
//            String link = elements.get(i).attr("data-href-url");
//            if (link.length() != 0 && !link.endsWith(".jpg") && !link.endsWith(".png")) {
////                Logs.out(link);
//
//                getPage(link);
//            }
//        }
//        //run get page
//
//        return ok();
//    }
}
