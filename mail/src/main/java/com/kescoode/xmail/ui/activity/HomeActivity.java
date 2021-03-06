package com.kescoode.xmail.ui.activity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.kescoode.adk.log.Logger;
import com.kescoode.adk.ui.ImmersiveSearchBar;
import com.kescoode.xmail.R;
import com.kescoode.xmail.controller.MailManager;
import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.domain.LocalFolder;
import com.kescoode.xmail.event.SyncFolderEvent;
import com.kescoode.xmail.service.MailService;
import com.kescoode.xmail.service.TimerService;
import com.kescoode.xmail.service.aidl.IRemoteMailService;
import com.kescoode.xmail.ui.activity.internal.MailConnActivity;
import com.kescoode.xmail.ui.adapter.MailListAdapter;
import com.melnykov.fab.FloatingActionButton;
import de.greenrobot.event.EventBus;

import java.util.List;

public class HomeActivity extends MailConnActivity implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.srl_index)
    SwipeRefreshLayout srlIndex;

    @InjectView(R.id.rcv_mails)
    RecyclerView rcVMails;

    @InjectView(R.id.fab_new)
    FloatingActionButton fabNew;

    @InjectView(R.id.search_bar)
    ImmersiveSearchBar isbSearch;

    private EventBus bus = EventBus.getDefault();
    private final ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mailService = IRemoteMailService.Stub.asInterface(service);
            firstRefreshInit();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mailService = null;
        }
    };

    private MailListAdapter adapter;
    private MailManager mailManager;

    private Account currentAccount;
    private LocalFolder currentFolder;

    private boolean firstRefresh = false;

    public static void start4Login(Context context) {
        Intent intent = new Intent(context, HomeActivity.class);
        intent.putExtra("is_login", true);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startAppService();
        initData();
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
        List<Account> accounts = mailManager.getAccounts();
        if (accounts.size() == 0) {
            AccountActivity.start(this, AccountActivity.TYPE_LOGIN);
            finish();
        } else {
            setContentView(R.layout.activity_home);
            ButterKnife.inject(this);

            currentAccount = accounts.get(0);
            setFolder(currentAccount.getFolder(currentAccount.getInboxFolderName()));
            initViews();
        }
    }

    private void initViews() {
        srlIndex.setColorSchemeResources(R.color.purple,
                R.color.green_light,
                R.color.orange_light,
                R.color.red_light);
        srlIndex.setOnRefreshListener(this);

        rcVMails.setHasFixedSize(true);
        rcVMails.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MailListAdapter(this);
        rcVMails.setAdapter(adapter);

        adapter.setDataSet(currentFolder);

        fabNew.attachToRecyclerView(rcVMails);

        isbSearch.setBackListener(new ImmersiveSearchBar.ActionBackListener() {
            @Override
            public void back() {
                isbSearch.showKeyboard(false);
                isbSearch.setVisibility(View.GONE);
            }
        });
        isbSearch.setHint(R.string.search_email);
    }

    private void firstRefreshInit() {
        if (!firstRefresh) {
            Intent intent = getIntent();
            if (intent.getBooleanExtra("is_login", false)) {
                if (!srlIndex.isRefreshing()) {
                    srlIndex.setRefreshing(true);
                    onRefresh();
                }
            }
            firstRefresh = true;
        }
    }

    @Override
    public void onRefresh() {
        try {
            mailService.syncFolder(currentAccount.getId(), currentFolder.getName(), 0);
        } catch (RemoteException e) {
            Logger.e("Can not bind Service");
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!bus.isRegistered(this)) {
            bus.register(this);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (bus.isRegistered(this)) {
            bus.unregister(this);
        }
    }

    @Override
    protected ServiceConnection getConn() {
        return conn;
    }

    public void onEvent(SyncFolderEvent event) {
        srlIndex.setRefreshing(false);
        adapter.setDataSet(currentFolder);
    }

    @OnClick(R.id.fab_new)
    void onFabNewClick() {
        MailOperationActivity.startMailOperation4Write(this, currentAccount.getId());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home_actions, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                isbSearch.appear(this);
                return true;
            case R.id.action_about:
                InformationActivity.startAbout(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void setFolder(LocalFolder folder) {
        currentFolder = folder;
        getSupportActionBar().setTitle(currentAccount.getDisplayFolderName(currentFolder.getName()));
    }
}
