package utils;

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

    public static void first(Map<String, Integer> map, int count) {

        int i = 0;
        for (Map.Entry<String, Integer> entry : map.entrySet()) {

            String key = entry.getKey();
            int value = entry.getValue();

            System.out.println(key + ": " + value);

            i++;
            if (i == count)
                break;
        }

    }
}
