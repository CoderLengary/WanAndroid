package com.example.lengary_l.wanandroid.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by CoderLengary
 */


public class BannerDetailData {
    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public int getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(int isVisible) {
        this.isVisible = isVisible;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Expose
    @SerializedName("desc")
    private String desc;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("imagePath")
    private String imagePath;
    @Expose
    @SerializedName("isVisible")
    private int isVisible;
    @Expose
    @SerializedName("order")
    private int order;
    @Expose
    @SerializedName("title")
    private String title;
    @Expose
    @SerializedName("type")
    private int type;
    @Expose
    @SerializedName("url")
    private String url;
}
