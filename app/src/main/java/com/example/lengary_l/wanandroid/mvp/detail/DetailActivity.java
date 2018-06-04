package com.example.lengary_l.wanandroid.mvp.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lengary_l.wanandroid.R;

public class DetailActivity extends AppCompatActivity {

    public static final String URL = "URL";
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


}
