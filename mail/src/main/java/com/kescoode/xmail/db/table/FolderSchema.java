package com.kescoode.xmail.db.table;

import android.provider.BaseColumns;

/**
 * 邮件文件夹表
 *
 * @author Kesco Lin
 */
public class FolderSchema implements BaseColumns {
    public static final String ACCOUNT_ID = "account_id";
    public static final String NAME = "name";
    public static final String TOTAL_COUNT = "total_count";
    public static final String UNREAD_COUNT = "unread_count";
    public static final String FLAGGED_COUNT = "flagged_count";
    public static final String UPDATE_TIME = "update_time";

    private FolderSchema() {
        throw new RuntimeException("Can not be initial.");
    }
}
