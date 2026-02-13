package com.example.hachimi.repository.network;

import static com.example.hachimi.MainActivity.basicPort;
import static com.example.hachimi.MainActivity.basicUrl;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.hachimi.repository.data.ResponseDataArray;
import com.example.hachimi.repository.model.HomeFragData;
import com.example.hachimi.utils.JsonHandle;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 包名称： com.example.hachimi.repository.network
 * 类名称：HomeNet
 * 类描述：档案相关的网络操作类，包含有请求档案和搜索档案两大方法。传入了handler用于线程通信，使用OKhttp三方库进行网络请求。
 * 创建人：韦西波
 */
public class HomeNet {
    public Handler handler;

    public HomeNet(Handler handler) {
        this.handler = handler;
    }

    public void requestData(View rootView) {
        if (handler == null) {
            Log.e("网络连接错误!", "The Handler is Empty!");
            return;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://" + basicUrl + ":" + basicPort + "/home/homepage").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("网络连接错误!", "HomeNet:" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                rootView.post(() -> {
                    //工具类解析JSON
                    JsonHandle jsonHandle = new JsonHandle();
                    ResponseDataArray<HomeFragData> decodedData = jsonHandle.decodeJSONArray(responseData, HomeFragData.class);
                    ArrayList<HomeFragData> list = decodedData.getData();
                    Message msg = new Message();
                    if (decodedData.equals(new ResponseDataArray<>())) {
                        msg.what = 0;
                        Log.e("错误!", "解析json出错" + responseData);
                        msg.obj = "json解析出错!";
                        handler.sendMessage(msg);
                        return;
                    }
                    if (!decodedData.getStatus().equals("200")) {
                        msg.what = 0;
                        msg.obj = decodedData.getInfo();
                        handler.sendMessage(msg);
                    } else {
                        msg.what = 1;
                        msg.obj = list;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
    }

    public void searchData(View rootView, String keyWords) {
        if (handler == null) {
            Log.e("网络连接错误!", "The Handler is Empty!");
            return;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://" + basicUrl + ":" + basicPort + "/home/search/" + keyWords).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("网络连接错误!", "HomeNet:" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                rootView.post(() -> {
                    //工具类解析JSON
                    JsonHandle jsonHandle = new JsonHandle();
                    ResponseDataArray<HomeFragData> decodedData = jsonHandle.decodeJSONArray(responseData, HomeFragData.class);
                    ArrayList<HomeFragData> list = decodedData.getData();

                    Message msg = new Message();
                    if (decodedData.equals(new ResponseDataArray<>())) {
                        msg.what = 0;
                        Log.e("错误!", "解析json出错" + responseData);
                        msg.obj = "json解析出错!";
                        handler.sendMessage(msg);
                        return;
                    }
                    if (!decodedData.getStatus().equals("200")) {
                        msg.what = 0;
                        msg.obj = decodedData.getInfo();
                        handler.sendMessage(msg);
                    } else {
                        msg.what = 2;
                        msg.obj = list;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
    }
}
