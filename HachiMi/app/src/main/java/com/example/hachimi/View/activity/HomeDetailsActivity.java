package com.example.hachimi.View.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hachimi.MainActivity;
import com.example.hachimi.R;
import com.example.hachimi.View.fragment.HomeFragment;
import com.example.hachimi.repository.model.HomeFragData;
import com.example.hachimi.repository.network.AdminNet;
import com.example.hachimi.utils.HomeFragViewModel;
import com.example.hachimi.utils.ImageHandler;
import com.example.hachimi.utils.SecurityHandle;
import com.example.hachimi.utils.SharedPreferenceHandler;

import java.lang.ref.WeakReference;

/**
 * 包名称： com.example.hachimi.View.activity
 * 类名称：HomeDetailsActivity
 * 类描述：某个档案的详细页面，管理员用户可以在此删除该档案。使用ViewModel获得档案相关的数据，Intent无法传递>1mb的单个数据，因此只用于打开activity。
 * 创建人：韦西波
 */
public class HomeDetailsActivity extends AppCompatActivity {
    private ImageView dIV_cat;
    private ImageView dBTN_back;
    private TextView dTV_title;
    private TextView dTV_detail;
    private ImageView dIV_delete;
    private HomeFragData data;
    private HomeFragViewModel viewModel;
    private Handler handler = new MyHandler(this);

    private static class MyHandler extends Handler {
        private WeakReference<HomeDetailsActivity> weakRefer;

        public MyHandler(HomeDetailsActivity activity) {
            this.weakRefer = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            HomeDetailsActivity activity = weakRefer.get();
            if (msg.what == -1) {
                Toast.makeText(activity, "删除失败!请重新登录!", Toast.LENGTH_SHORT).show();
                //Token有问题，清理一切，然后重新登录
                SharedPreferenceHandler.clearPersonnel(activity);
                SharedPreferenceHandler.clearSecret(activity);
                Intent intent = new Intent(activity, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                activity.startActivity(intent);
            } else if (msg.what == 1) {
                Toast.makeText(activity, "删除成功!刷新以确认结果!", Toast.LENGTH_SHORT).show();
                activity.finish();
            } else {
                Toast.makeText(activity, "删除失败!请检查网络后重试!" + msg.obj.toString(), Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_details_fragment);

        dIV_cat = findViewById(R.id.home_detail_iv_catPicture);
        dBTN_back = findViewById(R.id.home_detail_iv_back);
        dTV_title = findViewById(R.id.home_detail_tv_name);
        dTV_detail = findViewById(R.id.home_detail_tv_detail);
        dIV_delete = findViewById(R.id.home_detail_iv_delete);
        //共有fragment的viewModel，得到数据
        viewModel = HomeFragment.viewModel;
        viewModel.getHomeFragLiveData().observe(this, homeFragData -> {
            this.data = homeFragData;
            if (HomeDetailsActivity.this.data != null) {
                initEvent();
                initDelete();
            }
        });
    }

    private void initEvent() {
        //点返回按钮结束activity
        dBTN_back.setOnClickListener(view -> finish());

        Bitmap bitmap = ImageHandler.fromByteToBitmap(ImageHandler.fromBase64ToByteArray(data.getImage()), 180, 180);
        Drawable drawable = ImageHandler.fromBitmapToDrawable(bitmap, this);

        dIV_cat.setImageDrawable(drawable);
        dTV_title.setText(data.getTitle());
        dTV_detail.setText(data.getDescribe());
    }

    public void initDelete() {
        //判断删除逻辑
        int role = -1;
        try {
            SharedPreferences sp = SecurityHandle.getEncryptedSharedPreferences(this);
            role = sp.getInt("role", -1);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (role != 1) {
            dIV_delete.setImageResource(R.drawable.main_icon);
        } else {
            AdminNet adminNet = new AdminNet(handler);
            dIV_delete.setOnClickListener(view -> {
                //用AlertDialog提醒
                AlertDialog.Builder dialog = new AlertDialog.Builder(view.getContext());
                dialog.setTitle("你确定要删除这个档案吗?");
                dialog.setMessage("这会清除该档案的一切数据!");
                dialog.setCancelable(false);
                dialog.setPositiveButton("确认", (dialogInterface, i) -> {
                    adminNet.deleteHomeHandler(view, data);
                });
                dialog.setNegativeButton("取消", (DialogInterface, i) -> {
                });
                dialog.show();
            });
        }
    }
}