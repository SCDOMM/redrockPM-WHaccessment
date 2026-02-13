package com.example.hachimi.utils;

import android.util.Log;

import com.example.hachimi.repository.data.ResponseData;
import com.example.hachimi.repository.data.ResponseDataArray;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;

/**
 * 包名称： com.example.hachimi.utils
 * 类名称：JsonHandle
 * 类描述：JSON转换工具类，用于将后端传来的数据解析到通用数据类。使用了三方库Gson
 * 创建人：韦西波
 */
public class JsonHandle {
    private Gson gson = new Gson();

    public String StreamToString(InputStream input) {
        StringBuilder builder = new StringBuilder();
        String oneline;
        BufferedReader reader = new BufferedReader(new InputStreamReader(input));
        try {
            while ((oneline = reader.readLine()) != null) {
                builder.append(oneline).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                input.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return builder.toString();
    }

    //反序列化单个JSON
    public <T> ResponseData<T> decodeJSON(String jsonData, Class<T> tClass) {
        if (jsonData == null) {
            Log.e("解析JSON出错!", "decodeJSON:the JSON data is empty!");
            return null;
        }
        try {
            Type type = TypeToken.getParameterized(ResponseData.class, tClass).getType();
            ResponseData<T> data = gson.fromJson(jsonData, type);
            return data;
        } catch (Exception e) {
            Log.e("JSON解析出错!", "decodeJSON:" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    //反序列化JSON数组
    public <T> ResponseDataArray<T> decodeJSONArray(String jsonData, Class<T> tClass) {
        if (jsonData == null) {
            Log.e("解析JSON出错!", "decodeJSONArray:the JSON data is empty!");
            return new ResponseDataArray<>();
        }
        Type type = null;
        try {
            type = TypeToken.getParameterized(ResponseDataArray.class, tClass).getType();
            if (type != null) {
                return gson.fromJson(jsonData, type);
            }
        } catch (Exception e) {
            Log.e("解析JSON出错!", "decodeJSONArray:" + e.getMessage());
            e.printStackTrace();
        }
        return new ResponseDataArray<>();
    }
}
