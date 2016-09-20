package controllers;

import play.mvc.Result;
import utils.Category;
import utils.Logs;
import utils.Response;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static play.libs.Json.toJson;
import static play.mvc.Results.ok;

/**
 * Created by pavelkuzmin on 22/05/16.
 */
public class Chat {

    public static Result message(String message) throws InterruptedException {

        Logs.out(message);

        TimeUnit.SECONDS.sleep(1);

        return ok(toJson("[server response]"));
    }
}
