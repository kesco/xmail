package com.kescoode.xmail.ui.activity.internal;

import android.support.v7.app.ActionBarActivity;
import com.kescoode.xmail.R;
import com.kescoode.adk.view.Views;
import com.kescoode.xmail.ui.widget.AppBar;
import com.kescoode.xmail.ui.widget.XToolbar;

/**
 * App中的Acitivity基类,自动绑定ServiceConnection
 *
 * @author Kesco Lin
 */
public abstract class AppActivity extends ActionBarActivity {
    private AppBar appbar;
    public XToolbar toolbar;

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        findToolbarIfn();
    }

    private void findToolbarIfn() {
        appbar = Views.findById(this, R.id.appbar);
        toolbar = Views.findById(this, R.id.toolbar);
        if (appbar != null && toolbar != null) {
            setSupportActionBar(toolbar);
            appbar.setToolbar(toolbar);
        }
    }
}
