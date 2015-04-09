package com.kescoode.adk.demos;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.kescoode.adk.ui.CircleLogo;
import com.kescoode.adk.view.Views;


public class MainActivity extends ActionBarActivity {
    private CircleLogo clLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        clLogo = Views.findById(this, R.id.cl_logo);

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            boolean reverse = false;

            @Override
            public void onClick(View v) {
                if (reverse) {
                    clLogo.setLogoText("L");
                    clLogo.setLogoColor(getResources().getColor(android.R.color.holo_orange_dark));
                } else {
                    clLogo.setLogoText("魔");
                    clLogo.setLogoColor(getResources().getColor(android.R.color.holo_purple));
                }
                reverse = !reverse;
            }
        });
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
