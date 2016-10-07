package engine.type;

import com.avaje.ebean.Ebean;
import engine.API;
import engine.text.Utils;
import models.Flow;
import models.Page;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Logs;
import utils.Settings;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by pavelkuzmin on 27/09/2016.
 */
public class HTML {

    public static Page read(String url) {

        Document pageDocument = getPageDocument(url);
        String title = pageDocument.title();
        Logs.debug("Title: " + title);

        String text = pageDocument.body().text();

        Elements links = pageDocument.body().select("[href]");

        int externalLinks = 0;
        int domainLinks = 0;
        int pdfLinks = 0;

        String domain = Utils.getDomain(url);

        for (Element link : links) {

            if (link.attr("href").equals("/"))
                continue;

            String linkUrl = normalize(url, link.attr("href"));

            if (linkUrl.contains(domain))
                domainLinks++;

            if (Settings.depth && linkUrl.length() > 0)
                API.getPage(linkUrl);

            if (link.attr("href").startsWith("http"))
                externalLinks++;

            if (link.attr("href").endsWith("pdf"))
                pdfLinks++;
        }

        Logs.debug("Links: " + links.size());
        Logs.debug("External links: " + externalLinks);
        Logs.debug("Domain links: " + domainLinks);
        Logs.debug("PDF links: " + pdfLinks);

        String image = normalize(url, findImage(pageDocument));

        if (image.length() > 0)
            Logs.debug("Image: " + image);

        RSS.read(url, pageDocument);

        return new Page(url, title, text, image, null);
    }

    private static String normalize(String pageUrl, String url) {

        if (!url.startsWith("http")) {

            if (url.startsWith("/"))
                url = pageUrl.split("://")[0] + "://" + pageUrl.split("://")[1].split("/")[0] + url;

            else
                return "";
        }

        return url;
    }

    private static String findImage(Document pageDocument) {

        String imageUrl = "";

        Elements images = pageDocument.body().select("img[src]");

        Logs.debug("Images: " + images.size());

        for (Element image : images) {

            String height = image.attr("height");
            String src = image.attr("src");

//            Logs.debug("    I: " + image.attr("width") + " / " + height + " / " + src);

            if (height.length() > 0 && src.length() > 0) {

//                if (Integer.parseInt(height) > 200 && imageUrl.length() == 0)
//                    imageUrl = src;
            }
        }

        Elements imagesAlt = pageDocument.body().select("img[alt]");
        Logs.debug("With alt: " + imagesAlt.size());

//        if (imageUrl.length() == 0)
//            for (Element image : images) {
//
//                String src = image.attr("src");
//                if (src.length() > 0)
//                    imageUrl = src;
//            }

//            String location = image.attr("src");
//            InputStream stream = new URL(location).openStream();
//            Object obj = ImageIO.createImageInputStream(stream);
//            ImageReader reader = ImageIO.getImageReaders(obj).next();
//            System.out.println(next.getWidth(0));
//            stream.close();


        return imageUrl;
    }

    public static Document getPageDocument(String url) {

        Document doc = null;

        try {

            Connection connection = Jsoup.connect(url).timeout(Settings.timeout);

            doc = connection.userAgent(Settings.USER_AGENT).followRedirects(true).get();

        } catch (IOException e) {
            e.printStackTrace();
        }

        return doc;
    }

    public static Flow checkFlow(String link) {

        String url = "http://" + Utils.getDomain(link);

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
