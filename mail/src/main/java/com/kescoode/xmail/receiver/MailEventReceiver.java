package com.kescoode.xmail.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.controller.MailManager;
import com.kescoode.xmail.event.SettingCheckEvent;

import de.greenrobot.event.EventBus;

/**
 * 获取{@link com.kescoode.xmail.service.MailService}传达的事件并分发出去
 *
 * @author Kesco Lin
 */
public class MailEventReceiver extends BroadcastReceiver {
    private MailManager mailManager;
    private EventBus bus = EventBus.getDefault();

    public MailEventReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        mailManager = MailManager.getSingleTon(context); /* 这里可能会有点问题,看Service出现的情况而定 */
        SettingCheckEvent event = intent.getParcelableExtra("haha");
        bus.post(event);
        Logger.e("receive event: %s, %s", String.valueOf(event.ok), event.type.name());
    }
}
