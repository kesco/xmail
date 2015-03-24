package com.kescoode.xmail.service;

import android.content.Intent;
import android.os.IBinder;
import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.service.internal.CoreService;

/**
 * App中负责邮件收发功能的Service, Activity中
 */
public class MailService extends CoreService {

    public MailService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        /* 返回原来CoreService的设置 */
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }


}
