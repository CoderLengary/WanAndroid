package com.example.lengary_l.wanandroid.mvp.timeline;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.ReadLaterArticleData;
import com.example.lengary_l.wanandroid.interfaze.OnCategoryOnClickListener;
import com.example.lengary_l.wanandroid.interfaze.OnRecyclerViewItemOnClickListener;
import com.example.lengary_l.wanandroid.mvp.category.CategoryActivity;
import com.example.lengary_l.wanandroid.mvp.detail.DetailActivity;
import com.example.lengary_l.wanandroid.util.SettingsUtil;

import java.util.List;

/**
 * Created by CoderLengary
 */


public class ReadLaterFragment extends Fragment implements ReadLaterContract.View{

    private RecyclerView recyclerView;
    private LinearLayout emptyView;
    private NestedScrollView nestedScrollView;
    private SwipeRefreshLayout refreshLayout;
    private ReadLaterContract.Presenter presenter;
    private ReadLaterAdapter adapter;
    private int userId;


    public ReadLaterFragment(){

    }

    public static ReadLaterFragment newInstance(){
        return new ReadLaterFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getContext());
        userId = sp.getInt(SettingsUtil.USERID, -1);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_timeline_page, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void initViews(View view) {
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        nestedScrollView = view.findViewById(R.id.nested_scroll_view);
        refreshLayout = view.findViewById(R.id.refresh_layout);
        refreshLayout.setEnabled(false);
        emptyView = view.findViewById(R.id.empty_view);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.subscribe();
        presenter.getReadLaterArticles(userId);
    }

    @Override
    public void setPresenter(ReadLaterContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.unSubscribe();
    }

    @Override
    public void showReadLaterArticles(final List<ReadLaterArticleData> list) {
        if (list.isEmpty()) {
            showEmptyView(true);
            return;
        }
        showEmptyView(false);
        if (adapter != null) {
            adapter.updateData(list);
        }else {
            adapter = new ReadLaterAdapter(getContext(), list);
            adapter.setCategoryListener(new OnCategoryOnClickListener() {
                @Override
                public void onClick(View view, int position) {
                    Intent intent = new Intent(getContext(), CategoryActivity.class);
                    ReadLaterArticleData data = list.get(position);
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
                    ReadLaterArticleData data = list.get(position);
                    intent.putExtra(DetailActivity.URL, data.getLink());
                    intent.putExtra(DetailActivity.TITLE, data.getTitle());
                    intent.putExtra(DetailActivity.ID, data.getId());
                    intent.putExtra(DetailActivity.FROM_FAVORITE_FRAGMENT, false);
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


}
