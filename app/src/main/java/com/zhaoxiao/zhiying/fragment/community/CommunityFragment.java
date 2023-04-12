package com.zhaoxiao.zhiying.fragment.community;

import android.widget.FrameLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.mine.CodeLoginActivity;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.fragment.test.SelectedFragment;
import com.zhaoxiao.zhiying.fragment.test.UnselectedFragment;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.SearchBarView;

import butterknife.BindView;

public class CommunityFragment extends BaseFragment {

    @BindView(R.id.search_bar_view)
    SearchBarView searchBarView;
    @BindView(R.id.fl_community)
    FrameLayout flCommunity;
    private FragmentManager manager;

    private int flag = -1;

    public CommunityFragment() {
    }

    public static CommunityFragment newInstance() {
        return new CommunityFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_community;
    }

    @Override
    protected void initData() {
        searchBarView.setOnViewClick(new SearchBarView.onViewClick() {
            @Override
            public void searchClick() {

            }

            @Override
            public void rightTextClick() {

            }

            @Override
            public void rightDrawable1Click() {
                XToastUtils.toast("添加");
            }

            @Override
            public void rightDrawable2Click() {

            }
        });
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
                transaction.replace(R.id.fl_community, fragment).commit();
                flag=0;
            }
        } else if (account.equals("已过期")){
            if (flag!=0) {
                SpUtils.getInstance(getContext()).setString("account", "");
                UnselectedFragment fragment = UnselectedFragment.newInstance(true);
                transaction.replace(R.id.fl_community, fragment).commit();
                new MaterialDialog.Builder(getContext())
                        .title("登录状态已过期")
                        .content("登录状态已过期，请重新登录")
                        .positiveText(R.string.lab_yes)
                        .negativeText(R.string.lab_no)
                        .positiveColor(getResources().getColor(R.color.g_yellow))
                        .negativeColor(getResources().getColor(R.color.gray))
                        .onPositive((dialog, which) -> navigateTo(CodeLoginActivity.class))
                        .show();
                flag=0;
            }
        } else {
            if (flag!=1){
                CommunityLoggedFragment fragment = CommunityLoggedFragment.newInstance();
                transaction.replace(R.id.fl_community, fragment).commit();
                flag=1;
            }
        }
    }
}