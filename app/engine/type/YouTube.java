package engine.type;

import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import utils.Logs;

/**
 * Created by pavelkuzmin on 06/10/2016.
 */
public class YouTube {

    static void process(Document pageDocument) {

        Elements youtube = pageDocument.body().select("iframe[src=*youtube.com*]");
        Logs.debug("YouTube: " + youtube.size());
    }
}
