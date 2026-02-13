package com.example.hachimi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import com.example.hachimi.View.activity.LogonAcitvity;
import com.example.hachimi.View.adapter.MainAdapter;
import com.example.hachimi.View.fragment.ChatFragment;
import com.example.hachimi.View.fragment.HomeFragment;
import com.example.hachimi.View.fragment.ProfileFragment;
import com.example.hachimi.repository.network.ReversoNet;
import com.example.hachimi.utils.SecurityHandle;
import com.example.hachimi.utils.SharedPreferenceHandler;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * 包名称： com.example.hachimi
 * 类名称：MainActivity
 * 类描述：使用VP2和BNV创建主页。在每次启动时，MainActivity会自动进行一次登录检测，如果没有登录，就会弹出登录界面。
 * 如果登录了，MainActivity还会自动进行一次Token检测，如果Token过期，会让你重新登录
 * 创建人：韦西波
 *
 */
public class MainActivity extends AppCompatActivity {
    public static final String basicUrl = "192.168.69.239";
    public static final String basicPort = "8080";
    public ViewPager2 mVP2;
    public BottomNavigationView mBNV;
    private Handler handler = new MyHandler(this);

    //接收Token检测的信息
    private static class MyHandler extends Handler {
        private WeakReference<MainActivity> weakRefer;

        public MyHandler(MainActivity activity) {
            this.weakRefer = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            MainActivity activity = weakRefer.get();
            if (msg.what == 0) {
                Toast.makeText(activity, "登录过期，请重新登录!" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
                SharedPreferenceHandler.clearSecret(activity);
                SharedPreferenceHandler.clearPersonnel(activity);
                Intent intent = new Intent(activity, LogonAcitvity.class);
                activity.startActivity(intent);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mVP2 = findViewById(R.id.main_vp2_fragment);
        mBNV = findViewById(R.id.main_bnv_default);
        initEvent();
        initBnv();
        initVp2Listener();
    }

    private void initEvent() {
        //设置适配器等
        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(new HomeFragment());
        fragmentList.add(new ChatFragment());
        fragmentList.add(new ProfileFragment());
        MainAdapter adapter = new MainAdapter(this, fragmentList);
        mVP2.setAdapter(adapter);


        mVP2.setOnTouchListener((view, motionEvent) -> {
            view.getParent().requestDisallowInterceptTouchEvent(true);
            return false;
        });

        //登录检测
        SharedPreferences sp = getSharedPreferences("personnel", 0);
        boolean numberOfStarts = sp.getBoolean("numberOfStarts", false);
        //没登录过，登录。
        if (!numberOfStarts) {
            Toast.makeText(this, "请用户进行登录", Toast.LENGTH_SHORT).show();
            SharedPreferenceHandler.clearPersonnel(this);
            try {
                //自动得到游客身份
                SharedPreferences sp1 = SecurityHandle.getEncryptedSharedPreferences(this);
                sp1.edit().putInt("role", -1).apply();
            } catch (Exception e) {
                e.printStackTrace();
            }
            Intent intent = new Intent(MainActivity.this, LogonAcitvity.class);
            startActivity(intent);
        } else {
            //登录过，进行Token检测
            ReversoNet reversoNet = new ReversoNet(handler);
            reversoNet.jwtTestHandle(findViewById(android.R.id.content));
        }
    }

    //初始化BNV
    private void initBnv() {
        mBNV.setOnNavigationItemSelectedListener(Item -> {
            if (Item.getItemId() == R.id.menu_item1_Bnv) {
                mVP2.setCurrentItem(0, false);
            } else if (Item.getItemId() == R.id.menu_item2_Bnv) {
                mVP2.setCurrentItem(1, false);
            } else if (Item.getItemId() == R.id.menu_item3_Bnv) {
                mVP2.setCurrentItem(2, false);
            }
            return true;
        });
        mBNV.getMenu().getItem(1).setChecked(true);
    }

    private void initVp2Listener() {
        mVP2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                switch (position) {
                    case 0:
                        mBNV.setSelectedItemId(R.id.menu_item1_Bnv);
                        break;
                    case 1:
                        mBNV.setSelectedItemId(R.id.menu_item2_Bnv);
                        break;
                    case 2:
                        mBNV.setSelectedItemId(R.id.menu_item3_Bnv);
                        break;
                }
            }
        });
        mVP2.setUserInputEnabled(false);
    }
}