package controllers;

import play.mvc.Result;
import utils.Category;
import utils.Logs;
import utils.Response;

import java.util.ArrayList;
import java.util.List;

import static play.libs.Json.toJson;
import static play.mvc.Results.ok;

/**
 * Created by pavelkuzmin on 22/05/16.
 */
public class Chat {

    public static Result message(String message) {

        Logs.out(message);

        return ok(toJson(new Response(null, null)));
    }
}
