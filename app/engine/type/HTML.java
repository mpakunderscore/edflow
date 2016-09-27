package engine.type;

import com.avaje.ebean.Ebean;
import engine.API;
import engine.Crawler;
import engine.text.Analyser;
import models.Flow;
import models.Page;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Logs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavelkuzmin on 27/09/2016.
 */
public class HTML {

    static boolean depth = true;

    public static Page read(String url) {

        Document pageDocument = getPageDocument(url);
        String title = pageDocument.title();
        Logs.debug("Title: " + title);

        String text = pageDocument.body().text();

        Elements links = pageDocument.body().select("[href]");

        int externalLinks = 0;

        for (Element link : links) {

            if (depth)
                API.getPage(normalize(url, link.attr("href")));

            if (link.attr("href").startsWith("http"))
                externalLinks++;
        }

        Logs.debug("Links: " + links.size());
        Logs.debug("External links: " + externalLinks);

        String image = findImage(pageDocument);

        RSS.read(url, pageDocument);

        return new Page(url, title, text, image, null);
    }

    private static String normalize(String pageUrl, String url) {

        if (!url.startsWith("http"))
            url = pageUrl.split("://")[0]  + "://" + pageUrl.split("://")[1].split("/")[0] + url;

        return url;
    }

    private static String findImage(Document pageDocument) {

        String image = "";

        Elements images = pageDocument.body().select("img");

        Logs.debug("Images: " + images.size());

        Element img = images.first();

        if (img != null)
            return img.attr("src");

        return image;
    }

    public static Document getPageDocument(String url) {

        Document doc = null;

        try {

            Connection connection = Jsoup.connect(url).timeout(60000);

            doc = connection.userAgent(Crawler.USER_AGENT).followRedirects(true).get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }

    public static Flow checkFlow(String link) {

        String url = "http://" + Analyser.getDomain(link);

        Logs.out(url);

        Flow flow = null;

        flow = Ebean.find(Flow.class).where().where().eq("url", url).findUnique();

        if (flow != null)
            return flow;

        try {

            Document pageDocument = getPageDocument(url);

            String title = pageDocument.title();
            String text = pageDocument.body().text();
            String image = findImage(pageDocument);
            List<String> categories = new ArrayList<>();

            flow = new Flow(url, title, String.join(",", categories));
            Ebean.save(flow);

        } catch (Exception e) {
            return null;
        }

        return flow;
    }
}
