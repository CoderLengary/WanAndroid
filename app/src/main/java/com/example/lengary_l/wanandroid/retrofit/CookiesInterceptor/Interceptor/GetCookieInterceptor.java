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

package com.example.lengary_l.wanandroid.retrofit.CookiesInterceptor.Interceptor;

import android.support.annotation.NonNull;

import com.example.lengary_l.wanandroid.retrofit.CookiesInterceptor.Util.AbsCookiesUtil;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by CoderLengary
 */
public class GetCookieInterceptor implements Interceptor {

    private static GetCookieInterceptor INSTANCE;

    /**
     * Set-Cookie: cookies
     *
     * Example:
     * Set-Cookie: CHNCOOKUID=deleted; expires=Thu, 01-Jan-1970 00:00:01 GMT; path=/my
     */
    private static final String SET_COOKIES = "Set-Cookie";

    //We will intercept these urls whose response contains Cookies
    private List<String> mCookiesUrls;
    private AbsCookiesUtil mUtil;

    private GetCookieInterceptor(@NonNull AbsCookiesUtil util, @NonNull String...urls) {
        mCookiesUrls=Arrays.asList(urls);
        mUtil = util;
    }

    public static GetCookieInterceptor getInstance(@NonNull AbsCookiesUtil util, @NonNull String...urls) {
        if (INSTANCE == null) {
            INSTANCE = new GetCookieInterceptor(util, urls);
        }
        return INSTANCE;
    }

    // Get the request -> Get the response -> IsCookiesUrl? -> GetCookies -> EncodeCookies -> SaveCookies
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        Response response = chain.proceed(request);
        String requestUrl = request.url().toString();
        String host = request.url().host();

        if (isCookiesUrl(requestUrl) && !response.headers(SET_COOKIES).isEmpty()) {

            List<String> cookies = response.headers(SET_COOKIES);

            mUtil.encodeAndSaveCookies(cookies, host);

        }

        return response;
    }

    private boolean isCookiesUrl(String requestUrl) {
        for(String cookiesUrl: mCookiesUrls) {
            if (requestUrl.contains(cookiesUrl)) {
                return true;
            }
        }
        return false;
    }


}
