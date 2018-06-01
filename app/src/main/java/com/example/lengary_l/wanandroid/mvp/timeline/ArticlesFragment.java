package com.example.lengary_l.wanandroid.mvp.timeline;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
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
    private ArticlesContract.Presenter presenter;
    private SwipeRefreshLayout refreshLayout;
    private static final int INDEX = 0;
    private LinearLayoutManager layoutManager;
    private int currentPage;
    private ArticlesAdapter adapter;


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

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
               // presenter.getArticles(INDEX,true,true);
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy>0){
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == layoutManager.getItemCount()-1) {
                       // loadMore();
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getArticles(0, true, true);
    }

    @Override
    public void initViews(View view){
        emptyView = view.findViewById(R.id.empty_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recycle_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setVisibility(View.VISIBLE);
        emptyView.setVisibility(View.INVISIBLE);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));

    }

    @Override
    public void setPresenter(ArticlesContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void setLoadingIndicator(final boolean isActive) {
        refreshLayout.post(new Runnable() {
            @Override
            public void run() {
                refreshLayout.setRefreshing(isActive);
            }
        });
    }

    @Override
    public void showArticles(List<ArticleDetailData> list) {
        if (adapter!=null){

        }else {
            adapter = new ArticlesAdapter(getContext(), list);
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public void showEmptyView() {
        if (emptyView.getVisibility()!=View.VISIBLE){
            emptyView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }
    }


    private void loadMore(){
        currentPage+=1;
        presenter.getArticles(currentPage,true,false);
    }

}
