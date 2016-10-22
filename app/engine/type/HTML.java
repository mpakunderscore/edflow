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

        //Check if Flow (from RSS)
        if (RSS.read(url, pageDocument))
            return null;

        String text = pageDocument.body().text();

//        processLinks(url, pageDocument);

        YouTube.process(pageDocument);

        String image = findImage(url, pageDocument);

        return new Page(url, title, text, image, null);
    }

    private static void processLinks(String url, Document pageDocument) {

        Elements links = pageDocument.body().select("[href]");

        String domain = Utils.getDomain(url);

        int externalLinks = 0;
        int domainLinks = 0;
        int pdfLinks = 0;

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
    }

    private static String findImage(String url, Document pageDocument) {

        String mainImage = "";

        Elements images = pageDocument.body().select("img[src]");

        Logs.debug("Images: " + images.size());

        for (Element image : images) {

            try {

                Logs.debug("1: " + image.attr("src"));

                //TODO
                if (image.attr("src").equals("/"))
                    continue;

                String imgSrc = normalize(url, image.attr("src"));

                if (imgSrc.length() == 0) // || imgSrc.endsWith("svg") || imgSrc.endsWith(".svg.png")
                    continue;

                Logs.debug("2: " + imgSrc);

                URL imgUrl = new URL(imgSrc);
                final BufferedImage bufferedImage = ImageIO.read(imgUrl);

                if (bufferedImage == null)
                    continue;

                if (bufferedImage.getHeight() > 200) {
                    Logs.debug("Height: " + bufferedImage.getHeight());
                    mainImage = imgSrc;
                    break;
                }

            } catch (IOException e) {
//                e.printStackTrace();
            }

        }

        if (mainImage.length() > 0)
            Logs.debug("Image: " + mainImage);

        return mainImage;
    }

    private static String normalize(String pageUrl, String url) {

        if (!url.startsWith("http")) {

            if (url.startsWith("/")) {

                if (url.startsWith("//") && url.split("//")[1].contains("."))
                    url = url.replaceFirst("//", "http://");

                else if (url.startsWith("//") && !url.split("/")[1].contains("."))
                    url = url.replaceFirst("//", "");

                else if (url.startsWith("/") && !url.split("/")[1].contains(".")) {
                    url = pageUrl.split("://")[0] + "://" + pageUrl.split("://")[1].split("/")[0] + url;

                } else
                    return "";

            } else {
//                Logs.debug("ERROR link: " + url);
                return pageUrl + url;
            }
        }

        return url;
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

//    public static Flow checkFlow(String link) {
//
//        String url = "http://" + Utils.getDomain(link);
//
//        Logs.out(url);
//
//        Flow flow = null;
//
//        flow = Ebean.find(Flow.class).where().where().eq("url", url).findUnique();
//
//        if (flow != null)
//            return flow;
//
//        try {
//
//            Document pageDocument = getPageDocument(url);
//
//            String title = pageDocument.title();
//            String text = pageDocument.body().text();
//            String image = findImage(url, pageDocument);
//            List<String> categories = new ArrayList<>();
//
//            flow = new Flow(url, title, String.join(",", categories));
//            Ebean.save(flow);
//
//        } catch (Exception e) {
//            return null;
//        }
//
//        return flow;
//    }
}
