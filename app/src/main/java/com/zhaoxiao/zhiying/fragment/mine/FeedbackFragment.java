package com.zhaoxiao.zhiying.fragment.mine;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.xuexiang.xui.utils.XToastUtils;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.HomeActivity;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.util.EditTextUtil;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import java.util.Objects;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedbackFragment extends BaseFragment {

    @BindView(R.id.et_feedback)
    EditText etFeedback;
    @BindView(R.id.btn_commit)
    Button btnCommit;

    private UserService userService;
    private String account;

    public FeedbackFragment() {
    }

    public static FeedbackFragment newInstance() {
        return new FeedbackFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_feedback;
    }

    @Override
    protected void initData() {
        userService = (UserService) getService(UserService.class);
        account = SpUtils.getInstance(getContext()).getString("account","");

        btnCommit.setEnabled(!etFeedback.getText().toString().trim().equals(""));
        etFeedback.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnCommit.setEnabled(!editable.toString().trim().equals(""));
            }
        });
    }

    @OnClick({R.id.ll_feedback, R.id.btn_commit})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_feedback:
                EditTextUtil.showSoftInputFromWindow(requireActivity(), etFeedback);
                break;
            case R.id.btn_commit:
                commit();
                break;
        }
    }

    private void commit() {
        Call<Data<Boolean>> addFeedbackCall = userService.addFeedback(account, etFeedback.getText().toString().trim());
        addFeedbackCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    boolean success = response.body().getData();
                    if (success) {
                        XToastUtils.success("提交反馈成功");
                        requireActivity().finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }
}