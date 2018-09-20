/*
 * Copyright (c) 2018 CoderLengary
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.lengary_l.wanandroid.retrofit.CookiesInterceptor.Util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import java.util.List;

/**
 * Created by CoderLengary
 */
public abstract class AbsCookiesUtil {


    private SharedPreferences mSharedPreferences;

    AbsCookiesUtil(@NonNull Context context, String sharePreferenceName){
        mSharedPreferences = context.getApplicationContext().getSharedPreferences(sharePreferenceName, Context.MODE_PRIVATE);
    }

    public void encodeAndSaveCookies(@NonNull List<String> cookiesList, @NonNull String host) {
        String cookiesListString = encodeCookies(cookiesList, host);
        saveCookies(cookiesListString, host);
    }

    abstract String encodeCookies(@NonNull List<String> cookiesList, @NonNull String host);

    private void saveCookies(String cookiesListString, String host) {
        mSharedPreferences.edit().putString(host, cookiesListString).apply();
    }

    public final String getCookiesListString(@NonNull String host) {
        return mSharedPreferences.getString(host, "");
    }

}
