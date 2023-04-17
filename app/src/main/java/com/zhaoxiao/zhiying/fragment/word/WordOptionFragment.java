package com.zhaoxiao.zhiying.fragment.word;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.word.WordSimple;
import com.zhaoxiao.zhiying.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.OnClick;

public class WordOptionFragment extends BaseFragment {
    @BindView(R.id.tv_word)
    TextView tvWord;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    private WordSimple wordSimple;
    @BindView(R.id.fl_speech)
    FrameLayout flSpeech;

    private SpeechFragment speechFragment;
//    private SpeechReceiver speechReceiver;

    private final String BASE_URL = "https://dict.youdao.com/dictvoice?audio=";

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
//        tvPhone.setText("美 /" + wordSimple.getUsphone() + "/");
        tvPhone.setText("英 /" + wordSimple.getUkphone() + "/");

        speechFragment = (SpeechFragment) getChildFragmentManager().findFragmentById(R.id.fl_speech);

//        //注册广播
//        speechReceiver = new SpeechReceiver();
//        IntentFilter intentFilter = new IntentFilter();
//        intentFilter.addAction(SpeechFragment.ACTION_SPEECH_INIT);
//        intentFilter.addAction(SpeechFragment.ACTION_SPEECH_START);
//        intentFilter.addAction(SpeechFragment.ACTION_SPEECH_STOP);
//        intentFilter.addAction(SpeechFragment.ACTION_SPEECH_COMPLETE);
//        getContext().registerReceiver(speechReceiver, intentFilter);

        //speechFragment初始化后开始播放
        speechFragment.setOnSpeechFragmentCreatedListener(() -> speechFragment.play(BASE_URL+wordSimple.getUkspeech()));
    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        //播放视频
//        speechFragment.play(BASE_URL + wordSimple.getUsspeech());
////        speechFragment.play("https://media.w3.org/2010/05/sintel/trailer.mp4");
////        speechFragment.play("http://downsc.chinaz.net/Files/DownLoad/sound1/201906/11582.mp3");
////        speechFragment.play("https://dict.youdao.com/dictvoice.mp3?audio=word");
////        String url = BASE_URL + wordSimple.getUsspeech();
////        speechFragment.play("https://dict.youdao.com/dictvoice.mp3?afford=word");
//    }

    @OnClick(R.id.tv_phone)
    public void onClick() {
        speechFragment.replay();
    }

//    public class SpeechReceiver extends BroadcastReceiver {
//
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            UIControl(intent.getAction());
//        }
//    }
//
//    private void UIControl(String action) {
//        System.out.println("单词音频-->" + action);
//    }


    public boolean onBackPressed(){
        return speechFragment.onBackPressed();
    }
}