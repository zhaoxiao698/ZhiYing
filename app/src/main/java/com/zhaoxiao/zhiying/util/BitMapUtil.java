package com.zhaoxiao.zhiying.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class BitMapUtil {
    private static Bitmap bitmap;
    public static Bitmap getBitmap1(final String url) {
        Bitmap bm = null;
        try {
            URL iconUrl = new URL(url);
            URLConnection conn = iconUrl.openConnection();
            HttpURLConnection http = (HttpURLConnection) conn;

            int length = http.getContentLength();

            conn.connect();
            // 获得图像的字符流
            InputStream is = conn.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is, length);
            bm = BitmapFactory.decodeStream(bis);
            bis.close();
            is.close();// 关闭流
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }

    public static void getBitmap(final String url, final BitmapCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bm = null;
                try {
                    URL iconUrl = new URL(url);
                    URLConnection conn = iconUrl.openConnection();
                    HttpURLConnection http = (HttpURLConnection) conn;

                    int length = http.getContentLength();

                    conn.connect();
                    // 获得图像的字符流
                    InputStream is = conn.getInputStream();
                    BufferedInputStream bis = new BufferedInputStream(is, length);
                    bm = BitmapFactory.decodeStream(bis);
                    bis.close();
                    is.close(); // 关闭流

                    // 在获取到Bitmap后，通过回调将结果传递给调用方
                    if (callback != null) {
                        callback.onBitmapLoaded(bm);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    // 在出现异常时，通过回调通知调用方
                    if (callback != null) {
                        callback.onBitmapFailed(e);
                    }
                }
            }
        }).start();
    }

    public interface BitmapCallback {
        void onBitmapLoaded(Bitmap bitmap);
        void onBitmapFailed(Exception e);
    }
}