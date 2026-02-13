package com.example.hachimi.repository.network;

import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.example.hachimi.MainActivity;
import com.example.hachimi.repository.data.ResponseData;
import com.example.hachimi.repository.model.LogonResponseData;
import com.example.hachimi.repository.model.RegisterData;
import com.example.hachimi.repository.model.Setting2Data;
import com.example.hachimi.utils.JsonHandle;
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
 * description ： TODO:
 * author : 韦西波
 * email : a977628@outlook.com
 * date :
 * 𝐷𝐴𝑇𝐸{HOUR}:05
 */
public class ReversoNet {

    public Handler handler;

    public ReversoNet(Handler handler) {
        this.handler = handler;
    }

    public void loginHandle(String account, String password, View rootView) {
        String json = "{\"account\":\"" + account + "\",\"password\":\"" + password + "\"}";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.get("application/json"), json);
        Request request = new Request.Builder().post(body).url("http://" + MainActivity.basicUrl + ":" + MainActivity.basicPort + "/reverso/login").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                rootView.post(() -> {
                    Message msg = new Message();
                    msg.obj = e.getMessage();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    Log.e("网络请求错误!", "LogSign:" + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                rootView.post(() -> {
                    //工具类解析JSON
                    JsonHandle jsonHandle = new JsonHandle();
                    ResponseData<LogonResponseData> decodedData = jsonHandle.decodeJSON(responseData, LogonResponseData.class);

                    Message msg = new Message();
                    if (decodedData.getStatus().equals("200")) {
                        msg.what = 1;
                        msg.obj = decodedData.getData();
                    } else {
                        msg.what = 0;
                        msg.obj = decodedData.getInfo();
                    }
                    handler.sendMessage(msg);
                });
            }
        });
    }

    public void signHandle(RegisterData data, View rootView) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(MediaType.get("application/json"), json);
        Request request = new Request.Builder().post(body).url("http://" + MainActivity.basicUrl + ":" + MainActivity.basicPort + "/reverso/register").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                rootView.post(() -> {
                    Message msg = new Message();
                    msg.obj = e.getMessage();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    Log.e("网络请求错误!", "RegisterSign:" + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                rootView.post(() -> {
                    //工具类解析JSON
                    JsonHandle jsonHandle = new JsonHandle();
                    ResponseData<String> decodedData = jsonHandle.decodeJSON(responseData, String.class);
                    Message msg = new Message();
                    if (decodedData.getStatus().equals("200")) {
                        msg.what = 1;
                    } else {
                        msg.what = 0;
                    }
                    msg.obj = decodedData.getInfo();
                    handler.sendMessage(msg);
                });
            }
        });
    }

    public void deregisterHandle(String account, String password, View rootView) {
        String json = "{\"account\":\"" + account + "\",\"password\":\"" + password + "\"}";
        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(MediaType.get("application/json"), json);


        String[] token = SharedPreferenceHandler.getToken(rootView.getContext());
        if (token == null) {
            Toast.makeText(rootView.getContext(), "the token is null!", Toast.LENGTH_SHORT).show();
            return;
        }

        Request request = new Request.Builder().post(body).url("http://" + MainActivity.basicUrl + ":" + MainActivity.basicPort + "/reverso/deregister")
                .addHeader("Authorization", "Bearer " + token[0])
                .addHeader("Cookie", "refresh_token=" + token[1]).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                Log.e("网络请求错误!", "deregister:" + e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                rootView.post(() -> {
                    //工具类解析JSON
                    JsonHandle jsonHandle = new JsonHandle();
                    ResponseData<String> decodedData = jsonHandle.decodeJSON(responseData, String.class);

                    Message msg = new Message();
                    if (decodedData.getStatus().equals("200")) {
                        msg.what = 1;
                    } else if (decodedData.getStatus().equals("401")) {
                        msg.what = -1;
                    } else {
                        msg.what = 0;
                    }
                    msg.obj = decodedData.getInfo();
                    handler.sendMessage(msg);
                });
            }
        });
    }

    public void changeHandle(Setting2Data data, View rootView) {
        Gson gson = new Gson();
        String json = gson.toJson(data);
        OkHttpClient client = new OkHttpClient();

        String[] token = SharedPreferenceHandler.getToken(rootView.getContext());
        if (token == null) {
            Toast.makeText(rootView.getContext(), "the token is null!", Toast.LENGTH_SHORT).show();
            return;
        }

        RequestBody body = RequestBody.create(MediaType.get("application/json"), json);
        Request request = new Request.Builder().
                addHeader("Authorization", "Bearer " + token[0])
                .addHeader("Cookie", "refresh_token=" + token[1])
                .post(body).url("http://" + MainActivity.basicUrl + ":" + MainActivity.basicPort + "/reverso/change").build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                rootView.post(() -> {
                    Message msg = new Message();
                    msg.obj = e.getMessage();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    Log.e("网络请求错误!", "change:" + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                rootView.post(() -> {
                    //工具类解析JSON
                    JsonHandle jsonHandle = new JsonHandle();
                    ResponseData<String> decodedData = jsonHandle.decodeJSON(responseData, String.class);
                    Message msg = new Message();
                    if (decodedData.getStatus().equals("200")) {
                        msg.what = 1;
                    } else if (decodedData.getStatus().equals("401")) {
                        msg.what = -1;
                    } else {
                        msg.what = 0;
                    }
                    msg.obj = decodedData.getInfo();
                    handler.sendMessage(msg);
                });
            }
        });
    }

    public void jwtTestHandle(View rootView) {
        OkHttpClient client = new OkHttpClient();

        String[] token = SharedPreferenceHandler.getToken(rootView.getContext());
        if (token == null) {
            Toast.makeText(rootView.getContext(), "the token is null!", Toast.LENGTH_SHORT).show();
            return;
        }
        Request request = new Request.Builder().url("http://" + MainActivity.basicUrl + ":" + MainActivity.basicPort + "/reverso/test")
                .addHeader("Authorization", "Bearer " + token[0])
                .addHeader("Cookie", "refresh_token=" + token[1]).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                e.printStackTrace();
                rootView.post(() -> {
                    Message msg = new Message();
                    msg.obj = e.getMessage();
                    msg.what = 0;
                    handler.sendMessage(msg);
                    Log.e("网络请求错误!", "change:" + e.getMessage());
                });
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String responseData = response.body().string();
                rootView.post(() -> {
                    //工具类解析JSON
                    JsonHandle jsonHandle = new JsonHandle();
                    ResponseData<String> decodedData = jsonHandle.decodeJSON(responseData, String.class);
                    Message msg = new Message();
                    if (decodedData.getStatus().equals("200")) {
                        msg.what = 1;
                    } else {
                        msg.what = 0;
                    }
                    msg.obj = decodedData.getInfo();
                    handler.sendMessage(msg);
                });
            }
        });
    }


}
