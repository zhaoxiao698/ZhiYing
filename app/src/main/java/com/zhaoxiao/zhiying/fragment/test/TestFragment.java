package com.zhaoxiao.zhiying.fragment.test;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.test.SelectActivity;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.SearchBarView;

import butterknife.BindView;

public class TestFragment extends BaseFragment {
    @BindView(R.id.search_bar_view)
    SearchBarView searchBarView;
    private FragmentManager manager;

    public TestFragment() {
    }

    public static TestFragment newInstance() {
        return new TestFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_test;
    }

    @Override
    protected void initData() {
        searchBarView.setOnViewClick(new SearchBarView.onViewClick() {
            @Override
            public void searchClick() {

            }

            @Override
            public void rightTextClick() {
                navigateTo(SelectActivity.class);
            }

            @Override
            public void rightDrawable1Click() {
                navigateTo(SelectActivity.class);
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
        Integer questionBankId = getStringFromSp("questionBankId",false);
        if (questionBankId==null||questionBankId==-1) {
            UnselectedFragment fragment = UnselectedFragment.newInstance(false);
//            if(transaction.isEmpty())transaction.add(R.id.fl_test, fragment).commit();
//            else transaction.replace(R.id.fl_test, fragment).commit();
            transaction.replace(R.id.fl_test, fragment).commit();
        } else {
            SelectedFragment fragment = SelectedFragment.newInstance(questionBankId);
//            if(transaction.isEmpty())transaction.add(R.id.fl_test, fragment).commit();
//            else transaction.replace(R.id.fl_test, fragment).commit();
            transaction.replace(R.id.fl_test, fragment).commit();
        }
    }

    public void changeQuestionBank(String questionBank){
        searchBarView.setRightText(questionBank);
    }
}