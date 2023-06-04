package com.zhaoxiao.zhiying.activity.mine;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ThemeActivity extends BaseActivity {
    @BindView(R.id.btn_yellow)
    Button btnYellow;
    @BindView(R.id.btn_blue)
    Button btnBlue;
    @BindView(R.id.btn_red)
    Button btnRed;
    @BindView(R.id.btn_green)
    Button btnGreen;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @Override
    protected int initLayout() {
        return R.layout.activity_theme;
    }

    @Override
    protected void initData() {
        int themeColor = getStringFromSp("theme_color", false);
        switch (themeColor) {
            case -1:
            case 0:
                btnYellow.setTextColor(getResources().getColor(R.color.white));
                btnYellow.setBackground(getDrawable(R.drawable.shape_theme_btn));
                ivBack.setImageTintList(getResources().getColorStateList(R.color.g_yellow));
                ivBack.setImageResource(R.drawable.left_yellow);
                btnYellow.setText("使用中");
                break;
            case 1:
                btnBlue.setTextColor(getResources().getColor(R.color.white));
                btnBlue.setBackground(getDrawable(R.drawable.shape_theme_btn));
                ivBack.setImageTintList(getResources().getColorStateList(R.color.g_blue));
                ivBack.setImageResource(R.drawable.left_blue);
                btnBlue.setText("使用中");
                break;
            case 2:
                btnRed.setTextColor(getResources().getColor(R.color.white));
                btnRed.setBackground(getDrawable(R.drawable.shape_theme_btn));
                ivBack.setImageTintList(getResources().getColorStateList(R.color.g_red));
                ivBack.setImageResource(R.drawable.left_red);
                btnRed.setText("使用中");
                break;
            case 3:
                btnGreen.setTextColor(getResources().getColor(R.color.white));
                btnGreen.setBackground(getDrawable(R.drawable.shape_theme_btn));
                ivBack.setImageTintList(getResources().getColorStateList(R.color.g_green));
                ivBack.setImageResource(R.drawable.left_green);
                btnGreen.setText("使用中");
                break;
        }
    }

    @OnClick({R.id.iv_back, R.id.rl_yellow, R.id.rl_blue, R.id.rl_red, R.id.rl_green, R.id.btn_yellow, R.id.btn_blue, R.id.btn_red, R.id.btn_green })
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_yellow:
            case R.id.rl_yellow:
                saveStringToSp("theme_color", 0);
                recreate();
                break;
            case R.id.btn_blue:
            case R.id.rl_blue:
                saveStringToSp("theme_color", 1);
                recreate();
                break;
            case R.id.btn_red:
            case R.id.rl_red:
                saveStringToSp("theme_color", 2);
                recreate();
                break;
            case R.id.btn_green:
            case R.id.rl_green:
                saveStringToSp("theme_color", 3);
                recreate();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }
}