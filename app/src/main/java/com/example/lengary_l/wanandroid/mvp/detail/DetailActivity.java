package com.example.lengary_l.wanandroid.mvp.detail;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.source.FavoriteArticlesDataRepository;
import com.example.lengary_l.wanandroid.data.source.ReadLaterArticlesRepository;
import com.example.lengary_l.wanandroid.data.source.StatusDataRepository;
import com.example.lengary_l.wanandroid.data.source.local.FavoriteArticlesDataLocalSource;
import com.example.lengary_l.wanandroid.data.source.local.ReadLaterArticlesLocalSource;
import com.example.lengary_l.wanandroid.data.source.remote.FavoriteArticlesDataRemoteSource;
import com.example.lengary_l.wanandroid.data.source.remote.StatusDataRemoteSource;

/**
 * Created by CoderLengary
 */


public class DetailActivity extends AppCompatActivity {

    public static final String ID = "ID";
    public static final String URL = "URL";
    public static final String TITLE = "TITLE";
    public static final String FROM_FAVORITE_FRAGMENT = "FROM_FAVORITE_FRAGMENT";
    public static final String FROM_BANNER = "FROM_BANNER";
    private DetailFragment detailFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("navigation_bar_tint", true)) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }
        if (savedInstanceState != null) {
            detailFragment = (DetailFragment) getSupportFragmentManager().getFragment(savedInstanceState, "DetailFragment");
        }else {
            detailFragment = DetailFragment.newInstance();
        }
        new DetailPresenter(detailFragment, StatusDataRepository.getInstance(StatusDataRemoteSource.getInstance()),
                ReadLaterArticlesRepository.getInstance(ReadLaterArticlesLocalSource.getInstance()),
                FavoriteArticlesDataRepository.getInstance(FavoriteArticlesDataRemoteSource.getInstance(),
                        FavoriteArticlesDataLocalSource.getInstance()));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, detailFragment, DetailFragment.class.getSimpleName())
                .commit();

    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (detailFragment.isAdded()){
            getSupportFragmentManager().putFragment(outState, DetailFragment.class.getSimpleName(), detailFragment);
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //To make the AgentWeb handle the "On BackPress" logic .
        if (detailFragment.onFragmentKeyDown(keyCode, event)){
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
