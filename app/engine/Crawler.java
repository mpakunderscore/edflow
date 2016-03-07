package engine;

import com.avaje.ebean.Ebean;
import models.Page;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import play.mvc.Controller;
import utils.Logs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Crawler extends Controller {

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36";

    public static Page getPage(String url) {

        Logs.out(url);

        Page page = null;

        page = Ebean.find(Page.class).where().where().eq("url", url).findUnique();

        if (page != null)
            return page;

        Document pageDocument = Crawler.getPageDocument(url);

        String title = pageDocument.title();
        String text = pageDocument.body().text();
        String image = pageDocument.body().select("img").first().attr("src");
        List<String> categories = new ArrayList<>();

        page = new Page(url, title, text, image, String.join(",", categories));
        Ebean.save(page);

        return page;
    }

    public static Map<String, Integer> getWords(String[] textArray) {

        Map<String, Integer> words = new HashMap<>();

        for (String word : textArray) {

            if (words.containsKey(word))
                words.put(word, (words.get(word) + 1));

            else
                words.put(word, 1);
        }

        return words;
    }

    public static Document getPageDocument(String url) {

        Document doc = null;

        try {

            Connection connection = Jsoup.connect(url);

            doc = connection.userAgent(Crawler.USER_AGENT).followRedirects(true).get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }
}
