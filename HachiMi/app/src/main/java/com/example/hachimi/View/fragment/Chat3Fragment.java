package com.example.hachimi.View.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.icu.text.SimpleDateFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hachimi.MainActivity;
import com.example.hachimi.R;
import com.example.hachimi.repository.model.Chat1UploadData;
import com.example.hachimi.repository.network.ChatNet;
import com.example.hachimi.utils.ImageHandler;
import com.example.hachimi.utils.SharedPreferenceHandler;

import java.lang.ref.WeakReference;
import java.util.Date;
/**
 * 包名称： com.example.hachimi.View.fragment
 * 类名称：Chat3Fragment
 * 类描述：动态的上传页面，需要请求相册权限，Token验证不通过会重新登录。
 * 创建人：韦西波
 */
public class Chat3Fragment extends Fragment {
    private ImageView c3IV_default;
    private Button c3BTN_submit;
    private EditText c3ET_title;
    private EditText c3ET_desc;
    private TextView c3TV_number;
    private String coverBase64 = "";
    private View rootView;
    private Handler handler = new MyHandler(this);
    private boolean messageConfirm;
    private static final int REQUEST_CODE_PERMISSION = 1001;
    private static final int REQUEST_CODE_SELECT = 1002;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.chat3_fragment, container, false);

        c3IV_default = rootView.findViewById(R.id.chat3_iv_defaultImage);
        c3BTN_submit = rootView.findViewById(R.id.chat3_btn_submit);
        c3TV_number = rootView.findViewById(R.id.chat3_tv_number);
        c3ET_title = rootView.findViewById(R.id.chat3_etv_title);
        c3ET_desc = rootView.findViewById(R.id.chat3_etv_describe);

        initEvent();
        initUpload();
        return rootView;
    }

    private static class MyHandler extends Handler {
        private WeakReference<Chat3Fragment> fragWeak;

        public MyHandler(Chat3Fragment fragment) {
            this.fragWeak = new WeakReference<>(fragment);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Chat3Fragment fragment = fragWeak.get();
            if (fragment == null || fragment.getContext() == null) {
                return;
            }

            if (msg.what == 1) {
                Toast.makeText(fragment.getContext(), "上传动态成功！", Toast.LENGTH_SHORT).show();
                fragment.clearData();
            } else if (msg.what == 0) {
                Log.e("上传数据错误！", "Chat3:Fail to upload data!");
                Toast.makeText(fragment.getContext(), "上传失败!" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
            } else if (msg.what == -1) {
                Toast.makeText(fragment.getContext(), "上传失败!请重新登录!", Toast.LENGTH_SHORT).show();
                SharedPreferenceHandler.clearPersonnel(fragment.getContext());
                SharedPreferenceHandler.clearSecret(fragment.getContext());
                Intent intent = new Intent(fragment.getContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                fragment.startActivity(intent);
            }
        }
    }

    private void initEvent() {
        c3IV_default.setOnClickListener((view) -> checkPermission());
        //限制标题字数
        c3ET_title.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 20) {
                    c3ET_title.setText(charSequence.subSequence(0, 20));
                    c3ET_title.setSelection(20);
                }
            }
        });

        //显示字数,限制内容字数
        c3ET_desc.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                c3TV_number.setText("当前字数：" + charSequence.length() + "/" + "200");
                if (charSequence.length() > 200) {
                    c3ET_desc.setText(charSequence.subSequence(0, 200));
                    // 将光标移动到末尾
                    c3ET_desc.setSelection(200);
                }
            }
        });
    }

    private void initUpload() {
        c3BTN_submit.setOnClickListener((view -> {
            String title = c3ET_title.getText().toString();
            String desc = c3ET_desc.getText().toString();
            if (title.length() < 3 || desc.length() < 5) {
                Toast.makeText(getContext(), "标题/内容字数过少！", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences sp = getContext().getSharedPreferences("personnel", 0);
            Boolean numberOfStart = sp.getBoolean("numberOfStarts", false);
            String account = sp.getString("account", "null");
            if (!numberOfStart) {
                Toast.makeText(getContext(), "请先登录！！", Toast.LENGTH_SHORT).show();
                return;
            }
            //获得系统时间
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
            Date date = new Date(System.currentTimeMillis());
            String time = sdf.format(date);
            Chat1UploadData data = new Chat1UploadData(account, title, desc, coverBase64);
            ChatNet chatNet = new ChatNet(handler);
            chatNet.submitData(rootView, data);
        }));
    }

    public void clearData() {
        if (c3ET_title.getText() != null) {
            c3ET_title.setText("");
        }
        if (c3ET_desc.getText() != null) {
            c3ET_desc.setText("");
        }
        c3TV_number.setText("当前字数:0");
        if (!coverBase64.isEmpty()) {
            coverBase64 = "";
        }
        c3IV_default.setImageResource(R.drawable.ic_default_iv);
        Fragment parentFrag = getParentFragment();
        if (parentFrag instanceof ChatFragment) {
            ChatFragment parent = (ChatFragment) parentFrag;
            ViewPager2 pVP2 = parent.getcVP2Instance();
            if (pVP2 != null) {
                pVP2.setCurrentItem(0, false);
            }
        }
    }


    private void checkPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
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
                Log.e("相册开启错误!", "Chat3:no authority!");
                Toast.makeText(getContext(), "请开启相册权限", Toast.LENGTH_SHORT).show();
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
        ContentResolver resolver = getContext().getContentResolver();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(resolver, uri);
            c3IV_default.setImageBitmap(bitmap);
            byte[] bytes = ImageHandler.fromBitmapToByte(bitmap);
            coverBase64 = Base64.encodeToString(bytes, Base64.DEFAULT);

        } catch (Exception e) {
            e.printStackTrace();
            Log.e("图片加载错误!", "Chat3:" + e.getMessage());
            Toast.makeText(getContext(), "图片加载失败！", Toast.LENGTH_SHORT).show();
        }
    }
}