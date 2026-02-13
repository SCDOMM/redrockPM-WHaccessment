package com.example.hachimi.View.activity;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.hachimi.MainActivity;
import com.example.hachimi.R;
import com.example.hachimi.repository.model.Setting2Data;
import com.example.hachimi.repository.network.ReversoNet;
import com.example.hachimi.utils.ImageHandler;
import com.example.hachimi.utils.SharedPreferenceHandler;

import java.lang.ref.WeakReference;
/**
 * 包名称： com.example.hachimi.View.activity
 * 类名称：Setting2Activity
 * 类描述：用户在设置的第二个选项进行修改资料操作。若JWT认证不通过，则清除数据，要求重新登录。需要用到相册访问权限。
 * 创建人：韦西波
 */
public class Setting2Activity extends AppCompatActivity {
    private ImageView s2IVback;
    private ImageView s2IVprofile;
    private TextView s2TVsave;
    private TextView s2TVaccount;
    private EditText s2ETVname;
    private String profileBase64;
    private static final int REQUEST_CODE_PERMISSION = 1001;
    private static final int REQUEST_CODE_SELECT = 1002;
    private Handler handler = new MyHandler(this);
    private Setting2Data data;

    private static class MyHandler extends Handler {
        private WeakReference<Setting2Activity> weakRefer;

        public MyHandler(Setting2Activity activity) {
            this.weakRefer = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Setting2Activity activity = weakRefer.get();
            if (msg.what == -1) {
                Toast.makeText(activity, "注销失败!请重新登录!", Toast.LENGTH_SHORT).show();
                SharedPreferenceHandler.clearPersonnel(activity);
                SharedPreferenceHandler.clearSecret(activity);
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            } else if (msg.what == 0) {
                Toast.makeText(activity, "修改资料失败!" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1) {
                Toast.makeText(activity, "修改资料成功!", Toast.LENGTH_SHORT).show();
                SharedPreferenceHandler.setPersonnel(activity, activity.data.getProfile(), activity.data.getName(), activity.data.getAccount());
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting2_activity);
        s2IVback = findViewById(R.id.setting2_iv_back);
        s2IVprofile = findViewById(R.id.setting2_iv_profile);
        s2TVaccount = findViewById(R.id.setting2_tv_account);
        s2TVsave = findViewById(R.id.setting2_tv_save);
        s2ETVname = findViewById(R.id.setting2_et_name);

        SharedPreferences sp = getSharedPreferences("personnel", 0);
        profileBase64 = sp.getString("profile", "");

        initEvent();
        initSave();
    }
    private void initEvent() {
        //展示现有的账号和姓名，头像
        SharedPreferences sp = getSharedPreferences("personnel", 0);
        s2IVprofile.setImageDrawable(SharedPreferenceHandler.getProfile(this));
        s2TVaccount.setText("您的账号是:" + sp.getString("account", "棍木"));
        s2ETVname.setText(sp.getString("name", "棍木"));

        s2IVback.setOnClickListener(view -> {
            finish();
        });
        s2ETVname.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 8) {
                    s2ETVname.setText(charSequence.subSequence(0, 8));
                    s2ETVname.setSelection(8);
                }
            }
        });

    }

    private void initSave() {
        s2IVprofile.setOnClickListener(view -> {
            checkPermission();
        });

        s2TVsave.setOnClickListener(view -> {
            SharedPreferences sp = getSharedPreferences("personnel", 0);
            String name = s2ETVname.getText().toString();
            data = new Setting2Data(name, sp.getString("account", ""), profileBase64);
            ReversoNet reversoNet = new ReversoNet(handler);
            reversoNet.changeHandle(data, view);
        });
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            openGallery();
        } else {
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_CODE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openGallery();
            } else {
                Log.e("相册开启错误!", "setting2:no authority!");
                Toast.makeText(this, "请开启相册权限", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, REQUEST_CODE_SELECT);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SELECT && resultCode == Activity.RESULT_OK && data != null) {
            Uri uri = data.getData();
            if (uri != null) {
                displayImage(uri);
            }
        }
    }

    private void displayImage(Uri uri) {
        ContentResolver resolver = this.getContentResolver();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
            s2IVprofile.setImageBitmap(bitmap);
            byte[] bytes = ImageHandler.fromBitmapToByte(bitmap);
            profileBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("图片加载错误!", "setting2:" + e.getMessage());
            Toast.makeText(this, "图片加载失败！", Toast.LENGTH_SHORT).show();
        }
    }
}