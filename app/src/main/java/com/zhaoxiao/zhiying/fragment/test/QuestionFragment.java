package com.zhaoxiao.zhiying.fragment.test;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.Looper;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.flexbox.FlexboxLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.squareup.picasso.Picasso;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.test.QuestionActivity;
import com.zhaoxiao.zhiying.adapter.test.MyTestAdapter;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.entity.test.BankedM;
import com.zhaoxiao.zhiying.entity.test.BankedQuestion;
import com.zhaoxiao.zhiying.entity.test.CarefulM;
import com.zhaoxiao.zhiying.entity.test.CarefulQuestion;
import com.zhaoxiao.zhiying.entity.test.ClozeM;
import com.zhaoxiao.zhiying.entity.test.ClozeQuestion;
import com.zhaoxiao.zhiying.entity.test.ListeningM;
import com.zhaoxiao.zhiying.entity.test.ListeningQuestion;
import com.zhaoxiao.zhiying.entity.test.MatchM;
import com.zhaoxiao.zhiying.entity.test.MatchQuestion;
import com.zhaoxiao.zhiying.entity.test.NewM;
import com.zhaoxiao.zhiying.entity.test.NewQuestion;
import com.zhaoxiao.zhiying.entity.test.QuestionM;
import com.zhaoxiao.zhiying.entity.test.WritingM;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.fragment.study.ListenFragment;
import com.zhaoxiao.zhiying.fragment.word.SpeechFragment;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;
import com.zhaoxiao.zhiying.view.FixedViewPager;
import com.zhaoxiao.zhiying.view.MyVideoView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

public class QuestionFragment extends BaseFragment {

    @BindView(R.id.main)
    TextView main;
    @BindView(R.id.btn_hide)
    Button btnHide;
    @BindView(R.id.sliding_layout)
    SlidingUpPanelLayout slidingLayout;
    @BindView(R.id.iv_sliding)
    ImageView ivSliding;
    @BindView(R.id.fab)
    FloatingActionButton fab;
    @BindView(R.id.sv_main)
    NestedScrollView svMain;
    @BindView(R.id.view_empty)
    View viewEmpty;
    @BindView(R.id.tv_question_current)
    TextView tvQuestionCurrent;
    @BindView(R.id.tv_question_num)
    TextView tvQuestionNum;
    @BindView(R.id.viewPager)
    FixedViewPager viewPager;
    @BindView(R.id.iv_play)
    ImageView ivPlay;
    @BindView(R.id.tv_progress)
    TextView tvProgress;
    @BindView(R.id.sb)
    SeekBar sb;
    @BindView(R.id.tv_total)
    TextView tvTotal;
    @BindView(R.id.ll_listening)
    LinearLayout llListening;
    @BindView(R.id.fl_banked)
    FlexboxLayout flBanked;
    @BindView(R.id.ll_cloze)
    LinearLayout llCloze;
    @BindView(R.id.dragView)
    LinearLayout dragView;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    private QuestionM question;
    RelativeLayout.LayoutParams layoutParams;
    private int position;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private float anchorPoint;

    private TestListeningFragment testListeningFragment;
    private SimpleDateFormat time = new SimpleDateFormat("mm:ss");
    private Handler mHandler = new Handler(Looper.getMainLooper());

    public QuestionFragment() {
    }

    public static QuestionFragment newInstance(QuestionM question) {
        QuestionFragment fragment = new QuestionFragment();
        fragment.question = question;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_question;
    }

    @Override
    protected void initData() {
        if (getActivity() instanceof QuestionActivity) {
            testListeningFragment = ((QuestionActivity) getActivity()).getTestListeningFragment();
        }

//        fab.hide();
        setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
//        this.viewEmpty.setVisibility(View.GONE);

//        if (question instanceof CarefulM) {
        if (!StringUtils.isEmpty(question.getInfo())) {
            main.setText(Html.fromHtml(question.getInfo()));
        }
//        }
//        slidingLayout.setAnchorPoint(0.5f);
        if (question instanceof ListeningM) {
            llListening.setVisibility(View.VISIBLE);
        } else if (question instanceof BankedM) {
            flBanked.removeAllViews();
            if (((BankedM) question).getWordList() != null) {
                for (int i = 0; i < ((BankedM) question).getWordList().length; i++) {
                    ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_test_option, null);
                    TextView child = viewGroup.findViewById(R.id.tv_test_option);
                    viewGroup.removeView(child);
                    char text = (char) ('A' + i);
                    child.setText(text + ") " + ((BankedM) question).getWordList()[i]);
                    flBanked.addView(child);
                }
            }
        } else if (question instanceof NewM) {
            llCloze.removeAllViews();
            for (int i = 0; i < 7; i++) {
                ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_test_option, null);
                TextView child = viewGroup.findViewById(R.id.tv_test_option);
                viewGroup.removeView(child);
                char text = (char) ('A' + i);
                switch (i) {
                    case 0:
                        child.setText(text + ") " + ((NewM) question).getA());
                        break;
                    case 1:
                        child.setText(text + ") " + ((NewM) question).getB());
                        break;
                    case 2:
                        child.setText(text + ") " + ((NewM) question).getC());
                        break;
                    case 3:
                        child.setText(text + ") " + ((NewM) question).getD());
                        break;
                    case 4:
                        child.setText(text + ") " + ((NewM) question).getE());
                        break;
                    case 5:
                        child.setText(text + ") " + ((NewM) question).getF());
                        break;
                    case 6:
                        child.setText(text + ") " + ((NewM) question).getG());
                        break;
                }
                llCloze.addView(child);
            }
        } else if (question instanceof WritingM && !StringUtils.isEmpty(((WritingM) question).getImg())){
            ivImg.setVisibility(View.VISIBLE);
            Picasso.with(getContext())
                    .load(ApiConfig.BASE_URl+ ((WritingM) question).getImg())
                    .transform(new CircleCornerTransForm())
                    .into(ivImg);
        }
        slidingLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {

            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                switch (newState) {
                    case COLLAPSED:
                        ivSliding.setImageResource(R.drawable.up_test);
                        break;
                    case EXPANDED:
                    case ANCHORED:
                        ivSliding.setImageResource(R.drawable.down_test);
                        break;
                    default:
                        break;
                }
            }
        });

        //设置子题目
        if (question.getSubQuestionNum() > 0) {
            tvQuestionCurrent.setText(String.valueOf(position + 1));
            tvQuestionNum.setText("/" + question.getSubQuestionNum());

            if (question instanceof ListeningM) {
                for (ListeningQuestion listeningQuestion : ((ListeningM) question).getListeningQuestionList()) {
                    mFragments.add(SubQuestionFragment.newInstance(listeningQuestion, 4));
                }
            } else if (question instanceof BankedM) {
                for (BankedQuestion bankedQuestion : ((BankedM) question).getBankedQuestionList()) {
                    mFragments.add(SubQuestionFragment.newInstance(bankedQuestion, 15));
                }
            } else if (question instanceof MatchM) {
                for (MatchQuestion matchQuestion : ((MatchM) question).getMatchQuestionList()) {
                    mFragments.add(SubQuestionFragment.newInstance(matchQuestion, ((MatchM) question).getNum()));
                }
            } else if (question instanceof CarefulM) {
                for (CarefulQuestion carefulQuestion : ((CarefulM) question).getCarefulQuestionList()) {
                    mFragments.add(SubQuestionFragment.newInstance(carefulQuestion, 4));
                }
            } else if (question instanceof ClozeM) {
                for (ClozeQuestion clozeQuestion : ((ClozeM) question).getClozeQuestionList()) {
                    mFragments.add(SubQuestionFragment.newInstance(clozeQuestion, 4));
                }
            } else if (question instanceof NewM) {
                for (NewQuestion newQuestion : ((NewM) question).getNewQuestionList()) {
                    mFragments.add(SubQuestionFragment.newInstance(newQuestion, 7));
                }
            }

            viewPager.setOffscreenPageLimit(mFragments.size());
            viewPager.setAdapter(new MyTestAdapter(getChildFragmentManager(), mFragments));
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    QuestionFragment.this.position = position;
                    tvQuestionCurrent.setText(String.valueOf(position + 1));
                    float anchorPoint = ((SubQuestionFragment) mFragments.get(position)).getHeight();
                    if (Math.abs(QuestionFragment.this.anchorPoint - anchorPoint) > 0.01) {
                        QuestionFragment.this.anchorPoint = anchorPoint;
                        slidingLayout.setAnchorPoint(anchorPoint);
                    }
//                    slidingLayout.setAnchorPoint(anchorPoint);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else {
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            fab.hide();
        }
        if (mFragments.size() < 1) {
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
            fab.hide();
        }
    }

    public boolean onBackPressed() {
        if (slidingLayout != null &&
                (slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED || slidingLayout.getPanelState() == SlidingUpPanelLayout.PanelState.ANCHORED)) {
            slidingLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            return false;
        }
        return true;
    }

    public void setPanelState(SlidingUpPanelLayout.PanelState panelState) {
        if (slidingLayout.getPanelState() != panelState) {
            slidingLayout.setPanelState(panelState);
            if (panelState == SlidingUpPanelLayout.PanelState.HIDDEN) {
                fab.show();
                this.viewEmpty.setVisibility(View.GONE);
            } else {
                fab.hide();
                this.viewEmpty.setVisibility(View.VISIBLE);
            }
        }
    }

    @OnClick({R.id.fab, R.id.btn_hide,R.id.iv_play})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                float anchorPoint = ((SubQuestionFragment) mFragments.get(position)).getHeight();
                if (Math.abs(this.anchorPoint - anchorPoint) > 0.01) {
                    this.anchorPoint = anchorPoint;
                    slidingLayout.setAnchorPoint(anchorPoint);
                }
                setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
//                fab.hide();
//                layoutParams = (RelativeLayout.LayoutParams) sv.getLayoutParams();
//                layoutParams.setMargins(50, 0, 0, 50);
//                this.viewEmpty.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_hide:
                setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
//                fab.show();
//                layoutParams = (RelativeLayout.LayoutParams) sv.getLayoutParams();
//                layoutParams.setMargins(0, 0, 0, 0);
//                this.viewEmpty.setVisibility(View.GONE);
                break;
            case R.id.iv_play:
                System.out.println(testListeningFragment.getCurrentPlayState());
                if(testListeningFragment.isPlaying()){
                    testListeningFragment.pause();
                } else if(testListeningFragment.getCurrentPlayState() == MyVideoView.STATE_PAUSED){
                    testListeningFragment.resume();
                } else if(testListeningFragment.getCurrentPlayState() == MyVideoView.STATE_PLAYBACK_COMPLETED){
                    testListeningFragment.replay();
                } else if(testListeningFragment.getCurrentPlayState() == MyVideoView.STATE_IDLE) {
                    testListeningFragment.play(((ListeningM) question).getAudio());
                }
                break;
        }
    }

    public void setCurrentItem(int j) {
        if (j < 0) {
            return;
        }
        viewPager.setCurrentItem(j);
        float anchorPoint = ((SubQuestionFragment) mFragments.get(position)).getHeight();
        if (Math.abs(this.anchorPoint - anchorPoint) > 0.01) {
            this.anchorPoint = anchorPoint;
            slidingLayout.setAnchorPoint(anchorPoint);
        }
        setPanelState(SlidingUpPanelLayout.PanelState.ANCHORED);
    }

    //UI控制
    public void UIControl(String state) {
        switch (state) {
            case TestListeningFragment.ACTION_TEST_INIT:
                if (testListeningFragment != null) {
                    if (testListeningFragment.isPlaying()) {
                        ivPlay.setImageResource(R.drawable.pause1_yellow);
                    } else {
                        ivPlay.setImageResource(R.drawable.play1_yellow);
                    }
                    tvTotal.setText(time.format(testListeningFragment.getDuration()));
                    sb.setMax((int) testListeningFragment.getDuration());

                    sb.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                        @Override
                        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                            //这里很重要，如果不判断是否来自用户操作进度条，会不断执行下面语句块里面的逻辑，然后就会卡顿卡顿
                            if (fromUser) {
                                testListeningFragment.seekTo(seekBar.getProgress());
                            }
                            //进当滑动条到末端时，设置播放图标
                            if (progress == seekBar.getMax()/* || !mMyBinder.isPlaying()*/) {
                                ivPlay.setImageResource(R.drawable.play1_yellow);
                            }
                        }

                        @Override
                        public void onStartTrackingTouch(SeekBar seekBar) {

                        }

                        @Override
                        public void onStopTrackingTouch(SeekBar seekBar) {

                        }
                    });

                    mHandler.post(mRunnableTest);
                }
            case TestListeningFragment.ACTION_TEST_START:
                ivPlay.setImageResource(R.drawable.pause1_yellow);
                mHandler.post(mRunnableTest);
                break;
            case TestListeningFragment.ACTION_TEST_STOP:
            case TestListeningFragment.ACTION_TEST_COMPLETE:
                ivPlay.setImageResource(R.drawable.play1_yellow);
                mHandler.removeCallbacks(mRunnableTest);
                break;
            default:
                break;
        }
    }

    private final Runnable mRunnableTest = new Runnable() {
        @Override
        public void run() {
            int playPosition = (int) testListeningFragment.getCurrentPosition();
            sb.setProgress(playPosition);
            tvProgress.setText(time.format(playPosition));
            mHandler.postDelayed(mRunnableTest, 500);
        }
    };

    @Override
    public void onDestroyView() {
        mHandler.removeCallbacks(mRunnableTest);
        super.onDestroyView();
    }

    public void setImg(){
        ivPlay.setImageResource(R.drawable.play1_yellow);
        mHandler.removeCallbacks(mRunnableTest);
    }
}