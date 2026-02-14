package com.example.hachimi.repository.model;

import com.google.gson.annotations.SerializedName;

/**
 * 包名称： com.example.hachimi.repository.model
 * 类名称：Chat1UploadData
 * 类描述：Chat1Fragment的数据传输类，包括authorName作者姓名，desc动态描述，title动态标题，cover动态封面等
 * 创建人：韦西波
 */
public class Chat1UploadData {
    @SerializedName("author_account")
    private String authorAccount;
    @SerializedName("title")
    private String title;
    @SerializedName("desc")
    private String desc;
    @SerializedName("cover_image")
    private String coverImage;

    public Chat1UploadData(String authorAccount, String title, String desc, String coverImage) {
        this.authorAccount = authorAccount;
        this.title = title;
        this.desc = desc;
        this.coverImage = coverImage;
    }

    public String getDesc() {
        return desc;
    }

    public String getAuthorAccount() {
        return authorAccount;
    }

    public String getCoverImage() {
        return coverImage;
    }

    public String getTitle() {
        return title;
    }
}
