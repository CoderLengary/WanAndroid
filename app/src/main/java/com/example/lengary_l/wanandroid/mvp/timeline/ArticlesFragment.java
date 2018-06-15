package com.example.lengary_l.wanandroid.mvp.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lengary_l.wanandroid.glide.GlideLoader;
import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.BannerDetailData;
import com.example.lengary_l.wanandroid.interfaze.OnRecyclerViewItemOnClickListener;
import com.example.lengary_l.wanandroid.mvp.detail.DetailActivity;
import com.example.lengary_l.wanandroid.util.NetworkUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

public class ArticlesFragment extends Fragment implements ArticlesContract.View{
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private ArticlesContract.Presenter presenter;
    private SwipeRefreshLayout refreshLayout;
    private Banner banner;
    private static final int INDEX = 0;
    private LinearLayoutManager layoutManager;
    private int currentPage;
    private ArticlesAdapter adapter;
    private static final String TAG = "ArticlesFragment";
    private boolean isFirstLoad=true;
    private int mListSize = 0;


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
                currentPage = INDEX;
                presenter.getArticles(INDEX,true,true);

            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                Log.e(TAG, "onScrolled: is true" );
                if (dy>0){
                    Log.e(TAG, "onScrolled: layout manager last position is " +layoutManager.findLastVisibleItemPosition()
                    + " and the list-1 value is "+(mListSize-1));
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == mListSize-1) {
                        Log.e(TAG, "onScrolled: load more" );
                       loadMore();
                    }
                }
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad){
            Log.e(TAG, "onResume: is first load" );
            presenter.getArticles(INDEX, true, true);
            presenter.getBanner();
            currentPage = INDEX;
            isFirstLoad = false;
        }else {
            presenter.getArticles(currentPage,false,false);
        }
        if (banner != null) {
            banner.startAutoPlay();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    @Override
    public void onStop() {
        super.onStop();
        banner.stopAutoPlay();
    }

    @Override
    public void initViews(View view){
        banner = view.findViewById(R.id.banner);
        emptyView = view.findViewById(R.id.empty_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.recycler_view);
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
    public boolean isActive() {
        return isAdded()&&isResumed();
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
    public void showArticles(final List<ArticleDetailData> list) {
        if (adapter!=null){
            adapter.updateData(list);
        }else {
            adapter = new ArticlesAdapter(getContext(), list);
            adapter.setItemClickListener(new OnRecyclerViewItemOnClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    intent.putExtra(DetailActivity.URL, list.get(position).getLink());
                    intent.putExtra(DetailActivity.TITLE, list.get(position).getTitle());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        }
        mListSize = list.size();

        Log.e(TAG, "showArticles: list size "+list.size() );
    }

    @Override
    public void showEmptyView() {
        emptyView.setVisibility(View.VISIBLE);
        refreshLayout.setVisibility(View.INVISIBLE);

    }

    @Override
    public void showBanner(final List<BannerDetailData> list) {
        List<String> urls = new ArrayList<>();
        for (BannerDetailData item : list) {
            urls.add(item.getImagePath());
        }
        banner.setImages(urls);
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);
        banner.setImageLoader(new GlideLoader());
        banner.setBannerAnimation(Transformer.ZoomOutSlide);
        banner.isAutoPlay(true);
        banner.setDelayTime(7800);
        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Intent intent = new Intent(getContext(),DetailActivity.class);
                intent.putExtra(DetailActivity.URL, list.get(position).getUrl());
                intent.putExtra(DetailActivity.TITLE, list.get(position).getTitle());
                startActivity(intent);
            }
        });
        banner.start();
    }

    @Override
    public void hideBanner() {
        banner.setVisibility(View.GONE);
    }


    private void loadMore(){
        boolean isNetworkAvailable = NetworkUtil.isNetworkAvailable(getContext());
        if (isNetworkAvailable){
            currentPage+=1;
            Log.e(TAG, "loadMore: update page is "+currentPage );
            presenter.getArticles(currentPage,true,false);
        }else {
            Toast.makeText(getContext(),R.string.network_error,Toast.LENGTH_LONG).show();
        }

    }



}
