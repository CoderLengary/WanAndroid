package com.example.lengary_l.wanandroid.app;

import android.app.Application;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatDelegate;

import com.example.lengary_l.wanandroid.util.SettingsUtil;

import io.realm.Realm;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Realm.init(this);

        if (PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).getBoolean(SettingsUtil.KEY_NIGHT_MODE, false)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}
