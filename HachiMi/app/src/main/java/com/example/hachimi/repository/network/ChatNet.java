package com.example.hachimi.repository.network;

import static com.example.hachimi.MainActivity.basicPort;
import static com.example.hachimi.MainActivity.basicUrl;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hachimi.repository.data.ResponseData;
import com.example.hachimi.repository.data.ResponseDataArray;
import com.example.hachimi.repository.model.Chat1FragData;
import com.example.hachimi.repository.model.Chat1UploadData;
import com.example.hachimi.utils.JsonHandle;
import com.example.hachimi.utils.SecurityHandle;
import com.example.hachimi.utils.SharedPreferenceHandler;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 包名称： com.example.hachimi.repository.network
 * 类名称：ChatNet
 * 类描述：动态相关的网络操作类，包含有请求动态，搜索动态，上传动态三大方法。传入了handler用于线程通信，使用OKhttp三方库进行网络请求。
 * 创建人：韦西波
 */
public class ChatNet {
    public Handler handler;

    public ChatNet(Handler handler) {
        this.handler = handler;
    }

    //请求10个动态
    public void requestData(View rootView) {
        if (handler == null) {
            Log.e("网络连接错误!", "Handler is empty!");
            return;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://" + basicUrl + ":" + basicPort + "/chat/mainpage").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("网络连接错误!", "chatNet:" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                rootView.post(() -> {
                    //工具类解析JSON
                    JsonHandle jsonHandle = new JsonHandle();
                    ResponseDataArray<Chat1FragData> decodedData = jsonHandle.decodeJSONArray(responseData, Chat1FragData.class);
                    ArrayList<Chat1FragData> list = decodedData.getData();
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

    public void searchData(View rootView, String keyWords) {
        if (handler == null) {
            Log.e("网络连接错误!", "Handler is empty!");
            return;
        }
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url("http://" + basicUrl + ":" + basicPort + "/chat/search/" + keyWords).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("网络连接错误!", "ChatNet:" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                rootView.post(() -> {
                    //工具类解析JSON
                    JsonHandle jsonHandle = new JsonHandle();
                    ResponseDataArray<Chat1FragData> decodedData = jsonHandle.decodeJSONArray(responseData, Chat1FragData.class);
                    ArrayList<Chat1FragData> list = decodedData.getData();

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
                        msg.what = 3;
                        msg.obj = list;
                        handler.sendMessage(msg);
                    }
                });
            }
        });
    }

    public void submitData(View rootView, Chat1UploadData data) {
        if (handler == null) {
            Log.e("网络连接错误!", "Handler is empty!");
            return;
        }
        OkHttpClient client = new OkHttpClient();
        Gson gson = new Gson();
        String json = gson.toJson(data);
        RequestBody body = RequestBody.create(MediaType.get("application/json"), json);
        String[] token = SharedPreferenceHandler.getToken(rootView.getContext());
        if (token == null) {
            Toast.makeText(rootView.getContext(), "the token is null!", Toast.LENGTH_SHORT).show();
            return;
        }

        Request request = new Request.Builder().url("http://" + basicUrl + ":" + basicPort + "/chat/upload")
                .addHeader("Authorization", "Bearer " + token[0])
                .addHeader("Cookie", "refresh_token=" + token[1])
                .post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("网络连接错误!", "ChatNet:" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();

                rootView.post(() -> {
                    //工具类解析JSON
                    JsonHandle jsonHandle = new JsonHandle();

                    ResponseData<String> decodedData = jsonHandle.decodeJSON(responseData, String.class);
                    Message msg = new Message();
                    if (decodedData == null) {
                        msg.what = 0;
                        Log.e("错误!", "解析json出错" + responseData);
                        msg.obj = "json解析出错!";
                        handler.sendMessage(msg);
                        return;
                    }

                    if (decodedData.getStatus().equals("200")) {
                        msg.what = 1;
                    } else if (decodedData.getStatus().equals("401")) {
                        msg.what = -1;
                    } else {
                        msg.what = 0;
                    }

                    String newAccessToken = response.header("New-Access-Token");
                    if (newAccessToken!=null||!newAccessToken.isEmpty()){
                        try {
                            SecurityHandle.putNewAccessTokenToESP(rootView.getContext(),newAccessToken);
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                    msg.obj = decodedData.getInfo();
                    handler.sendMessage(msg);
                });
            }
        });
    }
}