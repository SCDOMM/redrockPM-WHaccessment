package com.example.hachimi.repository.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * 包名称： com.example.hachimi.repository.data
 * 类名称：ResponseDataArray
 * 类描述：通用的响应类，包括Status状态，Info信息和一个泛型集合(储存多条T类型的数据)
 * 创建人：韦西波
 */
public class ResponseDataArray<T> {
    @SerializedName("status")
    private String status;
    @SerializedName("info")
    private String info;
    @SerializedName("data")
    private ArrayList<T> data;

    public ArrayList<T> getData() {
        return data;
    }

    public String getInfo() {
        return info;
    }

    public String getStatus() {
        return status;
    }
}
