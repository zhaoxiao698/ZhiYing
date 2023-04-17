package com.zhaoxiao.zhiying.fragment.word;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;

import com.squareup.picasso.Picasso;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.entity.word.WordSimple;
import com.zhaoxiao.zhiying.fragment.BaseFragment;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.UnitConversion;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import butterknife.BindView;
import butterknife.OnClick;

public class WordDetailFragment extends BaseFragment {
    @BindView(R.id.tv_word)
    TextView tvWord;
    @BindView(R.id.tv_usphone)
    TextView tvUsphone;
    @BindView(R.id.tv_ukphone)
    TextView tvUkphone;
    @BindView(R.id.ll_tran)
    LinearLayout llTran;
    @BindView(R.id.iv_img)
    ImageView ivImg;
    @BindView(R.id.ll_sentence)
    LinearLayout llSentence;
    @BindView(R.id.tv_rem)
    TextView tvRem;
    @BindView(R.id.ll_phrase)
    LinearLayout llPhrase;
    @BindView(R.id.ll_rel)
    LinearLayout llRel;
    @BindView(R.id.ll_syno)
    LinearLayout llSyno;
    @BindView(R.id.card_sentence)
    CardView cardSentence;
    @BindView(R.id.card_rem)
    CardView cardRem;
    @BindView(R.id.card_phrase)
    CardView cardPhrase;
    @BindView(R.id.card_rel)
    CardView cardRel;
    @BindView(R.id.card_syno)
    CardView cardSyno;
    @BindView(R.id.fl_speech)
    FrameLayout flSpeech;
    private WordSimple wordSimple;

    private SpeechFragment speechFragment;
//    private SpeechReceiver speechReceiver;

    private final String BASE_URL = "https://dict.youdao.com/dictvoice?audio=";

    private int speechType = 0;

    public WordDetailFragment() {
    }

    public static WordDetailFragment newInstance(WordSimple wordSimple) {
        WordDetailFragment fragment = new WordDetailFragment();
        fragment.wordSimple = wordSimple;
        return fragment;
    }

    @Override
    protected int initLayout() {
        return R.layout.fragment_word_detail;
    }

    @Override
    protected void initData() {
        speechFragment = (SpeechFragment) getChildFragmentManager().findFragmentById(R.id.fl_speech);
//        speechFragment.setOnSpeechFragmentCreatedListener(() -> speechFragment.play(BASE_URL + wordSimple.getUsspeech()));


        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, UnitConversion.dp2px(getContext(), 5), 0, 0);

        tvWord.setText(wordSimple.getWordHead());

        tvUsphone.setText("美 /" + wordSimple.getUsphone() + "/");
        tvUkphone.setText("英 /" + wordSimple.getUkphone() + "/");

        Picasso.with(getContext())
                .load(wordSimple.getPicture())
                .transform(new CircleCornerTransForm())
                .into(ivImg);

//        llTran.removeAllViews();
        if (wordSimple.getTrans() == null || wordSimple.getTrans().size() == 0) {
//            llTran.setVisibility(View.GONE);
        } else {
            for (WordSimple.TransBean tran : wordSimple.getTrans()) {
                ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_word_tran, null);
                TextView tvPos = viewGroup.findViewById(R.id.tv_pos);
                TextView tvTran = viewGroup.findViewById(R.id.tv_tran);
                tvPos.setText(tran.getPos());
                tvTran.setText(tran.getTranCn());
                llTran.addView(viewGroup, lp);
            }
        }

//        llSentence.removeAllViews();
        if (wordSimple.getSentences() == null || wordSimple.getSentences().size() == 0) {
            cardSentence.setVisibility(View.GONE);
        } else {
            for (WordSimple.SentencesBean sentence : wordSimple.getSentences()) {
                ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_word_sentence, null);
                TextView tvContent = viewGroup.findViewById(R.id.tv_content);
                TextView tvCn = viewGroup.findViewById(R.id.tv_cn);
                tvContent.setText(sentence.getScontent());
                tvCn.setText(sentence.getScn());
                llSentence.addView(viewGroup, lp);
            }
        }

        if (StringUtils.isEmpty(wordSimple.getRemMethod())) {
            cardRem.setVisibility(View.GONE);
        } else {
            tvRem.setText(wordSimple.getRemMethod());
        }

//        llPhrase.removeAllViews();
        if (wordSimple.getPhrases() == null || wordSimple.getPhrases().size() == 0) {
            cardPhrase.setVisibility(View.GONE);
        } else {
            for (WordSimple.PhrasesBean phrase : wordSimple.getPhrases()) {
                ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_word_phrase, null);
                TextView tvContent = viewGroup.findViewById(R.id.tv_content);
                TextView tvCn = viewGroup.findViewById(R.id.tv_cn);
                tvContent.setText(phrase.getPcontent());
                tvCn.setText(phrase.getPcn());
                llPhrase.addView(viewGroup, lp);
            }
        }

//        llRel.removeAllViews();
        if (wordSimple.getRels() == null || wordSimple.getRels().size() == 0) {
            cardRel.setVisibility(View.GONE);
        } else {
            for (WordSimple.RealsBean real : wordSimple.getRels()) {
                String pos = real.getPos();
                for (WordSimple.RealsBean.WordsBean word : real.getWords()) {
                    ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_word_rel, null);
                    TextView tvHwd = viewGroup.findViewById(R.id.tv_hwd);
                    TextView tvPos = viewGroup.findViewById(R.id.tv_pos);
                    TextView tvTran = viewGroup.findViewById(R.id.tv_tran);
                    tvHwd.setText(word.getHwd());
                    tvTran.setText(word.getTran());
                    tvPos.setText(pos);
                    llRel.addView(viewGroup, lp);
                }
            }
        }

//        llSyno.removeAllViews();
        if (wordSimple.getSynos() == null || wordSimple.getSynos().size() == 0) {
            cardSyno.setVisibility(View.GONE);
        } else {
            for (WordSimple.SynosBean syno : wordSimple.getSynos()) {
                ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(getContext()).inflate(R.layout.layout_word_syno, null);
                TextView tvPos = viewGroup.findViewById(R.id.tv_pos);
                TextView tvTran = viewGroup.findViewById(R.id.tv_tran);
                TextView tvHwds = viewGroup.findViewById(R.id.tv_hwds);
                tvTran.setText(syno.getTran());
                tvPos.setText(syno.getPos());
                String hwds = "";
                for (int i = 0; i < syno.getHwds().size(); i++) {
                    if (i == syno.getHwds().size() - 1) {
                        hwds += syno.getHwds().get(i).getW();
                    }
                    hwds += syno.getHwds().get(i).getW() + " / ";
                }
                tvHwds.setText(hwds);
                llSyno.addView(viewGroup);
            }
        }
    }

    @OnClick({R.id.tv_usphone, R.id.tv_ukphone})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_usphone:
                if (speechType==2){
                    speechFragment.replay();
                }
                speechFragment.release();
                speechFragment.play(BASE_URL+wordSimple.getUsspeech());
                speechType=2;
                break;
            case R.id.tv_ukphone:
                if (speechType==1){
                    speechFragment.replay();
                }
                speechFragment.release();
                speechFragment.play(BASE_URL+wordSimple.getUkspeech());
                speechType=1;
                break;
        }
    }

    public boolean onBackPressed(){
        return speechFragment.onBackPressed();
    }
}