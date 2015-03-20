package com.kescoode.xmail.db.table;

import android.provider.BaseColumns;

/**
 * 邮件配置表
 *
 * @author Kesco Lin
 */
public class EmailConfigSchema implements BaseColumns {
    public static final String SEND_SERVER = "send_server";
    public static final String SEND_PORT = "send_port";
    public static final String SEND_SSL = "send_ssl";
    public static final String RECEIVE_SERVER = "receive_sever";
    public static final String RECEIVE_PORT = "receive_port";
    public static final String RECEIVE_SSL = "receive_ssl";

    private EmailConfigSchema() {
        throw new RuntimeException("Can not be initial.");
    }

}
