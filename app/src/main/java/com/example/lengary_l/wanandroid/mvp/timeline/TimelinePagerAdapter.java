package com.example.lengary_l.wanandroid.mvp.timeline;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.lengary_l.wanandroid.R;

/**
 * Created by CoderLengary
 */


public class TimelinePagerAdapter extends FragmentPagerAdapter {
    private ArticlesFragment articlesFragment;
    private FavoritesFragment favoritesFragment;
    private ReadLaterFragment readLaterFragment;
    private String[] titles;


    private final int pageCount = 3;

    public TimelinePagerAdapter(FragmentManager fm,
                                Context context,
                                ArticlesFragment articlesFragment,
                                FavoritesFragment favoritesFragment,
                                ReadLaterFragment readLaterFragment) {
        super(fm);
        this.articlesFragment = articlesFragment;
        this.favoritesFragment = favoritesFragment;
        this.readLaterFragment = readLaterFragment;
        titles = new String[]{
                context.getString(R.string.timeline_articles),
                context.getString(R.string.timeline_favorites),
                context.getString(R.string.timeline_read_later)
        };
    }

    @Override
    public Fragment getItem(int position) {
        Fragment fragment ;
        switch (position){
            case 0:
                fragment = articlesFragment;
                break;

            case 1:
                fragment = favoritesFragment;
                break;

            case 2:
                fragment = readLaterFragment;
                break;

            default:
                fragment=articlesFragment;
                break;
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return pageCount;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
