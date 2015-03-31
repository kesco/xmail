package com.kescoode.xmail.ui.activity.internal;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.kescoode.xmail.service.MailService;
import com.kescoode.xmail.service.aidl.IRemoteMailService;

/**
 * 绑定{@link com.kescoode.xmail.service.MailService}的Activity基类
 *
 * @author Kesco Lin
 */
public class MailConnActivity extends AppActivity {

    public IRemoteMailService mailService;
    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mailService = IRemoteMailService.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mailService = null;
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        connService();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(conn);
    }

    private void connService() {
        Intent intent = new Intent(this, MailService.class);
        if (!bindService(intent, conn, BIND_AUTO_CREATE)) {
            throw new RuntimeException("Can not bind MailService.");
        }
    }

}
