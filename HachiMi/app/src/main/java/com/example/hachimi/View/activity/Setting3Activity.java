package com.example.hachimi.View.activity;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.hachimi.R;
/**
 * 包名称： com.example.hachimi.View.activity
 * 类名称：Setting3Activity
 * 类描述：用户在设置的第三个选项获得APP相关的信息
 * 创建人：韦西波
 */
public class Setting3Activity extends AppCompatActivity {
    private ImageView s3IV_main;
    private TextView s3TV_version;
    private Button s3BTN_github;
    private ImageView s3IV_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting3_activity);

        s3IV_main = findViewById(R.id.setting3_iv_main);
        s3TV_version = findViewById(R.id.setting3_tv_version);
        s3BTN_github = findViewById(R.id.setting3_btn_github);
        s3IV_back = findViewById(R.id.setting3_iv_back);
        initEvent();
    }

    private void initEvent() {
        s3IV_main.setImageResource(R.drawable.main_icon);
        s3IV_back.setOnClickListener(view -> {
            finish();
        });
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            // 版本名称（如 "1.2.3"）
            String versionName = packageInfo.versionName;
            s3TV_version.setText("当前版本:" + versionName);
        } catch (Exception e) {
            s3TV_version.setText("版本获取失败!");
        }
        s3BTN_github.setOnClickListener(view -> {
            Uri webpage = Uri.parse("https://github.com/SCDOMM/redrockBE-homework");
            Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
            // 检查是否有应用能处理该 Intent
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            }
        });
    }
}