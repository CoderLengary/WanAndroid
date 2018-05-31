package com.example.lengary_l.wanandroid.mvp.timeline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lengary_l.wanandroid.R;

public class TimelineFragment extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ArticlesFragment articlesFragment;
    private FavoritesFragment favoritesFragment;
    private SeeLaterFragment seeLaterFragment;

    public TimelineFragment() {

    }

    public static TimelineFragment newInstance(){
        return new TimelineFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState!=null){

        }else {
            articlesFragment = ArticlesFragment.newInstance();
            favoritesFragment = FavoritesFragment.newInstance();
            seeLaterFragment = SeeLaterFragment.newInstance();
        }
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
                , getContext(), articlesFragment, favoritesFragment, seeLaterFragment));
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setupWithViewPager(viewPager);
    }


}
