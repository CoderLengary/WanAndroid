package com.example.lengary_l.wanandroid.mvp.timeline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.ArticleDetailData;

import java.util.List;

public class ArticlesFragment extends Fragment implements ArticlesContract.View{
    private RecyclerView recyclerView;
    private LinearLayout emptyView;

    public ArticlesFragment(){

    }

    public static ArticlesFragment newInstance(){
        return new ArticlesFragment();

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_page, container, false);
        initViews(view);
        return view;
    }
    @Override
    public void initViews(View view){
        emptyView = view.findViewById(R.id.empty_view);
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setAdapter(new ArticlesAdapter(getContext()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);


    }

    @Override
    public void setPresenter(ArticlesContract.Presenter presenter) {

    }


    @Override
    public void setLoadingIndicator(boolean isActive) {

    }

    @Override
    public void showArticles(List<ArticleDetailData> list) {

    }

    @Override
    public void showEmptyView() {

    }
}
