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
import com.example.hachimi.repository.model.LogonResponseData;
import com.example.hachimi.repository.network.ReversoNet;
import com.example.hachimi.utils.SecurityHandle;
import com.example.hachimi.utils.SharedPreferenceHandler;

import java.lang.ref.WeakReference;

/**
 * 包名称： com.example.hachimi.View.activity
 * 类名称：LogonActivity
 * 类描述：用户在此进行登录操作。
 * 创建人：韦西波
 */
public class LogonAcitvity extends AppCompatActivity {
    private EditText logET_acc;
    private EditText logET_pass;
    private Button logBTN_logon;
    private ImageView logIV_switch;
    private Handler handler = new MyHandler(this);
    ;
    private String account;

    private static class MyHandler extends Handler {
        private WeakReference<LogonAcitvity> weakRefer;

        public MyHandler(LogonAcitvity acitvity) {
            this.weakRefer = new WeakReference<>(acitvity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            LogonAcitvity activity = weakRefer.get();
            if (msg.what == 0) {
                Toast.makeText(activity, "登录失败!" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
                SharedPreferenceHandler.clearPersonnel(activity);
                SharedPreferenceHandler.clearSecret(activity);
            } else if (msg.what == 1) {
                Toast.makeText(activity, "登录成功!", Toast.LENGTH_SHORT).show();

                LogonResponseData data = (LogonResponseData) msg.obj;
                try {
                    SharedPreferenceHandler.setSecret(activity, data.getAccessToken(), data.getRefreshToken(), data.getUserRole());
                    SharedPreferenceHandler.setPersonnel(activity, data.getUserProfile(), data.getUserName(), activity.account);

                    int role = -1;
                    try {
                        SharedPreferences sp1 = SecurityHandle.getEncryptedSharedPreferences(activity);
                        role = sp1.getInt("role", -1);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (role == 1) {
                        Toast.makeText(activity, "您现在正以管理员身份登录!", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(activity, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logon_acitvity);
        logET_acc = findViewById(R.id.logon_etv_account);
        logET_pass = findViewById(R.id.logon_etv_password);
        logBTN_logon = findViewById(R.id.logon_btn_logon);
        logIV_switch = findViewById(R.id.logon_iv_register);
        initEvent();
        initLogon();
    }

    private void initEvent() {
        logIV_switch.setOnClickListener((view) -> {
            Intent intent = new Intent(LogonAcitvity.this, RegisterActivity.class);
            startActivity(intent);
            finish();
        });
        //进行字数限制
        logET_acc.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 12) {
                    logET_acc.setText(charSequence.subSequence(0, 12));
                    logET_acc.setSelection(12);
                }
            }
        });
        logET_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 12) {
                    logET_pass.setText(charSequence.subSequence(0, 12));
                    logET_pass.setSelection(12);
                }
            }
        });
    }

    private void initLogon() {
        logBTN_logon.setOnClickListener((view) -> {
            account = logET_acc.getText().toString();
            String password = logET_pass.getText().toString();
            if (account.length() < 8 || password.length() < 8) {
                Toast.makeText(LogonAcitvity.this, "错误:账号/密码长度不够!", Toast.LENGTH_SHORT).show();
                return;
            }

            ReversoNet reversoNet = new ReversoNet(handler);
            reversoNet.loginHandle(account, password, view);
        });
    }

}