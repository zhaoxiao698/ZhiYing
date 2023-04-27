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

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.api.WordService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.word.WordSimple;
import com.zhaoxiao.zhiying.fragment.word.WordDetailFragment;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordDetailActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.iv_collection)
    ImageView ivCollection;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.ll_back)
    LinearLayout llBack;

    private WordSimple wordSimple;
    private FragmentManager manager;
    private WordService wordService;

    private String account;
    private String bookId;

    //    private String account;

    private long startTime;
    private long endTime;
    private long duration;
    private UserService userService;

    @Override
    protected int initLayout() {
        return R.layout.activity_word_detail;
    }

    @Override
    protected void initData() {
        wordSimple = (WordSimple) getIntent().getSerializableExtra("wordSimple");

        account = SpUtils.getInstance(this).getString("account", "");
        bookId = getStringFromSp("word_bookId", true);

        manager = getSupportFragmentManager();
        WordDetailFragment fragment2 = WordDetailFragment.newInstance(wordSimple);
        FragmentTransaction transaction2 = manager.beginTransaction();
        transaction2.add(R.id.fl_content, fragment2).commit();

        wordService = (WordService) getService(WordService.class);

        boolean isCollected = wordSimple.getCollect();
        if(isCollected){
            ivCollection.setImageResource(R.drawable.star1_community);
        } else {
            ivCollection.setImageResource(R.drawable.star_community);
        }

        //添加单词记录
//        account = SpUtils.getInstance(this).getString("account", "");
        if (!account.equals("") && !account.equals("已过期")) {
//            addWordRecord(account, wordId);
            userService = (UserService) getService(UserService.class);
        }
    }

    @OnClick({R.id.iv_back, R.id.iv_collection, R.id.ll_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
            case R.id.ll_back:
                finish();
                break;
            case R.id.iv_collection:
                boolean isCollected = wordSimple.getCollect();
                collect(!isCollected);
                break;
        }
    }

    private void collect(boolean collect) {
        Call<Data<Boolean>> collectCall = wordService.collect(account, wordSimple.getWordId(), bookId, collect);
        collectCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        wordSimple.setCollect(collect);
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

    @Override
    protected void onStart() {
        super.onStart();
        startTime = System.currentTimeMillis();
    }

    @Override
    protected void onStop() {
        super.onStop();
        endTime = System.currentTimeMillis();
        duration = endTime - startTime;
        if (!account.equals("") && !account.equals("已过期")) {
            addPlanDo();
        }
    }

    private void addPlanDo(){
        Call<Data<Boolean>> addPlanDoCall = userService.addPlanDo(account, duration);
        addPlanDoCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        System.out.println("添加学习记录："+duration+"ms");
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }
}