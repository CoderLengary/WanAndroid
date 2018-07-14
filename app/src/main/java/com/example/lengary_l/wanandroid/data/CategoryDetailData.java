package com.example.lengary_l.wanandroid.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by CoderLengary
 */


public class CategoryDetailData {
    public List<CategoryDetailData> getChildren() {
        return children;
    }

    public void setChildren(List<CategoryDetailData> children) {
        this.children = children;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public int getParentChapterId() {
        return parentChapterId;
    }

    public void setParentChapterId(int parentChapterId) {
        this.parentChapterId = parentChapterId;
    }

    public int getVisible() {
        return visible;
    }

    public void setVisible(int visible) {
        this.visible = visible;
    }

    @Expose
    @SerializedName("children")
    private List<CategoryDetailData> children;
    @Expose
    @SerializedName("courseId")
    private int courseId;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("order")
    private int order;
    @Expose
    @SerializedName("parentChapterId")
    private int parentChapterId;
    @Expose
    @SerializedName("visible")
    private int visible;
}
