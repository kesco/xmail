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
            EmailConfigSchema.SEND + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailConfigSchema.RECEIVE + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailConfigSchema.USE_SUFFIX + TYPE_INTEGER + COLUMN_NOT_NULL + " );";

    public EmailConfigDao(Context context) {
        super(context);
    }

    /**
     * 根据Config ID来
     * @param id Config表ID
     * @return EmailConfig类或者null
     */
    public EmailConfig selectConfigFromDB(int id) {
        Cursor cursor = select(parseUri(TABLE_NAME), "select * from email_config where _id = ?", id);
        if (cursor.getCount() == 0) {
            return null;
        } else {
            return new EmailConfig(cursor);
        }
    }

    /**
     * 把服务器配置存进数据库
     * @param config 服务器配置对象
     * @return 插入列的ID
     */
    public long insertConfig2DB(EmailConfig config) {
        ContentValues values = new ContentValues();
        EmailConfig.SettingRaw raw = config.getDefaultRaw();
        values.put(EmailConfigSchema.DOMAIN, config.domain);
        values.put(EmailConfigSchema.SEND, raw.send);
        values.put(EmailConfigSchema.RECEIVE, raw.receive);
        values.put(EmailConfigSchema.USE_SUFFIX, raw.useSuffix);
        Uri uri = context.getContentResolver().insert(parseUri(TABLE_NAME), values);
        long id = ContentUris.parseId(uri);
        config.setId(id);
        return id;
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
