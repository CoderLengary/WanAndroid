package com.example.lengary_l.wanandroid.mvp.detail;

import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.RxBus.RxBus;
import com.example.lengary_l.wanandroid.appwidget.AppWidgetProvider;
import com.example.lengary_l.wanandroid.util.SettingsUtil;
import com.just.agentweb.AgentWeb;

/**
 * Created by CoderLengary
 */


public class DetailFragment extends Fragment implements DetailContract.View{
    private FrameLayout webViewContainer;
    private Toolbar toolbar;
    private DetailContract.Presenter presenter;
    private String url;
    private String title;
    private int id;
    private AgentWeb agentWeb;
    private int userId;
    private boolean isReadLater;
    private boolean isFavorite;
    private boolean isFromFavoriteFragment;
    private boolean isFromBanner;
    private boolean sendRxBus;
    private boolean sendRefreshBroadcast;


    public DetailFragment(){

    }

    public static DetailFragment newInstance(){
        return new DetailFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getActivity().getIntent();
        url = intent.getStringExtra(DetailActivity.URL);
        title = intent.getStringExtra(DetailActivity.TITLE);
        id = intent.getIntExtra(DetailActivity.ID, -1);
        userId= PreferenceManager.getDefaultSharedPreferences(getContext()).getInt(SettingsUtil.USERID, -1);
        isFromFavoriteFragment = intent.getBooleanExtra(DetailActivity.FROM_FAVORITE_FRAGMENT, false);
        isFromBanner = intent.getBooleanExtra(DetailActivity.FROM_BANNER, false);
        sendRxBus = false;
        sendRefreshBroadcast = false;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        initViews(view);
        loadUrl(url);
        toolbar.setTitle(title);
        setHasOptionsMenu(true);
        return view;
    }



    @Override
    public void onResume() {
        if (agentWeb!=null){
            agentWeb.getWebLifeCycle().onResume();
        }
        super.onResume();
        presenter.subscribe();
        presenter.checkIsReadLater(userId, id);

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
                presenter.checkIsFavorite(userId,id);
                final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(getActivity());
                View view = getActivity().getLayoutInflater().inflate(R.layout.actions_details_sheet, null);
                AppCompatTextView textFavorite = view.findViewById(R.id.text_view_favorite);
                textFavorite.setText(isFavorite ? R.string.detail_uncollect_article : R.string.detail_collect_article);
                textFavorite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isFavorite) {
                            presenter.uncollectArticle(userId, id);
                        } else {
                            presenter.collectArticle(userId, id);
                        }
                        bottomSheetDialog.dismiss();
                    }
                });

                if (isFromBanner) {
                    textFavorite.setVisibility(View.GONE);
                }

                AppCompatTextView textAddToReadLater = view.findViewById(R.id.text_view_read_later);
                textAddToReadLater.setText(isReadLater ? R.string.detail_remove_from_read_later : R.string.detail_add_to_read_later);
                textAddToReadLater.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (isReadLater) {
                            presenter.removeReadLaterArticle(userId, id);
                        } else {
                            presenter.insertReadLaterArticle(userId, id, System.currentTimeMillis());
                        }
                        isReadLater = !isReadLater;
                        sendRefreshBroadcast = !sendRefreshBroadcast;
                        bottomSheetDialog.dismiss();
                    }
                });

                if (isFromFavoriteFragment||isFromBanner) {
                    textAddToReadLater.setVisibility(View.GONE);
                }


                AppCompatTextView textCopyLink = view.findViewById(R.id.text_view_copy_the_link);
                textCopyLink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        copyLink();
                        bottomSheetDialog.dismiss();
                        showMessage(R.string.detail_copied_to_clipboard);
                    }
                });
                AppCompatTextView textShare = view.findViewById(R.id.text_view_share);
                textShare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        share();
                        bottomSheetDialog.dismiss();
                    }
                });
                AppCompatTextView textBrowser = view.findViewById(R.id.text_view_system_browser);
                textBrowser.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        openInBrowser();
                        bottomSheetDialog.dismiss();
                    }
                });
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
        ActionBar actionBar = activity.getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_close_white_24dp);
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

        WebView webView = agentWeb.getWebCreator().getWebView();
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setBuiltInZoomControls(true);
        settings.setDisplayZoomControls(false);
        settings.setUseWideViewPort(true);
        settings.setLoadsImagesAutomatically(true);
        settings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);


    }



    @Override
    public void onPause() {
        if (agentWeb != null) {
            agentWeb.getWebLifeCycle().onPause();
        }
        super.onPause();
        presenter.unSubscribe();
        if (sendRxBus) {
            //The {@link FavoriteFragment} will receive this message.
            RxBus.getInstance().send(RxBus.REFRESH);
            sendRxBus = !sendRxBus;
        }
        if (sendRefreshBroadcast){
            getActivity().sendBroadcast(AppWidgetProvider.getRefreshBroadcastIntent(getContext()));
            sendRefreshBroadcast = !sendRefreshBroadcast;
        }
    }


    @Override
    public void onDestroy() {
        if (agentWeb != null) {
            agentWeb.getWebLifeCycle().onDestroy();
        }
        super.onDestroy();
    }

    private void copyLink() {
        ClipboardManager manager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("text", Html.fromHtml(url).toString());
        manager.setPrimaryClip(data);

    }


    private void share() {
        try {
            Intent shareIntent = new Intent(Intent.ACTION_SEND).setType("text/plain");
            String shareText = title + " " + url;
            shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);
            startActivity(Intent.createChooser(shareIntent, getString(R.string.detail_share_to)));
        }catch (ActivityNotFoundException e){
            showMessage(R.string.detail_no_activity_found);
        }
    }

    private void openInBrowser() {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
        }catch (ActivityNotFoundException e){
            showMessage(R.string.detail_no_browser_found);
        }
    }
    private void showMessage(int stringRes) {
        Toast.makeText(getContext(),stringRes,Toast.LENGTH_SHORT).show();
    }


    public boolean onFragmentKeyDown(int keyCode, KeyEvent event) {
        return agentWeb.handleKeyEvent(keyCode, event);
    }

    @Override
    public void showCollectStatus(boolean isSuccess) {
        Toast.makeText(getContext(),
                isSuccess?getString(R.string.detail_collect_article_success):getString(R.string.detail_collect_article_error),
                Toast.LENGTH_LONG).show();

    }

    @Override
    public void showUnCollectStatus(boolean isSuccess) {
        Toast.makeText(getContext(),
                isSuccess?getString(R.string.detail_uncollect_article_success):getString(R.string.detail_uncollect_article_error),
                Toast.LENGTH_LONG).show();

    }

    @Override
    public boolean isActive() {
        return isAdded() && isResumed();
    }

    @Override
    public void saveSendRxBus() {
        sendRxBus = !sendRxBus;
    }

    @Override
    public void saveReadLaterState(boolean isReadLater) {
        this.isReadLater = isReadLater;
    }

    @Override
    public void saveFavoriteState(boolean isFavorite) {
        this.isFavorite = isFavorite;
    }






}
