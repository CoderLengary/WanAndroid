package com.example.lengary_l.wanandroid.ui;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.widget.Toast;

import com.example.lengary_l.wanandroid.BuildConfig;
import com.example.lengary_l.wanandroid.R;

/**
 * Created by CoderLengary
 */


public class AboutFragment extends PreferenceFragmentCompat {


    private Preference prefNavigationBar,prefVersion,prefRate,prefLicenses,
            preLiZhaoTaiLang,preHongYang,prefSourceCode,
            prefSendAdvices;

    public static AboutFragment newInstance() {
        return new AboutFragment();
    }


    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.about_prefs);
        initPrefs();
        prefNavigationBar.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Snackbar.make(getView(),getString(R.string.about_navigation_bar_restart_msg),Snackbar.LENGTH_SHORT).show();
                return true;
            }
        });
        prefVersion.setSummary(BuildConfig.VERSION_NAME);
        prefRate.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Uri uri = Uri.parse("market://details?id=" + getActivity().getPackageName());
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    showError(R.string.error);
                }
                return true;
            }
        });

        prefLicenses.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                Intent intent = new Intent(getContext(), LicensesActivity.class);
                startActivity(intent);
                return true;
            }
        });

        preLiZhaoTaiLang.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                openInBrowser(getString(R.string.about_lizhaotailang_link));
                return true;
            }
        });

        preHongYang.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                openInBrowser(getString(R.string.about_hongyang_link));
                return true;
            }
        });

        prefSourceCode.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                openInBrowser(getString(R.string.about_source_code_link));
                return true;
            }
        });

        prefSendAdvices.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference) {
                try {
                    Uri uri = Uri.parse(getString(R.string.mail_account));
                    Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.mail_topic));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    showError(R.string.error);
                }
                return true;
            }
        });
    }

    private void initPrefs() {
        prefNavigationBar = findPreference("navigation_bar_tint");
        prefVersion = findPreference("version");
        prefRate = findPreference("rate");
        prefLicenses = findPreference("licenses");
        preLiZhaoTaiLang = findPreference("lizhaotailang");
        preHongYang = findPreference("hongyang");
        prefSourceCode = findPreference("source_code");
        prefSendAdvices = findPreference("send_advice");
    }

    private void showError(int resId) {
        Toast.makeText(getContext(), getString(resId), Toast.LENGTH_SHORT).show();
    }

    private void openInBrowser(String url) {
        try {
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(url)));
        }catch (ActivityNotFoundException e){
            showError(R.string.detail_no_browser_found);
        }
    }

}
