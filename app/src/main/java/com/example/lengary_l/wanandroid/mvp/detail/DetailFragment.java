package com.example.lengary_l.wanandroid.mvp.detail;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.example.lengary_l.wanandroid.R;
import com.just.agentweb.AgentWeb;

public class DetailFragment extends Fragment implements DetailContract.View{
    private FrameLayout webViewContainer;
    private Toolbar toolbar;
    private DetailContract.Presenter presenter;
    private String url;
    private AgentWeb agentWeb;


    public DetailFragment(){

    }

    public static DetailFragment newInstance(){
        return new DetailFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        url = getActivity().getIntent().getStringExtra(DetailActivity.URL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initViews(view);
        setHasOptionsMenu(true);
        return view;
    }



    @Override
    public void onResume() {
        if (agentWeb!=null){
            agentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
        loadUrl(url);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.detail_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                getActivity().onBackPressed();
                break;

            case R.id.action_more:
                BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                View view = getActivity().getLayoutInflater().inflate(R.layout.actions_details_sheet, null);
                AppCompatTextView textFavorite = view.findViewById(R.id.text_view_favorite);
                AppCompatTextView textAddToReadLater = view.findViewById(R.id.text_view_read_later);
                AppCompatTextView textCopyLink = view.findViewById(R.id.text_view_copy_the_link);
                AppCompatTextView textShare = view.findViewById(R.id.text_view_share);
                AppCompatTextView textBrowser = view.findViewById(R.id.text_view_system_browser);
                bottomSheetDialog.setContentView(view);
                bottomSheetDialog.show();
                break;
        }
        return true;
    }


    @Override
    public void initViews(View view) {
        toolbar = view.findViewById(R.id.toolBar);
        DetailActivity activity = (DetailActivity) getActivity();
        activity.setSupportActionBar(toolbar);
        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        webViewContainer = view.findViewById(R.id.web_view_container);
    }


    @Override
    public void setPresenter(DetailContract.Presenter presenter) {
        this.presenter = presenter;
    }

    private void loadUrl(String url){
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(webViewContainer, new FrameLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url);
    }



    @Override
    public void onPause() {
        agentWeb.getWebLifeCycle().onPause();
        super.onPause();
    }

    @Override
    public void onStop() {
        agentWeb.getWebLifeCycle().onDestroy();
        super.onStop();
    }


}
