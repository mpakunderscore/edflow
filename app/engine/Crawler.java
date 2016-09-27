package engine;

import com.avaje.ebean.Ebean;
import models.Flow;
import models.Page;
import org.apache.commons.io.FileUtils;
import org.apache.pdfbox.cos.COSDocument;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import utils.Logs;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class Crawler {

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) " +
                                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                            "Chrome/31.0.1650.63 Safari/537.36";

    public static Page getPage(String url) {

        Page page = null;

        //TODO
        page = Ebean.find(Page.class).where().where().eq("url", url).findUnique();

        if (page != null)
            return page;

        try {

            String title = "Some debug title";
            String text = "Some debug text";
            String image = "/";

            if (url.endsWith(".pdf"))
                page = readPDF(url);

            else {

                Document pageDocument = getPageDocument(url);
                title = pageDocument.title();
                Logs.debug("Title: " + title);

                text = pageDocument.body().text();

                image = findImage(pageDocument);

                RSS.process(url, pageDocument);

                page = new Page(url, title, text, image, null);
            }

            //TODO check words length
            Logs.debug("Text length: " + page.text.length());
            //text

            //TODO 1.text 2.categories
            List<String> categories = new ArrayList<>();

            page.categories = String.join(",", categories);


            //TODO Parser
            int words = Parser.getSortedWords(page).size();
            Logs.debug("Sorted words: " + words);

            //TODO
            Ebean.save(page);

        } catch (Exception e) {
            return null;
        }

        return page;
    }

    private static Page readPDF(String url) {

        Logs.debug("Read PDF");

        PDFTextStripper pdfStripper = null;
        PDDocument pdDoc = null;
        COSDocument cosDoc = null;

        File file = new File("file");

        String title = "";
        String text = "";

        try {

            FileUtils.copyURLToFile(new URL(url), file);

            PDDocument doc = PDDocument.load(file);


            text = new PDFTextStripper().getText(doc);
            String[] lines = text.split(System.getProperty("line.separator"));

            title = lines[0];
            Logs.debug(title);

        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return new Page(url, title, text, "", null);
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
