package com.kescoode.xmail.service.task;

import android.content.Context;
import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.service.task.internal.Command;

/**
 * 发送邮件命令
 *
 * @author Kesco Lin
 */
public class SendMailCommand extends Command {

    public SendMailCommand(Context context, Account account) {
        super(context, account);
    }

    @Override
    public void task() {

    }

}
