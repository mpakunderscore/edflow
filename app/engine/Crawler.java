package engine;

import com.avaje.ebean.Ebean;
import models.Flow;
import models.Page;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import play.mvc.Controller;
import utils.Logs;

import java.io.IOException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Crawler {

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/31.0.1650.63 Safari/537.36";

    public static Page getPage(String url) {

        Logs.out(url);

        Page page = null;

        page = Ebean.find(Page.class).where().where().eq("url", url).findUnique();

        if (page != null)
            return page;

        Document pageDocument = getPageDocument(url);

        String title = pageDocument.title();
        String text = pageDocument.body().text();
        String image = findImage(pageDocument);
        List<String> categories = new ArrayList<>();

        page = new Page(url, title, text, image, String.join(",", categories));
        Ebean.save(page);

        return page;
    }

    private static String findImage(Document pageDocument) {

        String image = "";

        Element img = pageDocument.body().select("img").first();

        if (img != null)
            return img.attr("src");

        return image;
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

    public static Flow checkFlow(String url) {

        Logs.out(url);

        Flow flow = null;

        flow = Ebean.find(Flow.class).where().where().eq("url", url).findUnique();

        if (flow != null)
            return flow;

        Document pageDocument = getPageDocument(url);

        String title = pageDocument.title();
        String text = pageDocument.body().text();
        String image = findImage(pageDocument);
        List<String> categories = new ArrayList<>();

        flow = new Flow(url, title, String.join(",", categories));
        Ebean.save(flow);

        return flow;
    }
}
