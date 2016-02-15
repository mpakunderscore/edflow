package models;

/**
 * Created by pavelkuzmin on 08/02/16.
 */
public class Word {

    String title;
    String translate;

    public Word(String title, String translate) {
        this.title = title;
        this.translate = translate;
    }

    public String getTitle() {
        return title;
    }

    public String getTranslate() {
        return translate;
    }
}
