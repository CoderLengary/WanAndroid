package com.example.lengary_l.wanandroid.mvp.timeline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.lengary_l.wanandroid.R;
import com.youth.banner.Banner;

public class ReadLaterFragment extends Fragment {

    private Banner banner;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private NestedScrollView nestedScrollView;

    public ReadLaterFragment(){

    }

    public static ReadLaterFragment newInstance(){
        return new ReadLaterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_page, container, false);
        return view;
    }
}
