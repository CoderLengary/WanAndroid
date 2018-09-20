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

package com.example.lengary_l.wanandroid.retrofit.CookiesInterceptor;

import android.content.Context;
import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.retrofit.CookiesInterceptor.Interceptor.AddCookieInterceptor;
import com.example.lengary_l.wanandroid.retrofit.CookiesInterceptor.Interceptor.GetCookieInterceptor;
import com.example.lengary_l.wanandroid.retrofit.CookiesInterceptor.Util.AbsCookiesUtil;
import com.example.lengary_l.wanandroid.retrofit.CookiesInterceptor.Util.CookiesUtil;

/**
 * Created by CoderLengary
 */
public class CookiesManager {
    private AddCookieInterceptor mAddCookieInterceptor;
    private GetCookieInterceptor mGetCookieInterceptor;



    private CookiesManager(@NonNull Context context, @NonNull String sharePreferenceName, @NonNull AbsCookiesUtil util, @NonNull String...urls){
        mAddCookieInterceptor=AddCookieInterceptor.getInstance(util);
        mGetCookieInterceptor=GetCookieInterceptor.getInstance(util,urls);
    }

    public AddCookieInterceptor getAddCookiesInterceptor() {
        return mAddCookieInterceptor;
    }

    public GetCookieInterceptor getGetCookieInterceptor() {
        return mGetCookieInterceptor;
    }

    public static class Builder {
        private static final String COOKIES_SP_NAME = "CookiesSharePreference";
        private Context mContext;
        private String mSharePreferenceName;
        private AbsCookiesUtil mCookiesUtil;
        private String[] mUrls;

        public Builder(@NonNull Context context) {
            mSharePreferenceName = COOKIES_SP_NAME;
            mContext = context;
            mCookiesUtil = new CookiesUtil(context, COOKIES_SP_NAME);
        }

        public Builder sharePreferenceName(String name) {
            mSharePreferenceName = name;
            return this;
        }

        public Builder cookiesUtil(AbsCookiesUtil util) {
            mCookiesUtil = util;
            return this;
        }

        public Builder urls(@NonNull String...urls) {
            mUrls = urls;
            return this;
        }

        public CookiesManager build() {
            return new CookiesManager(mContext, mSharePreferenceName, mCookiesUtil, mUrls );
        }
    }


}
