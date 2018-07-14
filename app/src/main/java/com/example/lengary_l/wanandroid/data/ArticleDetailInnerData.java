package com.example.lengary_l.wanandroid.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmObject;

/**
 * Created by CoderLengary
 */


public class ArticleDetailInnerData extends RealmObject {

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("url")
    private String url;
}
