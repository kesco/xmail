package com.kescoode.xmail.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.kescoode.xmail.db.internal.DataDelegate;
import com.kescoode.xmail.db.table.AccountSchema;
import com.kescoode.xmail.domain.Account;

/**
 * 邮件用户Dao
 *
 * @author Kesco Lin
 */
public class AccountDao extends DataDelegate {
    public static final String TABLE_NAME = "account";
    private static final String SQL_CREATE_TABLE = DataDelegate.CREATE_TABLE + TABLE_NAME + " ( " + AccountSchema._ID +
            PRIMARY_KEY + AccountSchema.NAME + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            AccountSchema.PASSWD + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            AccountSchema.CONFIG_ID + TYPE_INTEGER + COLUMN_NOT_NULL + " );";

    public AccountDao(Context context) {
        super(context);
    }

    /**
     * 所有登录的邮箱帐户
     *
     * @return 已登录的帐户数组
     */
    public Account[] getAllAccounts() {
        Cursor cursor = select(parseUri(TABLE_NAME), "select * from account", new String[]{});
        Account[] accounts = new Account[cursor.getCount()];
        if (accounts.length != 0 && cursor.moveToFirst()) {
            for (int i = 0; i < accounts.length; i++) {
                accounts[i] = new Account(context, cursor);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return accounts;
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
