package utils;

import java.util.regex.Pattern;

/**
 * Created by pavelkuzmin on 05/10/2016.
 */
public class Settings {

    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_8_5) " +
                                            "AppleWebKit/537.36 (KHTML, like Gecko) " +
                                            "Chrome/31.0.1650.63 Safari/537.36";

    //What is word
    public final static Pattern wordPattern = Pattern.compile("[^\\s+\"\\d+(){}, –'“\\-=_@:$;#%!<>&\\|\\*\\?\\[\\]\\.\\/\\+\\\\]{2,}");

    //Use DB (read and save) while process get page
    public static boolean getPageDB = true; //for debug

    //Use bigrams in Parser (2 words)
    public final static boolean bigrams = true;

    //Use link depth redirect for crawl HTML TODO
    public static boolean depth = false;

    //HTML timeout
    public static int timeout = 60000;

    //API new thread while get page
    public static boolean getPageThread = true;
}
