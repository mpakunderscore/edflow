package engine;

import com.avaje.ebean.Ebean;
import engine.text.Analyser;
import engine.text.Language;
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
import utils.Settings;

import java.io.IOException;
import java.util.*;

public class Crawler {

    static Page getPage(String url) {

        Page page = null;

        //TODO
        if (Settings.getPageDB)
            page = Ebean.find(Page.class).where().eq("url", url).findUnique();

        if (page != null)
            return page;

        try {

            if (url.endsWith(".pdf"))
                page = PDF.read(url);

            else
                page = HTML.read(url);


            //TODO get language
            Language.getLanguage(page);

            //TODO check words length
            Logs.debug("Text length: " + page.text.length());

            //TODO 1.text 2.categories
            List<String> categories = new ArrayList<>();
            categories.add("none");
            page.categories = String.join(",", categories);

            //TODO Parser
            Map<String, Integer> sortedWords = Parser.getSortedWords(page);
            int words = sortedWords.size();
            page.wordsCount = words;
            Logs.debug("Sorted words: " + words);


            if (Settings.getPageDB)
                Ebean.save(page);

        } catch (Exception e) {

            Logs.debug("ERROR " + e.getMessage());
            return null;
        }

        return page;
    }
}
