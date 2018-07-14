package com.example.lengary_l.wanandroid.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by CoderLengary
 */


public class LoginDetailData extends RealmObject {
    @Expose
    @SerializedName("collectIds")
    private RealmList<Integer> collectIds;

    public RealmList<Integer> getCollectIds() {
        return collectIds;
    }

    public void setCollectIds(RealmList<Integer> collectIds) {
        this.collectIds = collectIds;
    }

    @Expose
    @SerializedName("email")
    private String email;
    @Expose
    @SerializedName("icon")
    private String icon;
    @Expose
    @SerializedName("id")
    private int id;
    @Expose
    @SerializedName("password")
    private String password;
    @Expose
    @SerializedName("type")
    private int type;
    @Expose
    @SerializedName("username")
    @PrimaryKey
    private String username;


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

}
