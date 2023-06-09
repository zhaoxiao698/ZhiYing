package com.zhaoxiao.zhiying.fragment.mine;

import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.HomeActivity;
import com.zhaoxiao.zhiying.activity.mine.CodeLogin2Activity;
import com.zhaoxiao.zhiying.activity.mine.CodeLoginActivity;
import com.zhaoxiao.zhiying.activity.mine.MessageActivity;
import com.zhaoxiao.zhiying.activity.mine.SetActivity;
import com.zhaoxiao.zhiying.activity.mine.SetPasswordActivity;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.fragment.test.UnselectedFragment;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import butterknife.BindView;
import butterknife.OnClick;

public class MineFragment extends BaseFragment {
    @BindView(R.id.iv_setting)
    ImageView ivSetting;
    @BindView(R.id.iv_message)
    ImageView ivMessage;
    @BindView(R.id.fl_mine)
    FrameLayout flMine;
    private FragmentManager manager;

    private int flag = -1;

    public MineFragment() {
    }

    public static MineFragment newInstance() {
        return new MineFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_mine;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onStart() {
        super.onStart();
        manager = getChildFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        String account = SpUtils.getInstance(getContext()).getString("account", "");
        if (StringUtils.isEmpty(account)) {
            if (flag!=0) {
                UnselectedFragment fragment = UnselectedFragment.newInstance(true);
                transaction.replace(R.id.fl_mine, fragment).commit();
                flag = 0;
            }
        } else if (account.equals("已过期")){
            if (flag!=0) {
//                SpUtils.getInstance(getContext()).setString("account","");
                UnselectedFragment fragment = UnselectedFragment.newInstance(true);
                transaction.replace(R.id.fl_mine, fragment).commit();
//                new MaterialDialog.Builder(getContext())
//                        .title("登录状态已过期")
//                        .content("登录状态已过期，请重新登录")
//                        .positiveText(R.string.lab_yes)
//                        .negativeText(R.string.lab_no)
//                        .positiveColor(getResources().getColor(R.color.g_yellow))
//                        .negativeColor(getResources().getColor(R.color.gray))
//                        .onPositive((dialog, which) -> navigateTo(CodeLoginActivity.class))
//                        .show();
                flag = 0;
            }
        } else {
            if (flag!=1) {
                MineLoggedFragment fragment = MineLoggedFragment.newInstance();
                transaction.replace(R.id.fl_mine, fragment).commit();
                flag = 1;
            }
        }
    }

    @OnClick({R.id.iv_setting, R.id.iv_message})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_setting:
//                XToastUtils.toast("设置");
                navigateTo(SetActivity.class);
                break;
            case R.id.iv_message:
//                XToastUtils.toast("消息");
                navigateTo(MessageActivity.class);
                break;
        }
    }
}