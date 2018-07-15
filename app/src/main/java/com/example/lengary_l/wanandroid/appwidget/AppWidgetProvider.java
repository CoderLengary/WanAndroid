package com.example.lengary_l.wanandroid.appwidget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.mvp.detail.DetailActivity;

/**
 * Created by CoderLengary
 */

public class AppWidgetProvider extends android.appwidget.AppWidgetProvider {
    private static final String REFRESH_ACTION = "com.example.lengary_l.wanandroid";

    public static Intent getRefreshBroadcastIntent(Context context) {
        return new Intent(REFRESH_ACTION)
                .setComponent(new ComponentName(context, AppWidgetProvider.class));
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            RemoteViews remoteViews = updateWidgetListView(context, appWidgetId);
            appWidgetManager.updateAppWidget(appWidgetId, remoteViews);
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds);

    }

    private RemoteViews updateWidgetListView(Context context, int id) {
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.list_app_widget);
        Intent intent = new Intent(context, AppWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, id);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        remoteViews.setRemoteAdapter(R.id.list_view_widget, intent);
        remoteViews.setEmptyView(R.id.list_view_widget, R.id.empty_view);

        Intent tempIntent = new Intent(context, DetailActivity.class);
        tempIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        remoteViews.setPendingIntentTemplate(R.id.list_view_widget,
                PendingIntent.getActivity(context, 0, tempIntent, PendingIntent.FLAG_CANCEL_CURRENT));

        return remoteViews;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        super.onReceive(context, intent);
        if (intent.getAction().equals(REFRESH_ACTION)) {
            AppWidgetManager manager = AppWidgetManager.getInstance(context);
            ComponentName name = new ComponentName(context, AppWidgetProvider.class);
            manager.notifyAppWidgetViewDataChanged(manager.getAppWidgetIds(name), R.id.list_view_widget);
        }
    }
}
