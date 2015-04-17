package com.kescoode.xmail.db.internal;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;
import android.os.Looper;
import android.util.SparseArray;

import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.AppConstant.DB;
import com.kescoode.xmail.AppCtx;
import com.kescoode.xmail.db.*;

import java.net.URI;


public class DataProvider extends ContentProvider {
    private static final String MIME_SINGLE = "vnd.android.cursor.item/";
    private static final String MIME_MULTIPLE = "vnd.android.cursor.dir/";

    private static final int NUM_ITEM_ACCOUNT = 0;
    private static final int NUM_DIR_ACCOUNT = 1;
    private static final int NUM_ITEM_ATTACHMENT = 2;
    private static final int NUM_DIR_ATTACHMENT = 3;
    private static final int NUM_ITEM_CONTACT = 4;
    private static final int NUM_DIR_CONTACT = 5;
    private static final int NUM_ITEM_EMAIL_CONFIG = 6;
    private static final int NUM_DIR_EMAIL_CONFIG = 7;
    private static final int NUM_ITEM_EMAIL = 8;
    private static final int NUM_DIR_EMAIL = 9;
    private static final int NUM_ITEM_FOLDER = 10;
    private static final int NUM_DIR_FOLDER = 11;
    private static final int NUM_ITEM_TASK = 12;
    private static final int NUM_DIR_TASK = 13;


    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);
    private static final SparseArray<String> TABLES = new SparseArray<>();

    static {
        URI_MATCHER.addURI(DB.PROVIDER_AUTHORITY, AccountDao.TABLE_NAME + "/#", NUM_ITEM_ACCOUNT);
        URI_MATCHER.addURI(DB.PROVIDER_AUTHORITY, AccountDao.TABLE_NAME, NUM_DIR_ACCOUNT);
        URI_MATCHER.addURI(DB.PROVIDER_AUTHORITY, AttachmentDao.TABLE_NAME + "/#", NUM_ITEM_ATTACHMENT);
        URI_MATCHER.addURI(DB.PROVIDER_AUTHORITY, AttachmentDao.TABLE_NAME, NUM_DIR_ATTACHMENT);
        URI_MATCHER.addURI(DB.PROVIDER_AUTHORITY, EmailConfigDao.TABLE_NAME + "/#", NUM_ITEM_EMAIL_CONFIG);
        URI_MATCHER.addURI(DB.PROVIDER_AUTHORITY, EmailConfigDao.TABLE_NAME, NUM_DIR_EMAIL_CONFIG);
        URI_MATCHER.addURI(DB.PROVIDER_AUTHORITY, EmailDao.TABLE_NAME + "/#", NUM_ITEM_EMAIL);
        URI_MATCHER.addURI(DB.PROVIDER_AUTHORITY, EmailDao.TABLE_NAME, NUM_DIR_EMAIL);
        URI_MATCHER.addURI(DB.PROVIDER_AUTHORITY, FolderDao.TABLE_NAME + "/#", NUM_ITEM_FOLDER);
        URI_MATCHER.addURI(DB.PROVIDER_AUTHORITY, FolderDao.TABLE_NAME, NUM_DIR_FOLDER);

        TABLES.append(NUM_ITEM_ACCOUNT, AccountDao.TABLE_NAME);
        TABLES.append(NUM_DIR_ACCOUNT, AccountDao.TABLE_NAME);
        TABLES.append(NUM_ITEM_ATTACHMENT, AttachmentDao.TABLE_NAME);
        TABLES.append(NUM_DIR_ATTACHMENT, AttachmentDao.TABLE_NAME);
        TABLES.append(NUM_ITEM_EMAIL_CONFIG, EmailConfigDao.TABLE_NAME);
        TABLES.append(NUM_DIR_EMAIL_CONFIG, EmailConfigDao.TABLE_NAME);
        TABLES.append(NUM_ITEM_EMAIL, EmailDao.TABLE_NAME);
        TABLES.append(NUM_DIR_EMAIL, EmailDao.TABLE_NAME);
        TABLES.append(NUM_ITEM_FOLDER, FolderDao.TABLE_NAME);
        TABLES.append(NUM_DIR_FOLDER, FolderDao.TABLE_NAME);
    }

    private DBManager DBManager;

    public DataProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int n = URI_MATCHER.match(uri);
        String table = TABLES.get(n);
        if (n % 2 != 0) {
            throw new RuntimeException("The mime type get from the uri must be an even number");
        }
        int index = DBManager.getWritableDatabase().delete(table, selection, selectionArgs);
        Logger.d("Uri: %s,\nReturn: %d", uri.toString(), index);
        return index;
    }

    @Override
    public String getType(Uri uri) {
        int index = URI_MATCHER.match(uri);
        String type = getMimePrefix(index) + TABLES.get(index);
        Logger.d("Uri: %s,\nReturn: %s", uri.toString(), type);
        return type;
    }

    private String getMimePrefix(int num) {
        int remainder = num % 2;
        if (remainder == 0) {
            return MIME_SINGLE;
        } else {
            return MIME_MULTIPLE;
        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        int n = URI_MATCHER.match(uri);
        String table = TABLES.get(n);
        if (n % 2 == 0) {
            throw new RuntimeException("The mime type get from the uri can not be an even number");
        }
        long index = DBManager.getWritableDatabase().insert(table, null, values);
        Logger.d("Uri: %s,\nReturn: %d", uri.toString(), index);
        return Uri.parse(DB.PROVIDER_AUTHORITY + "/" + table + "/" + index);
    }

    @Override
    public boolean onCreate() {
        /* 目前是单数据库，不排除以后会升级到多数据库 */
        DBManager = new DBManager(getContext(), DB.NAME, DB.VERSION);

        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        int n = URI_MATCHER.match(uri);
        String table = TABLES.get(n);
        Logger.d("Uri: %s,\nTable: %s,\nSQL: %s", uri.toString(), table, selection);
        /* 采用手写SQL */
        return DBManager.getReadableDatabase().rawQuery(selection, selectionArgs);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        int n = URI_MATCHER.match(uri);
        String table = TABLES.get(n);
//        if (n % 2 != 0) {
//            throw new RuntimeException("The mime type get from the uri must be an even number");
//        }
        int index = DBManager.getWritableDatabase().update(table, values, selection, selectionArgs);
        Logger.d("Uri: %s,\nReturn: %d", uri.toString(), index);
        return index;
    }

}
