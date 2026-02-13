package com.example.hachimi.repository.data;

import com.google.gson.annotations.SerializedName;

/**
 * 包名称： com.example.hachimi.repository.data
 * 类名称：ResponseData
 * 类描述：通用的响应类，包括Status状态，Info信息和一个T泛型类型参数，用于储存单个T类型的数据
 * 创建人：韦西波
 */
public class ResponseData<T> {
    @SerializedName("status")
    private String status;
    @SerializedName("info")
    private String info;
    @SerializedName("data")
    private T data;

    public T getData() {
        return data;
    }

    public String getInfo() {
        return info;
    }

    public String getStatus() {
        return status;
    }
}
