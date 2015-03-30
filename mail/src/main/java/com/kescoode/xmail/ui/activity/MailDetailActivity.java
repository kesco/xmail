package com.kescoode.xmail.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.fsck.k9.mail.MessagingException;
import com.kescoode.xmail.R;
import com.kescoode.xmail.controller.MailManager;
import com.kescoode.xmail.db.EmailDao;
import com.kescoode.xmail.db.FolderDao;
import com.kescoode.xmail.domain.LocalEmail;
import com.kescoode.xmail.domain.LocalFolder;
import com.kescoode.xmail.ui.widget.MailWebView;

public class MailDetailActivity extends ActionBarActivity {

    @InjectView(R.id.wb_mail)
    MailWebView mWbMail;

    private LocalEmail mMail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail_detail);
        ButterKnife.inject(this);

        initArgs();
        initViews();
    }

    private void initArgs() {
        Intent intent = getIntent();
        long id = intent.getLongExtra("mail_id", 0);

        MailManager mailManager = MailManager.getSingleTon(this);
        FolderDao folderDao = new FolderDao(this);
        LocalFolder folder = folderDao.selectFolder4Name(mailManager.getAccounts().get(0), "INBOX");

        EmailDao emailDao = new EmailDao(this);
        mMail = emailDao.selectMailsFromId(folder,  id);
    }

    private void initViews() {

        try {
            mWbMail.blockNetworkData(true);
            mWbMail.loadLocalData(mMail.getTextForDisplay());
            if (Build.VERSION_CODES.KITKAT < Build.VERSION.SDK_INT) {
                mWbMail.textAutoSize(true);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            Log.e("nimabe", "hanima");
        }
    }
}
