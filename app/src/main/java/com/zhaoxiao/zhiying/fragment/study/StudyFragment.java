package com.zhaoxiao.zhiying.fragment.study;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xuexiang.xui.widget.banner.anim.select.ZoomInEnter;
import com.xuexiang.xui.widget.banner.widget.banner.BannerItem;
import com.xuexiang.xui.widget.banner.widget.banner.base.BaseBanner;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.study.ArticleActivity;
import com.zhaoxiao.zhiying.activity.study.CalendarActivity;
import com.zhaoxiao.zhiying.activity.study.ChannelActivity;
import com.zhaoxiao.zhiying.activity.study.FtypeActivity;
import com.zhaoxiao.zhiying.activity.study.HotActivity;
import com.zhaoxiao.zhiying.activity.study.RecentActivity;
import com.zhaoxiao.zhiying.activity.study.SortActivity;
import com.zhaoxiao.zhiying.activity.word.WordActivity;
import com.zhaoxiao.zhiying.adapter.study.HotAdapter;
import com.zhaoxiao.zhiying.adapter.study.RecentAdapter;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.entity.study.Banner;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.Hot;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.entity.study.Recent;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.view.SearchBarView;
import com.zhaoxiao.zhiying.widget.RadiusImageBanner;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import com.zhaoxiao.zhiying.view.TitleBarView;

public class StudyFragment extends BaseFragment implements BaseBanner.OnItemClickListener<BannerItem> {

    @BindView(R.id.search_bar_view)
    SearchBarView searchBarView;
    @BindView(R.id.rib_simple_usage)
    RadiusImageBanner rib_simple_usage;
    @BindView(R.id.rl_recent)
    RelativeLayout rlRecent;
    @BindView(R.id.rv_recent)
    RecyclerView rvRecent;
    @BindView(R.id.rl_hot)
    RelativeLayout rlHot;
    @BindView(R.id.rv_hot)
    RecyclerView rvHot;
    @BindView(R.id.ll_all)
    LinearLayout llAll;
    @BindView(R.id.ll_radio)
    LinearLayout llRadio;
    @BindView(R.id.ll_listen)
    LinearLayout llListen;
    @BindView(R.id.ll_exam)
    LinearLayout llExam;
    @BindView(R.id.ll_speech)
    LinearLayout llSpeech;
    @BindView(R.id.ll_travel)
    LinearLayout llTravel;
    @BindView(R.id.ll_work)
    LinearLayout llWork;
    @BindView(R.id.ll_book)
    LinearLayout llBook;
    @BindView(R.id.ll_speak)
    LinearLayout llSpeak;
    @BindView(R.id.ll_tv)
    LinearLayout llTv;
    @BindView(R.id.srl)
    SmartRefreshLayout srl;

    private List<BannerItem> mData;

    private List<Recent> recentList;
    private RecentAdapter recentAdapter;
    private GridLayoutManager gridLayoutManagerRecent;

    private List<Hot> hotList;
    private HotAdapter hotAdapter;
    private GridLayoutManager gridLayoutManagerHot;

    private Retrofit retrofit;
    private StudyService studyService;

    private List<Banner> bannerList;

    private int refreshCount;

    public StudyFragment() {
    }

    public static StudyFragment newInstance() {
        return new StudyFragment();
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_study;
    }

    @Override
    protected void initData() {
        //刷新和加载
        srl.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull @NotNull RefreshLayout refreshLayout) {
                refreshCount=0;
                request();
            }
        });

        retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        studyService = retrofit.create(StudyService.class);

        gridLayoutManagerRecent = new GridLayoutManager(getContext(), 2, GridLayoutManager.HORIZONTAL, false);
        rvRecent.setLayoutManager(gridLayoutManagerRecent);
        recentAdapter = new RecentAdapter(getActivity(), R.layout.item_recent_layout);
        rvRecent.setAdapter(recentAdapter);
        recentAdapter.setOnItemClickListener(recentId -> navigateTo(ArticleActivity.class,"articleId",recentId));
//        StartSnapHelper startSnapHelper = new StartSnapHelper();
//        startSnapHelper.attachToRecyclerView(rv);

        gridLayoutManagerHot = new GridLayoutManager(getContext(), 3);
        rvHot.setLayoutManager(gridLayoutManagerHot);
        hotAdapter = new HotAdapter(getActivity(), R.layout.item_hot_layout, false);
        rvHot.setAdapter(hotAdapter);
        hotAdapter.setOnItemClickListener(hotId -> navigateTo(ChannelActivity.class,"channelId",hotId));
//        StartSnapHelper startSnapHelper = new StartSnapHelper();
//        startSnapHelper.attachToRecyclerView(rv);

        refreshCount = 0;
        request();

        searchBarView.setOnViewClick(new SearchBarView.onViewClick() {
            @Override
            public void searchClick(View view) {
                showToast("搜索");
            }

            @Override
            public void rightTextClick(View view) {
                showToast("文字");
            }

            @Override
            public void rightDrawable1Click(View view) {
//                showToast("图标1");
                navigateTo(CalendarActivity.class);
            }

            @Override
            public void rightDrawable2Click(View view) {
//                showToast("图标2");
                navigateTo(WordActivity.class);
            }
        });
    }

    private void request() {
        //轮播图
        Call<Data<List<Banner>>> bannerCall = studyService.getBanner();
        bannerCall.enqueue(new Callback<Data<List<Banner>>>() {
            @Override
            public void onResponse(Call<Data<List<Banner>>> call, Response<Data<List<Banner>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    bannerList = response.body().getData();
                    mData = new ArrayList<>();
                    for (Banner banner : bannerList) {
                        BannerItem item = new BannerItem();
                        item.imgUrl = banner.getImg();
                        item.title = String.valueOf(banner.getId());

                        mData.add(item);
                    }
                    rib_simple_usage
                            .setSelectAnimClass(ZoomInEnter.class)
                            .setSource(mData)
//                .setTransformerClass(ZoomOutSlideTransformer.class)
                            .setOnItemClickListener(StudyFragment.this::onItemClick)
                            .startScroll();

                    if(++refreshCount==3) srl.finishRefresh();
                }
            }

            @Override
            public void onFailure(Call<Data<List<Banner>>> call, Throwable t) {

            }
        });

        //最近更新
        Call<Data<PageInfo<Recent>>> recentCall = studyService.getRecentList(1, 6);
        recentCall.enqueue(new Callback<Data<PageInfo<Recent>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<Recent>>> call, Response<Data<PageInfo<Recent>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    recentList = response.body().getData().getList();
                    recentAdapter.setList(recentList);
                    recentAdapter.notifyDataSetChanged();
                    System.out.println("recent成功");
                    if(++refreshCount==3) srl.finishRefresh();
                } else System.out.println("recent失败");
            }

            @Override
            public void onFailure(Call<Data<PageInfo<Recent>>> call, Throwable t) {
                System.out.println("recent请求未完成");
            }
        });

        //热门推荐
        Call<Data<PageInfo<Hot>>> hotCall = studyService.getHotList(1, 9);
        hotCall.enqueue(new Callback<Data<PageInfo<Hot>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<Hot>>> call, Response<Data<PageInfo<Hot>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    hotList = response.body().getData().getList();
                    hotAdapter.setList(hotList);
                    hotAdapter.notifyDataSetChanged();
                    System.out.println("hot成功");
                    if(++refreshCount==3) srl.finishRefresh();
                } else System.out.println("hot失败");
            }

            @Override
            public void onFailure(Call<Data<PageInfo<Hot>>> call, Throwable t) {
                System.out.println("hot请求未完成");
            }
        });
    }

    public static String[] titles = new String[]{
            "伪装者:胡歌演绎'痞子特工'",
            "无心法师:生死离别!月牙遭虐杀",
            "花千骨:尊上沦为花千骨",
            "综艺饭:胖轩偷看夏天洗澡掀波澜",
            "碟中谍4:阿汤哥高塔命悬一线,超越不可能",
    };

    public static String[] urls = new String[]{//640*360 360/640=0.5625
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144160323071011277.jpg",//伪装者:胡歌演绎"痞子特工"
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144158380433341332.jpg",//无心法师:生死离别!月牙遭虐杀
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144160286644953923.jpg",//花千骨:尊上沦为花千骨
            "http://photocdn.sohu.com/tvmobilemvms/20150902/144115156939164801.jpg",//综艺饭:胖轩偷看夏天洗澡掀波澜
            "http://photocdn.sohu.com/tvmobilemvms/20150907/144159406950245847.jpg",//碟中谍4:阿汤哥高塔命悬一线,超越不可能
    };

    public static List<BannerItem> getBannerList() {
        ArrayList<BannerItem> list = new ArrayList<>();
        for (int i = 0; i < urls.length; i++) {
            BannerItem item = new BannerItem();
            item.imgUrl = urls[i];
            item.title = titles[i];

            list.add(item);
        }

        return list;
    }

    @Override
    public void onDestroyView() {
        rib_simple_usage.recycle();
        super.onDestroyView();
    }

    @Override
    public void onItemClick(View view, BannerItem item, int position) {
//        XToastUtils.toast("position--->" + position + ", item:" + item.title);
        navigateTo(ArticleActivity.class,"articleId",Integer.parseInt(item.title));
    }

    @OnClick({R.id.rl_recent, R.id.rl_hot, R.id.ll_all,R.id.ll_radio, R.id.ll_listen, R.id.ll_exam, R.id.ll_speech, R.id.ll_travel, R.id.ll_work, R.id.ll_book, R.id.ll_speak, R.id.ll_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.rl_recent:
                navigateTo(RecentActivity.class);
                break;
            case R.id.rl_hot:
                navigateTo(HotActivity.class);
                break;
            case R.id.ll_all:
                navigateTo(FtypeActivity.class);
                break;
            case R.id.ll_radio:
                HashMap<String,Object> mapRadio = new HashMap<>();
                mapRadio.put("ftypeId",1);
                mapRadio.put("stypeId",0);
                navigateTo(SortActivity.class,"map", mapRadio);
                break;
            case R.id.ll_listen:
                HashMap<String,Object> mapListen = new HashMap<>();
                mapListen.put("ftypeId",3);
                mapListen.put("stypeId",0);
                navigateTo(SortActivity.class,"map", mapListen);
                break;
            case R.id.ll_exam:
                HashMap<String,Object> mapExam = new HashMap<>();
                mapExam.put("ftypeId",4);
                mapExam.put("stypeId",0);
                navigateTo(SortActivity.class,"map", mapExam);
                break;
            case R.id.ll_speech:
                HashMap<String,Object> mapSpeech = new HashMap<>();
                mapSpeech.put("ftypeId",6);
                mapSpeech.put("stypeId",46);
                navigateTo(SortActivity.class,"map", mapSpeech);
                break;
            case R.id.ll_travel:
                HashMap<String,Object> mapTravel = new HashMap<>();
                mapTravel.put("ftypeId",8);
                mapTravel.put("stypeId",58);
                navigateTo(SortActivity.class,"map", mapTravel);
                break;
            case R.id.ll_work:
                HashMap<String,Object> mapWork = new HashMap<>();
                mapWork.put("ftypeId",10);
                mapWork.put("stypeId",0);
                navigateTo(SortActivity.class,"map", mapWork);
                break;
            case R.id.ll_book:
                HashMap<String,Object> mapBook = new HashMap<>();
                mapBook.put("ftypeId",10);
                mapBook.put("stypeId",80);
                navigateTo(SortActivity.class,"map", mapBook);
                break;
            case R.id.ll_speak:
                HashMap<String,Object> mapSpeak = new HashMap<>();
                mapSpeak.put("ftypeId",12);
                mapSpeak.put("stypeId",0);
                navigateTo(SortActivity.class,"map", mapSpeak);
                break;
            case R.id.ll_tv:
                HashMap<String,Object> mapTv = new HashMap<>();
                mapTv.put("ftypeId",13);
                mapTv.put("stypeId",91);
                navigateTo(SortActivity.class,"map", mapTv);
                break;
        }
    }
}