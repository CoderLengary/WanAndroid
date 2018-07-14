package com.example.lengary_l.wanandroid.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by CoderLengary
 */


public class HotKeyDetailData{
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("link")
    private String link;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("order")
    private int order;
    @Expose
    @SerializedName("visible")
    private int visible;
}
