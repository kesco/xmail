package com.kescoode.xmail.db.internal;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.kescoode.xmail.AppConstant;

import java.util.Objects;

/**
 * Dao层操作基类
 *
 * @author Kesco Lin
 */
public abstract class DataDelegate {
    protected static final String CREATE_TABLE = "create table ";
    protected static final String PRIMARY_KEY = " integer primary key, ";
    protected static final String TYPE_TEXT = " text";
    protected static final String TYPE_INTEGER = " integer";
    protected static final String COLUMN_NOT_NULL = " not null";

    protected final Context context;

    public DataDelegate(Context context) {
        this.context = context;
    }

    public abstract void onCreate(SQLiteDatabase db);

    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

    protected Uri parseUri(String table) {
        return Uri.parse("content://" + AppConstant.DB.PROVIDER_AUTHORITY + "/" + table);
    }

    /**
     * 用{@link android.content.ContentResolver}表达的Select语句
     *
     * @param uri  ContentProvider处理的URI
     * @param sql  执行的SQL语句
     * @param args SQL参数
     * @return 游标
     */
    protected Cursor select(Uri uri, String sql, @NonNull Object... args) {
        String[] sArgs = new String[args.length];
        for (int i = 0; i < args.length; i++) {
            sArgs[i] = String.valueOf(args[i]);
        }
        return context.getContentResolver().query(uri, null, sql, sArgs, null);
    }

}
