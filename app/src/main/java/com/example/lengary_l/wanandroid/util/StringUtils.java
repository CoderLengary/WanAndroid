package com.example.lengary_l.wanandroid.util;

import android.text.TextUtils;

public class StringUtils {
    public static boolean isInvalid(String text){
        boolean isInvalid = false;
        if (TextUtils.isEmpty(text)||text.contains(" ")){
            isInvalid = true;
        }
        return isInvalid;
    }
}
