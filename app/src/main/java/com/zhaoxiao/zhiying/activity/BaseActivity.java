package com.zhaoxiao.zhiying.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.util.TypedValue;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.jaeger.library.StatusBarUtil;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.api.ApiConfig;

import java.io.Serializable;

import butterknife.ButterKnife;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseActivity extends AppCompatActivity {

    public Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        int themeColor = getStringFromSp("theme_color",false);
        switch (themeColor){
            case -1:
            case 0:
                setTheme(R.style.Theme_ZhiYing);
                break;
            case 1:
                setTheme(R.style.Theme_ZhiYing_Blue);
                break;
            case 2:
                setTheme(R.style.Theme_ZhiYing_Red);
                break;
            case 3:
                setTheme(R.style.Theme_ZhiYing_Green);
                break;
        }
        setContentView(initLayout());
        setStatusBar();
        ButterKnife.bind(this);
//        initView();
        initData();
    }

    protected abstract int initLayout();

//    protected abstract void initView();

    protected abstract void initData();

    //弹出提示信息
    public void showToast(String msg) {
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
    }

    //弹出提示信息
    public void showToastSync(String msg) {
        Looper.prepare();
        Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    //导航
    public void navigateTo(Class cls) {
        Intent in = new Intent(mContext, cls);
        startActivity(in);
    }

    //导航ForResult
    public void navigateToForResult(Class cls,int requestCode) {
        Intent in = new Intent(mContext, cls);
        startActivityForResult(in,requestCode);
    }

    //导航传参ForResult
    public void navigateToForResult(Class cls, String name, Serializable obj,int requestCode) {
        Intent in = new Intent(mContext, cls);
        in.putExtra(name, obj);
        startActivityForResult(in,requestCode);
    }

    //跳转时清除前面的Activity
    public void navigateTo(Class cls,int flags){
        Intent in = new Intent(mContext, cls);
        in.setFlags(flags);
        startActivity(in);
    }

    //导航传参
    public void navigateTo(Class cls, String name, Serializable obj) {
        Intent in = new Intent(mContext, cls);
        in.putExtra(name, obj);
        startActivity(in);
    }

    //导航传参并清除前面的Activity
    public void navigateTo(Class cls, String name, Serializable obj, int flags) {
        Intent in = new Intent(mContext, cls);
        in.putExtra(name, obj);
        in.setFlags(flags);
        startActivity(in);
    }

//    //保存数据到本地
//    protected void saveStringToSp(String key, String value) {
//        SharedPreferences sp = getSharedPreferences("sp_ZhiYing", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sp.edit();
//        editor.putString(key, value);
//        editor.commit();
//    }
//
//    //从本地文件取出数据
//    protected String getStringFromSp(String key){
//        SharedPreferences sp = getSharedPreferences("sp_ZhiYing", Context.MODE_PRIVATE);
//        return sp.getString(key,"");
//    }

    //保存数据到本地
    public <T> void saveStringToSp(String key, T value) {
        SharedPreferences sp = getSharedPreferences("sp_ZhiYing", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if(value instanceof String) {
            editor.putString(key, (String) value);
        } else if(value instanceof Integer){
            editor.putInt(key, (Integer) value);
        }
        editor.commit();
    }

    //从本地文件取出数据
    protected <T> T getStringFromSp(String key, boolean isString) {
        SharedPreferences sp = getSharedPreferences("sp_ZhiYing", Context.MODE_PRIVATE);
        if(isString) return (T) sp.getString(key, "");
        else return (T)new Integer(sp.getInt(key,-1));
    }

    //删除
    protected void removeFromSp(String key){
        SharedPreferences sp = getSharedPreferences("sp_ZhiYing", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    //设置状态栏
    protected void setStatusBar() {
        StatusBarUtil.setTransparent(this);
        StatusBarUtils.setStatusBarLightMode(this);
    }

    //获取ApiService
    public Object getService(Class cls){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(cls);
    }

    public int getMyBgColor(){
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(R.attr.myBgColor, typedValue, true);
        return typedValue.data;
    }
}
