package com.kescoode.xmail.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.kescoode.xmail.R;
import com.kescoode.xmail.exception.XDynamicException;
import com.kescoode.xmail.ui.activity.internal.MailConnActivity;
import com.kescoode.xmail.ui.fragment.LoginAccountFragment;

/**
 * 帐号相关Activity
 *
 * @author Kesco Lin
 */
public class AccountActivity extends MailConnActivity {
    public static final int TYPE_LOGIN = 0;

    public static void start(Context context,int type) {
        Intent intent = new Intent(context, AccountActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        Intent intent = getIntent();
        int type = intent.getIntExtra("type", -1);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fg;
        if (type == TYPE_LOGIN) {
            fg = LoginAccountFragment.newInstance();
        } else {
            throw new XDynamicException("The intent %d code is wrong", type);
        }
        ft.add(R.id.fg_container, fg);
        ft.commit();
    }

}
