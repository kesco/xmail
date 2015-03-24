package com.kescoode.xmail.domain;

import com.fsck.k9.mail.*;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 邮件文件夹本地映射的业务对象
 *
 * @author Kesco Lin
 */
public class LocalFolder extends Folder<LocalMail> {

    @Override
    public void open(int mode) throws MessagingException {

    }

    @Override
    public void close() {

    }

    @Override
    public boolean isOpen() {
        return false;
    }

    @Override
    public int getMode() {
        return 0;
    }

    @Override
    public boolean create(FolderType type) throws MessagingException {
        return false;
    }

    @Override
    public boolean exists() throws MessagingException {
        return false;
    }

    @Override
    public int getMessageCount() throws MessagingException {
        return 0;
    }

    @Override
    public int getUnreadMessageCount() throws MessagingException {
        return 0;
    }

    @Override
    public int getFlaggedMessageCount() throws MessagingException {
        return 0;
    }

    @Override
    public LocalMail getMessage(String uid) throws MessagingException {
        return null;
    }

    @Override
    public List<LocalMail> getMessages(int start, int end, Date earliestDate, MessageRetrievalListener<LocalMail> listener) throws MessagingException {
        return null;
    }

    @Override
    public List<LocalMail> getMessages(MessageRetrievalListener<LocalMail> listener) throws MessagingException {
        return null;
    }

    @Override
    public List<LocalMail> getMessages(String[] uids, MessageRetrievalListener<LocalMail> listener) throws MessagingException {
        return null;
    }

    @Override
    public Map<String, String> appendMessages(List<? extends Message> messages) throws MessagingException {
        return null;
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
    public void fetch(List<LocalMail> messages, FetchProfile fp, MessageRetrievalListener<LocalMail> listener) throws MessagingException {

    }

    @Override
    public void delete(boolean recurse) throws MessagingException {

    }

    @Override
    public String getName() {
        return null;
    }

}
