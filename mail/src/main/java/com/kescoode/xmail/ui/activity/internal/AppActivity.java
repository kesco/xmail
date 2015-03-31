package com.kescoode.xmail.ui.activity.internal;

import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import com.kescoode.xmail.R;
import com.kescoode.xmail.ui.Views;

/**
 * App中的Acitivity基类,自动绑定ServiceConnection
 *
 * @author Kesco Lin
 */
public abstract class AppActivity extends ActionBarActivity {
    public Toolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        findToolbarIfn();
    }

    private void findToolbarIfn() {
        toolbar = Views.findById(this, R.id.toolbar);
        setSupportActionBar(toolbar);
    }
}
