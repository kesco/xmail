package com.kescoode.xmail;

/**
 * App全局常量
 *
 * @author Kesco Lin
 */
public final class AppConstant {
    private AppConstant() {
        throw new UnsupportedOperationException("Could not call the constructor");
    }

    public static class Event {
        private Event() {
            throw new UnsupportedOperationException("Could not call the constructor");
        }

        public static final String BROADCAST = "com.kescoode.xmail.action.EVENT";
    }

    public static class DB {
        private DB() {
            throw new UnsupportedOperationException("Could not call the constructor");
        }

        public static final String NAME = "xmail";
        public static final int VERSION = 1;
        public static final String PROVIDER_AUTHORITY = "com.kescoode.xmail.action.DATA"; /* Db Provider */
    }
}
