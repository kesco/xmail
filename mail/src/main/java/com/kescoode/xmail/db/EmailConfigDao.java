package com.kescoode.xmail.db;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.kescoode.xmail.db.internal.DataDelegate;
import com.kescoode.xmail.db.table.EmailConfigSchema;
import com.kescoode.xmail.domain.EmailConfig;

/**
 * 邮件配置Dao
 *
 * @author Kesco Lin
 */
public class EmailConfigDao extends DataDelegate {
    public static final String TABLE_NAME = "email_config";
    private static final String SQL_CREATE_TABLE = CREATE_TABLE + TABLE_NAME + " ( " + EmailConfigSchema._ID + PRIMARY_KEY +
            EmailConfigSchema.DOMAIN + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailConfigSchema.SEND_SERVER + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailConfigSchema.SEND_PORT + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            EmailConfigSchema.RECEIVE_SERVER + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailConfigSchema.RECEIVE_PORT + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            EmailConfigSchema.USE_SSL+ TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            EmailConfigSchema.USE_SUFFIX + TYPE_INTEGER + COLUMN_NOT_NULL + " );";

    public EmailConfigDao(Context context) {
        super(context);
    }

    public EmailConfig selectConfig2DB(int id) {
        return null;
    }

    public EmailConfig[] selectConfig2DB(String domain) {
        Cursor cursor = context.getContentResolver().query(parseUri(TABLE_NAME), null,
                EmailConfigSchema.DOMAIN + "= ?", new String[]{domain}, null);

        return null;
    }

    public long insertConfig2DB(EmailConfig config) {
        ContentValues values = new ContentValues();
        values.put(EmailConfigSchema.DOMAIN, config.getDomain());
        values.put(EmailConfigSchema.SEND_SERVER, config.getSendServer());
        values.put(EmailConfigSchema.SEND_PORT, config.getSendPort());
        values.put(EmailConfigSchema.RECEIVE_SERVER, config.getReceiveServer());
        values.put(EmailConfigSchema.RECEIVE_PORT, config.getReceivePort());
        values.put(EmailConfigSchema.USE_SSL, config.isUseSsl());
        values.put(EmailConfigSchema.USE_SUFFIX, config.isUseSuffix());

        Uri uri = context.getContentResolver().insert(parseUri(TABLE_NAME), values);
        return ContentUris.parseId(uri);
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
