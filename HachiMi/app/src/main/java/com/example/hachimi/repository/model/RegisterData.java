package com.example.hachimi.repository.model;

import com.google.gson.annotations.SerializedName;


/**
 * 包名称： com.example.hachimi.repository.model
 * 类名称：RegisterData
 * 类描述：RegisterData的数据类，用于往前端传递注册必要的数据，如姓名，账户，密码。用户头像需要稍后自行设置。
 * 创建人：韦西波
 * 创建时间：22小时
 */
public class RegisterData {
    @SerializedName("user_name")
    private String name;
    @SerializedName("account")
    private String account;
    @SerializedName("password")
    private String password;

    public RegisterData(String name, String account, String password) {
        this.account = account;
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getAccount() {
        return account;
    }

    public String getPassword() {
        return password;
    }
}
