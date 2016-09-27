package engine.type;

import java.net.URL;

//import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndEntryImpl;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import utils.Logs;

public class RSS {

    public static void read(String url, Document pageDocument) {

        String rssUrl = findRSSUrl(url, pageDocument);

        if (rssUrl.length() > 0)
            RSS.check(rssUrl);
    }

    private static void check(String url) {

        try {

            URL feedUrl = new URL(url);

//            SyndEntryImpl entry = new SyndEntryImpl();

            SyndFeedInput input = new SyndFeedInput();
            SyndFeed feed = input.build(new XmlReader(feedUrl));

            Logs.debug("Title: " + feed.getTitle());
            Logs.debug("Entries: " + feed.getEntries().size() + "");

            for (int i = 0; i < feed.getEntries().size(); i++) {

                try {

                    SyndEntryImpl entry = (SyndEntryImpl) feed.getEntries().get(i);
//                    Logs.debug("Content: " + entry.getDescription().getValue());

                } catch (Exception ignored) {

                }
            }

            Logs.debug("Description: " + feed.getDescription());
            Logs.debug("Author: " + feed.getAuthor());
            Logs.debug("FeedType: " + feed.getFeedType());
            Logs.debug("Language: " + feed.getLanguage());

            Logs.debug("Links: " + feed.getLinks().size());

//            Logs.out(feed.getLinks().toString());
//            Logs.out(feed.getImage().getLink());
//            Logs.out(feed.getCategories().toString());

        } catch (Exception ex) {

            ex.printStackTrace();
            System.err.println("ERROR: " + ex.getMessage());
        }
    }

    private static String findRSSUrl(String pageUrl, Document pageDocument) {

        String rssUrl = "";
        String type = "application/rss+xml";

        Elements links = pageDocument.select("link[type=application/rss+xml],link[type=application/atom+xml]");

        if (links != null) {

            if (links.size() == 0) {

                Logs.debug("NO RSS");
                return "";

            } else {
                Logs.debug(links.size() + " RSS links");
            }

            rssUrl = links.first().attr("href");
        }

        if (!rssUrl.startsWith("http"))
            rssUrl = pageUrl.split("://")[0]  + "://" + pageUrl.split("://")[1].split("/")[0] + rssUrl;

        Logs.out("RSS url: " + rssUrl);

        return rssUrl;
    }
}
