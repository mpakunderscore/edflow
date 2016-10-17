package engine;

import com.avaje.ebean.Ebean;
import engine.text.Utils;
import engine.text.Words;
import engine.type.HTML;
import engine.type.PDF;
import models.Page;
import utils.Logs;
import utils.Settings;

import java.util.*;

public class Crawler {

    public static void processPages(List<Page> pages) {

        long time = System.currentTimeMillis();

        for (Page page : pages) {
            getPage(page.url);
        }

        Logs.time("Process Crawler", time);
    }

    static Page getPage(String url) {

        Page page = page = Ebean.find(Page.class).where().eq("url", url).findUnique();

        Long id = null;
        if (page != null)
             id = page.id;

//        if (page != null)
//            return page;

        try {

            if (url.endsWith(".pdf"))
                page = PDF.read(url);

            else {

//                if (youtube)
//                    YouTube.read(url);
//                else

                    page = HTML.read(url);

                //TODO Check if Flow (from RSS)
                if (page == null)
                    return null;
            }


            Utils.getLanguage(page);

            //TODO check words length
            Logs.debug("Text length: " + page.text.length());

            //TODO 1.text 2.categories
            List<String> categories = new ArrayList<>();
            categories.add("none");
            page.categories = String.join(",", categories);

            //TODO Words
            Map<String, Integer> sortedWords = Words.getSortedWords(page);
            int words = sortedWords.size();
            page.wordsCount = words;
            Logs.debug("Sorted words: " + words);

            if (id == null)
                Ebean.save(page);

            else if (Settings.updatePage) {

                page.id = id;
                Ebean.update(page);
            }

        } catch (Exception e) {

            e.printStackTrace();
//            Logs.debug("ERROR " + e.getMessage());

            return null;
        }

        return page;
    }
}
