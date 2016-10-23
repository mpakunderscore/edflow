package engine;

import com.avaje.ebean.Ebean;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import engine.text.Utils;
import engine.text.Words;
import engine.type.HTML;
import engine.type.PDF;
import models.Flow;
import models.Page;
import utils.Logs;
import utils.Settings;

import java.io.IOException;
import java.net.URL;
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


            String language = Utils.getLanguage(page);
            page.language = language;

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

    static void processFlows() {

        List<Flow> flows = Ebean.find(Flow.class).findList();

        for (Flow flow : flows) {

            findNewPages(flow);
        }
    }

    private static void findNewPages(Flow flow) {

        try {

            Logs.debug("Flow title: " + flow.title);
            Logs.debug("Flow url: " + flow.url);
            Logs.debug("Flow: " + flow.url);

            URL feedUrl = new URL(flow.rssUrl);
            SyndFeedInput input = new SyndFeedInput();
            input.setAllowDoctypes(true);
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            boolean last = false;

            if (flow.lastPage != null) {

                for (int i = feed.getEntries().size(); i >= 0; --i) {

                    try {

                        SyndEntryImpl entry = (SyndEntryImpl) feed.getEntries().get(i);

                        if (flow.lastPage.equals(entry.getLink())) {
                            last = true;
                            continue;
                        }

                        if (last) {
                            getPage(entry.getLink());
                            flow.lastPage = entry.getLink();
                        }

                    } catch (Exception ignored) {

                    }
                }
            }

            if (flow.lastPage == null || !last) {

                for (int i = feed.getEntries().size(); i >= 0; --i) {

                    try {

                        SyndEntryImpl entry = (SyndEntryImpl) feed.getEntries().get(i);

                        getPage(entry.getLink());
                        flow.lastPage = entry.getLink();

                    } catch (Exception ignored) {

                    }
                }
            }


            Ebean.update(flow);

        } catch (Exception ex) {

            ex.printStackTrace();
//            System.err.println("ERROR: " + ex.getMessage());
        }
    }
}
