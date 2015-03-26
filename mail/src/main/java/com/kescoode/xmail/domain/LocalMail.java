package com.kescoode.xmail.domain;

import android.content.Context;

import com.fsck.k9.mail.internet.MimeMessage;

/**
 * 本地邮件业务对象
 *
 * @author Kesco Lin
 */
public class LocalMail extends MimeMessage {
    private final Context context;

    private String uid;
    private long accountId;
    private String senderList;
    private String toList;
    private String ccList;
    private String bccList;
    private boolean isRead;
    private boolean isFlagged;
    private boolean isForward;
    private String textPath;
    private String htmlPath;
    private String preview;
    private String subject;
    private long time;
    private int status = -1;

    public LocalMail(Context context, LocalFolder folder) {
        this.context = context;
        this.mFolder = folder;
    }

    @Override
    public LocalFolder getFolder() {
        return (LocalFolder) mFolder;
    }
}
