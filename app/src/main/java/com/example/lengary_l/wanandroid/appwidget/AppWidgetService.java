package com.example.lengary_l.wanandroid.appwidget;

import android.content.Intent;
import android.widget.RemoteViewsService;

/**
 * Created by CoderLengary
 */


public class AppWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new WidgetListFactory(this.getApplicationContext());
    }
}
