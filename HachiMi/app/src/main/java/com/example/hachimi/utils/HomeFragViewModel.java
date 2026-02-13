package com.example.hachimi.utils;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.hachimi.repository.model.HomeFragData;

/**
 * 包名称： com.example.hachimi.utils
 * 类名称：HomeFragViewModel
 * 类描述：利用viewModel将item数据从HomeFragment传递到HomeDetailActivity
 * 创建人：韦西波
 */
public class HomeFragViewModel extends ViewModel {
    private MutableLiveData<HomeFragData> HomeFragLiveData = new MutableLiveData<>();

    public void setHomeFragLiveData(HomeFragData data) {
        HomeFragLiveData.setValue(data);
    }

    public MutableLiveData<HomeFragData> getHomeFragLiveData() {
        return HomeFragLiveData;
    }

}
