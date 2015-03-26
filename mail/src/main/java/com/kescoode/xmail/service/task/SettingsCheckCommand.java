package com.kescoode.xmail.service.task;

import android.content.Context;
import android.content.Intent;

import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.Transport;
import com.fsck.k9.mail.store.RemoteStore;
import com.kescoode.xmail.AppConstant;
import com.kescoode.xmail.controller.MailManager;
import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.event.SettingCheckEvent;
import com.kescoode.xmail.service.task.internal.Command;

/**
 * 验证邮件服务器设置
 *
 * @author Kesco Lin
 */
public class SettingsCheckCommand extends Command {
    private final MailManager manager;

    public SettingsCheckCommand(Context context, Account account) {
        super(context, account);

        manager = MailManager.getSingleTon(context);
    }

    @Override
    public void task() {
        Transport transport = account.getTransport();
        RemoteStore remoteStore = account.getRemoteStore();
        Intent intent;
        // TODO: 异常捕获细粒度化，加入本地Folder初始化
        try {
            /* 检验发送服务器配置 */
            transport.close();
            transport.open();   /* 建立与SMTP服务器的连接，检索所有发件箱的内容 */
            transport.close();
            intent = new Intent(AppConstant.Event.BROADCAST);
            intent.putExtra(AppConstant.Event.TAG, new SettingCheckEvent(true, SettingCheckEvent.Type.SEND));
            context.sendBroadcast(intent);

            /* 检测接收服务器配置 */
            remoteStore.checkSettings();
            intent = new Intent(AppConstant.Event.BROADCAST);
            intent.putExtra(AppConstant.Event.TAG, new SettingCheckEvent(true, SettingCheckEvent.Type.RECEIVE));
            context.sendBroadcast(intent);

            manager.addAccount(account);
        } catch (MessagingException e) {
            intent = new Intent(AppConstant.Event.BROADCAST);
            intent.putExtra(AppConstant.Event.TAG, new SettingCheckEvent(false, SettingCheckEvent.Type.ALL));
            context.sendBroadcast(intent);
        }
    }
}
