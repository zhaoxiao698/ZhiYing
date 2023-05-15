package com.zhaoxiao.zhiying.fragment.mine;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.mine.CollectionActivity;
import com.zhaoxiao.zhiying.activity.mine.HistoryActivity;
import com.zhaoxiao.zhiying.activity.mine.MyAttentionActivity;
import com.zhaoxiao.zhiying.activity.mine.MyFanActivity;
import com.zhaoxiao.zhiying.activity.mine.MyTrendActivity;
import com.zhaoxiao.zhiying.activity.mine.NoteListActivity;
import com.zhaoxiao.zhiying.activity.mine.SetInfoActivity;
import com.zhaoxiao.zhiying.activity.mine.SetTimePlanActivity;
import com.zhaoxiao.zhiying.activity.mine.WrongQuestionActivity;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.mine.Plan;
import com.zhaoxiao.zhiying.entity.mine.User;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.util.NumberUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MineLoggedFragment extends BaseFragment {

    @BindView(R.id.iv_avatar)
    RadiusImageView ivAvatar;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_sign)
    TextView tvSign;
    @BindView(R.id.ll_info)
    LinearLayout llInfo;
    @BindView(R.id.ll_trend)
    LinearLayout llTrend;
    @BindView(R.id.ll_attention)
    LinearLayout llAttention;
    @BindView(R.id.ll_fan)
    LinearLayout llFan;
    @BindView(R.id.tv_plan_do)
    TextView tvPlanDo;
    @BindView(R.id.tv_slip)
    TextView tvSlip;
    @BindView(R.id.tv_plan)
    TextView tvPlan;
    @BindView(R.id.tv_minute)
    TextView tvMinute;
    @BindView(R.id.hpv)
    HorizontalProgressView hpv;
    @BindView(R.id.card_plan)
    CardView cardPlan;
    @BindView(R.id.ll_collection)
    LinearLayout llCollection;
    @BindView(R.id.ll_history)
    LinearLayout llHistory;
    @BindView(R.id.ll_note)
    LinearLayout llNote;
    @BindView(R.id.ll_wrong)
    LinearLayout llWrong;
    @BindView(R.id.tv_no_plan)
    TextView tvNoPlan;
    @BindView(R.id.tv_have_plan)
    TextView tvHavePlan;
    @BindView(R.id.rl_have_plan)
    RelativeLayout rlHavePlan;
    @BindView(R.id.tv_trend)
    TextView tvTrend;
    @BindView(R.id.tv_attention)
    TextView tvAttention;
    @BindView(R.id.tv_fan)
    TextView tvFan;

    private long timePlan;
    private long timePlanDo;
    private UserService userService;
    private String account;

    private Plan plan;

    private User user;

    public MineLoggedFragment() {
    }

    public static MineLoggedFragment newInstance() {
        return new MineLoggedFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_mine_logged;
    }

    @Override
    protected void initData() {
        userService = (UserService) getService(UserService.class);
        account = SpUtils.getInstance(getContext()).getString("account", "");
    }

    @OnClick({/*R.id.iv_avatar, */R.id.ll_info, R.id.ll_trend, R.id.ll_attention, R.id.ll_fan, R.id.hpv, R.id.card_plan, R.id.ll_collection, R.id.ll_history, R.id.ll_note, R.id.ll_wrong})
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.iv_avatar:
////                XToastUtils.toast("预览头像");
//                break;
            case R.id.ll_info:
//                XToastUtils.toast("个人信息");
                navigateTo(SetInfoActivity.class);
                break;
            case R.id.ll_trend:
//                XToastUtils.toast("动态");
                navigateTo(MyTrendActivity.class);
                break;
            case R.id.ll_attention:
//                XToastUtils.toast("关注");
                navigateTo(MyAttentionActivity.class);
                break;
            case R.id.ll_fan:
//                XToastUtils.toast("粉丝");
                navigateTo(MyFanActivity.class);
                break;
            case R.id.card_plan:
//                XToastUtils.toast("计划");
                navigateTo(SetTimePlanActivity.class, "timePlan", timePlan);
                break;
            case R.id.ll_collection:
//                XToastUtils.toast("收藏");
                navigateTo(CollectionActivity.class);
                break;
            case R.id.ll_history:
//                XToastUtils.toast("历史");
                navigateTo(HistoryActivity.class);
                break;
            case R.id.ll_note:
//                XToastUtils.toast("笔记");
                navigateTo(NoteListActivity.class);
                break;
            case R.id.ll_wrong:
//                XToastUtils.toast("错题");
                navigateTo(WrongQuestionActivity.class);
                break;
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        getPlan();
        getUserInfo();

//        timePlan = getStringFromSp("time_plan", false);
//        if (timePlan <= 0) {
//            tvHavePlan.setVisibility(View.GONE);
//            tvNoPlan.setVisibility(View.VISIBLE);
//        } else {
//            tvHavePlan.setVisibility(View.VISIBLE);
//            tvNoPlan.setVisibility(View.GONE);
//            tvPlan.setText(String.valueOf(timePlan));
//
//            getPlan();
//        }
    }

    private void getPlan() {
        Call<Data<Plan>> planCall = userService.getPlan(account);
        planCall.enqueue(new Callback<Data<Plan>>() {
            @Override
            public void onResponse(Call<Data<Plan>> call, Response<Data<Plan>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    plan = response.body().getData();
                    timePlan = plan.getPlan();
                    timePlanDo = plan.getPlanDo();
                    if (timePlan <= 0) {
                        rlHavePlan.setVisibility(View.GONE);
                        tvNoPlan.setVisibility(View.VISIBLE);
                    } else {
                        rlHavePlan.setVisibility(View.VISIBLE);
                        tvNoPlan.setVisibility(View.GONE);
                        tvPlan.setText(String.valueOf(timePlan / (1000 * 60)));
                        tvPlanDo.setText(String.valueOf(timePlanDo / (1000 * 60)));
                        hpv.setProgress((float) timePlanDo / timePlan * 100);
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Plan>> call, Throwable t) {

            }
        });
    }

    private void getUserInfo() {
        Call<Data<User>> getUserInfoCall = userService.getByAccount(account);
        getUserInfoCall.enqueue(new Callback<Data<User>>() {
            @Override
            public void onResponse(Call<Data<User>> call, Response<Data<User>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    user = response.body().getData();
                    tvName.setText(user.getName());
                    tvSign.setText(user.getSign());
                    Picasso.with(getContext())
                            .load(user.getAvatar())
                            .transform(new CircleCornerTransForm())
                            .into(ivAvatar);
                    tvTrend.setText(NumberUtils.intChange2Str(user.getTrendNum()));
                    tvAttention.setText(NumberUtils.intChange2Str(user.getAttentionNum()));
                    tvFan.setText(NumberUtils.intChange2Str(user.getFanNum()));
                }
            }

            @Override
            public void onFailure(Call<Data<User>> call, Throwable t) {

            }
        });
    }
}