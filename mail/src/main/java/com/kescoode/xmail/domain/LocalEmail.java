package com.kescoode.xmail.domain;

import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import com.fsck.k9.mail.*;
import com.fsck.k9.mail.internet.*;
import com.kescoode.xmail.domain.internal.HtmlConverter;
import com.kescoode.xmail.domain.internal.UiMessageContent;
import com.kescoode.xmail.domain.internal.UiMessageExtractor;

import java.util.Date;
import java.util.Locale;

/**
 * 本地邮件业务对象
 *
 * @author Kesco Lin
 */
public class LocalEmail extends MimeMessage {
    private final Context context;
    private volatile long id = -1;

    private boolean isRead = false;
    private boolean isFlagged = false;
    private boolean isForward = false;
    private String textPath;
    private String htmlPath;
    private String preview;
    private String subject;
    private int status = -1;

    public LocalEmail(Context context, LocalFolder folder, Cursor cursor) {
        this.context = context;
        this.mFolder = folder;

        this.id = cursor.getLong(0);
        this.mUid = cursor.getString(1);
        this.mFrom = Address.unpack(cursor.getString(3));
        this.mTo = Address.unpack(cursor.getString(4));
        this.mCc = Address.unpack(cursor.getString(5));
        this.mBcc = Address.unpack(cursor.getString(6));
        this.isRead = cursor.getInt(7) == 1;
        this.isFlagged = cursor.getInt(8) == 1;
        this.isForward = cursor.getInt(9) == 1;
        this.textPath = cursor.getString(10);
        this.htmlPath = cursor.getString(11);
        this.preview = cursor.getString(12);
        this.subject = cursor.getString(13);
        setInternalDate(new Date(cursor.getLong(14)));
        this.status = cursor.getInt(15);

        try {
            String mimeType = getMimeType();
            MimeMultipart mp = new MimeMultipart();
            mp.setSubType("mixed");
            if (mimeType != null && mimeType.toLowerCase(Locale.US).startsWith("multipart/")) {
                // If this is a multipart message, preserve both text
                // and html parts, as well as the subtype.
                mp.setSubType(mimeType.toLowerCase(Locale.US).replaceFirst("^multipart/", ""));
                if (textPath != null) {
                    LocalTextBody body = new LocalTextBody(textPath, htmlPath);
                    MimeBodyPart bp = new MimeBodyPart(body, "text/plain");
                    mp.addBodyPart(bp);
                }

                if (htmlPath != null) {
                    TextBody body = new TextBody(htmlPath);
                    MimeBodyPart bp = new MimeBodyPart(body, "text/html");
                    mp.addBodyPart(bp);
                }

                // If we have both text and html content and our MIME type
                // isn't multipart/alternative, then corral them into a new
                // multipart/alternative part and put that into the parent.
                // If it turns out that this is the only part in the parent
                // MimeMultipart, it'll get fixed below before we attach to
                // the message.
                if (textPath != null && htmlPath != null && !mimeType.equalsIgnoreCase("multipart/alternative")) {
                    MimeMultipart alternativeParts = mp;
                    alternativeParts.setSubType("alternative");
                    mp = new MimeMultipart();
                    mp.addBodyPart(new MimeBodyPart(alternativeParts));
                }
            } else if (mimeType != null && mimeType.equalsIgnoreCase("text/plain")) {
                // If it's text, add only the plain part. The MIME
                // container will drop away below.
                if (textPath != null) {
                    LocalTextBody body = new LocalTextBody(textPath, htmlPath);
                    MimeBodyPart bp = new MimeBodyPart(body, "text/plain");
                    mp.addBodyPart(bp);
                }
            } else if (mimeType != null && mimeType.equalsIgnoreCase("text/html")) {
                // If it's html, add only the html part. The MIME
                // container will drop away below.
                if (htmlPath != null) {
                    TextBody body = new TextBody(htmlPath);
                    MimeBodyPart bp = new MimeBodyPart(body, "text/html");
                    mp.addBodyPart(bp);
                }
            } else {
                // MIME type not set. Grab whatever part we can get,
                // with Text taking precedence. This preserves pre-HTML
                // composition behaviour.
                if (textPath != null) {
                    LocalTextBody body = new LocalTextBody(textPath, htmlPath);
                    MimeBodyPart bp = new MimeBodyPart(body, "text/plain");
                    mp.addBodyPart(bp);
                } else if (htmlPath != null) {
                    TextBody body = new TextBody(htmlPath);
                    MimeBodyPart bp = new MimeBodyPart(body, "text/html");
                    mp.addBodyPart(bp);
                }
            }

            if (mp.getCount() == 0) {
                // If we have no body, remove the container and create a
                // dummy plain text body. This check helps prevents us from
                // triggering T_MIME_NO_TEXT and T_TVD_MIME_NO_HEADERS
                // SpamAssassin rules.
                setHeader(MimeHeader.HEADER_CONTENT_TYPE, "text/plain");
                MimeMessageHelper.setBody(this, new TextBody(""));
            } else if (mp.getCount() == 1 &&
                    !(mp.getBodyPart(0) instanceof LocalAttachmentBodyPart)) {
                // If we have only one part, drop the MimeMultipart container.
                BodyPart part = mp.getBodyPart(0);
                setHeader(MimeHeader.HEADER_CONTENT_TYPE, part.getContentType());
                MimeMessageHelper.setBody(this, part.getBody());
            } else {
                // Otherwise, attach the MimeMultipart to the message.
                MimeMessageHelper.setBody(this, mp);
            }
        } catch (MessagingException e) {

        }
    }

    public LocalEmail(Context context, LocalFolder folder, Message remote) {
        this.context = context;
        this.mFolder = folder;

        this.mUid = remote.getUid();
        this.mFrom = remote.getFrom();
        try {
            this.mTo = remote.getRecipients(RecipientType.TO);
            this.mCc = remote.getRecipients(RecipientType.CC);
            this.mBcc = remote.getRecipients(RecipientType.BCC);

            // TODO: 这里和K9现在的实现不一样，以后要做相应的改变
            UiMessageContent content = UiMessageExtractor
                    .extractMessageFromScratch(context, remote);
            this.textPath = content.text;
            this.htmlPath = HtmlConverter.convertEmoji2Img(content.html);
            this.preview = content.calculateContentPreview(content.text);
            setInternalDate(remote.getSentDate() != null ? remote.getSentDate() : remote.getInternalDate());
        } catch (MessagingException e) {
            throw new RuntimeException("Can not load remote message");
        }
        this.subject = remote.getSubject();
    }

    public String getTextForDisplay() throws MessagingException {
        String text = null;    // First try and fetch an HTML part.
        Part part = MimeUtility.findFirstPartByMimeType(this, "text/html");
        if (part == null) {
            // If that fails, try and get a text part.
            part = MimeUtility.findFirstPartByMimeType(this, "text/plain");
            if (part != null && part.getBody() instanceof LocalTextBody) {
                text = ((LocalTextBody) part.getBody()).getBodyForDisplay();
            }
        } else {
            // We successfully found an HTML part; do the necessary character set decoding.
            text = MessageExtractor.getTextFromPart(part);
        }
        return text;
    }

    @Override
    public String getMimeType() {
        // TODO: 我觉得这样是非常不合理的做法，应该在数据库上做mime的记录，待项目进展把它重构掉
        if (TextUtils.isEmpty(textPath)) {
            return "text/html";
        } else if (TextUtils.isEmpty(htmlPath)) {
            return "text/plain";
        } else {
            return "multipart/alternative";
        }
    }

    @Override
    public LocalFolder getFolder() {
        return (LocalFolder) mFolder;
    }

    @Override
    public String getSubject() {
        return subject != null ? subject : "";
    }

    @Override
    public String getPreview() {
        return preview;
    }

    /**
     * 这里把Send Date和Internal Date统一了
     *
     * @return 发送日期
     */
    @Override
    public Date getSentDate() {
        return getInternalDate();
    }

    public long getFolderId() {
        return getFolder().getId();
    }

    public long getId() {
        if (id == -1) {
            throw new UnsupportedOperationException("The mail has not been insert into database");
        }
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isRead() {
        return isRead;
    }

    public void setRead(boolean isRead) {
        this.isRead = isRead;
    }

    public boolean isFlagged() {
        return isFlagged;
    }

    public void setFlagged(boolean isFlagged) {
        this.isFlagged = isFlagged;
    }

    public boolean isForward() {
        return isForward;
    }

    public void setForward(boolean isForward) {
        this.isForward = isForward;
    }

    public String getHtmlPath() {
        return htmlPath;
    }

    public String getTextPath() {
        return textPath;
    }

    public int getStatus() {
        return status;
    }
}
