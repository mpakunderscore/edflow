package controllers;

import models.Page;
import models.Word;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import play.mvc.Result;
import utils.Response;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.*;

import static play.libs.Json.toJson;
import static play.mvc.Results.ok;

/**
 * Created by pavelkuzmin on 08/02/16.
 */
public class Words {

    public static Result getWords(String categoryName, String language) {

        List<Word> words = new ArrayList<>();
        words.add(new Word("Word", "Слово"));

        return ok(toJson(new Response(null, words)));
    }
}
