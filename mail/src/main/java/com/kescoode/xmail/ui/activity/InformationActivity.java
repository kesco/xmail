package com.kescoode.xmail.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import com.kescoode.xmail.R;
import com.kescoode.xmail.exception.XDynamicException;
import com.kescoode.xmail.ui.activity.internal.AppActivity;
import com.kescoode.xmail.ui.fragment.AboutFragment;

public class InformationActivity extends AppActivity {
    private static final int TYPE_ABOUT = 0;

    public static void startAbout(Context context) {
        Intent intent = new Intent(context, InformationActivity.class);
        intent.putExtra("info_type", TYPE_ABOUT);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        Intent intent = getIntent();
        int type = intent.getIntExtra("info_type", -1);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        Fragment fg;
        if (type == TYPE_ABOUT) {
            fg = AboutFragment.newInstance();
        } else {
            throw new XDynamicException("The intent %d code is wrong", type);
        }
        ft.add(R.id.fg_container, fg);
        ft.commit();
    }

}
