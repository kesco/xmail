package com.kescoode.xmail.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.util.Log;
import android.view.*;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.fsck.k9.mail.MessagingException;
import com.kescoode.xmail.R;
import com.kescoode.xmail.controller.MailManager;
import com.kescoode.xmail.db.EmailDao;
import com.kescoode.xmail.db.FolderDao;
import com.kescoode.xmail.domain.LocalEmail;
import com.kescoode.xmail.domain.LocalFolder;
import com.kescoode.xmail.ui.activity.MailOperationActivity;
import com.kescoode.xmail.ui.fragment.internal.AppFragment;
import com.kescoode.xmail.ui.widget.MailWebView;

public class MailDetailFragment extends AppFragment<MailOperationActivity> {
    @InjectView(R.id.wb_mail)
    MailWebView mWbMail;

    private LocalEmail mMail;


    public static MailDetailFragment newInstance(long mailId) {
        MailDetailFragment fragment = new MailDetailFragment();
        Bundle args = new Bundle();
        args.putLong("mail_id", mailId);
        fragment.setArguments(args);
        return fragment;
    }

    public MailDetailFragment() {
        /* Empty */
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long id = getArguments().getLong("mail_id", 0);

            MailManager mailManager = MailManager.getSingleTon(getActivity());
            FolderDao folderDao = new FolderDao(getActivity());
            LocalFolder folder = folderDao.selectFolder4Name(mailManager.getAccounts().get(0), "INBOX");

            EmailDao emailDao = new EmailDao(getActivity());
            mMail = emailDao.selectMailsFromId(folder, id);
        }
    }

    @Override
    protected void onActAttachOnce(MailOperationActivity activity) {
        ActionBar bar = activity.getSupportActionBar();
        bar.setDisplayShowTitleEnabled(false);
        bar.setDisplayHomeAsUpEnabled(true);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mail_detail, container, false);
        ButterKnife.inject(this, view);

        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try {
            mWbMail.blockNetworkData(false);
            mWbMail.loadLocalData(mMail.getTextForDisplay());
            if (Build.VERSION_CODES.KITKAT < Build.VERSION.SDK_INT) {
                mWbMail.textAutoSize(true);
            }
        } catch (MessagingException e) {
            e.printStackTrace();
            Log.e("nimabe", "hanima");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_mail_detail_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getAct().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
