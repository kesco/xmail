package com.kescoode.xmail.service;

import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;

import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.AppConstant;
import com.kescoode.xmail.event.SettingCheckEvent;
import com.kescoode.xmail.service.aidl.IRemoteMailService;
import com.kescoode.xmail.service.internal.CoreService;

/**
 * App中负责邮件收发功能的Service, Activity中
 */
public class MailService extends CoreService {

    private final IRemoteMailService.Stub binder = new IRemoteMailService.Stub() {
        @Override
        public void login(String email, String password) throws RemoteException {
            Logger.e("Email: %s,\nPasswd: %s", email, password);
            Intent intent = new Intent(AppConstant.Event.BROADCAST);
            intent.putExtra("haha", new SettingCheckEvent(true, SettingCheckEvent.Type.SEND));
            sendBroadcast(intent);
        }
    };

    public MailService() {
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
        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }
}
