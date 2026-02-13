package com.example.hachimi.View.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.hachimi.R;
/**
 * 包名称： com.example.hachimi.View.fragment
 * 类名称：Chat2Fragment
 * 类描述：TODO 该fragment已弃用
 * 创建人：韦西波
 */
public class Chat2Fragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.chat2_fragment, container, false);
    }
}