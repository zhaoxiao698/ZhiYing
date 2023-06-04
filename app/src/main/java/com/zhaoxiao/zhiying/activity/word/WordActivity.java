package com.zhaoxiao.zhiying.activity.word;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.progress.HorizontalProgressView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.mine.CodeLoginActivity;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.WordService;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.word.BookMore;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WordActivity extends BaseActivity {
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_learned)
    TextView tvLearned;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.hpv)
    HorizontalProgressView hpv;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.tv_plan)
    TextView tvPlan;
    @BindView(R.id.card_plan)
    CardView cardPlan;
    @BindView(R.id.card_collection)
    CardView cardCollection;
    @BindView(R.id.card_setting)
    CardView cardSetting;
    @BindView(R.id.tv_word_list)
    TextView tvWordList;
    @BindView(R.id.tv_change_book)
    TextView tvChangeBook;
    @BindView(R.id.card_learn)
    CardView cardLearn;
    @BindView(R.id.card_review)
    CardView cardReview;
    @BindView(R.id.tv_plan_do)
    TextView tvPlanDo;
    @BindView(R.id.tv_learn)
    TextView tvLearn;
    @BindView(R.id.tv_review)
    TextView tvReview;

    private WordService wordService;
    private BookMore bookMore;

    @Override
    protected int initLayout() {
        return R.layout.activity_word;
    }

    @Override
    protected void initData() {
        wordService = (WordService) getService(WordService.class);
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshData();
    }

    private void refreshData() {
        String account = SpUtils.getInstance(this).getString("account", "");
        if (StringUtils.isEmpty(account) || account.equals("已过期")) {
            finish();
            navigateTo(CodeLoginActivity.class);
        } else {
            String bookId = getStringFromSp("word_bookId", true);
            if (StringUtils.isEmpty(bookId)) {
                navigateTo(SelectBookActivity.class);
            } else {
                getBook(account, bookId);
//                XToastUtils.toast("刷新数据");
            }
        }
    }

    private void getBook(String account, String bookId) {
        Call<Data<BookMore>> bookCall = wordService.getBookMore(account, bookId);
        bookCall.enqueue(new Callback<Data<BookMore>>() {
            @Override
            public void onResponse(Call<Data<BookMore>> call, Response<Data<BookMore>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    bookMore = response.body().getData();
                    tvName.setText(bookMore.getBook().getName());
                    tvLearned.setText("已学习：" + (bookMore.getBook().getNum() - bookMore.getLearnNum()));
                    tvTotal.setText("总词数：" + bookMore.getBook().getNum());
                    hpv.setProgress((float) (bookMore.getBook().getNum() - bookMore.getLearnNum()) / bookMore.getBook().getNum() * 100);
                    int word_plan = getStringFromSp("word_plan", false);
                    tvPlan.setText(String.valueOf(word_plan));
                    if (bookMore.getLearnedNum()==null){
                        tvPlanDo.setText(String.valueOf(0));
                    } else {
                        tvPlanDo.setText(String.valueOf(bookMore.getLearnedNum().getNum()));
                    }
                    tvLearn.setText(String.valueOf(bookMore.getLearnNum()));
                    tvReview.setText(String.valueOf(bookMore.getReviewNum()));
                    Picasso.with(mContext)
                            .load(bookMore.getBook().getImg())
                            .transform(new CircleCornerTransForm())
                            .into(ivImg);
                }
            }

            @Override
            public void onFailure(Call<Data<BookMore>> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.iv_back, R.id.tv_word_list, R.id.tv_change_book, R.id.card_collection, R.id.card_setting, R.id.card_learn, R.id.card_review})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_word_list:
//                String bookId = getStringFromSp("word_bookId",true);
                navigateTo(WordListActivity.class,"bookName",bookMore.getBook().getName());
//                XToastUtils.toast("单词列表");
                break;
            case R.id.tv_change_book:
                navigateTo(SelectBookActivity.class);
                break;
            case R.id.card_collection:
//                XToastUtils.toast("收藏夹");
                navigateTo(WordListActivity.class,"bookName","收藏");
                break;
            case R.id.card_setting:
                navigateTo(SetPlanActivity.class);
                break;
            case R.id.card_learn:
//                XToastUtils.toast("学习");
                navigateTo(WordLearnActivity.class,"type",0);
                break;
            case R.id.card_review:
//                XToastUtils.toast("复习");
                navigateTo(WordLearnActivity.class,"type",1);
                break;
        }
    }
}