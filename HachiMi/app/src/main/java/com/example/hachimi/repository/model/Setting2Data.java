package com.example.hachimi.repository.model;

import com.google.gson.annotations.SerializedName;

/**
 * 包名称： com.example.hachimi.repository.model
 * 类名称：Setting2Data
 * 类描述：Setting2Data的数据类，用于往前端传递需要修改的数据，如用户名，头像。
 * TODO 能修改的数据还是太少了
 * 创建人：韦西波
 * 创建时间：03小时
 */
public class Setting2Data {
    @SerializedName("user_name")
    private String name;
    @SerializedName("account")
    private String account;
    @SerializedName("profile_image")
    private String profile;

    public Setting2Data(String name, String account, String profile) {
        this.account = account;
        this.name = name;
        this.profile = profile;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getProfile() {
        return profile;
    }


}
