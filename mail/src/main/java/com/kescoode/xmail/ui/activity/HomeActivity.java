package com.kescoode.xmail.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.R;
import com.kescoode.xmail.controller.MailManager;
import com.kescoode.xmail.db.AccountDao;
import com.kescoode.xmail.db.EmailConfigDao;
import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.domain.EmailConfig;
import com.kescoode.xmail.service.MailService;
import com.kescoode.xmail.service.TimerService;
import com.kescoode.xmail.service.aidl.IRemoteMailService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;


public class HomeActivity extends ActionBarActivity {
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
    private MailManager mailManager;
    private List<Account> accounts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startAppService();
        initData();
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mailService.syncFolder(accounts.get(0).getId(), "INBOX");
                } catch (RemoteException e) {
                    Logger.e("Can not bind Service");
                }
            }
        });
    }

    private void startAppService() {
        // TODO: 考虑以后使用隐式URI来做 */
        Intent intent = new Intent(this, MailService.class);
        /* 启动MailService */
        startService(intent);
        intent = new Intent(this, TimerService.class);
        /* 启动TimerService */
        startService(intent);
    }

    private void initData() {
        mailManager = MailManager.getSingleTon(getApplicationContext());
        mailManager.dB2Account();
        accounts = mailManager.getAccounts();
        if (accounts.size() == 0) {
            Intent intent = new Intent(this, AccountActivity.class);
            // TODO: 到时候要加入识别码
            startActivity(intent);
            finish();
        }
    }

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
