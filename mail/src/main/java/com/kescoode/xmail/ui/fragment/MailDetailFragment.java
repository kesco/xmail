package com.kescoode.xmail.ui.fragment;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.view.*;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.fsck.k9.mail.Address;
import com.fsck.k9.mail.Message;
import com.fsck.k9.mail.MessagingException;
import com.kescoode.adk.graphics.MaterialColorPalette;
import com.kescoode.adk.ui.CircleLogo;
import com.kescoode.xmail.R;
import com.kescoode.xmail.controller.MailManager;
import com.kescoode.xmail.db.EmailDao;
import com.kescoode.xmail.db.FolderDao;
import com.kescoode.xmail.domain.LocalEmail;
import com.kescoode.xmail.domain.LocalFolder;
import com.kescoode.xmail.ui.activity.MailOperationActivity;
import com.kescoode.xmail.ui.fragment.internal.AppFragment;
import com.kescoode.xmail.ui.widget.MailWebView;

import java.text.DateFormat;

/**
 * 邮件详情页面
 *
 * @author Kesco Lin
 */
public class MailDetailFragment extends AppFragment<MailOperationActivity> {
    @InjectView(R.id.tv_title)
    TextView tvTitle;

    @InjectView(R.id.tv_sender)
    TextView tvSender;

    @InjectView(R.id.tv_receivers)
    TextView tvReceivers;

    @InjectView(R.id.tv_date)
    TextView tvDate;

    @InjectView(R.id.cl_people)
    CircleLogo clLogo;

    @InjectView(R.id.wb_mail)
    MailWebView wbMail;

    private LocalEmail mail;


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
            mail = emailDao.selectMailsFromId(folder, id);
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
        /* 显示主题 */
        StringBuilder sBuilder = new StringBuilder();
        String folder = mail.getFolder().getName();
        sBuilder.append(mail.getSubject())
                .append(" ")
                .append(folder);
        SpannableString sString = new SpannableString(sBuilder.toString());
        sString.setSpan(new BackgroundColorSpan(getResources().getColor(R.color.orange_light)),
                sString.length() - folder.length(), sString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        sString.setSpan(new AbsoluteSizeSpan(getResources().getDimensionPixelSize(R.dimen.item_mail_list_preview_text_size)),
                sString.length() - folder.length(), sString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        tvTitle.setText(sString);
        /* 显示发件人 */
        Address[] senders = mail.getFrom();
        if (0 != senders.length) {
            String from = senders[0].getPersonal();
            tvSender.setText(from);
            clLogo.setLogoColor(MaterialColorPalette.randomColor());
            clLogo.setLogoText(from.substring(0, 1));
        }
        /* 显示日期 */
        DateFormat dateFormat = android.text.format.DateFormat.getDateFormat(getAct());
        tvDate.setText(dateFormat.format(mail.getInternalDate()));
        try {
            /* 显示收件人 */
            Address[] recievers = mail.getRecipients(Message.RecipientType.TO);
            StringBuilder builder = new StringBuilder();
            builder.append(getResources().getString(R.string.message_compose_quote_header_to));
            for (Address address : recievers) {
                String to;
                if (TextUtils.isEmpty(address.getPersonal())) {
                    to = address.getAddress();
                } else {
                    to = address.getAddress();
                }
                builder.append(to);
                builder.append(";");
            }
            tvReceivers.setText(builder.toString());
            /* 显示Web信息 */
            wbMail.blockNetworkData(false);
            wbMail.loadLocalData(mail.getTextForDisplay());
            if (Build.VERSION_CODES.KITKAT < Build.VERSION.SDK_INT) {
                wbMail.textAutoSize(true);
            }
        } catch (MessagingException e) {
            /* 永远不会发生的异常 */
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
