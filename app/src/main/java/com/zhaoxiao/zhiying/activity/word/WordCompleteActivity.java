package com.zhaoxiao.zhiying.activity.word;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.adapter.word.WordCompleteAdapter;
import com.zhaoxiao.zhiying.api.WordService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.word.WordSimple;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordCompleteActivity extends BaseActivity {

    @BindView(R.id.tv_text1)
    TextView tvText1;
    @BindView(R.id.tv_num)
    TextView tvNum;
    @BindView(R.id.tv_text2)
    TextView tvText2;
    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.btn_rest)
    RoundButton btnRest;
    @BindView(R.id.btn_continue)
    RoundButton btnContinue;

    private Map<String,Object> map;
    private List<WordSimple> wordSimpleList;
    private int type;
    private WordService wordService;

    private WordCompleteAdapter wordCompleteAdapter;
    private LinearLayoutManager linearLayoutManager;

    @Override
    protected int initLayout() {
        return R.layout.activity_word_complete;
    }

    @Override
    protected void initData() {
        map = (Map<String, Object>) getIntent().getSerializableExtra("map");
        wordSimpleList = (List<WordSimple>) map.get("wordSimpleList");
        type = (int) map.get("type");

        switch (type){
            case 0:
                tvText1.setText("本组单词学习完成");
                tvText2.setText(" 个单词需要学习");
                btnContinue.setText("继续学习");
                break;
            case 1:
                tvText1.setText("本组单词复习完成");
                tvText2.setText(" 个单词需要复习");
                btnContinue.setText("继续复习");
                break;
        }

        linearLayoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        rv.setLayoutManager(linearLayoutManager);
        wordCompleteAdapter = new WordCompleteAdapter(this,wordSimpleList);
        rv.setAdapter(wordCompleteAdapter);

        wordService = (WordService) getService(WordService.class);
        getNeedNum();
    }

    private void getNeedNum() {
        String account = SpUtils.getInstance(this).getString("account","");
        String bookId = getStringFromSp("word_bookId",true);
        Call<Data<Map<String, Integer>>> needNumCall = wordService.getNeedNum(account, bookId);
        needNumCall.enqueue(new Callback<Data<Map<String, Integer>>>() {
            @Override
            public void onResponse(Call<Data<Map<String, Integer>>> call, Response<Data<Map<String, Integer>>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    Map<String,Integer> map = response.body().getData();
                    if (type==0){
                        tvNum.setText(String.valueOf(map.get("learnNum")));
                    } else if (type==1){
                        tvNum.setText(String.valueOf(map.get("reviewNum")));
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Map<String, Integer>>> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.btn_rest, R.id.btn_continue})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_rest:
                finish();
                break;
            case R.id.btn_continue:
                finish();
                navigateTo(WordLearnActivity.class,"type",type);
                break;
        }
    }
}