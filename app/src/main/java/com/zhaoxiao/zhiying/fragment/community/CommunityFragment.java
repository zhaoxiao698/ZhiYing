package com.zhaoxiao.zhiying.fragment.community;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xuexiang.xui.adapter.simple.XUISimpleAdapter;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.xuexiang.xui.widget.popupwindow.popup.XUIListPopup;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.community.PublishTrendActivity;
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

    private XUIListPopup mListPopup;

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
            public void searchClick(View view) {

            }

            @Override
            public void rightTextClick(View view) {

            }

            @Override
            public void rightDrawable1Click(View view) {
//                XToastUtils.toast("添加");
                initListPopupIfNeed();
                mListPopup.setAnimStyle(XUIPopup.ANIM_GROW_FROM_CENTER);
                mListPopup.setPreferredDirection(XUIPopup.DIRECTION_TOP);
                mListPopup.show(view);
            }

            @Override
            public void rightDrawable2Click(View view) {

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
//                SpUtils.getInstance(getContext()).setString("account", "");
                UnselectedFragment fragment = UnselectedFragment.newInstance(true);
                transaction.replace(R.id.fl_community, fragment).commit();
//                new MaterialDialog.Builder(getContext())
//                        .title("登录状态已过期")
//                        .content("登录状态已过期，请重新登录")
//                        .positiveText(R.string.lab_yes)
//                        .negativeText(R.string.lab_no)
//                        .positiveColor(getResources().getColor(R.color.g_yellow))
//                        .negativeColor(getResources().getColor(R.color.gray))
//                        .onPositive((dialog, which) -> navigateTo(CodeLoginActivity.class))
//                        .show();
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

    private void initListPopupIfNeed() {
        if (mListPopup == null) {

            String[] listItems = new String[]{
                    "发动态",
                    "创建话题",
            };

            XUISimpleAdapter adapter = XUISimpleAdapter.create(getContext(), listItems);
            mListPopup = new XUIListPopup(getContext(), adapter);
            mListPopup.create(DensityUtils.dp2px(getContext(), 100), ViewGroup.LayoutParams.WRAP_CONTENT, (adapterView, view, i, l) -> {
                switch (i){
                    case 0:navigateTo(PublishTrendActivity.class);break;
                    case 1:break;
                }
                mListPopup.dismiss();
            });
        }
    }
}