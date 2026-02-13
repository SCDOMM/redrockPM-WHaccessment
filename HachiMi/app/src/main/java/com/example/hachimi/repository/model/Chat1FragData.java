package com.example.hachimi.repository.model;

import com.google.gson.annotations.SerializedName;

/**
 * 包名称： com.example.hachimi.repository.model
 * 类名称：Chat1FragData
 * 类描述：Chat1Fragment的数据类，包括authorName作者姓名，desc动态描述，title动态标题，cover动态封面，profile作者头像
 * account作者账号，time发布时间等，配备有相应的getter，setter方法
 * 创建人：韦西波
 */
public class Chat1FragData {
    @SerializedName("author_name")
    private String authorName;
    @SerializedName("desc")
    private String describe;
    @SerializedName("title")
    private String title;
    @SerializedName("cover_image")
    private String cover;
    @SerializedName("profile_image")
    private String profile;
    @SerializedName("time")
    private String time;
    @SerializedName("account")
    private String account;

    public Chat1FragData(String authorName, String describe, String title, String cover, String profile, String time, String account) {
        this.time = time;
        this.describe = describe;
        this.authorName = authorName;
        this.cover = cover;
        this.title = title;
        this.profile = profile;
        this.account = account;
    }

    public String getAccount() {
        return account;
    }

    public String getAuthorName() {
        return authorName;
    }

    public String getTitle() {
        return title;
    }

    public String getTime() {
        return time;
    }

    public String getDescribe() {
        return describe;
    }

    public String getProfile() {
        return profile;
    }

    public String getCover() {
        return cover;
    }
}
