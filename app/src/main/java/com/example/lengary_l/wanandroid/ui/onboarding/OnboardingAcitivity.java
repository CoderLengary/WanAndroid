package com.example.lengary_l.wanandroid.ui.onboarding;

import android.animation.ArgbEvaluator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatButton;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.lengary_l.wanandroid.R;
import com.example.lengary_l.wanandroid.mvp.login.LoginActivity;
import com.example.lengary_l.wanandroid.util.SettingsUtil;

/**
 * Created by CoderLengary
 */


public class OnboardingAcitivity extends AppCompatActivity {

    private ViewPager viewPager;
    private AppCompatButton btnFinish;
    private ImageButton imgBtnPre;
    private ImageButton imgBtnNext;
    private ImageView[] indicators;

    private int[] bgColors;
    private int currentPosition;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onboarding);
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(this);
        if (sp.getBoolean("navigation_bar_tint", true)) {
            getWindow().setNavigationBarColor(ContextCompat.getColor(this,R.color.colorPrimaryDark));
        }
        if (sp.getBoolean(SettingsUtil.KEY_SKIP_GUIDE_PAGE,false)){
            navigateToMain();
        }else {
            initViews();
            initDatas();
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                    int color = (int) new ArgbEvaluator().evaluate(positionOffset, bgColors[position], bgColors[position == 2 ? position  : position+1]);
                    viewPager.setBackgroundColor(color);
                }

                @Override
                public void onPageSelected(int position) {
                    currentPosition = position;
                    viewPager.setBackgroundColor(bgColors[position]);
                    updateIndicators(position);
                    imgBtnPre.setVisibility(position == 0 ? View.GONE : View.VISIBLE);
                    imgBtnNext.setVisibility(position == 2 ? View.GONE : View.VISIBLE);
                    btnFinish.setVisibility(position == 2 ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            btnFinish.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(OnboardingAcitivity.this);
                    sp.edit().putBoolean(SettingsUtil.KEY_SKIP_GUIDE_PAGE, true).apply();
                    navigateToMain();
                }
            });

            imgBtnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentPosition += 1;
                    viewPager.setCurrentItem(currentPosition);
                }
            });

            imgBtnPre.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    currentPosition -= 1;
                    viewPager.setCurrentItem(currentPosition);
                }
            });
        }
    }


    private void initDatas(){
        bgColors = new int[]{ContextCompat.getColor(this,R.color.colorPrimary)
                ,ContextCompat.getColor(this,R.color.cyan_500)
                ,ContextCompat.getColor(this,R.color.light_blue_500)
        };
    }
    private void initViews() {
        OnboardingPagerAdapter pagerAdapter = new OnboardingPagerAdapter(getSupportFragmentManager());
        viewPager = findViewById(R.id.container);
        viewPager.setAdapter(pagerAdapter);
        btnFinish = findViewById(R.id.btn_finish);
        imgBtnPre = findViewById(R.id.image_btn_pre);
        imgBtnNext = findViewById(R.id.image_btn_next);
        indicators = new ImageView[]{
                findViewById(R.id.img_indicator_0),
                findViewById(R.id.img_indicator_1),
                findViewById(R.id.img_indicator_2)};
    }

    private void updateIndicators(int position) {
        for (int i = 0; i < indicators.length; i++) {
            indicators[i].setBackgroundResource(i == position ? R.drawable.onboarding_indicator_selected : R.drawable.onboarding_indicator_unselected);
        }
    }


    private void navigateToMain() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }


}
