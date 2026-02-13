package com.example.hachimi.View.activity;

import android.content.Intent;
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

import com.example.hachimi.R;
import com.example.hachimi.repository.model.RegisterData;
import com.example.hachimi.repository.network.ReversoNet;

import java.lang.ref.WeakReference;
/**
 * 包名称： com.example.hachimi.View.activity
 * 类名称：RegisterActivity
 * 类描述：用户在此进行注册操作。
 * 创建人：韦西波
 */
public class RegisterActivity extends AppCompatActivity {
    private EditText regET_name;
    private EditText regET_acc;
    private EditText regET_pass;
    private EditText regET_repass;
    private ImageView regIV_switch;
    private Button regBTN_register;
    private Handler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<RegisterActivity> weakRefer;

        public MyHandler(RegisterActivity acitvity) {
            this.weakRefer = new WeakReference<>(acitvity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            RegisterActivity activity = weakRefer.get();

            if (msg.what == 0) {
                Toast.makeText(activity, "注册失败！" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
            } else if (msg.what == 1) {
                Toast.makeText(activity, "注册成功！", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(activity, LogonAcitvity.class);
                activity.startActivity(intent);
                activity.finish();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        regET_name = findViewById(R.id.register_etv_username);
        regET_acc = findViewById(R.id.register_etv_account);
        regET_pass = findViewById(R.id.register_etv_password);
        regET_repass = findViewById(R.id.register_etv_repassword);
        regIV_switch = findViewById(R.id.register_iv_logon);
        regBTN_register = findViewById(R.id.register_btn_register);
        initEvent();
        initRegister();
    }

    private void initEvent() {
        regIV_switch.setOnClickListener((view) -> {
            Intent intent = new Intent(RegisterActivity.this, LogonAcitvity.class);
            startActivity(intent);
            finish();
        });
        //依旧字数限制
        regET_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 8) {
                    regET_name.setText(charSequence.subSequence(0, 8));
                    regET_name.setSelection(8);
                }
            }
        });
        regET_acc.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 12) {
                    regET_acc.setText(charSequence.subSequence(0, 12));
                    regET_acc.setSelection(12);
                }
            }
        });
        regET_pass.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 12) {
                    regET_pass.setText(charSequence.subSequence(0, 12));
                    regET_pass.setSelection(12);
                }
            }
        });
        regET_repass.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable editable) {

            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() > 12) {
                    regET_repass.setText(charSequence.subSequence(0, 12));
                    regET_repass.setSelection(12);
                }
            }
        });
    }

    private void initRegister() {
        regBTN_register.setOnClickListener((view) -> {
            String name = regET_name.getText().toString();
            String account = regET_acc.getText().toString();
            String password = regET_pass.getText().toString();
            String repassword = regET_repass.getText().toString();
            if (!repassword.equals(password)) {
                Toast.makeText(RegisterActivity.this, "两次输入密码不一致！", Toast.LENGTH_SHORT).show();
                return;
            }
            if (name.length() < 3 || account.length() < 8 || password.length() < 8 || repassword.length() < 8) {
                Toast.makeText(RegisterActivity.this, "错误:账户名/账号/密码长度不够!", Toast.LENGTH_SHORT).show();
                return;
            }
            RegisterData data = new RegisterData(name, account, password);
            ReversoNet reversoNet = new ReversoNet(handler);
            reversoNet.signHandle(data, view);
        });
    }

}