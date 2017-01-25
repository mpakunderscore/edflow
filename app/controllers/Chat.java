package controllers;

import play.mvc.Result;
import utils.Logs;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import static play.libs.Json.toJson;
import static play.mvc.Results.ok;

/**
 * Created by pavelkuzmin on 22/05/16.
 */
public class Chat {

    public static Result message(String message) throws InterruptedException, IOException {

        Logs.out(message);

//        Process proc = Runtime.getRuntime().exec("python chatbot-rnn-master/chatbot.py");
//
//        BufferedReader stdInput = new BufferedReader(new InputStreamReader(proc.getInputStream()));
//
//        BufferedReader stdError = new BufferedReader(new InputStreamReader(proc.getErrorStream()));
//
//        // read the output from the command
//        System.out.println("Here is the standard output of the command:\n");
//        String s = null;
//        while ((s = stdInput.readLine()) != null) {
//            System.out.println(s);
//        }
//
//        // read any errors from the attempted command
//        System.out.println("Here is the standard error of the command (if any):\n");
//        while ((s = stdError.readLine()) != null) {
//            System.out.println(s);
//        }


        return ok(toJson("?"));
    }
}
