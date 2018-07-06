package com.example.lengary_l.wanandroid.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import com.example.lengary_l.wanandroid.R;

public class PrefsActivity extends AppCompatActivity{
    public static final String EXTRA_FLAG = "EXTRA_FLAG";
    public static final int  FLAG_ABOUT = 0, FLAG_LICENSES = 1;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prefs);
        initViews();
        Fragment fragment ;
        Intent intent = getIntent();
        if (intent.getIntExtra(EXTRA_FLAG, -1) == FLAG_ABOUT) {
            setTitle(R.string.nav_about);
            fragment = new AboutFragment();
        }else if (intent.getIntExtra(EXTRA_FLAG,-1)==FLAG_LICENSES){
            setTitle(R.string.about_licenses);
            fragment = new LicensesFragment();
        }else {
            throw new RuntimeException("Please set flag when launching PrefsActivity.");
        }

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, fragment)
                .commit();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    private void initViews() {
        setSupportActionBar((Toolbar) findViewById(R.id.toolBar));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }
}
