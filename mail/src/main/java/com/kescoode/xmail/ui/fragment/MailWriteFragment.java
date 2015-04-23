package com.kescoode.xmail.ui.fragment;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.ActionBar;
import android.text.TextUtils;
import android.view.*;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.kescoode.adk.device.KeyBoard;
import com.kescoode.adk.view.Views;
import com.kescoode.xmail.R;
import com.kescoode.xmail.controller.MailManager;
import com.kescoode.xmail.domain.Account;
import com.kescoode.xmail.domain.MailBuilder;
import com.kescoode.xmail.exception.XDynamicException;
import com.kescoode.xmail.ui.activity.InformationActivity;
import com.kescoode.xmail.ui.activity.MailOperationActivity;
import com.kescoode.xmail.ui.fragment.internal.AppFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * 撰写邮件页面
 *
 * @author Kesco Lin
 */
public class MailWriteFragment extends AppFragment<MailOperationActivity> {
    private static final int REQUEST_CODE_FILE = 1345;

    @InjectView(R.id.tv_sender)
    TextView tvSender;

    @InjectView(R.id.et_to)
    EditText etTo;

    @InjectView(R.id.et_subject)
    EditText etSubject;

    @InjectView(R.id.et_body)
    EditText etBody;

    @InjectView(R.id.ll_content)
    LinearLayout llContent;

    private Account account;
    private List<AttachmentHolder> attachHolders = new ArrayList<>();

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
        etTo.requestFocus();
        KeyBoard.showSoftKeyBoardForce(getAct());
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_mail_write_actions, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                KeyBoard.hideSoftKeyBoardForce(getAct(), etTo);
                finish();
                return true;
            case R.id.action_attachment:
                openFileBroswer();
                return true;
            case R.id.action_send:
                sendMail();
                finish();
                return true;
            case R.id.action_about:
                InformationActivity.startAbout(getAct());
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void openFileBroswer() {
        KeyBoard.hideSoftKeyBoardForce(getAct(), etTo);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("gagt/sdf");
        try {
            startActivityForResult(intent, REQUEST_CODE_FILE);
        } catch (ActivityNotFoundException e) {
            // TODO: 添加自己的文件处理器
        }
    }

    private void addAttachment(String path) {
        View attachment = LayoutInflater.from(getAct()).inflate(R.layout.item_attachment_list, null);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.setMargins(getResources().getDimensionPixelSize(R.dimen.item_mail_write_vertical_margin), 0,
                getResources().getDimensionPixelSize(R.dimen.item_mail_attachment_horizontal_right_margin), 0);
        attachment.setLayoutParams(layoutParams);
        TextView tv = Views.findById(attachment, R.id.tv_attachment);
        File file = new File(path);
        tv.setText(file.getName());
        AttachmentHolder holder = new AttachmentHolder(attachment, path);
        attachment.setOnClickListener(holder);
        attachHolders.add(holder);
        llContent.addView(attachment);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_FILE && resultCode == Activity.RESULT_OK) {
            addAttachment(data.getData().getPath());
        }
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
        if (attachHolders.size() != 0) {
            for (AttachmentHolder holder : attachHolders) {
                builder.addAttachment(holder.path);
            }
        }
        try {
            getAct().mailService.sendMail(account.getId(), builder);
        } catch (RemoteException e) {
            e.printStackTrace();
            // TODO: 加入错误处理
        }
    }

    private void finish() {
        KeyBoard.hideSoftKeyBoardForce(getAct(), etTo);
        getAct().finish();
    }

    private class AttachmentHolder implements View.OnClickListener {
        private View attach;
        protected final String path;

        public AttachmentHolder(View attach, String path) {
            this.attach = attach;
            this.path = path;
        }

        @Override
        public void onClick(View v) {
            attachHolders.remove(this);
            llContent.removeView(attach);
        }
    }
}
