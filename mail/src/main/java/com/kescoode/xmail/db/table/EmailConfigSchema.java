package com.kescoode.xmail.db.table;

import android.provider.BaseColumns;

/**
 * 邮件配置表
 *
 * @author Kesco Lin
 */
public class EmailConfigSchema implements BaseColumns {
    public static final String DOMAIN = "domain";
    public static final String SEND_SERVER = "send_server";
    public static final String SEND_PORT = "send_port";
    public static final String RECEIVE_SERVER = "receive_server";
    public static final String RECEIVE_PORT = "receive_port";
    public static final String USE_SSL = "use_ssl";
    public static final String USE_SUFFIX = "use_suffix";

    private EmailConfigSchema() {
        throw new RuntimeException("Can not be initial.");
    }

}
