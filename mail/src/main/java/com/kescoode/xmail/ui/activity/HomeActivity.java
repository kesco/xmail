package com.kescoode.xmail.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.R;
import com.kescoode.xmail.controller.MailManager;
import com.kescoode.xmail.db.FolderDao;
import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.domain.LocalFolder;
import com.kescoode.xmail.service.MailService;
import com.kescoode.xmail.service.TimerService;
import com.kescoode.xmail.service.aidl.IRemoteMailService;
import com.kescoode.xmail.ui.adapter.MailListAdapter;

import java.util.List;


public class HomeActivity extends ActionBarActivity implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.srl_index)
    SwipeRefreshLayout mSrlIndex;

    @InjectView(R.id.rcv_mails)
    RecyclerView mRcvMails;

    private MailListAdapter adapter;

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
        ButterKnife.inject(this);
        initViews();
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

    private void initViews() {
        mSrlIndex.setOnRefreshListener(this);
        mSrlIndex.setColorSchemeColors(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);

        mRcvMails.setHasFixedSize(true);
        mRcvMails.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MailListAdapter(this);
        mRcvMails.setAdapter(adapter);

        FolderDao dao = new FolderDao(this);
        LocalFolder folder = dao.selectFolder4Name(accounts.get(0), "INBOX");
        adapter.setDataSet(folder);
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
    public void onRefresh() {
        try {
            mailService.syncFolder(accounts.get(0).getId(), "INBOX");
        } catch (RemoteException e) {
            Logger.e("Can not bind Service");
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
