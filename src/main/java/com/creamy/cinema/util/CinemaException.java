package com.creamy.cinema.util;

import java.util.logging.Level;
import java.util.logging.Logger;

public class CinemaException extends Exception {
    public CinemaException(String message, Exception e) {
        super(message);
        CinemaLogger.getLogger().log(Level.SEVERE, message, e);
    }
}
