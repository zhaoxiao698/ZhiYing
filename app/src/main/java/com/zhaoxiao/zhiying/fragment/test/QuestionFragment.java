package com.zhaoxiao.zhiying.fragment.test;

import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.test.QuestionActivity;
import com.zhaoxiao.zhiying.adapter.test.MyTestAdapter;
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
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.view.FixedViewPager;

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
    private QuestionM question;
    RelativeLayout.LayoutParams layoutParams;
    private int position;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private float anchorPoint;

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
//        fab.hide();
        setPanelState(SlidingUpPanelLayout.PanelState.HIDDEN);
//        this.viewEmpty.setVisibility(View.GONE);

//        if (question instanceof CarefulM) {
            main.setText(Html.fromHtml(question.getInfo()));
//        }
//        slidingLayout.setAnchorPoint(0.5f);
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
            tvQuestionNum.setText("/"+question.getSubQuestionNum());

            if (question instanceof ListeningM) {
                for (ListeningQuestion listeningQuestion : ((ListeningM) question).getListeningQuestionList()) {
                    mFragments.add(SubQuestionFragment.newInstance(listeningQuestion,4));
                }
            } else if (question instanceof BankedM) {
                for (BankedQuestion bankedQuestion : ((BankedM) question).getBankedQuestionList()) {
                    mFragments.add(SubQuestionFragment.newInstance(bankedQuestion,4));
                }
            } else if (question instanceof MatchM) {
                for (MatchQuestion matchQuestion : ((MatchM) question).getMatchQuestionList()) {
                    mFragments.add(SubQuestionFragment.newInstance(matchQuestion,((MatchM) question).getNum()));
                }
            } else if (question instanceof CarefulM) {
                for (CarefulQuestion carefulQuestion : ((CarefulM) question).getCarefulQuestionList()) {
                    mFragments.add(SubQuestionFragment.newInstance(carefulQuestion,4));
                }
            } else if (question instanceof ClozeM) {
                for (ClozeQuestion clozeQuestion : ((ClozeM) question).getClozeQuestionList()) {
                    mFragments.add(SubQuestionFragment.newInstance(clozeQuestion,4));
                }
            } else if (question instanceof NewM) {
                for (NewQuestion newQuestion : ((NewM) question).getNewQuestionList()) {
                    mFragments.add(SubQuestionFragment.newInstance(newQuestion,7));
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
                    float anchorPoint = ((SubQuestionFragment)mFragments.get(position)).getHeight();
                    if(Math.abs(QuestionFragment.this.anchorPoint-anchorPoint)>0.01){
                        QuestionFragment.this.anchorPoint=anchorPoint;
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
        if (mFragments.size()<1){
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

    @OnClick({R.id.fab, R.id.btn_hide})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab:
                float anchorPoint = ((SubQuestionFragment)mFragments.get(position)).getHeight();
                if(Math.abs(this.anchorPoint-anchorPoint)>0.01){
                    this.anchorPoint=anchorPoint;
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
        }
    }
}