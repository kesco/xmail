package com.kescoode.xmail.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.kescoode.xmail.db.internal.DataDelegate;
import com.kescoode.xmail.db.table.EmailSchema;
import com.kescoode.xmail.domain.LocalMail;

/**
 * 邮件Dao
 *
 * @author Kesco Lin
 */
public class EmailDao extends DataDelegate {
    public static final String TABLE_NAME = "email";
    private static final String SQL_CREATE_TABLE = CREATE_TABLE + TABLE_NAME + " ( " + EmailSchema._ID + PRIMARY_KEY +
            EmailSchema.UID + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailSchema.ACCOUNT_ID + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            EmailSchema.SENDER_LIST + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailSchema.TO_LIST + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailSchema.CC_LIST + TYPE_TEXT + ", " +
            EmailSchema.BCC_LIST + TYPE_TEXT + ", " +
            EmailSchema.IS_READ + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            EmailSchema.IS_FLAGGED + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            EmailSchema.IS_FORWARD + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            EmailSchema.TEXT_PATH + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailSchema.HTML_PATH + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailSchema.PREVIEW + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailSchema.SUBJECT + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailSchema.UPDATE_TIME + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            EmailSchema.STATUS + TYPE_INTEGER + COLUMN_NOT_NULL + " );";


    public EmailDao(Context context) {
        super(context);
    }

    /**
     * 把邮件存进数据库
     *
     * @param mail 邮件业务对象
     * @return 插入的ID
     */
    public long insertMail2DB(LocalMail mail) {
        throw new UnsupportedOperationException("have not code");
    }

    /**
     * 更新数据库内的邮件
     *
     * @param mail 邮件业务对象
     * @return 更新的ID
     */
    public int updateMail2DB(LocalMail mail) {
        throw new UnsupportedOperationException("have not code");
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
