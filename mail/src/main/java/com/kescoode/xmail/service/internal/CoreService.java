package com.kescoode.xmail.service.internal;

import android.app.Service;
import android.content.Intent;

/**
 * App中的Service基类
 *
 * @author Kesco Lin
 */
public abstract class CoreService extends Service {

    public CoreService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        /* 通过startService的都必须手动关闭 */
        return START_STICKY;
    }
}
