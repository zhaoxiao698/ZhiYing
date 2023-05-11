package com.zhaoxiao.zhiying.activity.study;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.CalendarLayout;
import com.haibin.calendarview.CalendarView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.mine.CodeLoginActivity;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.mine.CalendarInfo;
import com.zhaoxiao.zhiying.entity.mine.Plan;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import java.util.Calendar;

public class CalendarActivity extends BaseActivity implements
        CalendarView.OnCalendarSelectListener,
        CalendarView.OnYearChangeListener {
    @BindView(R.id.tv_month_day)
    TextView tvMonthDay;
    @BindView(R.id.tv_year)
    TextView tvYear;
    @BindView(R.id.tv_lunar)
    TextView tvLunar;
    @BindView(R.id.ib_calendar)
    ImageView ibCalendar;
    @BindView(R.id.tv_current_day)
    TextView tvCurrentDay;
    @BindView(R.id.fl_current)
    FrameLayout flCurrent;
    @BindView(R.id.rl_tool)
    RelativeLayout rlTool;
    @BindView(R.id.calendarView)
    CalendarView calendarView;
    @BindView(R.id.calendarLayout)
    CalendarLayout calendarLayout;
    @BindView(R.id.tv_continuous)
    TextView tvContinuous;
    @BindView(R.id.tv_total)
    TextView tvTotal;

    private int mYear;

    private UserService userService;
    private String account;
    private CalendarInfo calendarInfo;

//    @BindView(R.id.rv)
//    RecyclerView rv;
//
//    private StudyService studyService;
//
//    private List<Hot> hotList;
//    private HotAdapter hotAdapter;
//    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int initLayout() {
        return R.layout.activity_calendar;
    }

    @Override
    protected void initData() {
        account = SpUtils.getInstance(this).getString("account", "");
        if (StringUtils.isEmpty(account) || account.equals("已过期")) {
            finish();
            navigateTo(CodeLoginActivity.class);
        } else {
            calendarLayout = findViewById(R.id.calendarLayout);
            calendarView.setOnCalendarSelectListener(this);
            calendarView.setOnYearChangeListener(this);
            tvYear.setText(String.valueOf(calendarView.getCurYear()));
            mYear = calendarView.getCurYear();
            tvMonthDay.setText(calendarView.getCurMonth() + "月" + calendarView.getCurDay() + "日");
            tvLunar.setText("今日");
            tvCurrentDay.setText(String.valueOf(calendarView.getCurDay()));

//            int year = calendarView.getCurYear();
//            int month = calendarView.getCurMonth();

            userService = (UserService) getService(UserService.class);
            getPlanList();

//            Map<String, Calendar> map = new HashMap<>();
//            map.put(getSchemeCalendar(year, month, 3, 0xFF40db25, "20").toString(),
//                    getSchemeCalendar(year, month, 3, 0xFF40db25, "20"));
//            map.put(getSchemeCalendar(year, month, 6, 0xFFe69138, "33").toString(),
//                    getSchemeCalendar(year, month, 6, 0xFFe69138, "33"));
//            map.put(getSchemeCalendar(year, month, 9, 0xFFdf1356, "25").toString(),
//                    getSchemeCalendar(year, month, 9, 0xFFdf1356, "25"));
//            map.put(getSchemeCalendar(year, month, 13, 0xFFedc56d, "50").toString(),
//                    getSchemeCalendar(year, month, 13, 0xFFedc56d, "50"));
//            map.put(getSchemeCalendar(year, month, 14, 0xFFedc56d, "80").toString(),
//                    getSchemeCalendar(year, month, 14, 0xFFedc56d, "80"));
//            map.put(getSchemeCalendar(year, month, 15, 0xFFaacc44, "20").toString(),
//                    getSchemeCalendar(year, month, 15, 0xFFaacc44, "20"));
//            map.put(getSchemeCalendar(year, month, 18, 0xFFbc13f0, "70").toString(),
//                    getSchemeCalendar(year, month, 18, 0xFFbc13f0, "70"));
//            map.put(getSchemeCalendar(year, month, 25, 0xFF13acf0, "36").toString(),
//                    getSchemeCalendar(year, month, 25, 0xFF13acf0, "36"));
//            map.put(getSchemeCalendar(year, month, 27, 0xFF13acf0, "95").toString(),
//                    getSchemeCalendar(year, month, 27, 0xFF13acf0, "95"));
//            //此方法在巨大的数据量上不影响遍历性能，推荐使用
//            calendarView.setSchemeDate(map);

//            studyService = (StudyService) getService(StudyService.class);
//            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
//            rv.setLayoutManager(linearLayoutManager);
//            hotAdapter = new HotAdapter(this, R.layout.item_hot_more_layout,true);
//            rv.setAdapter(hotAdapter);
//            hotAdapter.setOnItemClickListener(hotId -> navigateTo(ChannelActivity.class,"channelId",hotId));
//            getHotList();
        }
    }


    private Calendar getSchemeCalendar(int year, int month, int day, int color, String text) {
        Calendar calendar = new Calendar();
        calendar.setYear(year);
        calendar.setMonth(month);
        calendar.setDay(day);
        calendar.setSchemeColor(color);//如果单独标记颜色、则会使用这个颜色
        calendar.setScheme(text);
        return calendar;
    }

    @Override
    public void onCalendarOutOfRange(Calendar calendar) {

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onCalendarSelect(Calendar calendar, boolean isClick) {
        tvLunar.setVisibility(View.VISIBLE);
        tvYear.setVisibility(View.VISIBLE);
        tvMonthDay.setText(calendar.getMonth() + "月" + calendar.getDay() + "日");
        tvYear.setText(String.valueOf(calendar.getYear()));
        tvLunar.setText(calendar.getLunar());
        mYear = calendar.getYear();

//        XToastUtils.toast("");
    }

    @Override
    public void onYearChange(int year) {
        tvMonthDay.setText(String.valueOf(year));
    }

    @OnClick({R.id.tv_month_day, R.id.fl_current})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_month_day:
                if (!calendarLayout.isExpand()) {
                    calendarLayout.expand();
                    return;
                }
                calendarView.showYearSelectLayout(mYear);
                tvLunar.setVisibility(View.GONE);
                tvYear.setVisibility(View.GONE);
                tvMonthDay.setText(String.valueOf(mYear));
                break;
            case R.id.fl_current:
                calendarView.scrollToCurrent();
                break;
        }
    }

    private void getPlanList() {
        Call<Data<CalendarInfo>> planListCall = userService.getCalendarInfo(account);
        planListCall.enqueue(new Callback<Data<CalendarInfo>>() {
            @Override
            public void onResponse(Call<Data<CalendarInfo>> call, Response<Data<CalendarInfo>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    calendarInfo = response.body().getData();

                    Map<String, Calendar> map = new HashMap<>();
                    for (Plan plan : calendarInfo.getPlanList()) {
//                        TimeZone timeZone = TimeZone.getTimeZone("GMT+8");
//                        TimeZone.setDefault(timeZone);
                        Date dateTest = new Date();
                        java.util.Calendar calendar = java.util.Calendar.getInstance();
                        calendar.setTime(plan.getAddTime());                    //放入Date类型数据
                        int year = calendar.get(java.util.Calendar.YEAR);
                        int month = calendar.get(java.util.Calendar.MONTH) + 1;
                        int day = calendar.get(java.util.Calendar.DATE);
                        int scheme = (int) ((float) plan.getPlanDo() / plan.getPlan() * 100);
                        map.put(getSchemeCalendar(year, month, day, 0xFF40db25, Integer.toString(scheme)).toString(),
                                getSchemeCalendar(year, month, day, 0xFF40db25, Integer.toString(scheme)));
                    }
                    calendarView.setSchemeDate(map);

                    tvContinuous.setText(String.valueOf(calendarInfo.getContinuous()));
                    tvTotal.setText(String.valueOf(calendarInfo.getTotal()));
                }
            }

            @Override
            public void onFailure(Call<Data<CalendarInfo>> call, Throwable t) {

            }
        });
    }

//    private void getHotList() {
//        Call<Data<PageInfo<Hot>>> hotCall = studyService.getHotList(1, 8);
//        hotCall.enqueue(new Callback<Data<PageInfo<Hot>>>() {
//            @Override
//            public void onResponse(Call<Data<PageInfo<Hot>>> call, Response<Data<PageInfo<Hot>>> response) {
//                if (response.body() != null && response.body().getCode() == 10000) {
//                    hotList = response.body().getData().getList();
//                    hotAdapter.setList(hotList);
//                    hotAdapter.notifyDataSetChanged();
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Data<PageInfo<Hot>>> call, Throwable t) {
//                System.out.println("请求未完成");
//            }
//        });
//    }
}