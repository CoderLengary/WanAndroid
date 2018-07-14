package com.example.lengary_l.wanandroid.util;

import android.text.TextUtils;

/**
 * Created by CoderLengary
 */


public class StringUtil {
    public static boolean isInvalid(String text){
        boolean isInvalid = false;
        if (TextUtils.isEmpty(text)||text.contains(" ")){
            isInvalid = true;
        }
        return isInvalid;
    }

    public static String replaceInvalidChar(String text) {
        return text.replace("<em class='highlight'>", "")
                .replace("</em>", "")
                .replace("&mdash;", "-")
                .replace("&ndash;", "-")
                .replace("&ldquo;", "'")
                .replace("&rdquo;", "'")
                .replace("&amp;","&");
    }
}
