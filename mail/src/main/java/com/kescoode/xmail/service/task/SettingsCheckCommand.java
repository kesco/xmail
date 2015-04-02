package com.kescoode.xmail.service.task;

import android.content.Context;
import com.fsck.k9.mail.MessagingException;
import com.fsck.k9.mail.Transport;
import com.fsck.k9.mail.store.RemoteStore;
import com.kescoode.xmail.controller.MailManager;
import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.domain.LocalStore;
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
        LocalStore localStore = account.getLocalStore();
        try {
            /* 检验发送服务器配置 */
            transport.close();
            transport.open();   /* 建立与SMTP服务器的连接，检索所有发件箱的内容 */
            transport.close();
            sendBroadCaset(new SettingCheckEvent(true, SettingCheckEvent.Type.SEND));
        } catch (MessagingException e) {
            sendBroadCaset(new SettingCheckEvent(false, SettingCheckEvent.Type.SEND));
        }

        try {
            /* 检测接收服务器配置 */
            remoteStore.checkSettings();
            manager.addAccount(account);
            localStore.syncRemote(remoteStore.getPersonalNamespaces(false));
            sendBroadCaset(new SettingCheckEvent(true, SettingCheckEvent.Type.RECEIVE));
        } catch (MessagingException e) {
            sendBroadCaset(new SettingCheckEvent(false, SettingCheckEvent.Type.RECEIVE));
        }
    }
}
