package com.kescoode.xmail.db;

import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import com.fsck.k9.mail.Address;
import com.fsck.k9.mail.Message;
import com.fsck.k9.mail.MessagingException;
import com.kescoode.xmail.db.internal.DataDelegate;
import com.kescoode.xmail.db.table.EmailSchema;
import com.kescoode.xmail.domain.LocalEmail;
import com.kescoode.xmail.domain.LocalFolder;

/**
 * 邮件Dao
 *
 * @author Kesco Lin
 */
public class EmailDao extends DataDelegate {
    public static final String TABLE_NAME = "email";
    private static final String SQL_CREATE_TABLE = CREATE_TABLE + TABLE_NAME + " ( " + EmailSchema._ID + PRIMARY_KEY +
            EmailSchema.UID + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
            EmailSchema.FOLDER_ID + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
            EmailSchema.FROM_LIST + TYPE_TEXT + COLUMN_NOT_NULL + ", " +
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
            EmailSchema.DATE + TYPE_INTEGER + COLUMN_NOT_NULL + ", " +
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
    public long insertMail2DB(LocalEmail mail) {
        ContentValues values = new ContentValues();
        values.put(EmailSchema.UID, mail.getUid());
        values.put(EmailSchema.FOLDER_ID, mail.getFolderId());
        values.put(EmailSchema.FROM_LIST, Address.pack(mail.getFrom()));
        try {
            values.put(EmailSchema.TO_LIST, Address.pack(mail.getRecipients(Message.RecipientType.TO)));
            values.put(EmailSchema.CC_LIST, Address.pack(mail.getRecipients(Message.RecipientType.CC)));
            values.put(EmailSchema.BCC_LIST, Address.pack(mail.getRecipients(Message.RecipientType.BCC)));
        } catch (MessagingException e) {
            /* 不会发生的异常 */
        }
        values.put(EmailSchema.IS_READ, mail.isRead());
        values.put(EmailSchema.IS_FLAGGED, mail.isFlagged());
        values.put(EmailSchema.IS_FORWARD, mail.isForward());
        values.put(EmailSchema.TEXT_PATH, mail.getTextPath());
        values.put(EmailSchema.HTML_PATH, mail.getHtmlPath());
        values.put(EmailSchema.PREVIEW, mail.getPreview());
        values.put(EmailSchema.SUBJECT, mail.getSubject());
        values.put(EmailSchema.DATE, mail.getInternalDate().getTime());
        values.put(EmailSchema.STATUS, mail.getStatus());

        Uri uri = context.getContentResolver().insert(parseUri(TABLE_NAME), values);
        long index = ContentUris.parseId(uri);
        mail.setId(index);
        return index;
    }

    /**
     * 更新数据库内的邮件
     *
     * @param mail 邮件业务对象
     * @return 更新的ID
     */
    public int updateMail2DB(LocalEmail mail) {
        ContentValues values = new ContentValues();
        values.put(EmailSchema.UID, mail.getUid());
        values.put(EmailSchema.FOLDER_ID, mail.getFolderId());
        values.put(EmailSchema.FROM_LIST, Address.pack(mail.getFrom()));
        try {
            values.put(EmailSchema.TO_LIST, Address.pack(mail.getRecipients(Message.RecipientType.TO)));
            values.put(EmailSchema.CC_LIST, Address.pack(mail.getRecipients(Message.RecipientType.CC)));
            values.put(EmailSchema.BCC_LIST, Address.pack(mail.getRecipients(Message.RecipientType.BCC)));
        } catch (MessagingException e) {
            /* 不会发生的异常 */
        }
        values.put(EmailSchema.IS_READ, mail.isRead());
        values.put(EmailSchema.IS_FLAGGED, mail.isFlagged());
        values.put(EmailSchema.IS_FORWARD, mail.isForward());
        values.put(EmailSchema.TEXT_PATH, mail.getTextPath());
        values.put(EmailSchema.HTML_PATH, mail.getHtmlPath());
        values.put(EmailSchema.PREVIEW, mail.getPreview());
        values.put(EmailSchema.SUBJECT, mail.getSubject());
        values.put(EmailSchema.DATE, mail.getInternalDate().getTime());
        values.put(EmailSchema.STATUS, mail.getStatus());
        return context.getContentResolver().update(parseUri(TABLE_NAME), values, "uid = ?",
                new String[]{String.valueOf(mail.getUid())});
    }

    /**
     * 获取文件夹里的所有邮件
     *
     * @param folder 文件夹对象
     * @return 邮件对象数组
     */
    public LocalEmail[] selectMails4Folder(LocalFolder folder) {
        Cursor cursor = select(parseUri(TABLE_NAME), "select * from email where folder_id = ?", folder.getId());
        int num = cursor.getCount();
        LocalEmail[] mails = new LocalEmail[num];
        if (num > 0) {
            int index = 0;
            if (cursor.moveToFirst()) {
                do {
                    mails[index] = new LocalEmail(context, folder, cursor);
                    index += 1;
                } while (cursor.moveToNext());
            } else {
                throw new RuntimeException("DB cannot load emails");
            }
        }
        cursor.close();
        return mails;
    }

    /**
     * 根据邮件ID获取邮件
     *
     * @param folder 所属邮箱文件夹
     * @param id     邮件ID
     * @return 邮件对象
     */
    public LocalEmail selectMailsFromId(LocalFolder folder, long id) {
        Cursor cursor;
        LocalEmail email = null;
        cursor = select(parseUri(TABLE_NAME), "select * from email where _id = ?", id);
        if (cursor.moveToFirst()) {
            email = new LocalEmail(context, folder, cursor);
        }
        cursor.close();
        return email;
    }

    /**
     * 根据邮件UID获取邮件
     *
     * @param folder 所属邮箱文件夹
     * @param uid 邮件UID
     * @return 邮件对象
     */
    public LocalEmail selectMailsFromUid(LocalFolder folder, String uid) {
        Cursor cursor;
        LocalEmail email = null;
        cursor = select(parseUri(TABLE_NAME), "select * from email where uid = ?", uid);
        if (cursor.moveToFirst()) {
            email = new LocalEmail(context, folder, cursor);
        }
        cursor.close();
        return email;
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
