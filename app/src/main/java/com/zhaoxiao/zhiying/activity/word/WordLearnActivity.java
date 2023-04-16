package com.zhaoxiao.zhiying.activity.word;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.api.WordService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.study.PageInfo;
import com.zhaoxiao.zhiying.entity.word.WordSimple;
import com.zhaoxiao.zhiying.fragment.word.WordDetailFragment;
import com.zhaoxiao.zhiying.fragment.word.WordOptionFragment;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordLearnActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_current_num)
    TextView tvCurrentNum;
    @BindView(R.id.tv_slip)
    TextView tvSlip;
    @BindView(R.id.tv_total_num)
    TextView tvTotalNum;
    @BindView(R.id.iv_collection)
    ImageView ivCollection;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.hpv)
    HorizontalProgressView hpv;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.ll_know)
    LinearLayout llKnow;
    @BindView(R.id.ll_vague)
    LinearLayout llVague;
    @BindView(R.id.ll_not_know)
    LinearLayout llNotKnow;
    @BindView(R.id.ll_next)
    LinearLayout llNext;
    @BindView(R.id.ll_wrong)
    LinearLayout llWrong;
    @BindView(R.id.tv_next)
    TextView tvNext;

    private WordService wordService;
    private List<WordSimple> wordSimpleList;
    private int type;
    float currentProgress;
    private int position;
    private FragmentManager manager;
    private int proficiency;

    private String account;
    private int num;
    private String bookId;

    @Override
    protected int initLayout() {
        return R.layout.activity_word_learn;
    }

    @Override
    protected void initData() {
        type = (int) getIntent().getSerializableExtra("type");

        account = SpUtils.getInstance(this).getString("account", "");
        num = getStringFromSp("word_group", false);
        bookId = getStringFromSp("word_bookId", true);

//        tvTotalNum.setText(String.valueOf(num));

        wordService = (WordService) getService(WordService.class);

        manager = getSupportFragmentManager();

        switch (type) {
            case 0:
                getLearnList();
                break;
            case 1:
                getReviewList();
                break;
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_collection, R.id.ll_know, R.id.ll_vague, R.id.ll_not_know, R.id.ll_next, R.id.ll_wrong})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.iv_collection:
//                XToastUtils.toast("收藏");
                boolean isCollected = wordSimpleList.get(position).getCollect();
                collect(!isCollected);
                break;
            case R.id.ll_know:
                proficiency = 1;
                llOption(1);
                break;
            case R.id.ll_vague:
                proficiency = 0;
                llOption(1);
                break;
            case R.id.ll_not_know:
                proficiency = -1;
                llOption(2);
                break;
            case R.id.ll_next:
//                if (position == wordSimpleList.size()-1) {
//                    XToastUtils.toast("完成1");
//                } else {
                    addWordRecord();
//                }
                break;
            case R.id.ll_wrong:
//                if (position == wordSimpleList.size()-1) {
//                    XToastUtils.toast("记错了1");
//                } else {
                    proficiency = -1;
                    addWordRecord();
//                }
                break;
        }
    }

    private void llOption(int type) {
        currentProgress = (float) (position + 1) / wordSimpleList.size() * 100;
        hpv.setProgress(currentProgress);
        boolean isCollected = wordSimpleList.get(position).getCollect();
        if(isCollected){
            ivCollection.setImageResource(R.drawable.star1_community);
        } else {
            ivCollection.setImageResource(R.drawable.star_community);
        }
        switch (type) {
            case 0:
                llKnow.setVisibility(View.VISIBLE);
                llVague.setVisibility(View.VISIBLE);
                llNotKnow.setVisibility(View.VISIBLE);

                llNext.setVisibility(View.GONE);
                llWrong.setVisibility(View.GONE);

                WordOptionFragment fragment0 = WordOptionFragment.newInstance(wordSimpleList.get(position));
                FragmentTransaction transaction0 = manager.beginTransaction();
                transaction0.replace(R.id.fl_content, fragment0).commit();
                break;
            case 1:
                llKnow.setVisibility(View.GONE);
                llVague.setVisibility(View.GONE);
                llNotKnow.setVisibility(View.GONE);

                llNext.setVisibility(View.VISIBLE);
                llWrong.setVisibility(View.VISIBLE);

                WordDetailFragment fragment1 = WordDetailFragment.newInstance(wordSimpleList.get(position));
                FragmentTransaction transaction1 = manager.beginTransaction();
                transaction1.replace(R.id.fl_content, fragment1).commit();
                break;
            case 2:
                llKnow.setVisibility(View.GONE);
                llVague.setVisibility(View.GONE);
                llNotKnow.setVisibility(View.GONE);

                llNext.setVisibility(View.VISIBLE);
                llWrong.setVisibility(View.GONE);

                WordDetailFragment fragment2 = WordDetailFragment.newInstance(wordSimpleList.get(position));
                FragmentTransaction transaction2 = manager.beginTransaction();
                transaction2.replace(R.id.fl_content, fragment2).commit();
                break;
        }
    }

    private void getReviewList() {
        Call<Data<PageInfo<WordSimple>>> reviewCall = wordService.getReviewList(account, num, bookId);
        reviewCall.enqueue(new Callback<Data<PageInfo<WordSimple>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<WordSimple>>> call, Response<Data<PageInfo<WordSimple>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    wordSimpleList = response.body().getData().getList();
                    tvTotalNum.setText(String.valueOf(response.body().getData().getTotal()));
                    llOption(0);
                }
            }

            @Override
            public void onFailure(Call<Data<PageInfo<WordSimple>>> call, Throwable t) {

            }
        });
    }

    private void getLearnList() {
//        String account = SpUtils.getInstance(this).getString("account","");
//        int num = getStringFromSp("word_group",false);
//        String bookId = getStringFromSp("word_bookId",true);
        Call<Data<PageInfo<WordSimple>>> learnCall = wordService.getLearnList(account, num, bookId);
        learnCall.enqueue(new Callback<Data<PageInfo<WordSimple>>>() {
            @Override
            public void onResponse(Call<Data<PageInfo<WordSimple>>> call, Response<Data<PageInfo<WordSimple>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    wordSimpleList = response.body().getData().getList();
                    tvTotalNum.setText(String.valueOf(response.body().getData().getTotal()));
                    llOption(0);
                }
            }

            @Override
            public void onFailure(Call<Data<PageInfo<WordSimple>>> call, Throwable t) {

            }
        });
    }

//    private void setData(){
//        currentProgress = (float) (position+1)/wordSimpleList.size()*100;
//        hpv.setProgress(currentProgress);
//
//        WordOptionFragment fragment0 = WordOptionFragment.newInstance(wordSimpleList.get(0));
//        FragmentTransaction transaction0 = manager.beginTransaction();
//        transaction0.add(R.id.fl_content, fragment0).commit();
//    }

    private void addWordRecord() {
        String wordId = wordSimpleList.get(position).getWordId();
        Call<Data<Map<String,Integer>>> addWordRecordCall = wordService.addWordRecord(account, wordId, proficiency, bookId);
        addWordRecordCall.enqueue(new Callback<Data<Map<String,Integer>>>() {
            @Override
            public void onResponse(Call<Data<Map<String,Integer>>> call, Response<Data<Map<String,Integer>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    Map<String,Integer> mapResponse = response.body().getData();
                    wordSimpleList.get(position).setProficiency(mapResponse.get("proficiency"));
                    if (mapResponse.get("nextDayNum")==null){
                        wordSimpleList.get(position).setNextDayNum(-1);
                    }else {
                        wordSimpleList.get(position).setNextDayNum(mapResponse.get("nextDayNum"));
                    }
                    position++;
                    if (position == wordSimpleList.size()-1) {
                        tvCurrentNum.setText(String.valueOf(position + 1));
                        llOption(0);
                        tvNext.setText("完成");
//                        tvNext.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                XToastUtils.toast("完成");
//                            }
//                        });

//                        llNext.setOnClickListener(view -> XToastUtils.toast("完成"));
//                        llWrong.setOnClickListener(view -> XToastUtils.toast("记错了"));
                    } else if (position > wordSimpleList.size()-1){
                        finish();
                        Map<String,Object> map = new HashMap<>();
                        map.put("wordSimpleList",wordSimpleList);
                        map.put("type",type);
                        navigateTo(WordCompleteActivity.class,"map", (Serializable) map);
                    } else {
                        tvCurrentNum.setText(String.valueOf(position + 1));
                        llOption(0);
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Map<String,Integer>>> call, Throwable t) {

            }
        });
    }

    private void collect(boolean collect) {
        Call<Data<Boolean>> collectCall = wordService.collect(account, wordSimpleList.get(position).getWordId(), bookId, collect);
        collectCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        wordSimpleList.get(position).setCollect(collect);
                        if (collect){
                            ivCollection.setImageResource(R.drawable.star1_community);
                        } else {
                            ivCollection.setImageResource(R.drawable.star_community);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }
}