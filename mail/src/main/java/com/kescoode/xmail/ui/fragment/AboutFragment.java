package com.kescoode.xmail.ui.fragment;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import com.kescoode.xmail.R;
import com.kescoode.xmail.ui.activity.InformationActivity;
import com.kescoode.xmail.ui.fragment.internal.AppFragment;

/**
 * 关于页面
 *
 * @author Kesco Lin
 */
public class AboutFragment extends AppFragment<InformationActivity> {

    public static AboutFragment newInstance() {
        AboutFragment fragment = new AboutFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public AboutFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_about, container, false);
    }

    @Override
    protected void onActAttachOnce(InformationActivity activity) {
        ActionBar bar = activity.getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);
        bar.setTitle(R.string.about);
        setHasOptionsMenu(true);
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
