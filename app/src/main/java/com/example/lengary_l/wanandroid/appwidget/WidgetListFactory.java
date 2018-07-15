package com.example.lengary_l.wanandroid.appwidget;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.data.ReadLaterArticleData;
import com.example.lengary_l.wanandroid.mvp.detail.DetailActivity;
import com.example.lengary_l.wanandroid.realm.RealmHelper;
import com.example.lengary_l.wanandroid.util.SettingsUtil;
import com.example.lengary_l.wanandroid.util.StringUtil;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.Sort;

/**
 * Created by CoderLengary
 */


public class WidgetListFactory implements RemoteViewsService.RemoteViewsFactory {

    private Context context;
    private int userId;

    public WidgetListFactory(Context context) {
        this.context = context;
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(context);
        userId = sp.getInt(SettingsUtil.USERID, -1);
    }

    @Override
    public void onCreate() {

    }

    @Override
    public void onDataSetChanged() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        Realm realm=Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(RealmHelper.DATABASE_NAME)
                .build());
        return realm.where(ReadLaterArticleData.class)
                .equalTo("userId", userId)
                .findAll()
                .size();
    }

    @Override
    public RemoteViews getViewAt(int i) {
        RemoteViews remoteViews = new RemoteViews(
                context.getPackageName(), R.layout.item_list_app_widget);
        Realm realm=Realm.getInstance(new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .name(RealmHelper.DATABASE_NAME)
                .build());
        List<ReadLaterArticleData> list = realm.copyFromRealm(
                realm.where(ReadLaterArticleData.class)
                        .equalTo("userId", userId)
                        .sort("timestamp", Sort.DESCENDING)
                        .findAll());
        ReadLaterArticleData data = list.get(i);
        remoteViews.setTextViewText(R.id.text_view_author,data.getAuthor());
        remoteViews.setTextViewText(R.id.text_view_title, StringUtil.replaceInvalidChar(data.getTitle()));
        Intent intent = new Intent();
        intent.putExtra(DetailActivity.ID, data.getId());
        intent.putExtra(DetailActivity.TITLE, data.getTitle());
        intent.putExtra(DetailActivity.URL, data.getLink());
        intent.putExtra(DetailActivity.FROM_FAVORITE_FRAGMENT, false);
        intent.putExtra(DetailActivity.FROM_BANNER, false);
        remoteViews.setOnClickFillInIntent(R.id.item_main, intent);
        return remoteViews;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
