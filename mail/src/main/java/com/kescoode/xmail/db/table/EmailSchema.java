package com.kescoode.xmail.db.table;

import android.provider.BaseColumns;

/**
 * Email表
 *
 * @author Kesco Lin
 */
public class EmailSchema implements BaseColumns {
    public static final String UID = "uid";
    public static final String FOLDER_ID = "folder_id";
    public static final String FROM_LIST = "from_list";
    public static final String TO_LIST = "to_list";
    public static final String CC_LIST = "cc_list";
    public static final String BCC_LIST = "bcc_list";
    public static final String IS_READ = "is_read";
    public static final String IS_FLAGGED = "is_flagged";
    public static final String IS_FORWARD = "is_forward";
    public static final String TEXT_PATH = "text_path";
    public static final String HTML_PATH = "html_path";
    public static final String PREVIEW = "preview";
    public static final String SUBJECT = "subject";
    public static final String DATE = "date";
    public static final String STATUS = "status";

    private EmailSchema() {
        throw new RuntimeException("Can not be initial.");
    }
}
