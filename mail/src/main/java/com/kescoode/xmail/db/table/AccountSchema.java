package com.kescoode.xmail.db.table;

import android.provider.BaseColumns;

/**
 * 邮件用户表
 *
 * @author Kesco Lin
 */
public class AccountSchema implements BaseColumns {
    public static final String NAME = "name";
    public static final String PASSWD = "passwd";
    public static final String CONFIG_ID = "config_id";

    private AccountSchema() {
        throw new RuntimeException("Can not be initial.");
    }

}
