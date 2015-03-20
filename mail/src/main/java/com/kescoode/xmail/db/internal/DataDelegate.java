package com.kescoode.xmail.db.internal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

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

    private final Context context;

    public DataDelegate(Context context) {
        this.context = context;
    }

    public abstract void onCreate(SQLiteDatabase db);

    public abstract void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion);

}
