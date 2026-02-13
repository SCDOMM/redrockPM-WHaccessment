package com.example.hachimi.utils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * 包名称： com.example.hachimi.utils
 * 类名称：ChatFragViewModel
 * 类描述：利用viewModel将搜索数据从ChatFragment传递到Chat1Fragment
 * 创建人：韦西波
 */
public class ChatFragViewModel extends ViewModel {
    private MutableLiveData<String> stringData = new MutableLiveData<>();

    public ChatFragViewModel() {
        stringData.setValue("");
    }

    public void setString(String str) {
        stringData.setValue(str);
    }

    public MutableLiveData<String> getData() {
        return stringData;
    }

}
