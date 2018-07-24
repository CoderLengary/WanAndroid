package com.example.lengary_l.wanandroid.mvp.timeline;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lengary_l.wanandroid.MainActivity;
import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.BannerDetailData;
import com.example.lengary_l.wanandroid.glide.GlideLoader;
import com.example.lengary_l.wanandroid.interfaze.OnCategoryOnClickListener;
import com.example.lengary_l.wanandroid.interfaze.OnRecyclerViewItemOnClickListener;
import com.example.lengary_l.wanandroid.mvp.category.CategoryActivity;
import com.example.lengary_l.wanandroid.mvp.detail.DetailActivity;
import com.example.lengary_l.wanandroid.util.NetworkUtil;
import com.example.lengary_l.wanandroid.util.SettingsUtil;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by CoderLengary
 */


public class ArticlesFragment extends Fragment implements ArticlesContract.View{
    private NestedScrollView nestedScrollView;
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private ArticlesContract.Presenter presenter;
    private SwipeRefreshLayout refreshLayout;
    private Banner banner;
    private  final int INDEX = 0;
    private LinearLayoutManager layoutManager;
    private int currentPage;
    private ArticlesAdapter adapter;

    private boolean isFirstLoad=true;
    private String userName;
    private String password;
    private SharedPreferences sp;


    public ArticlesFragment(){

    }

    public static ArticlesFragment newInstance(){
        return new ArticlesFragment();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        userName = sp.getString(SettingsUtil.USERNAME, "");
        password = sp.getString(SettingsUtil.PASSWORD, "");
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
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {

            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    loadMore();
                }
            }
        });



        return view;
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        Toast.makeText(getContext(),"change",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad){
            presenter.autoLogin(userName,password);
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
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void initViews(View view){
        banner = (Banner) getActivity().getLayoutInflater().inflate(R.layout.container_banner, null);
        banner.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getContext().getResources().getDisplayMetrics().heightPixels/4));
        emptyView = view.findViewById(R.id.empty_view);
        layoutManager = new LinearLayoutManager(getContext());
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
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
            adapter.setCategoryListener(new OnCategoryOnClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(getContext(), CategoryActivity.class);
                    ArticleDetailData data = list.get(position);
                    if (data.getChapterName().isEmpty()) {
                        return;
                    }
                    intent.putExtra(CategoryActivity.CATEGORY_ID, data.getChapterId());
                    intent.putExtra(CategoryActivity.CATEGORY_NAME, data.getChapterName());
                    startActivity(intent);
                }
            });
            adapter.setItemClickListener(new OnRecyclerViewItemOnClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    ArticleDetailData data = list.get(position);
                    intent.putExtra(DetailActivity.URL, data.getLink());
                    intent.putExtra(DetailActivity.TITLE, data.getTitle());
                    intent.putExtra(DetailActivity.ID, data.getId());
                    intent.putExtra(DetailActivity.FROM_FAVORITE_FRAGMENT, false);
                    intent.putExtra(DetailActivity.FROM_BANNER, false);
                    startActivity(intent);
                }
            });
            adapter.setHeaderView(banner);
            recyclerView.setAdapter(adapter);
        }

    }

    @Override
    public void showEmptyView(boolean toShow) {
        emptyView.setVisibility(toShow?View.VISIBLE:View.INVISIBLE);
        nestedScrollView.setVisibility(!toShow?View.VISIBLE:View.INVISIBLE);

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
                BannerDetailData data = list.get(position);
                intent.putExtra(DetailActivity.URL, data.getUrl());
                intent.putExtra(DetailActivity.TITLE, data.getTitle());
                intent.putExtra(DetailActivity.ID, data.getId());
                intent.putExtra(DetailActivity.FROM_FAVORITE_FRAGMENT, false);
                intent.putExtra(DetailActivity.FROM_BANNER, true);
                startActivity(intent);
            }
        });
        banner.start();
    }

    @Override
    public void hideBanner() {
        banner.setVisibility(View.GONE);
    }


    @Override
    public void showAutoLoginFail() {
        final AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        alertDialog.setTitle(R.string.warning_title);
        alertDialog.setMessage(getString(R.string.tip));
        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.sure), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity main = (MainActivity) getActivity();
                main.navigateToLogin();
                alertDialog.dismiss();
            }
        });
        alertDialog.show();
    }

    private void loadMore(){
        boolean isNetworkAvailable = NetworkUtil.isNetworkAvailable(getContext());
        if (isNetworkAvailable){
            currentPage+=1;
            presenter.getArticles(currentPage,true,false);
        }else {
            Toast.makeText(getContext(),R.string.network_error,Toast.LENGTH_LONG).show();
        }

    }



}
