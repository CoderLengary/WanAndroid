package com.example.lengary_l.wanandroid.mvp.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.example.lengary_l.wanandroid.R;

public class DetailActivity extends AppCompatActivity {

    public static final String ID = "ID";
    public static final String URL = "URL";
    public static final String TITLE = "TITLE";
    public static final String FAVORITE_STATE = "FAVORITE_STATE";
    private DetailFragment detailFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        if (savedInstanceState != null) {
            detailFragment = (DetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, "DetailFragment");
        }else {
            detailFragment = DetailFragment.newInstance();
        }
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, detailFragment, "DetailFragment")
                .commit();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (detailFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState, "DetailFragment", detailFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (detailFragment.onFragmentKeyDown(keyCode, event)){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
