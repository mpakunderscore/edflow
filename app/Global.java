import play.*;
import utils.ScheduledTask;

import java.util.Timer;

public class Global extends GlobalSettings {

    @Override
    public void onStart(Application app) {
//        Logger.info("Application has started");

        Timer time = new Timer(); // Instantiate Timer Object
        ScheduledTask st = new ScheduledTask(); // Instantiate SheduledTask class
        time.schedule(st, 0, 60*1000); // Create Repetitively task for every 1 m
    }

    @Override
    public void onStop(Application app) {
//        Logger.info("Application shutdown...");
    }
}