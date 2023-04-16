package com.zhaoxiao.zhiying.fragment.word;

import android.widget.TextView;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.word.WordSimple;
import com.zhaoxiao.zhiying.fragment.BaseFragment;

import butterknife.BindView;

public class WordOptionFragment extends BaseFragment {
    @BindView(R.id.tv_word)
    TextView tvWord;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    private WordSimple wordSimple;

    public WordOptionFragment() {
    }

    public static WordOptionFragment newInstance(WordSimple wordSimple) {
        WordOptionFragment fragment = new WordOptionFragment();
        fragment.wordSimple = wordSimple;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_word_option;
    }

    @Override
    protected void initData() {
        tvWord.setText(wordSimple.getWordHead());
        tvPhone.setText("美 /"+wordSimple.getUsphone()+"/");
    }
}