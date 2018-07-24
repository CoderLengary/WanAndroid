package com.example.lengary_l.wanandroid.mvp.timeline;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.RxBus.RxBus;
import com.example.lengary_l.wanandroid.data.FavoriteArticleDetailData;
import com.example.lengary_l.wanandroid.interfaze.OnCategoryOnClickListener;
import com.example.lengary_l.wanandroid.interfaze.OnRecyclerViewItemOnClickListener;
import com.example.lengary_l.wanandroid.mvp.category.CategoryActivity;
import com.example.lengary_l.wanandroid.mvp.detail.DetailActivity;
import com.example.lengary_l.wanandroid.util.NetworkUtil;

import java.util.List;

import io.reactivex.functions.Consumer;

/**
 * Created by CoderLengary
 */


public class FavoritesFragment extends Fragment implements FavoritesContract.View{
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private NestedScrollView nestedScrollView;
    private FavoritesContract.Presenter presenter;
    private FavoritesAdapter adapter;
    private int currentPage;
    private boolean isFirstLoad=true;
    private final int INDEX = 0;
    private SwipeRefreshLayout refreshLayout;


    public FavoritesFragment(){

    }

    public static FavoritesFragment newInstance(){
        return new FavoritesFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        RxBus.getInstance().subscribe(String.class,new Consumer<String>(){

            @Override
            public void accept(String s) throws Exception {
                if (s.equals(RxBus.REFRESH)) {
                    isFirstLoad = true;
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_page, container, false);
        initViews(view);
        nestedScrollView.setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
            @Override
            public void onScrollChange(NestedScrollView v, int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                if (scrollY == (v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                    loadMore();
                }
            }
        });
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = INDEX;
                presenter.getFavoriteArticles(INDEX, true, true);

            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
        if (isFirstLoad){
            presenter.getFavoriteArticles(INDEX, true,true);

            currentPage = INDEX;
            isFirstLoad = false;
        }else {
            presenter.getFavoriteArticles(INDEX, false,false);
        }


    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
        RxBus.getInstance().unSubscribe();
    }

    @Override
    public void showFavoriteArticles(final List<FavoriteArticleDetailData> list) {
        if (list.isEmpty()) {
            showEmptyView(true);
            return;
        }
        showEmptyView(false);
        if (adapter != null) {
            adapter.updateData(list);
        }else {
            adapter = new FavoritesAdapter(getContext(), list);
            adapter.setCategoryListener(new OnCategoryOnClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(getContext(), CategoryActivity.class);
                    FavoriteArticleDetailData data = list.get(position);
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
                    FavoriteArticleDetailData data = list.get(position);
                    intent.putExtra(DetailActivity.URL, data.getLink());
                    intent.putExtra(DetailActivity.TITLE, data.getTitle());
                    intent.putExtra(DetailActivity.ID, data.getOriginId());
                    intent.putExtra(DetailActivity.FROM_FAVORITE_FRAGMENT, true);
                    intent.putExtra(DetailActivity.FROM_BANNER, false);
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        }
    }

    @Override
    public boolean isActive() {
        return isAdded()&&isResumed();
    }

    @Override
    public void showEmptyView(boolean toShow) {
        emptyView.setVisibility(toShow?View.VISIBLE:View.INVISIBLE);
        nestedScrollView.setVisibility(!toShow?View.VISIBLE:View.INVISIBLE);
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
    public void initViews(View view) {
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyView = view.findViewById(R.id.empty_view);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimary));
    }

    @Override
    public void setPresenter(FavoritesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void loadMore() {
        boolean isNetworkAvailable = NetworkUtil.isNetworkAvailable(getContext());
        if (isNetworkAvailable){
            currentPage += 1;
            presenter.getFavoriteArticles(currentPage, true,false);
        }else {
            Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }



}
