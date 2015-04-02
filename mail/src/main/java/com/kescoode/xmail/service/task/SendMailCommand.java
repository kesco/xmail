package com.kescoode.xmail.service.task;

import android.content.Context;
import com.fsck.k9.mail.Message;
import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.Transport;
import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.domain.MailBuilder;
import com.kescoode.xmail.exception.XDynamicException;
import com.kescoode.xmail.service.task.internal.Command;

/**
 * 发送邮件命令
 *
 * @author Kesco Lin
 */
public class SendMailCommand extends Command {
    private final MailBuilder mailBuilder;

    public SendMailCommand(Context context, Account account, MailBuilder builder) {
        super(context, account);
        mailBuilder = builder;
    }

    @Override
    public void task() {
        try {
            Transport transport = account.getTransport();
            Message mail = mailBuilder.build(context, account);
            transport.sendMessage(mail);
        } catch (MessagingException e) {
            // TODO: 改为发送事件通知
            throw new XDynamicException("Send mail fail");
        }
    }

}
