package com.zhaoxiao.zhiying.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.StudyService;

import java.io.Serializable;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public abstract class BaseFragment extends Fragment {

    protected View mRootView;

    protected Unbinder unbinder;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = inflater.inflate(initLayout(), container, false);
            unbinder = ButterKnife.bind(this, mRootView);
//            initView();
        }
        return mRootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    protected abstract int initLayout();

//    protected abstract void initView();

    protected abstract void initData();

    //弹出提示信息
    public void showToast(String msg) {
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
    }

    //弹出提示信息
    public void showToastSync(String msg) {
        Looper.prepare();
        Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
        Looper.loop();
    }

    //导航
    public void navigateTo(Class cls) {
        Intent in = new Intent(getActivity(), cls);
        startActivity(in);
    }

    //导航传参
    public void navigateTo(Class cls, String name, Serializable obj) {
        Intent in = new Intent(getActivity(), cls);
        in.putExtra(name, obj);
        startActivity(in);
    }

    //导航传参并清除前面的Activity
    public void navigateTo(Class cls, String name, Serializable obj,int flags) {
        Intent in = new Intent(getActivity(), cls);
        in.putExtra(name, obj);
        in.setFlags(flags);
        startActivity(in);
    }

    //导航传参ForResult
    public void navigateToForResult(Class cls,int requestCode) {
        Intent in = new Intent(getActivity(), cls);
        startActivityForResult(in,requestCode);
    }

    //导航传参ForResult
    public void navigateToForResult(Class cls, String name, Serializable obj,int requestCode) {
        Intent in = new Intent(getActivity(), cls);
        in.putExtra(name, obj);
        startActivityForResult(in,requestCode);
    }

    //跳转时清除前面的Activity
    public void navigateTo(Class cls,int flags){
        Intent in = new Intent(getActivity(), cls);
        in.setFlags(flags);
        startActivity(in);
    }

    //保存数据到本地
    public void saveStringToSp(String key, String value) {
        SharedPreferences sp = getActivity().getSharedPreferences("sp_ceramics", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, value);
        editor.commit();
    }

    //从本地文件取出数据
    protected String getStringFromSp(String key) {
        SharedPreferences sp = getActivity().getSharedPreferences("sp_ceramics", Context.MODE_PRIVATE);
        return sp.getString(key, "");
    }

    //删除
    protected void removeFromSp(String key){
        SharedPreferences sp = getActivity().getSharedPreferences("sp_ceramics", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.remove(key);
        editor.commit();
    }

    //获取ApiService
    public Object getService(Class cls){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(cls);
    }
}
