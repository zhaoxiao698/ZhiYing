package com.zhaoxiao.zhiying.activity.mine;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.util.cache.CacheDataManager;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhaoxiao.zhiying.activity.mine.ServiceProtocolActivity.KEY_PROTOCOL_TITLE;

public class SetActivity extends BaseActivity {
    @BindView(R.id.tv_cache)
    TextView tvCache;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    @Override
    protected int initLayout() {
        return R.layout.activity_set;
    }

    @Override
    protected void initData() {
        int themeColor = getStringFromSp("theme_color", false);
        switch (themeColor) {
            case -1:
            case 0:
                ivBack.setImageResource(R.drawable.left_yellow);
                break;
            case 1:
                ivBack.setImageResource(R.drawable.left_blue);
                break;
            case 2:
                ivBack.setImageResource(R.drawable.left_red);
                break;
            case 3:
                ivBack.setImageResource(R.drawable.left_green);
                break;
        }

        String totalCacheSize = "";
        try {
            totalCacheSize = CacheDataManager.getTotalCacheSize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tvCache.setText(" （" + totalCacheSize + "）");
    }

    @OnClick({R.id.iv_back, R.id.rl_info, R.id.rl_account, R.id.rl_cache, R.id.rl_help, R.id.rl_user_protocol, R.id.rl_privacy_protocol, R.id.rl_theme, R.id.rl_about, R.id.rl_logout})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_info:
                navigateTo(SetInfoActivity.class);
                break;
            case R.id.rl_account:
                navigateTo(AccountActivity.class);
                break;
            case R.id.rl_cache:
                CacheDataManager.clearAllCache(this);
                XToastUtils.toast("清除成功");
                String totalCacheSize = "";
                try {
                    totalCacheSize = CacheDataManager.getTotalCacheSize(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                tvCache.setText(" （" + totalCacheSize + "）");
                break;
            case R.id.rl_help:
                navigateTo(HelpActivity.class);
                break;
            case R.id.rl_user_protocol:
                navigateTo(ServiceProtocolActivity.class, KEY_PROTOCOL_TITLE, "用户协议");
                break;
            case R.id.rl_privacy_protocol:
                navigateTo(ServiceProtocolActivity.class, KEY_PROTOCOL_TITLE, "隐私政策");
                break;
            case R.id.rl_theme:
                navigateTo(ThemeActivity.class);
                break;
            case R.id.rl_about:
                navigateTo(AboutActivity.class);
                break;
            case R.id.rl_logout:
                new MaterialDialog.Builder(this)
                        .title("退出登录")
                        .content("确定要退出登录吗？")
                        .positiveText("确定")
                        .negativeText("取消")
                        .positiveColor(getResources().getColor(getMyBgColor()))
                        .negativeColor(getResources().getColor(R.color.gray))
                        .onPositive((dialog, which) -> {
                            SpUtils.getInstance(this).setString("account", "");
                            navigateTo(CodeLoginActivity.class, Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        })
                        .onNegative((dialog, which) -> {
                        })
                        .show();
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