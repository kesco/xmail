package com.kescoode.xmail.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.AppConstant;
import com.kescoode.xmail.controller.MailManager;
import com.kescoode.xmail.event.SettingCheckEvent;

import de.greenrobot.event.EventBus;

/**
 * 获取{@link com.kescoode.xmail.service.MailService}传达的事件并分发出去
 *
 * @author Kesco Lin
 */
public class MailEventReceiver extends BroadcastReceiver {
    private EventBus bus = EventBus.getDefault();

    public MailEventReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Object event = intent.getParcelableExtra(AppConstant.Event.TAG);
        bus.post(event);
        Logger.e("receive event: %s", event.getClass().getCanonicalName());
    }
}
