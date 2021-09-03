package com.saamba.helpers;

import org.apache.log4j.Logger;

import java.text.MessageFormat;

public class SaambaLog {

    private Logger log;

    public SaambaLog(Class caller) {
        log = Logger.getLogger(caller);
    }

    public void warn(String error) {
        log.warn(MessageFormat.format("Warn: {0}", error));
    }

    public void trace(String error) {
        log.trace(MessageFormat.format("Error: {0}", error));
    }

    public void error(String error) {
        log.error(MessageFormat.format("Error: {0}", error));
    }

    public void fatal(String error) {
        log.fatal(MessageFormat.format("FATAL: {0}", error));
    }

    public void info(String message) {
        log.warn(MessageFormat.format("Info: {0}", message));
    }
}
