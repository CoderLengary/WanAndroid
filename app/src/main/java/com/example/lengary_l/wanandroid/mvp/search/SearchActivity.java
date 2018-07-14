package com.example.lengary_l.wanandroid.mvp.search;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataRepository;
import com.example.lengary_l.wanandroid.data.source.HotKeyDataRepository;
import com.example.lengary_l.wanandroid.data.source.remote.ArticlesDataRemoteSource;
import com.example.lengary_l.wanandroid.data.source.remote.HotKeyDataRemoteSource;

/**
 * Created by CoderLengary
 */


public class SearchActivity extends AppCompatActivity{


    private SearchFragment searchFragment;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("navigation_bar_tint", true)) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }
        if (savedInstanceState != null) {
            searchFragment = (SearchFragment) getSupportFragmentManager().getFragment(savedInstanceState, SearchFragment.class.getSimpleName());
        }else {
            searchFragment = SearchFragment.newInstance();
        }
        new SearchPresenter(HotKeyDataRepository.getInstance(HotKeyDataRemoteSource.getInstance())
                , ArticlesDataRepository.getInstance(ArticlesDataRemoteSource.getInstance()), searchFragment);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, searchFragment, SearchFragment.class.getSimpleName())
                .commit();
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (searchFragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, SearchFragment.class.getSimpleName(), searchFragment);
        }
    }
}
