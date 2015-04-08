package com.kescoode.xmail.ui.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.view.*;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import com.dd.CircularProgressButton;
import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.R;
import com.kescoode.xmail.event.SettingCheckEvent;
import com.kescoode.xmail.ui.activity.AccountActivity;
import com.kescoode.xmail.ui.activity.HomeActivity;
import com.kescoode.xmail.ui.fragment.internal.AppFragment;
import de.greenrobot.event.EventBus;

/**
 * 邮箱登录页面
 *
 * @author Kesco Lin
 */
public class LoginAccountFragment extends AppFragment<AccountActivity> {

    @InjectView(R.id.et_email)
    EditText etEmail;

    @InjectView(R.id.et_email_passwd)
    EditText etPasswd;

    @InjectView(R.id.cpb_login)
    CircularProgressButton cpbLogin;

    private final EventBus bus = EventBus.getDefault();

    public static LoginAccountFragment newInstance() {
        return new LoginAccountFragment();
    }

    public LoginAccountFragment() {
        /* Empty */
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_account, container, false);
        ButterKnife.inject(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getAct().getSupportActionBar().setTitle(R.string.login_label);
        cpbLogin.setIndeterminateProgressMode(true);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!bus.isRegistered(this)) {
            bus.register(this);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (bus.isRegistered(this)) {
            bus.unregister(this);
        }
    }

    @OnClick(R.id.cpb_login)
    void btnLoginClick() {
        cpbLogin.setProgress(50);
        try {
            if (getAct().mailService != null) {
                getAct().mailService.login(etEmail.getText().toString().trim(),
                        etPasswd.getText().toString().trim());
            }
        } catch (RemoteException e) {
            throw new RuntimeException("Can not bind MailService.");
        }
    }

    public void onEventMainThread(SettingCheckEvent event) {
        Logger.e("Login Event: %s %s", String.valueOf(event.ok), event.type.name());
        if (event.type == SettingCheckEvent.Type.RECEIVE && event.ok) {
            cpbLogin.setProgress(100);
            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getAct(), HomeActivity.class);
                    startActivity(intent);
                    getAct().finish();
                }
            }, 1500);
        } else if (!event.ok) {
            cpbLogin.setProgress(-1);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.fragment_login_actions, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_about:
                // TODO: 加入App说明
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
