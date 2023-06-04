package com.zhaoxiao.zhiying.activity.community;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SimpleItemAnimator;

import com.google.android.flexbox.FlexboxLayout;
import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.basic.IBridgeViewLifecycle;
import com.luck.picture.lib.basic.PictureSelectionModel;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.decoration.GridSpacingItemDecoration;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.engine.VideoPlayerEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.entity.MediaExtraInfo;
import com.luck.picture.lib.interfaces.OnGridItemSelectAnimListener;
import com.luck.picture.lib.interfaces.OnQueryFilterListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.interfaces.OnSelectAnimListener;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.style.BottomNavBarStyle;
import com.luck.picture.lib.style.PictureSelectorStyle;
import com.luck.picture.lib.style.SelectMainStyle;
import com.luck.picture.lib.style.TitleBarStyle;
import com.luck.picture.lib.utils.DensityUtil;
import com.luck.picture.lib.utils.MediaUtils;
import com.luck.picture.lib.utils.PictureFileUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.mine.NoteListActivity;
import com.zhaoxiao.zhiying.adapter.community.GridImageAdapter;
import com.zhaoxiao.zhiying.api.CommunityService;
import com.zhaoxiao.zhiying.entity.community.Topic;
import com.zhaoxiao.zhiying.entity.study.Article;
import com.zhaoxiao.zhiying.entity.study.ArticleDetail;
import com.zhaoxiao.zhiying.entity.study.ArticleNote;
import com.zhaoxiao.zhiying.entity.study.ArticleNoteDetail;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.test.QuestionM;
import com.zhaoxiao.zhiying.entity.test.TestNoteDetail;
import com.zhaoxiao.zhiying.listener.DragListener;
import com.zhaoxiao.zhiying.listener.OnItemLongClickListener;
import com.zhaoxiao.zhiying.util.EPSoftKeyBoardListener;
import com.zhaoxiao.zhiying.util.EditTextUtil;
import com.zhaoxiao.zhiying.util.PictureSelector.FullyGridLayoutManager;
import com.zhaoxiao.zhiying.util.PictureSelector.GlideEngine;
import com.zhaoxiao.zhiying.util.UnitConversion;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PublishTrendActivity extends BaseActivity/* implements ImageSelectGridAdapter.OnAddPicClickListener*/ {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.btn_publish)
    Button btnPublish;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.et_title)
    EditText etTitle;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.et_info)
    EditText etInfo;
    @BindView(R.id.iv_add_img)
    ImageView ivAddImg;
    @BindView(R.id.iv_at)
    ImageView ivAt;
    @BindView(R.id.iv_add_topic)
    ImageView ivAddTopic;
    @BindView(R.id.iv_add_more)
    ImageView ivAddMore;
    @BindView(R.id.fl_option)
    FlexboxLayout flOption;
    @BindView(R.id.ll_share_note)
    LinearLayout llShareNote;
    @BindView(R.id.ll_share_article)
    LinearLayout llShareArticle;
    @BindView(R.id.ll_share_question)
    LinearLayout llShareQuestion;
    @BindView(R.id.fl_more)
    FlexboxLayout flMore;
    @BindView(R.id.sv_content)
    ScrollView svContent;
    //    @BindView(R.id.recycler_view)
//    RecyclerView recyclerView;
    @BindView(R.id.recycler)
    RecyclerView mRecyclerView;
    @BindView(R.id.tv_delete_text)
    TextView tvDeleteText;
    @BindView(R.id.fl_topic)
    FlexboxLayout flTopic;
//    @BindView(R.id.recycler)
//    RecyclerView recycler;
    @BindView(R.id.tv_share_title)
    TextView tvShareTitle;
    @BindView(R.id.tv_share_type)
    TextView tvShareType;
    @BindView(R.id.rl_share_link)
    RelativeLayout rlShareLink;
    @BindView(R.id.iv_del)
    ImageView ivDel;

    private ViewGroup.LayoutParams linearParams;
    private boolean isExpand;

//    private ImageSelectGridAdapter mAdapter;
//
//    private List<LocalMedia> mSelectList = new ArrayList<>();

    //图片选择
    private final static String TAG = "PictureSelectorTag";
    private final static String TAG_EXPLAIN_VIEW = "TAG_EXPLAIN_VIEW";
    private final static int ACTIVITY_RESULT = 1;
    private final static int CALLBACK_RESULT = 2;
    private final static int LAUNCHER_RESULT = 3;
    private GridImageAdapter mAdapter;
    private int maxSelectNum = 9;
    private int maxSelectVideoNum = 1;

    private int chooseMode = SelectMimeType.ofImage();
    private boolean isHasLiftDelete;
    private boolean needScaleBig = true;
    private boolean needScaleSmall = false;
    private boolean isUseSystemPlayer = false;
    private int language = LanguageConfig.SYSTEM_LANGUAGE;
    private int x = 0, y = 0;
    private int animationMode = AnimationType.DEFAULT_ANIMATION;
    private PictureSelectorStyle selectorStyle;
    private final List<LocalMedia> mData = new ArrayList<>();
    private ActivityResultLauncher<Intent> launcherResult;
    private int resultMode = LAUNCHER_RESULT;
    private ImageEngine imageEngine;
    private VideoPlayerEngine videoPlayerEngine;

    //话题
    private List<Integer> topicIdList = new ArrayList<>();
    public final static int TOPIC_RESULT = 4;

    //分享
    private boolean shareContent = false;
    public final static int NOTE_RESULT = 5;
    public final static int ARTICLE_RESULT = 6;
    public final static int QUESTION_RESULT = 7;
    public final static int AT_RESULT = 8;
    private boolean shareLink = false;

    private RelativeLayout.LayoutParams linearParams1;

    private int linkId=0;
    private int linkType=0;
    private int linkTable=0;

    private CommunityService communityService;
    private String account;

    private Map<String,Object> linkMap;

    @Override
    protected int initLayout() {
        return R.layout.activity_publish_trend;
    }

    @Override
    protected void initData() {
        if (getIntent().getSerializableExtra("linkMap") != null) {
            linkMap = (Map<String, Object>) getIntent().getSerializableExtra("linkMap");
            if (!shareLink) {
                    rlShareLink.setVisibility(View.VISIBLE);
                    tvShareTitle.setText((String) linkMap.get("info"));
//                    tvShareType.setText("动态");
                    tvShareType.setText((String) linkMap.get("type"));
                    shareLink=true;

                    linkId= (int) linkMap.get("linkId");
//                    linkType=5;
                    linkType=(int) linkMap.get("linkType");
            }
        }

        communityService = (CommunityService) getService(CommunityService.class);
        account = SpUtils.getInstance(this).getString("account","");

        linearParams = (RelativeLayout.LayoutParams) flMore.getLayoutParams();
        linearParams.height = 0;
        flMore.setLayoutParams(linearParams);

        linearParams1 = (RelativeLayout.LayoutParams) svContent.getLayoutParams();
        linearParams1.bottomMargin = UnitConversion.dp2px(this,50);
        svContent.setLayoutParams(linearParams1);

        EPSoftKeyBoardListener.setListener(this, new EPSoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {
                linearParams.height = 0;
                flMore.setLayoutParams(linearParams);
                ivAddMore.setImageTintList(getResources().getColorStateList(R.color.black));
                isExpand=false;
                linearParams1.bottomMargin = UnitConversion.dp2px(PublishTrendActivity.this,50);
                svContent.setLayoutParams(linearParams1);
            }

            @Override
            public void keyBoardHide(int height) {

            }
        });

        btnPublish.setEnabled(!etInfo.getText().toString().trim().equals(""));
//        btnPublish.setEnabled(!etInfo.toString().trim().equals(""));
        etInfo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                btnPublish.setEnabled(!editable.toString().trim().equals(""));
            }
        });

        //图片选择
//        GridLayoutManager manager = new GridLayoutManager(this, 4, RecyclerView.VERTICAL, false);
//        recyclerView.setLayoutManager(manager);
//        recyclerView.setAdapter(mAdapter = new ImageSelectGridAdapter(this, this));
//        mAdapter.setSelectList(mSelectList);
//        mAdapter.setSelectMax(8);
//        mAdapter.setOnItemClickListener((position, v) -> PictureSelector.create(PublishTrendActivity.this).themeStyle(R.style.XUIPictureStyle).openExternalPreview(position, mSelectList));
        initPictureSelector();
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.setFocusable(false);
    }

    @OnClick({R.id.iv_back, R.id.btn_publish, R.id.iv_add_img, R.id.iv_at, R.id.iv_add_topic, R.id.iv_add_more, R.id.fl_option, R.id.ll_share_note, R.id.ll_share_article, R.id.ll_share_question, R.id.fl_more, R.id.iv_del})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_publish:
//                XToastUtils.toast("发布");
                publish();
                break;
            case R.id.iv_add_img:
                openPicture();
                break;
            case R.id.iv_at:
                XToastUtils.toast("@");
//                navigateToForResult(TopicSelectActivity.class, TOPIC_RESULT);
                break;
            case R.id.iv_add_topic:
//                XToastUtils.toast("#");
                navigateToForResult(TopicSelectActivity.class, TOPIC_RESULT);
                break;
            case R.id.iv_add_more:
                if (isExpand) {
                    linearParams.height = 0;
                    flMore.setLayoutParams(linearParams);
                    ivAddMore.setImageTintList(getResources().getColorStateList(R.color.black));
                    isExpand = false;
                    linearParams1.bottomMargin = UnitConversion.dp2px(PublishTrendActivity.this,50);
                    svContent.setLayoutParams(linearParams1);
                } else {
//                flMore.setVisibility(View.VISIBLE);
                    linearParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
                    flMore.setLayoutParams(linearParams);
                    ivAddMore.setImageTintList(getResources().getColorStateList(R.color.g_yellow));
//                ivAddMore.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.g_yellow)));
                    EditTextUtil.hideKeyboard(this);
                    isExpand = true;
                    linearParams1.bottomMargin = UnitConversion.dp2px(PublishTrendActivity.this,50+145);
                    svContent.setLayoutParams(linearParams1);
                }
                break;
            case R.id.fl_option:
                break;
            case R.id.ll_share_note:
                if (shareLink){
                    XToastUtils.toast("不能同时添加多个链接");
                    break;
                }
                navigateToForResult(NoteListActivity.class, "share", true, NOTE_RESULT);
                break;
            case R.id.ll_share_article:
                if (shareLink){
                    XToastUtils.toast("不能同时添加多个链接");
                    break;
                }
                navigateToForResult(ArticleSelectActivity.class, ARTICLE_RESULT);
                break;
            case R.id.ll_share_question:
                if (shareLink){
                    XToastUtils.toast("不能同时添加多个链接");
                    break;
                }
                navigateToForResult(QuestionSelectActivity.class, QUESTION_RESULT);
                break;
            case R.id.iv_del:
                rlShareLink.setVisibility(View.GONE);
                shareLink = false;
                break;
        }
    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case PictureConfig.CHOOSE_REQUEST:
//                    // 图片选择
//                    mSelectList = PictureSelector.obtainMultipleResult(data);
//                    mAdapter.setSelectList(mSelectList);
//                    mAdapter.notifyDataSetChanged();
//                    break;
//                default:
//                    break;
//            }
//        }
//    }
//
//    @Override
//    public void onAddPicClick() {
//        Utils.getPictureSelector(this)
//                .selectionMedia(mSelectList)
//                .forResult(PictureConfig.CHOOSE_REQUEST);
//    }


    private void initPictureSelector() {
        // 注册需要写在onCreate或Fragment onAttach里，否则会报java.lang.IllegalStateException异常
        launcherResult = createActivityResultLauncher();

        selectorStyle = new PictureSelectorStyle();
        setSelectorStyle();

        FullyGridLayoutManager manager = new FullyGridLayoutManager(this,
                4, GridLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(manager);
        RecyclerView.ItemAnimator itemAnimator = mRecyclerView.getItemAnimator();
        if (itemAnimator != null) {
            ((SimpleItemAnimator) itemAnimator).setSupportsChangeAnimations(false);
        }
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(4,
                DensityUtil.dip2px(this, 8), false));
        mAdapter = new GridImageAdapter(this, mData);
        mAdapter.setSelectMax(maxSelectNum + maxSelectVideoNum);
        mRecyclerView.setAdapter(mAdapter);
//        if (savedInstanceState != null && savedInstanceState.getParcelableArrayList("selectorList") != null) {
//            mData.clear();
//            mData.addAll(savedInstanceState.getParcelableArrayList("selectorList"));
//        }

        imageEngine = GlideEngine.createGlideEngine();

        mAdapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                // 预览图片、视频、音频
                PictureSelector.create(PublishTrendActivity.this)
                        .openPreview()
                        .setImageEngine(imageEngine)
                        .setSelectorUIStyle(selectorStyle)
                        .setLanguage(language)
                        .isPreviewFullScreenMode(true)
                        .isPreviewZoomEffect(true, mRecyclerView)
                        .setAttachViewLifecycle(new IBridgeViewLifecycle() {
                            @Override
                            public void onViewCreated(Fragment fragment, View view, Bundle savedInstanceState) {
//                                PictureSelectorPreviewFragment previewFragment = (PictureSelectorPreviewFragment) fragment;
//                                MediumBoldTextView tvShare = view.findViewById(R.id.tv_share);
//                                tvShare.setVisibility(View.VISIBLE)
//                                previewFragment.addAminViews(tvShare);
//                                ViewGroup.MarginLayoutParams layoutParams = (ViewGroup.MarginLayoutParams) tvShare.getLayoutParams();
//                                layoutParams.topMargin = cb_preview_full.isChecked() ? DensityUtil.getStatusBarHeight(getContext()) : 0;
//                                tvShare.setOnClickListener(new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View v) {
//                                        PicturePreviewAdapter previewAdapter = previewFragment.getAdapter();
//                                        ViewPager2 viewPager2 = previewFragment.getViewPager2();
//                                        LocalMedia media = previewAdapter.getItem(viewPager2.getCurrentItem());
//                                        ToastUtils.showToast(fragment.getContext(), "自定义分享事件:" + viewPager2.getCurrentItem());
//                                    }
//                                });
                            }

                            @Override
                            public void onDestroy(Fragment fragment) {
//                                if (cb_preview_full.isChecked()) {
//                                    // 如果是全屏预览模式且是startFragmentPreview预览，回到自己的界面时需要恢复一下自己的沉浸式状态
//                                    // 以下提供2种解决方案:
//                                    // 1.通过ImmersiveManager.immersiveAboveAPI23重新设置一下沉浸式
//                                    int statusBarColor = ContextCompat.getColor(getContext(), R.color.ps_color_grey);
//                                    int navigationBarColor = ContextCompat.getColor(getContext(), R.color.ps_color_grey);
//                                    ImmersiveManager.immersiveAboveAPI23(MainActivity.this,
//                                            true, true,
//                                            statusBarColor, navigationBarColor, false);
//                                    // 2.让自己的titleBar的高度加上一个状态栏高度且内容PaddingTop下沉一个状态栏的高度
//                                }
                            }
                        })
                        .startActivityPreview(position, true, mAdapter.getData());
            }

            @Override
            public void openPicture() {
                PublishTrendActivity.this.openPicture();
            }
        });

        mAdapter.setItemLongClickListener(new OnItemLongClickListener() {
            @Override
            public void onItemLongClick(RecyclerView.ViewHolder holder, int position, View v) {
                int itemViewType = holder.getItemViewType();
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    mItemTouchHelper.startDrag(holder);
                }
            }
        });
        // 绑定拖拽事件
        mItemTouchHelper.attachToRecyclerView(mRecyclerView);
        // 清除缓存
//        clearCache();
    }

    private void openPicture() {
        boolean mode = true;
        // 进入系统相册
        // 进入相册
        PictureSelectionModel selectionModel = PictureSelector.create(PublishTrendActivity.this)
                .openGallery(chooseMode)
                .setSelectorUIStyle(selectorStyle)
                .setImageEngine(imageEngine)
                .setVideoPlayerEngine(videoPlayerEngine)
                .isUseSystemVideoPlayer(isUseSystemPlayer)
                .isPageSyncAlbumCount(true)
                .setQueryFilterListener(new OnQueryFilterListener() {
                    @Override
                    public boolean onFilter(LocalMedia media) {
                        return false;
                    }
                })
                //.setExtendLoaderEngine(getExtendLoaderEngine())
                .setSelectionMode(SelectModeConfig.MULTIPLE)
                .setLanguage(language)
                .setQuerySortOrder("")
                .isDisplayTimeAxis(true)
                .isOnlyObtainSandboxDir(false)
                .isPageStrategy(true)
                .isOriginalControl(false)
                .isDisplayCamera(true)
                .isOpenClickSound(false)
                .isFastSlidingSelect(true)
                //.setOutputCameraImageFileName("luck.jpeg")
                //.setOutputCameraVideoFileName("luck.mp4")
                .isWithSelectVideoImage(true)
                .isPreviewFullScreenMode(true)
                .isPreviewZoomEffect(true)
                .isPreviewImage(true)
                .setGridItemSelectAnimListener(new OnGridItemSelectAnimListener() {

                    @Override
                    public void onSelectItemAnim(View view, boolean isSelected) {
                        AnimatorSet set = new AnimatorSet();
                        set.playTogether(
                                ObjectAnimator.ofFloat(view, "scaleX", isSelected ? 1F : 1.12F, isSelected ? 1.12f : 1.0F),
                                ObjectAnimator.ofFloat(view, "scaleY", isSelected ? 1F : 1.12F, isSelected ? 1.12f : 1.0F)
                        );
                        set.setDuration(350);
                        set.start();
                    }
                })
                .setSelectAnimListener(new OnSelectAnimListener() {

                    @Override
                    public long onSelectAnim(View view) {
                        Animation animation = AnimationUtils.loadAnimation(PublishTrendActivity.this, R.anim.ps_anim_modal_in);
                        view.startAnimation(animation);
                        return animation.getDuration();
                    }
                })
                //.setQueryOnlyMimeType(PictureMimeType.ofGIF())
                .isMaxSelectEnabledMask(true)
                .isDirectReturnSingle(true)
                .setMaxSelectNum(maxSelectNum)
                .setMaxVideoSelectNum(maxSelectVideoNum)
                .setRecyclerAnimationMode(animationMode)
                .isGif(false)
                .setSelectedData(mAdapter.getData());
        forSelectResult(selectionModel);
    }

    /**
     * 创建一个ActivityResultLauncher
     *
     * @return
     */
    private ActivityResultLauncher<Intent> createActivityResultLauncher() {
        return registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        int resultCode = result.getResultCode();
                        if (resultCode == RESULT_OK) {
                            ArrayList<LocalMedia> selectList = PictureSelector.obtainSelectorList(result.getData());
                            analyticalSelectResults(selectList);
                        } else if (resultCode == RESULT_CANCELED) {
                            Log.i(TAG, "onActivityResult PictureSelector Cancel");
                        }
                    }
                });
    }

    private void setSelectorStyle() {
        TitleBarStyle whiteTitleBarStyle = new TitleBarStyle();
        whiteTitleBarStyle.setTitleBackgroundColor(ContextCompat.getColor(this, R.color.ps_color_white));
        whiteTitleBarStyle.setTitleDrawableRightResource(R.drawable.ic_orange_arrow_down);
        whiteTitleBarStyle.setTitleLeftBackResource(R.drawable.ps_ic_black_back);
        whiteTitleBarStyle.setTitleTextColor(ContextCompat.getColor(this, R.color.ps_color_black));
        whiteTitleBarStyle.setTitleCancelTextColor(ContextCompat.getColor(this, R.color.ps_color_53575e));
        whiteTitleBarStyle.setDisplayTitleBarLine(true);

        BottomNavBarStyle whiteBottomNavBarStyle = new BottomNavBarStyle();
        whiteBottomNavBarStyle.setBottomNarBarBackgroundColor(Color.parseColor("#EEEEEE"));
        whiteBottomNavBarStyle.setBottomPreviewSelectTextColor(ContextCompat.getColor(this, R.color.ps_color_53575e));

        whiteBottomNavBarStyle.setBottomPreviewNormalTextColor(ContextCompat.getColor(this, R.color.ps_color_9b));
        whiteBottomNavBarStyle.setBottomPreviewSelectTextColor(ContextCompat.getColor(this, R.color.ps_color_fa632d));
        whiteBottomNavBarStyle.setCompleteCountTips(false);
        whiteBottomNavBarStyle.setBottomEditorTextColor(ContextCompat.getColor(this, R.color.ps_color_53575e));
        whiteBottomNavBarStyle.setBottomOriginalTextColor(ContextCompat.getColor(this, R.color.ps_color_53575e));

        SelectMainStyle selectMainStyle = new SelectMainStyle();
        selectMainStyle.setStatusBarColor(ContextCompat.getColor(this, R.color.ps_color_white));
        selectMainStyle.setDarkStatusBarBlack(true);
        selectMainStyle.setSelectNormalTextColor(ContextCompat.getColor(this, R.color.ps_color_9b));
        selectMainStyle.setSelectTextColor(ContextCompat.getColor(this, R.color.ps_color_fa632d));
        selectMainStyle.setPreviewSelectBackground(R.drawable.ps_demo_white_preview_selector);
        selectMainStyle.setSelectBackground(R.drawable.ps_checkbox_selector);
        selectMainStyle.setSelectText(R.string.ps_done_front_num);
        selectMainStyle.setMainListBackgroundColor(ContextCompat.getColor(this, R.color.ps_color_white));

        selectorStyle.setTitleBarStyle(whiteTitleBarStyle);
        selectorStyle.setBottomBarStyle(whiteBottomNavBarStyle);
        selectorStyle.setSelectMainStyle(selectMainStyle);
    }

    private void forSelectResult(PictureSelectionModel model) {
        switch (resultMode) {
            case ACTIVITY_RESULT:
                model.forResult(PictureConfig.CHOOSE_REQUEST);
                break;
            case CALLBACK_RESULT:
                model.forResult(new MeOnResultCallbackListener());
                break;
            default:
                model.forResult(launcherResult);
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        // TODO: add setContentView(...) invocation
//        ButterKnife.bind(this);
//    }

    /**
     * 选择结果
     */
    private class MeOnResultCallbackListener implements OnResultCallbackListener<LocalMedia> {
        @Override
        public void onResult(ArrayList<LocalMedia> result) {
            analyticalSelectResults(result);
        }

        @Override
        public void onCancel() {
            Log.i(TAG, "PictureSelector Cancel");
        }
    }

    /**
     * 处理选择结果
     *
     * @param result
     */
    private void analyticalSelectResults(ArrayList<LocalMedia> result) {
        for (LocalMedia media : result) {
            if (media.getWidth() == 0 || media.getHeight() == 0) {
                if (PictureMimeType.isHasImage(media.getMimeType())) {
                    MediaExtraInfo imageExtraInfo = MediaUtils.getImageSize(this, media.getPath());
                    media.setWidth(imageExtraInfo.getWidth());
                    media.setHeight(imageExtraInfo.getHeight());
                } else if (PictureMimeType.isHasVideo(media.getMimeType())) {
                    MediaExtraInfo videoExtraInfo = MediaUtils.getVideoSize(this, media.getPath());
                    media.setWidth(videoExtraInfo.getWidth());
                    media.setHeight(videoExtraInfo.getHeight());
                }
            }
            Log.i(TAG, "文件名: " + media.getFileName());
            Log.i(TAG, "是否压缩:" + media.isCompressed());
            Log.i(TAG, "压缩:" + media.getCompressPath());
            Log.i(TAG, "初始路径:" + media.getPath());
            Log.i(TAG, "绝对路径:" + media.getRealPath());
            Log.i(TAG, "是否裁剪:" + media.isCut());
            Log.i(TAG, "裁剪路径:" + media.getCutPath());
            Log.i(TAG, "是否开启原图:" + media.isOriginal());
            Log.i(TAG, "原图路径:" + media.getOriginalPath());
            Log.i(TAG, "沙盒路径:" + media.getSandboxPath());
            Log.i(TAG, "水印路径:" + media.getWatermarkPath());
            Log.i(TAG, "视频缩略图:" + media.getVideoThumbnailPath());
            Log.i(TAG, "原始宽高: " + media.getWidth() + "x" + media.getHeight());
            Log.i(TAG, "裁剪宽高: " + media.getCropImageWidth() + "x" + media.getCropImageHeight());
            Log.i(TAG, "文件大小: " + PictureFileUtils.formatAccurateUnitFileSize(media.getSize()));
            Log.i(TAG, "文件时长: " + media.getDuration());
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                boolean isMaxSize = result.size() == mAdapter.getSelectMax();
                int oldSize = mAdapter.getData().size();
                mAdapter.notifyItemRangeRemoved(0, isMaxSize ? oldSize + 1 : oldSize);
                mAdapter.getData().clear();

                mAdapter.getData().addAll(result);
                mAdapter.notifyItemRangeInserted(0, result.size());
            }
        });
    }

    private final ItemTouchHelper mItemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
        @Override
        public boolean isLongPressDragEnabled() {
            return true;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        }

        @Override
        public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                viewHolder.itemView.setAlpha(0.7f);
            }
            return makeMovementFlags(ItemTouchHelper.DOWN | ItemTouchHelper.UP
                    | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT, 0);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            try {
                //得到item原来的position
                int fromPosition = viewHolder.getAbsoluteAdapterPosition();
                //得到目标position
                int toPosition = target.getAbsoluteAdapterPosition();
                int itemViewType = target.getItemViewType();
                if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                    if (fromPosition < toPosition) {
                        for (int i = fromPosition; i < toPosition; i++) {
                            Collections.swap(mAdapter.getData(), i, i + 1);
                        }
                    } else {
                        for (int i = fromPosition; i > toPosition; i--) {
                            Collections.swap(mAdapter.getData(), i, i - 1);
                        }
                    }
                    mAdapter.notifyItemMoved(fromPosition, toPosition);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        @Override
        public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView,
                                @NonNull RecyclerView.ViewHolder viewHolder, float dx, float dy, int actionState, boolean isCurrentlyActive) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                if (needScaleBig) {
                    needScaleBig = false;
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(
                            ObjectAnimator.ofFloat(viewHolder.itemView, "scaleX", 1.0F, 1.1F),
                            ObjectAnimator.ofFloat(viewHolder.itemView, "scaleY", 1.0F, 1.1F));
                    animatorSet.setDuration(50);
                    animatorSet.setInterpolator(new LinearInterpolator());
                    animatorSet.start();
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            needScaleSmall = true;
                        }
                    });
                }
                int targetDy = tvDeleteText.getTop() - viewHolder.itemView.getBottom();
                if (dy >= targetDy) {
                    //拖到删除处
                    mDragListener.deleteState(true);
                    if (isHasLiftDelete) {
                        //在删除处放手，则删除item
                        viewHolder.itemView.setVisibility(View.INVISIBLE);
                        mAdapter.delete(viewHolder.getAbsoluteAdapterPosition());
                        resetState();
                        return;
                    }
                } else {
                    //没有到删除处
                    if (View.INVISIBLE == viewHolder.itemView.getVisibility()) {
                        //如果viewHolder不可见，则表示用户放手，重置删除区域状态
                        mDragListener.dragState(false);
                    }
                    mDragListener.deleteState(false);
                }
                super.onChildDraw(c, recyclerView, viewHolder, dx, dy, actionState, isCurrentlyActive);
            }
        }

        @Override
        public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
            int itemViewType = viewHolder != null ? viewHolder.getItemViewType() : GridImageAdapter.TYPE_CAMERA;
            if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                if (ItemTouchHelper.ACTION_STATE_DRAG == actionState) {
                    mDragListener.dragState(true);
                }
                super.onSelectedChanged(viewHolder, actionState);
            }
        }

        @Override
        public long getAnimationDuration(@NonNull RecyclerView recyclerView, int animationType, float animateDx, float animateDy) {
            isHasLiftDelete = true;
            return super.getAnimationDuration(recyclerView, animationType, animateDx, animateDy);
        }

        @Override
        public void clearView(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
            int itemViewType = viewHolder.getItemViewType();
            if (itemViewType != GridImageAdapter.TYPE_CAMERA) {
                viewHolder.itemView.setAlpha(1.0F);
                if (needScaleSmall) {
                    needScaleSmall = false;
                    AnimatorSet animatorSet = new AnimatorSet();
                    animatorSet.playTogether(
                            ObjectAnimator.ofFloat(viewHolder.itemView, "scaleX", 1.1F, 1.0F),
                            ObjectAnimator.ofFloat(viewHolder.itemView, "scaleY", 1.1F, 1.0F));
                    animatorSet.setInterpolator(new LinearInterpolator());
                    animatorSet.setDuration(50);
                    animatorSet.start();
                    animatorSet.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            needScaleBig = true;
                        }
                    });
                }
                super.clearView(recyclerView, viewHolder);
                mAdapter.notifyItemChanged(viewHolder.getAbsoluteAdapterPosition());
                resetState();
            }
        }
    });

    private final DragListener mDragListener = new DragListener() {
        @Override
        public void deleteState(boolean isDelete) {
            if (isDelete) {
                if (!TextUtils.equals("松手即可删除", tvDeleteText.getText())) {
                    tvDeleteText.setText("松手即可删除");
                    tvDeleteText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_dump_delete, 0, 0);
                }
            } else {
                if (!TextUtils.equals("拖动到此处删除", tvDeleteText.getText())) {
                    tvDeleteText.setText("拖动到此处删除");
                    tvDeleteText.setCompoundDrawablesRelativeWithIntrinsicBounds(0, R.drawable.ic_normal_delete, 0, 0);
                }
            }

        }

        @Override
        public void dragState(boolean isStart) {
            if (isStart) {
                if (tvDeleteText.getAlpha() == 0F) {
                    ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(tvDeleteText, "alpha", 0F, 1F);
                    alphaAnimator.setInterpolator(new LinearInterpolator());
                    alphaAnimator.setDuration(120);
                    alphaAnimator.start();
                }
            } else {
                if (tvDeleteText.getAlpha() == 1F) {
                    ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(tvDeleteText, "alpha", 1F, 0F);
                    alphaAnimator.setInterpolator(new LinearInterpolator());
                    alphaAnimator.setDuration(120);
                    alphaAnimator.start();
                }
            }
        }
    };

    /**
     * 重置
     */
    private void resetState() {
        isHasLiftDelete = false;
        mDragListener.deleteState(false);
        mDragListener.dragState(false);
    }

    //@、#话题、分享
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            if (requestCode == TOPIC_RESULT) {
                Topic topic = (Topic) data.getExtras().getSerializable("topic");
//            Log.i(TAG, topic.toString());
                if (!topicIdList.contains(topic.getId())) {
                    ViewGroup viewGroup = (ViewGroup) LayoutInflater.from(mContext).inflate(R.layout.layout_topic_publish_item, null);
                    RelativeLayout child = viewGroup.findViewById(R.id.rl_option);
                    viewGroup.removeView(child);
                    RoundButton tvOption = child.findViewById(R.id.tv_option);
                    tvOption.setText("#" + topic.getName());
                    tvOption.setOnClickListener(view -> XToastUtils.info(topic.getName()));
                    ImageView ivDel = child.findViewById(R.id.iv_del);
                    ivDel.setOnClickListener(view -> {
                        flTopic.removeView(child);
                        topicIdList.remove(Integer.valueOf(topic.getId()));
                    });
                    flTopic.addView(child);
                    topicIdList.add(topic.getId());
                }
            } else if (requestCode == NOTE_RESULT) {
                if (!shareLink) {
                    if (data.getExtras().getSerializable("articleNoteDetail") != null) {
                        ArticleNoteDetail articleNoteDetail = (ArticleNoteDetail) data.getExtras().getSerializable("articleNoteDetail");
                        rlShareLink.setVisibility(View.VISIBLE);
                        tvShareTitle.setText(articleNoteDetail.getInfo());
                        tvShareType.setText("文章笔记");
                        shareLink=true;

                        linkId=articleNoteDetail.getArticleId();
                        linkType=1;
                    } else if (data.getExtras().getSerializable("testNoteDetail") != null) {
                        TestNoteDetail testNoteDetail = (TestNoteDetail) data.getExtras().getSerializable("testNoteDetail");
                        rlShareLink.setVisibility(View.VISIBLE);
                        tvShareTitle.setText(testNoteDetail.getInfo());
                        tvShareType.setText("题目笔记");
                        shareLink=true;

                        linkId=testNoteDetail.getQuestionId();
                        linkType=1;
                        linkTable= (int) data.getExtras().getSerializable("table");
                    }
                }
            } else if (requestCode == ARTICLE_RESULT) {
                if (!shareLink) {
                    if (data.getExtras().getSerializable("article") != null) {
                        Article article = (Article) data.getExtras().getSerializable("article");
                        rlShareLink.setVisibility(View.VISIBLE);
                        tvShareTitle.setText(article.getTitle());
                        tvShareType.setText("文章");
                        shareLink=true;

                        linkId=article.getId();
                        linkType=1;
                    }
                }
            } else if (requestCode == QUESTION_RESULT) {
                if (!shareLink) {
                    if (data.getExtras().getSerializable("question") != null) {
                        QuestionM question = (QuestionM) data.getExtras().getSerializable("question");
                        rlShareLink.setVisibility(View.VISIBLE);
                        tvShareTitle.setText(question.getInfo());
                        tvShareType.setText("题目");
                        shareLink=true;

                        linkId=question.getId();
                        linkType=1;
                        linkTable= (int) data.getExtras().getSerializable("table");
                    }
                }
            } else if (requestCode == AT_RESULT) {

            }
        }
    }

    private void publish() {
        MediaType textType = MediaType.parse("text/plain");
        RequestBody accountBody = RequestBody.create(textType, account);
        RequestBody titleBody = RequestBody.create(textType, etTitle.getText().toString());
        RequestBody infoBody = RequestBody.create(textType, etInfo.getText().toString());
        RequestBody linkIdBody = null;
        RequestBody linkTypeBody = null;
        RequestBody linkTableBody = null;
//        if (shareLink){
            linkIdBody = RequestBody.create(textType, String.valueOf(linkId));
            linkTypeBody = RequestBody.create(textType, String.valueOf(linkType));
            linkTableBody = RequestBody.create(textType, String.valueOf(linkTable));
//        }
        StringBuilder sb = new StringBuilder();
        for (Integer topicId : topicIdList) {
            sb.append(topicId).append(",");
        }
        String topicIdListString = "";
        if (sb.length()>0){
            topicIdListString = sb.substring(0, sb.length() - 1);
        }
        RequestBody topicIdListBody = RequestBody.create(textType, topicIdListString);

        List<MultipartBody.Part> imgFileList = new ArrayList<>();
        for (LocalMedia localMedia : mAdapter.getData()) {
            String imgRealPath = localMedia.getRealPath();
            File file = new File(imgRealPath);
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("imgFileList", file.getName(), fileBody);
            imgFileList.add(filePart);
        }

        Map<String, RequestBody> params = new HashMap<>();
        params.put("account", accountBody);
        params.put("title", titleBody);
        params.put("info", infoBody);
//        if (shareLink) {
            params.put("linkId", linkIdBody);
            params.put("linkType", linkTypeBody);
            params.put("linkTable", linkTableBody);
//        }
        params.put("topicIdList", topicIdListBody);

        Call<Data<Boolean>> publishTrendCall = communityService.publishTrend(params, imgFileList);
        publishTrendCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        XToastUtils.toast("发布成功");
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {
                System.out.println(t.toString());
            }
        });
    }
}