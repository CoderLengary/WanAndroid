package com.example.lengary_l.wanandroid;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.lengary_l.wanandroid.util.SettingsUtil;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textView = findViewById(R.id.text_view);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        textView.setText(sharedPreferences.getString(SettingsUtil.USERNAME,"Error"));

    }
}
