package com.kescoode.xmail.db.table;

import android.provider.BaseColumns;

/**
 * 邮件配置表
 *
 * @author Kesco Lin
 */
public class EmailConfigSchema implements BaseColumns {
    public static final String DOMAIN = "domain";
    public static final String SEND = "send";
    public static final String RECEIVE = "receive";
    public static final String USE_SUFFIX = "use_suffix";

    private EmailConfigSchema() {
        throw new RuntimeException("Can not be initial.");
    }

}
