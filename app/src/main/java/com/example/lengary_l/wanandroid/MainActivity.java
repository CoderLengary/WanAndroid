package com.example.lengary_l.wanandroid;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;

import com.example.lengary_l.wanandroid.mvp.timeline.TimelineFragment;
import com.example.lengary_l.wanandroid.util.SettingsUtil;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    private TimelineFragment timelineFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initFragments(savedInstanceState);

    }



    private void initViews(){
        toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        bottomNavigationView = findViewById(R.id.bottom_navigation_view);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.nav_timeline:
                        showTimelineFragment();
                        break;
                        default:break;
                }
                return true;
            }
        });
    }

    private void initFragments(Bundle savedInstanceState){
        if (savedInstanceState!=null){

        }else {
            timelineFragment = TimelineFragment.newInstance();
        }
        if (!timelineFragment.isAdded()){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_layout, timelineFragment, "TimelineFragment")
                    .commit();
        }
    }

    private void showTimelineFragment(){
        getSupportFragmentManager().beginTransaction()
                .show(timelineFragment)
                .commit();
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_sign_out:
                break;

            case R.id.nav_switch_theme:

                drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

                    }

                    @Override
                    public void onDrawerOpened(@NonNull View drawerView) {

                    }

                    @Override
                    public void onDrawerClosed(@NonNull View drawerView) {
                        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        if ((getResources().getConfiguration().uiMode& Configuration.UI_MODE_NIGHT_MASK)
                                ==Configuration.UI_MODE_NIGHT_YES){
                            sp.edit().putBoolean(SettingsUtil.KEY_NIGHT_MODE, false);
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        }else {
                            sp.edit().putBoolean(SettingsUtil.KEY_NIGHT_MODE, true);
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        }
                        getWindow().setWindowAnimations(R.style.WindowAnimationFadeInOut);
                        recreate();
                    }

                    @Override
                    public void onDrawerStateChanged(int newState) {

                    }
                });
                break;

            case R.id.nav_settings:
                break;

            case R.id.nav_about:
                break;
                default:break;

        }
        drawerLayout.closeDrawer(Gravity.START);
        return true;
    }
}
