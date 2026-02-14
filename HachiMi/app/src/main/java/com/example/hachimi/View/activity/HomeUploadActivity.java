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
import com.example.hachimi.repository.model.HomeFragData;
import com.example.hachimi.repository.network.AdminNet;
import com.example.hachimi.utils.ImageHandler;
import com.example.hachimi.utils.SharedPreferenceHandler;

import java.lang.ref.WeakReference;
/**
 * 包名称： com.example.hachimi.View.activity
 * 类名称：HomeUploadActivity
 * 类描述：管理员可以在此上传新的档案，需要用到相册权限
 * 创建人：韦西波
 */
public class HomeUploadActivity extends AppCompatActivity {
    private ImageView uIV_default;
    private EditText uETV_title;
    private EditText uETV_desc;
    private TextView uTV_confirm;
    private ImageView uIV_back;
    private static final int REQUEST_CODE_PERMISSION = 1001;
    private static final int REQUEST_CODE_SELECT = 1002;
    private String coverBase64;
    private Handler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<HomeUploadActivity> weakRefer;

        public MyHandler(HomeUploadActivity activity) {
            this.weakRefer = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            HomeUploadActivity activity = weakRefer.get();
            if(activity==null){
                return;
            }
            if (msg.what == -1) {
                Toast.makeText(activity, "上传失败!请重新登录!", Toast.LENGTH_SHORT).show();
                SharedPreferenceHandler.clearPersonnel(activity);
                SharedPreferenceHandler.clearSecret(activity);
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            } else if (msg.what == 1) {
                Toast.makeText(activity, "上传成功!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            } else {
                Toast.makeText(activity, "上传失败!请检查网络后重试!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_upload_activity);
        uIV_default = findViewById(R.id.home_upload_iv_defaultImage);
        uETV_title = findViewById(R.id.home_upload_etv_title);
        uETV_desc = findViewById(R.id.home_upload_etv_describe);
        uTV_confirm = findViewById(R.id.home_upload_tv_confirm);
        uIV_back = findViewById(R.id.home_upload_iv_back);
        initEvent();
        initUpload();
    }
    //进行字数限制，执行退出逻辑等
    private void initEvent() {
        uIV_back.setOnClickListener(view -> {
            finish();
        });
        uETV_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 8) {
                    uETV_title.setText(charSequence.subSequence(0, 8));
                    uETV_title.setSelection(8);
                }
            }
        });
        uETV_desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 200) {
                    uETV_desc.setText(charSequence.subSequence(0, 200));
                    uETV_desc.setSelection(200);
                }
            }
        });
    }
    //点击上传按钮后，，
    private void initUpload() {
        uIV_default.setOnClickListener(view -> {
            checkPermission();
        });
        uTV_confirm.setOnClickListener(view -> {
            String title = uETV_title.getText().toString();
            String desc = uETV_desc.getText().toString();
            if (title.length() < 2 || desc.length() < 10) {
                Toast.makeText(this, "错误:标题/描述长度过短!", Toast.LENGTH_SHORT).show();
                return;
            }
            HomeFragData data = new HomeFragData(title, desc, coverBase64);
            AdminNet adminNet = new AdminNet(handler);
            adminNet.uploadHandler(view, data);
        });
    }
    //以下都是相册请求相关的代码
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
                Log.e("相册开启错误!", "no authority!");
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
            uIV_default.setImageBitmap(bitmap);
            byte[] bytes = ImageHandler.fromBitmapToByte(bitmap);
            coverBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("图片加载错误!", e.getMessage());
            Toast.makeText(this, "图片加载失败！", Toast.LENGTH_SHORT).show();
        }
    }


}