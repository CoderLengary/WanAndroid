package com.example.lengary_l.wanandroid.mvp.category;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.ArticleDetailData;
import com.example.lengary_l.wanandroid.interfaze.OnRecyclerViewItemOnClickListener;
import com.example.lengary_l.wanandroid.mvp.detail.DetailActivity;
import com.example.lengary_l.wanandroid.util.NetworkUtil;

import java.util.List;

/**
 * Created by CoderLengary
 */


public class CategoryFragment extends Fragment implements CategoryContract.View {
    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private Toolbar toolbar;
    private CategoryContract.Presenter presenter;
    private boolean isFirstLoad=true;
    private int categoryId;
    private String categoryName;
    private LinearLayoutManager layoutManager;
    private CategoryAdapter adapter;
    private static final int INDEX = 0;
    private int mListSize;
    private int currentPage;



    public CategoryFragment(){

    }

    public static CategoryFragment newInstance(){
        return new CategoryFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryId=getActivity().getIntent().getIntExtra(CategoryActivity.CATEGORY_ID,-1);
        categoryName = getActivity().getIntent().getStringExtra(CategoryActivity.CATEGORY_NAME);
    }

    @Override
    public void onResume() {
        super.onResume();
        toolbar.setTitle(categoryName);
        if (isFirstLoad){
            presenter.getArticlesFromCatg(INDEX,categoryId,true,true);
            currentPage = INDEX;
            isFirstLoad = false;
        }else {
            presenter.getArticlesFromCatg(currentPage, categoryId, false,false);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_category, container, false);
        initViews(view);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    if (layoutManager.findLastCompletelyVisibleItemPosition() == mListSize - 1) {
                        loadMore();
                    }
                }
            }
        });
        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void initViews(View view) {
        CategoryActivity activity = (CategoryActivity) getActivity();
        toolbar = view.findViewById(R.id.toolBar);
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        recyclerView = view.findViewById(R.id.recycler_view);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        emptyView = view.findViewById(R.id.empty_view);
    }

    @Override
    public void setPresenter(CategoryContract.Presenter presenter) {
        this.presenter = presenter;
    }


    @Override
    public void showArticles(final List<ArticleDetailData> list) {
        if (adapter == null) {
            adapter = new CategoryAdapter(getContext(), list);
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
            recyclerView.setAdapter(adapter);
        }else {
            adapter.updateData(list);
        }
        mListSize = list.size();
    }

    @Override
    public void showEmptyView(boolean toShow) {
        emptyView.setVisibility(toShow?View.VISIBLE:View.INVISIBLE);
        recyclerView.setVisibility(!toShow?View.VISIBLE:View.INVISIBLE);
    }

    @Override
    public boolean isActive() {
        return isAdded() && isResumed();
    }

    private void loadMore() {
        boolean isNetworkAvailable = NetworkUtil.isNetworkAvailable(getContext());
        if (isNetworkAvailable) {
            currentPage += 1;
            presenter.getArticlesFromCatg(currentPage, categoryId, true, false);
        }else {
            Toast.makeText(getContext(), R.string.network_error, Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
                default:break;
        }
        return true;
    }
}
