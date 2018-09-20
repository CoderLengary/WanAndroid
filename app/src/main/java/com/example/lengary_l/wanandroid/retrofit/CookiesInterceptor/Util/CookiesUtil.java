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
import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

/**
 * Created by CoderLengary
 */
public class CookiesUtil extends AbsCookiesUtil {


    public CookiesUtil(@NonNull Context context, String sharePreferenceName) {
        super(context, sharePreferenceName);
    }

    @Override
    public String encodeCookies(@NonNull List<String> cookiesList, @NonNull String host) {
        StringBuilder sb = new StringBuilder();
        HashSet<String> set = new HashSet<>();
        for (String cookie: cookiesList) {
            String[] array = cookie.split(";");
            for (String item: array) {
                set.add(item);
            }
        }


        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next()).append(";");
        }

        int lastIndex = sb.lastIndexOf(";");
        if (sb.length() -1 == lastIndex) {
            sb.deleteCharAt(lastIndex);
        }
        return sb.toString();
    }

}
