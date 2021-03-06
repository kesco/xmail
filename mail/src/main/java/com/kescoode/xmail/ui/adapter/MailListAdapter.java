package com.kescoode.xmail.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.fsck.k9.mail.Address;
import com.kescoode.adk.graphics.MaterialColorPalette;
import com.kescoode.adk.ui.CircleLogo;
import com.kescoode.adk.view.Views;
import com.kescoode.xmail.R;
import com.kescoode.xmail.db.EmailDao;
import com.kescoode.xmail.domain.LocalEmail;
import com.kescoode.xmail.domain.LocalFolder;
import com.kescoode.xmail.ui.activity.MailOperationActivity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 邮件主页的邮件列表适配器
 *
 * @author Kesco Lin
 */
public class MailListAdapter extends RecyclerView.Adapter<MailListAdapter.ViewHolder> {

    private Context mCtx;
    private LayoutInflater mInflater;

    private EmailDao dao;
    private List<LocalEmail> mMails = null;

    public MailListAdapter(Context context) {
        mCtx = context;
        mInflater = LayoutInflater.from(mCtx);
        dao = new EmailDao(context);
    }

    public void setDataSet(LocalFolder folder) {
        mMails = new ArrayList<>(Arrays.asList(dao.selectMails4Folder(folder)));
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.item_mail_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if (null != mMails) {
            holder.initView(mMails.get(position), mCtx);
        }
    }

    @Override
    public int getItemCount() {
        return null != mMails ? mMails.size() : 0;
    }

    protected static final class ViewHolder extends RecyclerView.ViewHolder {

        private TextView mTvTitle;
        private TextView mTvPreview;
        private TextView tvDate;
        private CircleLogo clPeople;

        private ViewHolder(View itemView) {
            super(itemView);

            mTvTitle = (TextView) itemView.findViewById(R.id.tv_subject);
            mTvPreview = (TextView) itemView.findViewById(R.id.tv_preview);
            tvDate = Views.findById(itemView, R.id.tv_date);
            clPeople = Views.findById(itemView, R.id.cl_people);
        }

        protected void initView(final LocalEmail message, final Context context) {
            mTvTitle.setText(message.getSubject());
            Address[] addresses = message.getFrom();
            if (0 != addresses.length) {
                String logoText = addresses[0].getPersonal() != null ?
                        addresses[0].getPersonal() : addresses[0].getAddress();
                clPeople.setLogoColor(MaterialColorPalette.randomColor());
                clPeople.setLogoText(logoText.substring(0, 1));
            }
            mTvPreview.setText(message.getPreview());
            java.text.DateFormat dateFormat = DateFormat.getDateFormat(context);
            tvDate.setText(dateFormat.format(message.getInternalDate()));

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    MailOperationActivity.startMailOperation4Detail(context, message.getId());
                }
            });
        }

    }
}
