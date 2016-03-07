package utils;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by pavelkuzmin on 07/03/16.
 */
public class Logs {

    public static void out(String text) {

        String caller = new Exception().getStackTrace()[1].getClassName();

        System.out.println(caller + ": " + text);
    }

    public static void first(Map<String, ? extends Number> map, int count) {

        String caller = new Exception().getStackTrace()[1].getClassName();

        int i = 0;
        for (Map.Entry<String, ? extends Number> entry : map.entrySet()) {

            String key = entry.getKey();
            Number value = entry.getValue();

            System.out.println(caller + ": " + key + " -> " + value);

            i++;
            if (i == count)
                break;
        }

    }

    public static void time(long time) {

        String caller = new Exception().getStackTrace()[1].getClassName();

        System.err.println(caller + ": TIME " + (System.currentTimeMillis() - time)/1000 + " sec.");
    }
}
