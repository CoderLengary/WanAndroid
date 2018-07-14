package com.example.lengary_l.wanandroid.mvp.category;

import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataRepository;
import com.example.lengary_l.wanandroid.data.source.remote.ArticlesDataRemoteSource;

/**
 * Created by CoderLengary
 */


public class CategoryActivity extends AppCompatActivity {
    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";

    private CategoryFragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("navigation_bar_tint", true)) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }
        if (savedInstanceState != null) {
            fragment = (CategoryFragment) getSupportFragmentManager().getFragment(savedInstanceState, CategoryFragment.class.getSimpleName());
        } else {
            fragment = CategoryFragment.newInstance();
        }

        new CategoryPresenter(fragment, ArticlesDataRepository.getInstance(ArticlesDataRemoteSource.getInstance()));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, fragment, CategoryFragment.class.getSimpleName())
                .commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()) {
            getSupportFragmentManager().putFragment(outState, CategoryFragment.class.getSimpleName(), fragment);
        }
    }
}
