package com.example.hachimi.repository.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * 包名称： com.example.hachimi.repository.model
 * 类名称：HomeFragData
 * 类描述：HomeFragData的数据类，包括title档案标题，desc档案描述，image档案图片等
 * 创建人：韦西波
 */
public class HomeFragData implements Serializable {
    @SerializedName("title")
    private String title;
    @SerializedName("desc")
    private String desc;
    @SerializedName("image")
    private String image;

    public HomeFragData(String title, String describe, String image) {
        this.title = title;
        this.desc = describe;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public String getDescribe() {
        return desc;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}