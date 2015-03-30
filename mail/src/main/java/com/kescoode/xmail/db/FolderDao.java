package com.kescoode.xmail.db;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.fsck.k9.mail.MessagingException;
import com.kescoode.xmail.db.internal.DataDelegate;
import com.kescoode.xmail.db.table.FolderSchema;
import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.domain.LocalFolder;

/**
 * 邮件文件夹Dao
 *
 * @author Kesco Lin
 */
public class FolderDao extends DataDelegate {
    public static final String TABLE_NAME = "folder";
    private static final String SQL_CREATE_TABLE = CREATE_TABLE + TABLE_NAME + " ( " + FolderSchema._ID + PRIMARY_KEY +
            FolderSchema.ACCOUNT_ID + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            FolderSchema.NAME + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            FolderSchema.TOTAL_COUNT + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            FolderSchema.UNREAD_COUNT + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            FolderSchema.FLAGGED_COUNT + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            FolderSchema.UPDATE_TIME + TYPE_INTEGER + " );";

    public FolderDao(Context context) {
        super(context);
    }

    /**
     * 保存邮件文件夹进数据库
     *
     * @param folder 文件夹
     * @return 插入ID
     */
    public long insertFolder2DB(LocalFolder folder) {
        ContentValues values = new ContentValues();
        try {
            values.put(FolderSchema.ACCOUNT_ID, folder.getAccountId());
            values.put(FolderSchema.NAME, folder.getName());
            values.put(FolderSchema.TOTAL_COUNT, folder.getMessageCount());
            values.put(FolderSchema.UNREAD_COUNT, folder.getUnreadMessageCount());
            values.put(FolderSchema.FLAGGED_COUNT, folder.getFlaggedMessageCount());
            values.put(FolderSchema.UPDATE_TIME, folder.getUpdateTime());
        } catch (MessagingException e) {
            /* 永远不会触发 */
        }

        Uri uri = context.getContentResolver().insert(parseUri(TABLE_NAME), values);
        long index = ContentUris.parseId(uri);
        folder.setId(index);
        return index;
    }

    /**
     * 获取该帐户的所有文件夹
     *
     * @param account 邮件帐户
     * @return 文件夹数组
     */
    public LocalFolder[] selectFoldersFromDB(Account account) {
        Cursor cursor = select(parseUri(TABLE_NAME),
                "select * from folder where account_id = ?", account.getId());
        int num = cursor.getCount();
        LocalFolder[] folders = new LocalFolder[num];
        if (num > 0) {
            int index = 0;
            if (cursor.moveToFirst()) {
                do {
                    folders[index] = new LocalFolder(context, account, cursor);
                    index += 1;
                } while (cursor.moveToNext());
            } else {
                throw new RuntimeException("DB cannot load folders");
            }
        }
        cursor.close();
        return folders;
    }

    public LocalFolder selectFolder4Name(Account account, String name) {
        Cursor cursor = select(parseUri(TABLE_NAME), "select * from folder where name = ? and account_id = ?",
                name,account.getId());
        if (cursor.moveToFirst()) {
            return new LocalFolder(context, account, cursor);
        } else {
            throw new RuntimeException("DB cannot load the folder");
        }
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
