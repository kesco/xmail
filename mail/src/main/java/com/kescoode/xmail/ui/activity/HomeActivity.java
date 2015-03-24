package com.kescoode.xmail.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.kescoode.adk.log.Logger;
import com.kescoode.xmail.R;
import com.kescoode.xmail.db.AccountDao;
import com.kescoode.xmail.db.EmailConfigDao;
import com.kescoode.xmail.domain.EmailConfig;
import com.kescoode.xmail.service.MailService;
import com.kescoode.xmail.service.TimerService;

import org.json.JSONException;
import org.json.JSONObject;


public class HomeActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startAppService();
        setContentView(R.layout.activity_main);

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountDao dao = new AccountDao(HomeActivity.this);
                if (dao.getAllAccounts().length == 0) {
                    Logger.e("have no account");
                } else {
                    Logger.e("have %d accounts",dao.getAllAccounts().length);
                }
            }
        });
    }

    private void startAppService() {
        // TODO: 考虑以后使用隐式URI来做 */
        Intent intent = new Intent(this, MailService.class);
        /* 启动MailService */
        startService(intent);
        intent = new Intent(this, TimerService.class);
        /* 启动TimerService */
        startService(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
