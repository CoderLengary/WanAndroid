package com.example.lengary_l.wanandroid.mvp.search;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.data.HotKeyDetailData;
import com.example.lengary_l.wanandroid.interfaze.OnRecyclerViewItemOnClickListener;
import com.example.lengary_l.wanandroid.mvp.category.CategoryAdapter;
import com.example.lengary_l.wanandroid.mvp.detail.DetailActivity;
import com.example.lengary_l.wanandroid.util.NetworkUtil;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.List;

/**
 * Created by CoderLengary
 */


public class SearchFragment extends Fragment implements SearchContract.View {
    private static final String TAG = "SearchFragment";
    private SearchContract.Presenter presenter;
    private Toolbar toolbar;
    private SearchView searchView;
    private RecyclerView recyclerView;
    private TagFlowLayout flowLayout;
    private LinearLayout emptyView;
    private final int INDEX = 0;
    private int currentPage;
    private String keyWords;
    private boolean isFirstLoad = true;
    private CategoryAdapter adapter;
    private int articlesListSize;
    private LinearLayoutManager layoutManager;

    public SearchFragment() {

    }

    public static SearchFragment newInstance() {
        return new SearchFragment();
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initViews(view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                hideImn();
                hideTagFlowLayout(true);
                presenter.searchArticles(INDEX, query, true,true);
                keyWords = query;
                currentPage = INDEX;
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                hideTagFlowLayout(false);
                return true;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == articlesListSize - 1) {
                        loadMore();
                    }
                }
            }
        });

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
        if (isFirstLoad){
            presenter.getHotKeys(true);
            isFirstLoad = false;
        }else {
            presenter.getHotKeys(false);
            presenter.searchArticles(currentPage, keyWords, false,false);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
                default:break;
        }
        return true;
    }

    @Override
    public void initViews(View view) {
        layoutManager = new LinearLayoutManager(getContext());
        SearchActivity searchActivity = (SearchActivity) getActivity();
        toolbar = view.findViewById(R.id.toolBar);
        searchActivity.setSupportActionBar(toolbar);
        searchActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        searchView = view.findViewById(R.id.search_view);
        searchView.setIconified(false);
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        flowLayout = view.findViewById(R.id.flow_layout);
        emptyView = view.findViewById(R.id.empty_view);

    }

    @Override
    public void setPresenter(SearchContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void showArticles(final List<ArticleDetailData> articlesList) {
        if (articlesList.isEmpty()) {
            showEmptyView(true);
            return;
        }
        showEmptyView(false);
        if (adapter == null) {
            adapter = new CategoryAdapter(getContext(),articlesList);
            adapter.setItemClickListener(new OnRecyclerViewItemOnClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(getContext(), DetailActivity.class);
                    ArticleDetailData data = articlesList.get(position);
                    intent.putExtra(DetailActivity.ID, data.getId());
                    intent.putExtra(DetailActivity.URL, data.getLink());
                    intent.putExtra(DetailActivity.TITLE, data.getTitle());
                    intent.putExtra(DetailActivity.FROM_FAVORITE_FRAGMENT, false);
                    intent.putExtra(DetailActivity.FROM_BANNER, false);
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        }else {
            adapter.updateData(articlesList);
        }
        articlesListSize = articlesList.size();
    }

    @Override
    public void showHotKeys(final List<HotKeyDetailData> hotKeyList) {
        flowLayout.setAdapter(new TagAdapter<HotKeyDetailData>(hotKeyList) {
            @Override
            public View getView(FlowLayout parent, int position, HotKeyDetailData hotKeyDetailData) {
                TextView textView = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.item_flow_layout, flowLayout, false);
                if (hotKeyList == null) {
                    return null;
                }
                textView.setText(hotKeyDetailData.getName());
                flowLayout.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
                    @Override
                    public boolean onTagClick(View view, int position, FlowLayout parent) {
                        searchView.setQuery(hotKeyList.get(position).getName(),true);
                        return true;
                    }
                });
                return textView;
            }
        });
    }

    @Override
    public boolean isActive() {
        return isAdded() && isResumed();
    }

    private void loadMore() {
        boolean isNetworkAvailable = NetworkUtil.isNetworkAvailable(getContext());
        if (isNetworkAvailable) {
            currentPage += 1;
            presenter.searchArticles(currentPage, keyWords, true, false);
        } else {
            Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }
    }

    private void hideTagFlowLayout(boolean hide) {
        if (hide) {
            flowLayout.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }else {
            flowLayout.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }
    @Override
    public void hideImn() {
        InputMethodManager manager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (manager.isActive()) {
            manager.hideSoftInputFromWindow(searchView.getWindowToken(), 0);
        }
    }

    @Override
    public void showEmptyView(boolean toShow) {
        emptyView.setVisibility(toShow?View.VISIBLE:View.INVISIBLE);
        recyclerView.setVisibility(!toShow?View.VISIBLE:View.INVISIBLE);
    }
}
