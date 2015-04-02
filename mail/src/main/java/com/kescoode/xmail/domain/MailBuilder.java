package com.kescoode.xmail.domain;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import com.fsck.k9.mail.Address;
import com.fsck.k9.mail.Body;
import com.fsck.k9.mail.Message;
import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.internet.*;
import com.kescoode.xmail.domain.internal.TempFileBody;
import com.kescoode.xmail.domain.internal.TempFileMessageBody;
import org.apache.james.mime4j.codec.EncoderUtil;
import org.apache.james.mime4j.util.MimeUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 包含Mail信息，用于IPC
 *
 * @author Kesco Lin
 */
public class MailBuilder implements Parcelable {
    private String toList;
    private String ccList;
    private String bccList;
    private String subject;
    private String content;
    private List<String> attachments = new ArrayList<>();

    public MailBuilder() {
    }

    private MailBuilder(Parcel source) {
        toList = source.readString();
        ccList = source.readString();
        bccList = source.readString();
        subject = source.readString();
        content = source.readString();
        source.readStringList(attachments);
    }

    /**
     * @param account
     * @return
     * @throws MessagingException
     */
    public MimeMessage build(Context context, Account account) throws MessagingException {
        MimeMessage mail = new MimeMessage();
        mail.setSentDate(new Date(), true);
        /* 设置地址信息 */
        mail.setFrom(generateAddress(account.getUserEmail())[0]);
        mail.setRecipients(Message.RecipientType.TO, generateAddress(toList));
        mail.setRecipients(Message.RecipientType.CC, generateAddress(ccList));
        mail.setRecipients(Message.RecipientType.BCC, generateAddress(bccList));
        mail.setSubject(subject);                                                   /* 设置标题 */

        // TODO: 加入附件支持
        TextBody textContent = generateTextBody(content);
        if (attachments.size() != 0) {
            MimeMultipart mp = new MimeMultipart();
            mp.addBodyPart(new MimeBodyPart(textContent, "text/plain"));
            addAttachmentsToMail(context, mp);
            MimeMessageHelper.setBody(mail, mp);
        } else {
            MimeMessageHelper.setBody(mail, textContent);
        }
        return mail;
    }

    private Address[] generateAddress(String address) {
        return Address.parseUnencoded(address);
    }

    /**
     * 生成邮件文字部分，目前不做HTML的生成
     *
     * @param content 邮件文本
     * @return 文字Body
     */
    private TextBody generateTextBody(String content) {
        // TODO: 加入HTML的生成，不过我觉得在手机上没什么用，除非以后加上特殊邮件的适配
        int composedMessageLength = 0;
        int composedMessageOffset = 0;

        String text = content.replace("\n", "\r\n");
        composedMessageLength = text.length();
        composedMessageOffset = 0;

        TextBody body = new TextBody(text);
        body.setComposedMessageLength(composedMessageLength);
        body.setComposedMessageOffset(composedMessageOffset);

        return body;
    }

    private void addAttachmentsToMail(Context context, MimeMultipart mp) throws MessagingException {
        Body body;
        for (String path : attachments) {
            LocalAttachment attachment = new LocalAttachment(context, path);
            if (MimeUtil.isMessage(attachment.getMime())) {
                body = new TempFileMessageBody(attachment.getPath());
            } else {
                body = new TempFileBody(attachment.getPath());
            }
            MimeBodyPart bp = new MimeBodyPart(body);
            bp.addHeader(MimeHeader.HEADER_CONTENT_TYPE, String.format(
                    "%s;\n name=\"%s\"", attachment.getMime(), EncoderUtil
                            .encodeIfNecessary(attachment.getName(),
                                    EncoderUtil.Usage.WORD_ENTITY, 7)));

            bp.setEncoding(MimeUtility.getEncodingforType(attachment.getMime()));
            bp.addHeader(MimeHeader.HEADER_CONTENT_DISPOSITION, String.format(
                    "attachment;\n filename=\"%s\";\n size=%d",
                    attachment.getName(), attachment.getSize()));
            mp.addBodyPart(bp);
        }
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(toList);
        dest.writeString(ccList);
        dest.writeString(bccList);
        dest.writeString(subject);
        dest.writeString(content);
        dest.writeStringList(attachments);
    }

    public static Parcelable.Creator<MailBuilder> CREATOR = new Parcelable.Creator<MailBuilder>() {
        @Override
        public MailBuilder createFromParcel(Parcel source) {
            return new MailBuilder(source);
        }

        @Override
        public MailBuilder[] newArray(int size) {
            return new MailBuilder[size];
        }
    };

    public MailBuilder setToList(String toList) {
        this.toList = toList;
        return this;
    }

    public MailBuilder setCcList(String ccList) {
        this.ccList = ccList;
        return this;
    }

    public MailBuilder setBccList(String bccList) {
        this.bccList = bccList;
        return this;
    }

    public MailBuilder setSubject(String subject) {
        this.subject = subject;
        return this;
    }

    public MailBuilder setContent(String content) {
        this.content = content;
        return this;
    }

    public MailBuilder addAttachment(String path) {
        this.attachments.add(path);
        return this;
    }
}
