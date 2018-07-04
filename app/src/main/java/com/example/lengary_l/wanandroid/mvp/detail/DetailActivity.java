package com.example.lengary_l.wanandroid.mvp.detail;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataRepository;
import com.example.lengary_l.wanandroid.data.source.StatusDataRepository;
import com.example.lengary_l.wanandroid.data.source.local.ArticlesDataLocalSource;
import com.example.lengary_l.wanandroid.data.source.remote.ArticlesDataRemoteSource;
import com.example.lengary_l.wanandroid.data.source.remote.StatusDataRemoteSource;

public class DetailActivity extends AppCompatActivity {

    public static final String ID = "ID";
    public static final String URL = "URL";
    public static final String TITLE = "TITLE";
    public static final String FAVORITE_STATE = "FAVORITE_STATE";
    public static final String USER_ID = "USER_ID";
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
        new DetailPresenter(detailFragment, StatusDataRepository.getInstance(StatusDataRemoteSource.getInstance()),
                ArticlesDataRepository.getInstance(ArticlesDataRemoteSource.getInstance(), ArticlesDataLocalSource.getInstance()));
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
