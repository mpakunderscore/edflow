package engine;

import com.avaje.ebean.Ebean;
import engine.text.Analyser;
import engine.text.Parser;
import engine.type.HTML;
import engine.type.PDF;
import engine.type.RSS;
import models.Flow;
import models.Page;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import utils.Logs;

import java.io.IOException;
import java.util.*;

public class Crawler {

    static boolean db = true; //for debug

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) " +
                                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                            "Chrome/31.0.1650.63 Safari/537.36";

    static Page getPage(String url) {

        Page page = null;

        //TODO
        if (db)
            page = Ebean.find(Page.class).where().eq("url", url).findUnique();

        if (page != null)
            return page;

        try {

            if (url.endsWith(".pdf"))
                page = PDF.read(url);

            else
                page = HTML.read(url);


            //TODO check words length
            Logs.debug("Text length: " + page.text.length());
            //text

            //TODO Parser
            Map<String, Integer> sortedWords = Parser.getSortedWords(page);
            int words = sortedWords.size();
            Logs.debug("Sorted words: " + words);


            //TODO 1.text 2.categories
            List<String> categories = new ArrayList<>();
            page.categories = String.join(",", categories);


            if (db)
                Ebean.save(page);

        } catch (Exception e) {

            Logs.debug("ERROR " + e.getMessage());
            return null;
        }

        return page;
    }
}
