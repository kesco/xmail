package com.kescoode.xmail.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.kescoode.xmail.db.internal.DataDelegate;
import com.kescoode.xmail.db.table.AttachmentSchema;

/**
 * 附件Dao
 *
 * @author Kesco Lin
 */
public class AttachmentDao extends DataDelegate {
    public static final String TABLE_NAME = "attachment";
    private static final String SQL_CREATE_TABLE = CREATE_TABLE + TABLE_NAME + " ( " + AttachmentSchema._ID + PRIMARY_KEY +
            AttachmentSchema.EMAIL_ID + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            AttachmentSchema.NAME + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            AttachmentSchema.MIME + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            AttachmentSchema.PATH + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            AttachmentSchema.SIZE + TYPE_INTEGER + COLUMN_NOT_NULL + " );";

    public AttachmentDao(Context context) {
        super(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        /* 目前还是初版，没有升级数据库逻辑 */
    }

}
