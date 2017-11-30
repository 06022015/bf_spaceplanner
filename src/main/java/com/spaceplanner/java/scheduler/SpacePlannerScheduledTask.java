package com.spaceplanner.java.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;

/**
 * Created by IntelliJ IDEA.
 * User: ashifqureshi
 * Date: 26/06/15
 * Time: 2:31 PM
 * To change this template use File | Settings | File Templates.
 */
@Component
public class SpacePlannerScheduledTask {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Scheduled(cron = "*/5 * * * * ?")
    public void reportCurrentTime() {
        //System.out.println("The time is now " + dateFormat.format(new Date()));
    }
}
