package com.example.lengary_l.wanandroid.app;

import android.app.Application;
import android.content.Context;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import com.example.lengary_l.wanandroid.util.SettingsUtil;

import io.realm.Realm;

/**
 * Created by CoderLengary
 */

public class App extends Application {

    private static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Realm.init(this);
        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(SettingsUtil.KEY_NIGHT_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }

    public static Context getContext(){
        return context;
    }
}
