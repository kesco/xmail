package com.kescoode.xmail.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.kescoode.xmail.db.internal.DataDelegate;
import com.kescoode.xmail.db.table.FolderSchema;

/**
 * 邮件文件夹Dao
 *
 * @author Kesco Lin
 */
public class FolderDao extends DataDelegate{
    public static final String TABLE_NAME = "folder";
    private static final String SQL_CREATE_TABLE = CREATE_TABLE + TABLE_NAME + " ( " + FolderSchema._ID + PRIMARY_KEY +
            FolderSchema.ACCOUNT_ID + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            FolderSchema.NAME + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            FolderSchema.TOTAL_COUNT + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            FolderSchema.FLAGGED_COUNT + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            FolderSchema.UPDATE_TIME + TYPE_INTEGER + " );";

    public FolderDao(Context context) {
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
