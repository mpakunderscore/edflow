package utils;

/**
 * Created by pavelkuzmin on 07/03/16.
 */
public class Logs {

    public static void out(String text) {

        String caller = new Exception().getStackTrace()[1].getClassName();

        System.out.println(caller + ": " + text);
    }
}
