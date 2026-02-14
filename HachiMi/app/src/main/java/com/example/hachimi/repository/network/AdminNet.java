package com.example.hachimi.repository.network;

import static com.example.hachimi.MainActivity.basicPort;
import static com.example.hachimi.MainActivity.basicUrl;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.hachimi.repository.data.ResponseData;
import com.example.hachimi.repository.model.Chat1FragData;
import com.example.hachimi.repository.model.HomeFragData;
import com.example.hachimi.utils.JsonHandle;
import com.example.hachimi.utils.SecurityHandle;
import com.example.hachimi.utils.SharedPreferenceHandler;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * 包名称： com.example.hachimi.repository.network
 * 类名称：AdminNet
 * 类描述：管理员的网络操作类，包含有上传档案，删除档案，删除动态等功能。传入了handler用于线程通信，使用OKhttp三方库进行网络请求。
 * 创建人：韦西波
 * 创建时间：47小时
 */
public class AdminNet {
    public Handler handler;

    public AdminNet(Handler handler) {
        this.handler = handler;
    }

    //删除档案的网络请求方法
    public void deleteHomeHandler(View rootView, HomeFragData data) {
        if (handler == null) {
            Log.e("网络连接错误!", "AdminNet:Handler is empty!");
            return;
        }
        OkHttpClient client = new OkHttpClient();
        //序列化json
        Gson gson = new Gson();
        String json = gson.toJson(data);
        RequestBody body = RequestBody.create(MediaType.get("application/json"), json);
        String[] token = SharedPreferenceHandler.getToken(rootView.getContext());
        if (token == null) {
            Log.e("获取Token错误!", "token is empty");
            return;
        }

        Request request = new Request.Builder().url("http://" + basicUrl + ":" + basicPort + "/admin/deleteHome")
                .addHeader("Authorization", "Bearer " + token[0])
                .addHeader("Cookie", "refresh_token=" + token[1])
                .post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("网络连接错误!", "admin:" + e.getMessage());
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

    //上传档案的网络请求方法
    public void uploadHandler(View rootView, HomeFragData data) {
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
            Log.e("获取Token错误!", "token is empty");
            return;
        }

        Request request = new Request.Builder().url("http://" + basicUrl + ":" + basicPort + "/admin/uploadHome")
                .addHeader("Authorization", "Bearer " + token[0])
                .addHeader("Cookie", "refresh_token=" + token[1])
                .post(body).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("网络连接错误!", "admin:" + e.getMessage());
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

    //删除动态的网络请求方法
    public void deleteChatHandler(View rootView, Chat1FragData data) {
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
            Log.e("获取Token错误!", "token is empty");
            return;
        }

        Request request = new Request.Builder().url("http://" + basicUrl + ":" + basicPort + "/admin/deleteChat")
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
