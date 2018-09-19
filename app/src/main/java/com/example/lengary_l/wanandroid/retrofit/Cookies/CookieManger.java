package com.example.lengary_l.wanandroid.retrofit.Cookies;

import android.content.Context;

import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

/**
 * Created by CoderLengary
 */


public class CookieManger implements CookieJar {


    private  Context mContext;

    private static PersistentCookieStore cookieStore;

    public CookieManger(Context context) {
        mContext = context.getApplicationContext();
        if (cookieStore == null) {
            cookieStore = new PersistentCookieStore(mContext);
        }

    }


    @Override
    public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
        if (cookies != null && cookies.size() > 0) {
            for (Cookie item : cookies) {
                cookieStore.add(url, item);
            }
        }
    }

    @Override
    public List<Cookie> loadForRequest(HttpUrl url) {

        return cookieStore.get(url);
    }

    public static void clearAllCookies() {
        cookieStore.removeAll();
    }


}