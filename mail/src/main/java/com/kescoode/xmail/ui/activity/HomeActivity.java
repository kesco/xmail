package com.kescoode.xmail.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.R;
import com.kescoode.xmail.controller.MailManager;
import com.kescoode.xmail.db.FolderDao;
import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.domain.LocalFolder;
import com.kescoode.xmail.event.SyncFolderEvent;
import com.kescoode.xmail.service.MailService;
import com.kescoode.xmail.service.TimerService;
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

    private MailListAdapter adapter;

    private MailManager mailManager;
    private List<Account> accounts;

    private EventBus bus = EventBus.getDefault();

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
        accounts = mailManager.getAccounts();
        if (accounts.size() == 0) {
            AccountActivity.start(this, AccountActivity.TYPE_LOGIN);
            finish();
        } else {
            setContentView(R.layout.activity_main);
            ButterKnife.inject(this);
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

        FolderDao dao = new FolderDao(this);
        LocalFolder folder = dao.selectFolder4Name(accounts.get(0), "INBOX");
        adapter.setDataSet(folder);

        fabNew.attachToRecyclerView(rcVMails);
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

    public void onEvent(SyncFolderEvent event) {
        srlIndex.setRefreshing(false);
        FolderDao dao = new FolderDao(this);
        LocalFolder folder = dao.selectFolder4Name(accounts.get(0), "INBOX");
        adapter.setDataSet(folder);
    }

    @OnClick(R.id.fab_new)
    void onFabNewClick() {
        MailOperationActivity.startMailOperation(this, MailOperationActivity.OPERATION_WRITE);
    }

}
