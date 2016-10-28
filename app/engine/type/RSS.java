package engine.type;

import java.io.Reader;
import java.net.URL;

//import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.avaje.ebean.Ebean;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import models.Flow;
import models.Page;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import utils.Logs;

public class RSS {

    public static boolean read(String url, Document pageDocument) {

        String rssUrl = findRSSUrl(url, pageDocument);

        if (rssUrl.length() > 0)
            RSS.check(url, rssUrl, pageDocument);

        return false;
    }

    private static void check(String url, String rssUrl, Document pageDocument) {

        try {

            URL feedUrl = new URL(rssUrl);

//            SyndEntryImpl entry = new SyndEntryImpl();

            SyndFeedInput input = new SyndFeedInput();
            input.setAllowDoctypes(true);

            SyndFeed feed = input.build(new XmlReader(feedUrl));
//            SyndFeed feed = feedFetcher.retrieveFeed("your-rss-reader-user-agent", feedUrl);

            if (feed.getLink().equals(url)) {
                Logs.debug("RSS FEED");
            }

            Logs.debug("Title: " + feed.getTitle());
            Logs.debug("Entries: " + feed.getEntries().size() + "");

            for (int i = 0; i < feed.getEntries().size(); i++) {

                try {

                    SyndEntryImpl entry = (SyndEntryImpl) feed.getEntries().get(i);

                    if (pageDocument.title().contains(entry.getTitle())) {

                        Logs.debug("    T: " + entry.getTitle());
//                        Logs.debug("    D: " + entry.getDescription().getValue());

                    } else
                        Logs.debug("T: " + entry.getTitle());

                } catch (Exception ignored) {

                }
            }

            Logs.debug("Link: " + feed.getLink());
            Logs.debug("Description: " + feed.getDescription());
            Logs.debug("Author: " + feed.getAuthor());
            Logs.debug("FeedType: " + feed.getFeedType());
            Logs.debug("Language: " + feed.getLanguage());

            Logs.debug("Links: " + feed.getLinks().size());

            Flow flow = checkRSSFlow(feed, rssUrl);

//            Logs.out(feed.getLinks().toString());
//            Logs.out(feed.getImage().getLink());
//            Logs.out(feed.getCategories().toString());

        } catch (Exception ex) {

            ex.printStackTrace();
//            System.err.println("ERROR: " + ex.getMessage());
        }
    }

    //TODO FUCK RSS
    private static Flow checkRSSFlow(SyndFeed feed, String rssUrl) {

        Flow flow = Ebean.find(Flow.class).where().eq("url", feed.getLink()).findUnique();

        if (flow != null)
            return flow;

        if (feed.getEntries().size() > 0) {

            SyndEntryImpl entry = (SyndEntryImpl) feed.getEntries().get(0);
            String entryLink = entry.getLink().split("#")[0]; //.split("/?")[0]

            Logs.debug(feed.getLink());
            Logs.debug(entryLink);

            if (feed.getLink().equals(entryLink) || feed.getLink().contains("github.com")) //TODO
                return null;

            flow = new Flow(feed.getLink(), rssUrl, feed.getTitle(), "");

            Ebean.save(flow);

            return flow;
        }

        return null;

//        for (int i = 0; i < feed.getEntries().size(); i++) {
//
//            try {
//
//                SyndEntryImpl entry = (SyndEntryImpl) feed.getEntries().get(i);
//
//                String entryLink = entry.getLink().split("#")[0].split("/?")[0];
//
//                Logs.debug(feed.getLink());
//                Logs.debug(entryLink);
//
//                if (feed.getLink().equals(entryLink))
//                    return null;
//
//            } catch (Exception ignored) {
//
//            }
//        }
//
//        return new Flow(feed.getLink(), rssUrl, feed.getTitle(), "");
    }

    private static String findRSSUrl(String pageUrl, Document pageDocument) {

        String rssUrl = "";
        String type = "application/rss+xml";

        Elements links = pageDocument.select("link[type=application/rss+xml],link[type=application/atom+xml],a:contains(RSS)");

        if (links != null) {

            if (links.size() == 0) {

                Logs.debug("NO RSS");
                return "";

            } else {

                Logs.debug("RSS links: " + links.size());

//                for (Element link : links)
//                    Logs.debug(link.attr("href"));
            }

            rssUrl = links.first().attr("href");
        }

        if (!rssUrl.startsWith("http")) {

            if (!rssUrl.startsWith("/"))
                rssUrl = "/" + rssUrl;

            rssUrl = pageUrl.split("://")[0] + "://" + pageUrl.split("://")[1].split("/")[0] + rssUrl;
        }

        Logs.out("RSS url: " + rssUrl);

        return rssUrl;
    }
}
