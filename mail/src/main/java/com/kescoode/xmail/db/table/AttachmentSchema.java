package com.kescoode.xmail.db.table;

import android.provider.BaseColumns;

/**
 * 附件表
 *
 * @author Kesco Lin
 */
public class AttachmentSchema implements BaseColumns {
    public static final String NAME = "name";
    public static final String EMAIL_ID = "email_id";
    public static final String MIME = "mime";
    public static final String PATH = "path";
    public static final String SIZE = "size";

    private AttachmentSchema() {
        throw new RuntimeException("Can not be initial.");
    }

}
