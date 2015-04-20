package com.kescoode.adk.log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Log开关类
 *
 * @author Kesco Lin
 */
public class Logger {
    protected static final String LOG_TAG = Logger.class.getCanonicalName();
    private static List<LogTrunk> trunks = new CopyOnWriteArrayList<>();

    private Logger() {
        /* Empty */
    }

    public static void i(String msg,Object... args) {
        for (LogTrunk trunk : trunks) {
            trunk.i(msg, args);
        }
    }

    public static void d(String msg,Object... args) {
        for (LogTrunk trunk : trunks) {
            trunk.d(msg, args);
        }
    }

    public static void e(String msg,Object... args) {
        for (LogTrunk trunk : trunks) {
            trunk.e(msg, args);
        }
    }

    public static void w(String msg,Object... args) {
        for (LogTrunk trunk : trunks) {
            trunk.w(msg, args);
        }
    }

    public static void appendTrunk(LogTrunk trunk) {
        trunks.add(trunk);
    }

    public static List<LogTrunk> getTrunks() {
        return trunks;
    }

    public static void clearTrunk() {
        trunks.clear();
    }

    public static abstract class LogTrunk {
        protected abstract void i(String msg,Object... args);
        protected abstract void d(String msg,Object... args);
        protected abstract void e(String msg,Object... args);
        protected abstract void w(String msg,Object... args);
    }

}
