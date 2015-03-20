package com.kescoode.xmail;

/**
 * App全局常量
 *
 * @author Kesco Lin
 */
public final class AppConstant {
    private AppConstant() {
        /* Empty */
    }

    public static class DB {
        private DB() {
            /* Empty */
        }

        public static final String NAME = "xmail";
        public static final int VERSION = 1;
        public static final String PROVIDER_AUTHORITY = "com.kescoode.xmail.action.DATA"; /* Db Provider */
    }
}
