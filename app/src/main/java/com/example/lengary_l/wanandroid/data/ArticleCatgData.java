package com.example.lengary_l.wanandroid.data;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ArticleCatgData {

    @SerializedName("data")
    private List<ArticleCatgDetailData> data;

    private int errorCode;
    private String errorMsg;

}
