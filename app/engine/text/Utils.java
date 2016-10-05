package engine.text;

import com.google.common.base.Optional;
import com.optimaize.langdetect.LanguageDetector;
import com.optimaize.langdetect.LanguageDetectorBuilder;
import com.optimaize.langdetect.i18n.LdLocale;
import com.optimaize.langdetect.ngram.NgramExtractors;
import com.optimaize.langdetect.profiles.LanguageProfile;
import com.optimaize.langdetect.profiles.LanguageProfileReader;
import com.optimaize.langdetect.text.CommonTextObjectFactories;
import com.optimaize.langdetect.text.TextObject;
import com.optimaize.langdetect.text.TextObjectFactory;
import models.Page;
import utils.Logs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by pavelkuzmin on 05/10/2016.
 */
public class Utils {

    public static Map<String, Integer> sortWords(Map<String, Integer> words) {

        LinkedHashMap<String, Integer> sortedWords = words
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return sortedWords;
    }

    public static Map<String, Double> sortWordsDouble(Map<String, Double> words) {

        LinkedHashMap<String, Double> sortedWords = words
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return sortedWords;
    }

    public static Map<String, Double> sortWordsDoubleBack(Map<String, Double> words) {

        LinkedHashMap<String, Double> sortedWords = words
                .entrySet()
                .stream()
                .sorted(Map.Entry.<String, Double>comparingByValue())
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));

        return sortedWords;
    }

    static String checkText(String text) {

        for (String word : text.split(" ")) {

            word = word.trim();

            if (word.length() > 15) {
                text = text.replace(word, "");
            }
        }

        return text;
    }

    static List<String> getLastTen(Map<String, Double> words) {

        List<String> categories = new ArrayList<>();

        int i = 0;
        for (Map.Entry word : words.entrySet()) {

            categories.add((String) word.getKey());

            i++;
            if (i == 10)
                break;
        }

        return categories;
    }

    public static String getLanguage(Page page) throws IOException {

        //load all languages:
        List<LanguageProfile> languageProfiles = new LanguageProfileReader().readAllBuiltIn();

        //build language detector:
        LanguageDetector languageDetector = LanguageDetectorBuilder.create(NgramExtractors.standard())
                .withProfiles(languageProfiles)
                .build();

        //create a text object factory
        TextObjectFactory textObjectFactory = CommonTextObjectFactories.forDetectingOnLargeText();

        //query:
        TextObject textObject = textObjectFactory.forText(page.text);
        Optional<LdLocale> lang = languageDetector.detect(textObject);

        Logs.out(lang.get().getLanguage());

        return lang.get().getLanguage();
    }

    public static String getDomain(String url) {
        return url.replace("http://", "").replace("https://", "").replace("www.", "").split("/")[0];
    }
}
