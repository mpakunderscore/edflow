package utils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by pavelkuzmin on 07/03/16.
 */
public class Logs {

    private static boolean debug = true;
    private static char caller;

    public static void out(String text) {

        String caller = new Exception().getStackTrace()[1].getClassName();

        System.out.println(getCaller() + text);
    }

    public static void first(Map<String, ? extends Number> map, int count) {

        int i = 0;
        for (Map.Entry<String, ? extends Number> entry : map.entrySet()) {

            String key = entry.getKey();
            Number value = entry.getValue();

            System.out.println(getCaller() + key + " -> " + value);

            i++;
            if (i == count)
                break;
        }
    }

    public static void time(long time) {

        String caller = new Exception().getStackTrace()[1].getClassName();

        System.err.println(getCaller() + "TIME " + (System.currentTimeMillis() - time)/1000 + " sec.");
    }

    public static void debug(String text) {

        if (!debug)
            return;

        System.out.println(getCaller() + (char) 27 + "[31m" + text + (char) 27 + "[0m");
    }

    private static String getCaller() {

        String caller = new Exception().getStackTrace()[2].getClassName();

        int n = 15 - caller.length();

        if (n < 0)
            n = 0;

        String space = new String(new char[n]).replace('\0', ' ');

        return space + caller + " | ";
    }
}
