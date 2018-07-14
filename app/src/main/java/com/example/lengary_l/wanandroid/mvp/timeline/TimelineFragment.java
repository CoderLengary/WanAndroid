package com.example.lengary_l.wanandroid.mvp.timeline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.source.ArticlesDataRepository;
import com.example.lengary_l.wanandroid.data.source.BannerDataRepository;
import com.example.lengary_l.wanandroid.data.source.FavoriteArticlesDataRepository;
import com.example.lengary_l.wanandroid.data.source.LoginDataRepository;
import com.example.lengary_l.wanandroid.data.source.ReadLaterArticlesRepository;
import com.example.lengary_l.wanandroid.data.source.local.FavoriteArticlesDataLocalSource;
import com.example.lengary_l.wanandroid.data.source.local.LoginDataLocalSource;
import com.example.lengary_l.wanandroid.data.source.local.ReadLaterArticlesLocalSource;
import com.example.lengary_l.wanandroid.data.source.remote.ArticlesDataRemoteSource;
import com.example.lengary_l.wanandroid.data.source.remote.BannerDataRemoteSource;
import com.example.lengary_l.wanandroid.data.source.remote.FavoriteArticlesDataRemoteSource;
import com.example.lengary_l.wanandroid.data.source.remote.LoginDataRemoteSource;

/**
 * Created by CoderLengary
 */


public class TimelineFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArticlesFragment articlesFragment;
    private FavoritesFragment favoritesFragment;
    private ReadLaterFragment readLaterFragment;

    public TimelineFragment() {

    }

    public static TimelineFragment newInstance(){
        return new TimelineFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){
            FragmentManager fragmentManager = getChildFragmentManager();
            articlesFragment = (ArticlesFragment) fragmentManager.getFragment(savedInstanceState, ArticlesFragment.class.getSimpleName());
            favoritesFragment = (FavoritesFragment) fragmentManager.getFragment(savedInstanceState, FavoritesFragment.class.getSimpleName());
            readLaterFragment = (ReadLaterFragment) fragmentManager.getFragment(savedInstanceState, ReadLaterFragment.class.getSimpleName());
        }else {
            articlesFragment = ArticlesFragment.newInstance();
            favoritesFragment = FavoritesFragment.newInstance();
            readLaterFragment = ReadLaterFragment.newInstance();
        }

        new ArticlesPresenter(articlesFragment,
                ArticlesDataRepository.getInstance(ArticlesDataRemoteSource.getInstance()),
                BannerDataRepository.getInstance(BannerDataRemoteSource.getInstance()),
                LoginDataRepository.getInstance(LoginDataLocalSource.getInstance(), LoginDataRemoteSource.getInstance()));

        new FavoritesPresenter(favoritesFragment,
                FavoriteArticlesDataRepository.getInstance(FavoriteArticlesDataRemoteSource.getInstance(),
                        FavoriteArticlesDataLocalSource.getInstance()));

        new ReadLaterPresenter(readLaterFragment,
                ReadLaterArticlesRepository.getInstance(ReadLaterArticlesLocalSource.getInstance()));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline, container, false);
        initViews(view);
        return view;
    }

    private void initViews(View view){
        tabLayout = view.findViewById(R.id.tab_layout);
        viewPager = view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new TimelinePagerAdapter(getChildFragmentManager()
                , getContext(), articlesFragment, favoritesFragment, readLaterFragment));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        FragmentManager fragmentManager = getChildFragmentManager();
        if (articlesFragment.isAdded()){
            fragmentManager.putFragment(outState,ArticlesFragment.class.getSimpleName(),articlesFragment);
        }
        if (favoritesFragment.isAdded()){
            fragmentManager.putFragment(outState, FavoritesFragment.class.getSimpleName(), favoritesFragment);
        }
        if (readLaterFragment.isAdded()){
            fragmentManager.putFragment(outState, ReadLaterFragment.class.getSimpleName(), readLaterFragment);
        }
    }
}
