package com.example.hachimi.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 包名称： com.example.hachimi.utils
 * 类名称：ImageHandler
 * 类描述：利用官方库处理各种图片数据，让base64,byte[],bitmap,drawable之间相互转换 TODO 一些方法暂时意义不明
 * 创建人：韦西波
 */
public class ImageHandler {
    public static byte[] fromBase64ToByteArray(String base64String) {
        if (base64String == null || base64String.trim().isEmpty()) {
            Log.e("格式转换错误!", "Base64ToByte:base64Data is empty!");
            return null;
        }
        try {
            // 检查是否有前缀
            int index = base64String.indexOf(",");
            String pureBase64 = index != -1 ? base64String.substring(index + 1) : base64String;
            // 解码base64字符串
            return Base64.decode(pureBase64, 0);
        } catch (IllegalArgumentException e) {
            Log.e("格式转换错误!", "Base64ToByte:" + e.getMessage());
            return null;
        }
    }

    public static byte[] fromBitmapToByte(Bitmap bitmap) {
        if (bitmap == null) {
            Log.e("格式转换错误!", "BitmapToByte:the bitmap is null!");
            return null;
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        boolean success = bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);
        if (!success) {
            Log.e("格式转换错误!", "BitmapToByte:compress failed!");
            return null;
        }

        byte[] byteArray = out.toByteArray();
        try {
            out.flush();
            out.close();
        } catch (IOException e) {
            Log.e("格式转换错误!", "BitmapToByte:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
        return byteArray;
    }

    public static Bitmap fromByteToBitmap(byte[] bytes, int maxWidth, int maxHeight) {
        if (bytes == null || bytes.length == 0) {
            Log.e("格式转换错误!", "ByteToBitmap:the byte is empty!");
            return null;
        }
        if (maxHeight <= 0 || maxWidth <= 0) {
            Log.e("格式转换错误!", "ByteToBitmap:the maxWidth/Height is invalid!");
            return null;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();

            options.inJustDecodeBounds = true;
            BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            if (options.outWidth <= 0 || options.outHeight <= 0) {
                Log.e("格式转换错误!", "ByteToBitmap:decode error!");
                return null;
            }

            options.inSampleSize = caculateInSampleSize(options.outWidth, options.outHeight, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;

            options.inDither = true;
            options.inScaled = true;
            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
            if (bitmap == null) {
                Log.e("格式转换错误!", "ByteToBitmap:cannot decode!");
                return null;
            }
            return bitmap;
        } catch (Exception e) {
            Log.e("格式转换错误!", "ByteToBitmap:" + e.getMessage());
            e.printStackTrace();
            return null;
        } catch (OutOfMemoryError e) {
            Log.e("格式转换错误!内存溢出!", "ByteToBitmap:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }

    private static int caculateInSampleSize(int originWidth, int originHeight, int maxWidth, int maxHeight) {
        int inSampleSize = 1;
        if (originWidth > maxWidth || originHeight > maxHeight) {
            int halfWidth = originWidth / 2;
            int halfHeight = originHeight / 2;
            while ((halfWidth / inSampleSize) >= maxWidth && (halfHeight / inSampleSize) >= maxHeight) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap fromFileToBitmap(File file, int maxWidth, int maxHeight) {
        if (file == null || !file.exists()) {
            Log.e("格式转换错误!", "FileToBitmap:the file is empty!");
            return null;
        }
        if (maxHeight <= 0 || maxWidth <= 0) {
            Log.e("格式转换错误!", "ByteToBitmap:the maxWidth/Height is invalid!");
            return null;
        }
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            if (options.outWidth <= 0 || options.outHeight <= 0) {
                Log.e("格式转换错误!", "FileToBitmap:decode error!");
                return null;
            }

            options.inSampleSize = caculateInSampleSize(options.outWidth, options.outHeight, maxWidth, maxHeight);
            options.inJustDecodeBounds = false;
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            Bitmap bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), options);
            if (bitmap == null) {
                Log.e("格式转换错误!", "FileToBitmap:cannot decode!");
                return null;
            }
            return bitmap;
        } catch (Exception e) {
            Log.e("格式转换错误!", "FileToBitmap:" + e.getMessage());
            e.printStackTrace();
            return null;
        }

    }


    public static Drawable fromBitmapToDrawable(Bitmap temp, Context context) {
        if (temp == null || context == null) {
            Log.e("格式转换错误!", "BitmapToDrawable:the bitmap/context is empty!");
            return null;
        }
        return new BitmapDrawable(context.getResources(), temp);
    }

    public static Bitmap fromUriToBitmap(Context context, Uri uri, int maxWidth, int maxHeight) {
        if (uri == null || context == null) {
            Log.e("格式转换错误!", "UriToBitmap:the uri/context is empty!");
            return null;
        }
        InputStream inputStream = null;
        try {
            inputStream = context.getContentResolver().openInputStream(uri);
            byte[] bytes = inputStreamToByte(inputStream);
            return fromByteToBitmap(bytes, maxWidth, maxHeight);
        } catch (Exception e) {
            Log.e("格式转换错误!", "UriToBitmap:" + e.getMessage());
            e.printStackTrace();
            return null;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    Log.e("格式转换错误!", "UriToBitmap:" + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    private static byte[] inputStreamToByte(InputStream inputStream) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] bytes = new byte[4096];
        int length;
        try {
            while ((length = inputStream.read(bytes)) != -1) {
                outputStream.write(bytes, 0, length);
            }
            byte[] bytes1 = outputStream.toByteArray();
            outputStream.flush();
            outputStream.close();
            return bytes1;
        } catch (Exception e) {
            Log.e("格式转换错误!", "ISToByte:" + e.getMessage());
            e.printStackTrace();
            return null;
        }
    }


}
