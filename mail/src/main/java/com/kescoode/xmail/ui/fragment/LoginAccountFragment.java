package com.kescoode.xmail.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_account, container, false);
        ButterKnife.inject(this, view);
        return view;
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

    @OnClick(R.id.btn_login)
    void btnLoginClick() {
        try {
            if (getAct().mailService != null) {
                getAct().mailService.login(etEmail.getText().toString().trim(),
                        etPasswd.getText().toString().trim());
            }
        } catch (RemoteException e) {
            throw new RuntimeException("Can not bind MailService.");
        }
    }

    public void onEvent(SettingCheckEvent event) {
        Logger.e("Login Event: %s %s", String.valueOf(event.ok), event.type.name());
        if (event.type == SettingCheckEvent.Type.RECEIVE) {
            Intent intent = new Intent(getAct(), HomeActivity.class);
            startActivity(intent);
            getAct().finish();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

}
