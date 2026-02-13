package com.example.hachimi.repository.model;

import com.google.gson.annotations.SerializedName;

/**
 * 包名称： com.example.hachimi.repository.model
 * 类名称：LogonResponseData
 * 类描述：LogonResponseData的数据类，用于接收后端传来的响应数据，包含有用户名，头像，Token过期时间，用户类别，和双Token
 * TODO expirationTime暂时没有卵用
 * 创建人：韦西波
 * 创建时间：22小时
 */
public class LogonResponseData {
    @SerializedName("user_name")
    private String userName;
    @SerializedName("profile_image")
    private String userProfile;
    @SerializedName("expiration_time")
    private String expirationTime;
    @SerializedName("user_role")
    private int userRole;
    @SerializedName("access_token")
    private String accessToken;
    @SerializedName("refresh_token")
    private String refreshToken;

    public String getAccessToken() {
        return accessToken;
    }

    public String getExpirationTime() {
        return expirationTime;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public String getUserName() {
        return userName;
    }

    public String getUserProfile() {
        return userProfile;
    }

    public int getUserRole() {
        return userRole;
    }
}
