package com.kescoode.xmail.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * 定时任务Service
 *
 * @author Kesco Lin
 */
public class TimerService extends Service {
    public TimerService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
