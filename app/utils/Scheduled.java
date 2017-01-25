package utils;

import engine.API;
import engine.Crawler;

import java.util.TimerTask;
import java.util.Date;

// Create a class extends with TimerTask
public class Scheduled extends TimerTask {

    Date now; // to display current time

    // Add your task here
    public void run() {

        now = new Date(); // initialize date
        Logs.debug("Scheduled, time: " + now); // Display current time

//        Crawler.processFlows();
//        API.gather();
    }
}