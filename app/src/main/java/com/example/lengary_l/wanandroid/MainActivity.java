package com.example.lengary_l.wanandroid;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.lengary_l.wanandroid.data.source.CategoriesDataRepository;
import com.example.lengary_l.wanandroid.data.source.remote.CategoriesDataRemoteSource;
import com.example.lengary_l.wanandroid.mvp.categories.CategoriesFragment;
import com.example.lengary_l.wanandroid.mvp.categories.CategoriesPresenter;
import com.example.lengary_l.wanandroid.mvp.search.SearchActivity;
import com.example.lengary_l.wanandroid.mvp.timeline.TimelineFragment;
import com.example.lengary_l.wanandroid.util.SettingsUtil;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = "MainActivity";

    private static final String KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID="KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID";

    private NavigationView navigationView;
    private DrawerLayout drawerLayout;
    private Toolbar toolbar;
    private BottomNavigationView bottomNavigationView;

    private TimelineFragment timelineFragment;
    private CategoriesFragment categoriesFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
        initFragments(savedInstanceState);
        new CategoriesPresenter(CategoriesDataRepository.getInstance(CategoriesDataRemoteSource.getInstance()), categoriesFragment);
        if (savedInstanceState!=null){
            int selectId = savedInstanceState.getInt(KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID);
            switch (selectId){
                case R.id.nav_timeline:
                    showFragment(timelineFragment);
                    break;

                case R.id.nav_categories:
                    showFragment(categoriesFragment);
                    break;

                default:
                    break;
            }
        }else {
            showFragment(timelineFragment);
        }
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_timeline:
                        showFragment(timelineFragment);
                        break;

                    case R.id.nav_categories:
                        showFragment(categoriesFragment);
                        break;

                    default:
                        break;
                }
                return true;
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(KEY_BOTTOM_NAVIGATION_VIEW_SELECTED_ID,bottomNavigationView.getSelectedItemId());
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (timelineFragment.isAdded()) {
            fragmentManager.putFragment(outState, "TimelineFragment", timelineFragment);
        }
        if (categoriesFragment.isAdded()) {
            fragmentManager.putFragment(outState, "CategoriesFragment", categoriesFragment);
        }
    }

    private void initViews() {
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

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_search:
                Log.e(TAG, "onOptionsItemSelected: " );
                Intent intent = new Intent(this, SearchActivity.class);
                startActivity(intent);
                break;
                default:break;
        }
        return true;
    }

    private void initFragments(Bundle savedInstanceState) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (savedInstanceState != null) {
            timelineFragment = (TimelineFragment) fragmentManager.getFragment(savedInstanceState, "TimelineFragment");
            categoriesFragment = (CategoriesFragment) fragmentManager.getFragment(savedInstanceState, "CategoriesFragment");
        } else {
            timelineFragment = TimelineFragment.newInstance();
            categoriesFragment = CategoriesFragment.newInstance();
        }
        if (!timelineFragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .add(R.id.frame_layout, timelineFragment, "TimelineFragment")
                    .commit();
        }
        if (!categoriesFragment.isAdded()) {
            fragmentManager.beginTransaction()
                    .add(R.id.frame_layout, categoriesFragment, "CategoriesFragment")
                    .commit();

        }
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (fragment instanceof TimelineFragment) {
            fragmentManager.beginTransaction()
                    .show(timelineFragment)
                    .hide(categoriesFragment)
                    .commit();
            toolbar.setTitle(R.string.timeline_label);

        } else if (fragment instanceof CategoriesFragment) {
            fragmentManager.beginTransaction()
                    .show(categoriesFragment)
                    .hide(timelineFragment)
                    .commit();
            toolbar.setTitle(R.string.categories_label);
        }
    }




    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
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
                        if ((getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK)
                                == Configuration.UI_MODE_NIGHT_YES) {
                            sp.edit().putBoolean(SettingsUtil.KEY_NIGHT_MODE, false);
                            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        } else {
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
            default:
                break;

        }
        drawerLayout.closeDrawer(Gravity.START);
        return true;
    }

}
