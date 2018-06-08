package com.example.lengary_l.wanandroid.mvp.category;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.source.CategoryDataRepository;
import com.example.lengary_l.wanandroid.data.source.remote.CategoryRemoteDataSource;

public class CategoryActivity extends AppCompatActivity {
    private static final String TAG = "CategoryActivity";

    public static final String CATEGORY_ID = "CATEGORY_ID";
    public static final String CATEGORY_NAME = "CATEGORY_NAME";

    private CategoryFragment fragment;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.container);
        if (savedInstanceState != null) {
            fragment = (CategoryFragment) getSupportFragmentManager().getFragment(savedInstanceState, "CategoryFragment");
        }else {
            fragment = CategoryFragment.newInstance();
        }
        new CategoryPresenter( fragment,CategoryDataRepository.getInstance(
                CategoryRemoteDataSource.getInstance()));
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.view_pager, fragment, "CategoryFragment")
                .commit();

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment.isAdded()){
            getSupportFragmentManager().putFragment(outState, "CategoryFragment", fragment);
        }
    }
}
