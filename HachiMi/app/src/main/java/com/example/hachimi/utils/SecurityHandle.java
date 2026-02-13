package com.example.hachimi.utils;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.security.crypto.EncryptedSharedPreferences;
import androidx.security.crypto.MasterKey;

/**
 * 包名称： com.example.hachimi.utils
 * 类名称：SecurityHandle
 * 类描述：用于获得ESP实例，安全储存Token等数据。使用了Androidx的ESP库。
 * 创建人：韦西波
 * 创建时间：29小时
 */
public class SecurityHandle {

    public static SharedPreferences getEncryptedSharedPreferences(Context context) throws Exception {
        MasterKey masterKey = new MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build();

        return EncryptedSharedPreferences.create(
                context,
                "secure_prefs",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        );
    }
}
