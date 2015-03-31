package com.kescoode.xmail.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
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
import com.kescoode.xmail.ui.activity.internal.MailConnActivity;
import com.kescoode.xmail.ui.adapter.MailListAdapter;

import java.util.List;

public class HomeActivity extends MailConnActivity implements SwipeRefreshLayout.OnRefreshListener {

    @InjectView(R.id.srl_index)
    SwipeRefreshLayout mSrlIndex;

    @InjectView(R.id.rcv_mails)
    RecyclerView mRcvMails;

    private MailListAdapter adapter;

    private MailManager mailManager;
    private List<Account> accounts;

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
    public void onRefresh() {
        try {
            mailService.syncFolder(accounts.get(0).getId(), "INBOX");
        } catch (RemoteException e) {
            Logger.e("Can not bind Service");
        }
    }
}
