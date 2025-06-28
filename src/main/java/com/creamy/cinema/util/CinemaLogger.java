package com.creamy.cinema.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.LogManager;
import java.util.logging.Logger;

public class CinemaLogger {
    private static Logger logger;

    public static Logger getLogger() {
        return logger;
    }

    public static void init() {
        try {
            new File(System.getProperty("catalina.base") + "/logs/cinema").mkdirs();
            LogManager.getLogManager().readConfiguration(CinemaLogger.class.getClassLoader().getResourceAsStream("logging.properties"));
            logger = Logger.getLogger("CinemaLogger");
            System.out.println("Cinema Exception properly initiated.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
