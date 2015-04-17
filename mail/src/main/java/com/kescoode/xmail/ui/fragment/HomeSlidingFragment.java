package com.kescoode.xmail.ui.fragment;

import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.kescoode.xmail.R;
import com.kescoode.xmail.ui.activity.internal.MailConnActivity;
import com.kescoode.xmail.ui.fragment.internal.AppFragment;

/**
 * 主页面侧滑栏
 *
 * @author Kesco Lin
 */
public class HomeSlidingFragment extends AppFragment<MailConnActivity> {

    public static HomeSlidingFragment newInstance(String param1, String param2) {
        HomeSlidingFragment fragment = new HomeSlidingFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    public HomeSlidingFragment() {
        /* Empty */
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
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_sliding, container, false);
    }

}
