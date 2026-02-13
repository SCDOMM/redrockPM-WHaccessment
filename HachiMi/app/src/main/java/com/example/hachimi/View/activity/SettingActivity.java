package com.example.hachimi.View.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hachimi.R;
import com.example.hachimi.View.adapter.SettingAdapter;
import com.example.hachimi.repository.model.SettingData;

import java.util.ArrayList;
/**
 * 包名称： com.example.hachimi.View.activity
 * 类名称：SettingActivity
 * 类描述：设置本身，通过RV的item的不同id打开不同的设置选项。
 * 创建人：韦西波
 */
public class SettingActivity extends AppCompatActivity {
    private ImageView sIVback;
    private RecyclerView sRV;
    private ArrayList<SettingData> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting_activity);

        sIVback = findViewById(R.id.setting_iv_back);
        sRV = findViewById(R.id.setting_tv_rv);
        initEvent();
    }

    private void initEvent() {
        dataList = new ArrayList<>();
        dataList.add(new SettingData("注销账号", 1));
        dataList.add(new SettingData("修改资料", 2));
        dataList.add(new SettingData("关于应用", 3));
        SettingAdapter adapter = new SettingAdapter(dataList);
        adapter.setRegisterListener(data -> {
            Intent intent = null;

            if (data.getSettingID() == 1) {
                intent = new Intent(SettingActivity.this, Setting1Activity.class);
            } else if (data.getSettingID() == 2) {
                intent = new Intent(SettingActivity.this, Setting2Activity.class);
            } else if (data.getSettingID() == 3) {
                intent = new Intent(SettingActivity.this, Setting3Activity.class);
            }
            startActivity(intent);
        });
        sIVback.setOnClickListener(view -> {
            finish();
        });


        sRV.setLayoutManager(new LinearLayoutManager(this));
        sRV.setAdapter(adapter);

        DividerItemDecoration divider = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        sRV.addItemDecoration(divider);
    }
}