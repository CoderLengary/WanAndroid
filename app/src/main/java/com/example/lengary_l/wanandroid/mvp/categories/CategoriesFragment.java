package com.example.lengary_l.wanandroid.mvp.categories;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.CategoryDetailData;
import com.example.lengary_l.wanandroid.interfaze.OnFlowLayoutItemOnClickListener;
import com.example.lengary_l.wanandroid.mvp.category.CategoryActivity;
import com.zhy.view.flowlayout.FlowLayout;

import java.util.List;

/**
 * Created by CoderLengary
 */


public class CategoriesFragment extends Fragment implements CategoriesContract.View{


    private RecyclerView recyclerView;
    private CategoriesContract.Presenter presenter;
    private CategoriesAdapter adapter;
    private SwipeRefreshLayout refreshLayout;
    private LinearLayout emptyView;
    private boolean isFirstLoad = true;

    public CategoriesFragment(){

    }

    public static CategoriesFragment newInstance(){
        return new CategoriesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_categories,container,false);
        initViews(view);
        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                presenter.subscribe();
            }
        });
        return view;
    }

    @Override
    public void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyView = view.findViewById(R.id.empty_view);
        refreshLayout = view.findViewById(R.id.refresh_layout);

    }

    @Override
    public void setPresenter(CategoriesContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showCategories(List<CategoryDetailData> list) {
        if (adapter==null){
            adapter = new CategoriesAdapter(getContext(), list);
            adapter.setOnFlowLayoutItemOnClickListener(new OnFlowLayoutItemOnClickListener() {
                @Override
                public void onClick(View view, int position, FlowLayout parent, List<CategoryDetailData> children) {
                    Intent intent = new Intent(getContext(), CategoryActivity.class);
                    intent.putExtra(CategoryActivity.CATEGORY_ID, children.get(position).getId());
                    intent.putExtra(CategoryActivity.CATEGORY_NAME, children.get(position).getName());
                    startActivity(intent);
                }
            });
            recyclerView.setAdapter(adapter);
        }else {
            adapter.updateData(list);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isFirstLoad){
            presenter.subscribe();
            isFirstLoad = false;
        }else {
            presenter.getCategories(false);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    @Override
    public void showEmptyView(boolean toShow) {
        emptyView.setVisibility(toShow?View.VISIBLE:View.INVISIBLE);
        refreshLayout.setVisibility(!toShow?View.VISIBLE:View.INVISIBLE);
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
    public boolean isActive() {
        return isAdded()&&isResumed();
    }
}
