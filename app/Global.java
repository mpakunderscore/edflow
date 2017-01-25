import play.*;
import utils.Scheduled;

import java.util.Timer;

public class Global extends GlobalSettings {

    private long period = 60*1000*10;

    @Override
    public void onStart(Application app) {
//        Logger.info("Application has started");

        Timer time = new Timer(); // Instantiate Timer Object
        Scheduled scheduled = new Scheduled(); // Instantiate SheduledTask class
        time.schedule(scheduled, 0, period); // Create Repetitively task for every 1 m
    }

    @Override
    public void onStop(Application app) {
//        Logger.info("Application shutdown...");
    }
}