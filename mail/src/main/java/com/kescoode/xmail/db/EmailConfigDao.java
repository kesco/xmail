package com.kescoode.xmail.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.kescoode.xmail.db.internal.DataDelegate;
import com.kescoode.xmail.db.table.EmailConfigSchema;

/**
 * 邮件配置Dao
 *
 * @author Kesco Lin
 */
public class EmailConfigDao extends DataDelegate {
    public static final String TABLE_NAME = "email_config";
    private static final String SQL_CREATE_TABLE = CREATE_TABLE + TABLE_NAME + " ( " + EmailConfigSchema._ID + PRIMARY_KEY +
            EmailConfigSchema.SEND_SERVER + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailConfigSchema.SEND_PORT + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            EmailConfigSchema.SEND_SSL + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            EmailConfigSchema.RECEIVE_SERVER + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailConfigSchema.RECEIVE_PORT + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            EmailConfigSchema.RECEIVE_SSL + TYPE_INTEGER + COLUMN_NOT_NULL + " );";

    public EmailConfigDao(Context context) {
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
