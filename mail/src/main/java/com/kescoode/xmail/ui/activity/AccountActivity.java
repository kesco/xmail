package com.kescoode.xmail.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.R;
import com.kescoode.xmail.event.SettingCheckEvent;
import com.kescoode.xmail.service.MailService;
import com.kescoode.xmail.service.aidl.IRemoteMailService;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import de.greenrobot.event.EventBus;

/**
 * 帐号添加页面
 *
 * @author Kesco Lin
 */
public class AccountActivity extends ActionBarActivity {

    @InjectView(R.id.et_email)
    EditText etEmail;

    @InjectView(R.id.et_email_passwd)
    EditText etPasswd;

    private IRemoteMailService mailService;
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
    private final EventBus bus = EventBus.getDefault();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.inject(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        connService();
        if (!bus.isRegistered(this)) {
            bus.register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(conn);
        if (bus.isRegistered(this)) {
            bus.unregister(this);
        }
    }

    private void connService() {
        Intent intent = new Intent(this, MailService.class);
        if (!bindService(intent, conn, BIND_AUTO_CREATE)) {
            throw new RuntimeException("Can not bind MailService.");
        }
    }

    @OnClick(R.id.btn_login)
    void btnLoginClick() {
        try {
            if (mailService != null) {
                mailService.login(etEmail.getText().toString().trim(),
                        etPasswd.getText().toString().trim());
            }
        } catch (RemoteException e) {
            throw new RuntimeException("Can not bind MailService.");
        }
    }

    public void onEvent(SettingCheckEvent event) {
        Logger.e("haha %s %s", String.valueOf(event.ok), event.type.name());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_account, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
