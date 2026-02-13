package com.example.hachimi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.Toast;

import androidx.core.content.ContextCompat;

import com.example.hachimi.R;

/**
 * 包名称： com.example.hachimi.utils
 * 类名称：SharedPreferenceHandler
 * 类描述：SP的通用工具类，包含有清除个人数据(personnel)，清除ESP包含的Token，设置Token，获得个人头像等方法。
 * 创建人：韦西波
 * 创建时间：37小时
 */
public class SharedPreferenceHandler {
    public static String defaultProfile = "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAgGBgcGBQgHBwcJCQgKDBQNDAsLDBkSEw8UHRofHh0aHBwgJC4nICIsIxwcKDcpLDAxNDQ0Hyc5PTgyPC4zNDL/2wBDAQkJCQwLDBgNDRgyIRwhMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjIyMjL/wAARCAACAAQDASIAAhEBAxEB/8QAFQABAQAAAAAAAAAAAAAAAAAAAAf/xAAUEAEAAAAAAAAAAAAAAAAAAAAA/9oADAMBAAIRAxEAPwCdABmX/9k=";

    public static void clearPersonnel(Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("personnel", 0).edit();
        editor.putBoolean("numberOfStarts", false).putString("profile", "未知").
                putString("name", "棍木").putString("account", "").apply();
    }

    public static void clearSecret(Context context) {
        try {
            SharedPreferences sp0 = SecurityHandle.getEncryptedSharedPreferences(context);
            sp0.edit().putString("accessToken", "").putString("refreshToken", "").putInt("role", -1).apply();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Drawable getProfile(Context context) {
        SharedPreferences sp = context.getSharedPreferences("personnel", 0);
        Boolean numberOfStarts = sp.getBoolean("numberOfStarts", false);
        String profile = sp.getString("profile", "未知");
        if (!numberOfStarts || profile.equals("未知")) {
            return ContextCompat.getDrawable(context, R.drawable.main_icon);
        }
        byte[] bytes = ImageHandler.fromBase64ToByteArray(profile);
        Bitmap bitmap = ImageHandler.fromByteToBitmap(bytes, 180, 180);
        return ImageHandler.fromBitmapToDrawable(bitmap, context);
    }

    public static void setPersonnel(Context context, String profile, String name, String account) {
        SharedPreferences.Editor editor = context.getSharedPreferences("personnel", 0).edit();
        editor.putString("name", name).putString("profile", profile).putString("account", account).putBoolean("numberOfStarts", true).apply();
    }

    public static void setSecret(Context context, String accessToken, String refreshToken, int role) throws Exception {
        SharedPreferences sp = SecurityHandle.getEncryptedSharedPreferences(context);
        sp.edit().putString("accessToken", accessToken).putString("refreshToken", refreshToken).putInt("role", role).apply();
    }

    public static String[] getToken(Context context) {
        String accessToken = "";
        String refreshToken = "";
        try {
            SharedPreferences sp = SecurityHandle.getEncryptedSharedPreferences(context);
            accessToken = sp.getString("accessToken", "");
            refreshToken = sp.getString("refreshToken", "");
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
        String[] a = new String[]{accessToken, refreshToken};
        return a;
    }


}
