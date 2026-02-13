package com.example.hachimi.View.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hachimi.MainActivity;
import com.example.hachimi.R;
import com.example.hachimi.repository.network.ReversoNet;
import com.example.hachimi.utils.SecurityHandle;
import com.example.hachimi.utils.SharedPreferenceHandler;

import java.lang.ref.WeakReference;
/**
 * 包名称： com.example.hachimi.View.activity
 * 类名称：Setting1Activity
 * 类描述：用户在设置的第一个选项进行注销操作。若JWT认证不通过，则清除数据，要求重新登录。
 * 创建人：韦西波
 */
public class Setting1Activity extends AppCompatActivity {
    private Button drBTN_confirm;
    private EditText drETV_password;
    private EditText drETV_repassword;
    private ImageView drIV_back;
    private Handler handler = new MyHandler(this);

    public static class MyHandler extends Handler {
        private WeakReference<Setting1Activity> weakRefer;

        public MyHandler(Setting1Activity activity) {
            this.weakRefer = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            Setting1Activity activity = weakRefer.get();
            if (msg.what == -1) {
                Toast.makeText(activity, "注销失败!请重新登录!", Toast.LENGTH_SHORT).show();
                SharedPreferenceHandler.clearPersonnel(activity);
                SharedPreferenceHandler.clearSecret(activity);
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            } else if (msg.what == 0) {
                Toast.makeText(activity, "注销失败!" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1) {
                Toast.makeText(activity, "注销成功!" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
                SharedPreferences.Editor editor = activity.getSharedPreferences("personnel", 0).edit();
                editor.putBoolean("numberOfStarts", false).putString("profile", "未知").
                        putString("name", "棍木").putString("account", "").apply();
                try {
                    SharedPreferences sp0 = SecurityHandle.getEncryptedSharedPreferences(activity);
                    sp0.edit().putString("accessToken", "").putString("refreshToken", "").putInt("role", -1).apply();
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
                }
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            }

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting1_activity);

        drBTN_confirm = findViewById(R.id.deregister_btn_confirm);
        drETV_password = findViewById(R.id.deregister_etv_password);
        drETV_repassword = findViewById(R.id.deregister_etv_repassword);
        drIV_back = findViewById(R.id.deregister_iv_back);
        initEvent();
        initDeregister();
    }

    private void initEvent() {
        drIV_back.setOnClickListener(view -> finish());
        //字数限制
        drETV_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 12) {
                    drETV_password.setText(charSequence.subSequence(0, 12));
                    drETV_password.setSelection(12);
                }
            }
        });
        drETV_repassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 12) {
                    drETV_repassword.setText(charSequence.subSequence(0, 12));
                    drETV_repassword.setSelection(12);
                }
            }
        });

    }

    private void initDeregister() {
        drBTN_confirm.setOnClickListener(view -> {
            String password = drETV_password.getText().toString();
            String repassword = drETV_repassword.getText().toString();
            if (!password.equals(repassword)) {
                Toast.makeText(this, "错误!两次密码输入不一致!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 8) {
                Toast.makeText(this, "错误!密码长度过短!", Toast.LENGTH_SHORT).show();
                return;
            }
            SharedPreferences sp0 = getSharedPreferences("personnel", 0);
            String account = sp0.getString("account", "");
            ReversoNet reversoNet = new ReversoNet(handler);
            reversoNet.deregisterHandle(account, password, view);
        });
    }

}