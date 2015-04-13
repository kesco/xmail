package com.kescoode.xmail.domain;

import android.content.Context;
import android.database.Cursor;
import com.fsck.k9.mail.*;
import com.kescoode.xmail.db.EmailDao;

import java.util.*;

/**
 * 邮件文件夹本地映射的业务对象
 *
 * @author Kesco Lin
 */
public class LocalFolder extends Folder<LocalEmail> {
    private final Context context;
    private final Account account;
    private final EmailDao emailDao;

    private volatile long id = -1;
    private final String name;
    private int totalCount;
    private int unReadCount;
    private int flaggedCount;

    public LocalFolder(Context context, Account account, Cursor cursor) {
        this.context = context;
        this.account = account;
        this.emailDao = new EmailDao(context);
        this.id = cursor.getLong(0);
        this.name = cursor.getString(2);
        this.totalCount = cursor.getInt(3);
        this.unReadCount = cursor.getInt(4);
        this.flaggedCount = cursor.getInt(5);
        setLastUpdate(cursor.getLong(6));
    }

    public LocalFolder(Context context, Account account, Folder remote) throws MessagingException {
        this.context = context;
        this.account = account;
        this.emailDao = new EmailDao(context);
        this.name = remote.getName();
        this.totalCount = remote.getMessageCount();
        this.unReadCount = remote.getUnreadMessageCount();
        this.flaggedCount = remote.getFlaggedMessageCount();
        setLastUpdate(remote.getLastUpdate());
    }

    @Override

    public void open(int mode) throws MessagingException {

    }

    @Override
    public void close() {
        /* Nothing. */
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public int getMode() {
        return OPEN_MODE_RW;
    }

    @Override
    public boolean create(FolderType type) throws MessagingException {
        return false;
    }

    @Override
    public boolean exists() throws MessagingException {
        return true;
    }


    @Override
    public int getMessageCount() throws MessagingException {
        return totalCount;
    }

    @Override
    public int getUnreadMessageCount() throws MessagingException {
        return unReadCount;
    }

    @Override
    public int getFlaggedMessageCount() throws MessagingException {
        return flaggedCount;
    }

    public void setMessageCount(int total) {
        this.totalCount = total;
    }

    public void setUnreadMessageCount(int unread) {
        this.unReadCount = unread;
    }

    public void setFlaggedCount(int flagged) {
        this.flaggedCount = flagged;
    }

    @Override
    public LocalEmail getMessage(String uid) throws MessagingException {
        return emailDao.selectMailsFromUid(this, uid);
    }

    @Override
    public List<LocalEmail> getMessages(int start, int end, Date earliestDate, MessageRetrievalListener<LocalEmail> listener) throws MessagingException {
        return null;
    }

    @Override
    public List<LocalEmail> getMessages(MessageRetrievalListener<LocalEmail> listener) throws MessagingException {
        return null;
    }

    @Override
    public List<LocalEmail> getMessages(String[] uids, MessageRetrievalListener<LocalEmail> listener) throws MessagingException {
        return null;
    }

    @Override
    public Map<String, String> appendMessages(List<? extends Message> messages) throws MessagingException {
        open(OPEN_MODE_RW);
        if (getMode() != OPEN_MODE_RW) {
            throw new RuntimeException("Can not open the folder");
        }
        // TODO: 思考下，能用来做什么
        Map<String, String> uidMap = new HashMap<>();
        for (Message remote : messages) {
            LocalEmail email = new LocalEmail(context, this, remote);
            if (getMessage(remote.getUid()) == null) {
                emailDao.insertMail2DB(email);
            } else {
                emailDao.updateMail2DB(email);
            }
        }
        return uidMap;
    }

    @Override
    public void setFlags(List<? extends Message> messages, Set<Flag> flags, boolean value) throws MessagingException {

    }

    @Override
    public void setFlags(Set<Flag> flags, boolean value) throws MessagingException {

    }

    @Override
    public String getUidFromMessageId(Message message) throws MessagingException {
        return null;
    }

    @Override
    public void fetch(List<LocalEmail> messages, FetchProfile fp, MessageRetrievalListener<LocalEmail> listener) throws MessagingException {

    }

    @Override
    public void delete(boolean recurse) throws MessagingException {

    }

    @Override
    public String getName() {
        return name;
    }

    public synchronized void setLastUpdate(long current) {
        try {
            /* 因为我们没有做Push的功能现在，所以先用LastChecked代替 */
            setLastChecked(current);
        } catch (MessagingException e) {
            /* 不会发生的异常 */
        }
    }

    public long getAccountId() {
        return account.getId();
    }

    public long getId() {
        if (id == -1) {
            throw new UnsupportedOperationException("The folder has not been inserted into database.");
        }
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
