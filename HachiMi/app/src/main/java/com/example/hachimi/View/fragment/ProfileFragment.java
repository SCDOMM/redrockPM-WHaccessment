package com.example.hachimi.View.fragment;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.hachimi.MainActivity;
import com.example.hachimi.R;
import com.example.hachimi.View.activity.LogonAcitvity;
import com.example.hachimi.View.activity.SettingActivity;
import com.example.hachimi.utils.SharedPreferenceHandler;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
/**
 * 包名称： com.example.hachimi.View.fragment
 * 类名称：ProfileFragment
 * 类描述：用户的个人资料，点击右下角FAB可以退出登录/进入登录界面，视用户登录状态而定
 * 创建人：韦西波
 */
public class ProfileFragment extends Fragment {
    private View rootView;
    private ImageView proIV;
    private ImageView proIVsetting;
    private TextView proTV;
    private FloatingActionButton proFAB;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        initRefresh();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.profile_fragment, container, false);
        proIV = rootView.findViewById(R.id.profile_iv_profile);
        proTV = rootView.findViewById(R.id.profile_tv_name);
        proFAB = rootView.findViewById(R.id.profile_fab_logon);
        proIVsetting = rootView.findViewById(R.id.profile_iv_setting);
        initRefresh();
        initEvent();
        initFAB();
        return rootView;
    }

    private void initRefresh() {
        SharedPreferences sp = getContext().getSharedPreferences("personnel", 0);
        String name = sp.getString("name", "棍木");
        Boolean numberOfStarts = sp.getBoolean("numberOfStarts", false);
        if (!numberOfStarts) {
            SharedPreferenceHandler.clearPersonnel(getContext());
            SharedPreferenceHandler.clearSecret(getContext());
        }
        proTV.setText(name);
        proIV.setImageDrawable(SharedPreferenceHandler.getProfile(getContext()));
    }

    private void initEvent() {
        proIVsetting.setOnClickListener(view -> {
            Intent intent = new Intent(this.getContext(), SettingActivity.class);
            startActivity(intent);
        });
    }

    private void initFAB() {
        SharedPreferences sp = getContext().getSharedPreferences("personnel", 0);
        Boolean numberOfStarts = sp.getBoolean("numberOfStarts", false);
        if (!numberOfStarts) {
            proFAB.setImageResource(R.drawable.ic_register_iv);
            proFAB.setOnClickListener(view -> {
                Intent intent = new Intent(getContext(), LogonAcitvity.class);
                startActivity(intent);
            });
        } else {
            proFAB.setImageResource(R.drawable.ic_quit_iv);
            proFAB.setOnClickListener(view -> {
                AlertDialog.Builder dialog = new AlertDialog.Builder(this.getContext());
                dialog.setTitle("你确定要退出登录吗?");
                dialog.setMessage("这会清除你的登录状态!");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", (dialogInterface, i) -> {
                    SharedPreferenceHandler.clearPersonnel(getContext());
                    SharedPreferenceHandler.clearSecret(getContext());
                    Intent intent = new Intent(getContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                });
                dialog.setNegativeButton("取消", (DialogInterface, i) -> {
                });
                dialog.show();
            });
        }

    }


}