package com.kescoode.adk.demos;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import com.kescoode.adk.ui.CircleLogo;
import com.kescoode.adk.ui.ImmersiveSearchBar;
import com.kescoode.adk.view.Views;


public class MainActivity extends ActionBarActivity {
    private ImmersiveSearchBar isbSearch;
    private CircleLogo clLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isbSearch = Views.findById(this, R.id.isb_search);
//        setSupportActionBar(isbSearch);
        isbSearch.setVisibility(View.GONE);

        RecyclerView rv = Views.findById(this, R.id.rv_demos);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(new DemosAdapter(this));

        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isbSearch.appear(MainActivity.this);
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
