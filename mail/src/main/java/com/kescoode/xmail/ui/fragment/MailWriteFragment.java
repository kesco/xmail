package com.kescoode.xmail.ui.fragment;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.*;
import android.widget.EditText;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.kescoode.xmail.R;
import com.kescoode.xmail.controller.MailManager;
import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.domain.MailBuilder;
import com.kescoode.xmail.exception.XDynamicException;
import com.kescoode.xmail.ui.activity.MailOperationActivity;
import com.kescoode.xmail.ui.fragment.internal.AppFragment;

/**
 * 撰写邮件页面
 *
 * @author Kesco Lin
 */
public class MailWriteFragment extends AppFragment<MailOperationActivity> {
    @InjectView(R.id.tv_sender)
    TextView tvSender;

    @InjectView(R.id.et_to)
    EditText etTo;

    @InjectView(R.id.et_subject)
    EditText etSubject;

    @InjectView(R.id.et_body)
    EditText etBody;

    private Account account;

    public static MailWriteFragment newInstance(long accountId) {
        MailWriteFragment fragment = new MailWriteFragment();
        Bundle args = new Bundle();
        args.putLong("account_id", accountId);
        fragment.setArguments(args);
        return fragment;
    }

    public MailWriteFragment() {
        /* Empty */
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            long id = getArguments().getLong("account_id", -1L);
            account = MailManager.getSingleTon(getAct()).getAccount(id);
            if (id == -1L) {
                throw new XDynamicException("Error account id: %d", id);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mail_write, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    protected void onActAttachOnce(MailOperationActivity activity) {
        ActionBar bar = activity.getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle(R.string.write_email);
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvSender.setText(account.getUserEmail());
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_mail_write_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getAct().finish();
                return true;
            case R.id.action_send:
                sendMail();
                getAct().finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void sendMail() {
        String to = etTo.getText().toString().trim();
        String subject = etSubject.getText().toString().trim();
        String body = etBody.getText().toString();
        if (TextUtils.isEmpty(to) || TextUtils.isEmpty(subject) || TextUtils.isEmpty(body)) {
            // TODO: 加入这部分的处理
            return;
        }
        MailBuilder builder = new MailBuilder();
        builder.setToList(to)
                .setSubject(subject)
                .setContent(body);
        try {
            getAct().mailService.sendMail(account.getId(),builder);
        } catch (RemoteException e) {
            e.printStackTrace();
            // TODO: 加入错误处理
        }
    }
}
