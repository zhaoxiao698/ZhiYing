package com.zhaoxiao.zhiying.activity.mine;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.core.content.ContextCompat;

import com.luck.picture.lib.animators.AnimationType;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.engine.ImageEngine;
import com.luck.picture.lib.engine.VideoPlayerEngine;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.interfaces.OnGridItemSelectAnimListener;
import com.luck.picture.lib.interfaces.OnQueryFilterListener;
import com.luck.picture.lib.interfaces.OnResultCallbackListener;
import com.luck.picture.lib.interfaces.OnSelectAnimListener;
import com.luck.picture.lib.language.LanguageConfig;
import com.luck.picture.lib.style.BottomNavBarStyle;
import com.luck.picture.lib.style.PictureSelectorStyle;
import com.luck.picture.lib.style.SelectMainStyle;
import com.luck.picture.lib.style.TitleBarStyle;
import com.squareup.picasso.Picasso;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.imageview.RadiusImageView;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.community.PublishTrendActivity;
import com.zhaoxiao.zhiying.activity.study.ChannelActivity;
import com.zhaoxiao.zhiying.activity.study.NoteActivity;
import com.zhaoxiao.zhiying.adapter.community.GridImageAdapter;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.mine.User;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.fragment.study.ListenFragment;
import com.zhaoxiao.zhiying.util.EditTextUtil;
import com.zhaoxiao.zhiying.util.PictureSelector.GlideEngine;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
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

public class SetInfoActivity extends BaseActivity {
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.iv_avatar)
    RadiusImageView ivAvatar;
    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_sign)
    EditText etSign;
    @BindView(R.id.et_sex)
    EditText etSex;
    @BindView(R.id.et_age)
    EditText etAge;
    @BindView(R.id.et_mail)
    EditText etMail;

    private UserService userService;
    private String account;
    private User user;

    //图片选择
    private final static String TAG = "PictureSelectorTag";
    private final static String TAG_EXPLAIN_VIEW = "TAG_EXPLAIN_VIEW";
    private final static int ACTIVITY_RESULT = 1;
    private final static int CALLBACK_RESULT = 2;
    private final static int LAUNCHER_RESULT = 3;
    private GridImageAdapter mAdapter;
    private int maxSelectNum = 1;
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

    private String path;
    private String realPath="";
    private boolean selectImg = false;

    @Override
    protected int initLayout() {
        return R.layout.activity_set_info;
    }

    @Override
    protected void initData() {
        userService = (UserService) getService(UserService.class);
        account = SpUtils.getInstance(this).getString("account","");
//        etSex.setEnabled(false);
        etSex.setFocusable(false);
//        etSex.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                XToastUtils.toast("点击");
//            }
//        });
        getUserInfo();

        selectorStyle = new PictureSelectorStyle();
        setSelectorStyle();
        imageEngine = GlideEngine.createGlideEngine();
    }

    @OnClick({R.id.iv_back, R.id.rl_avatar, R.id.rl_name, R.id.rl_sign, R.id.rl_sex, R.id.et_sex, R.id.rl_age, R.id.rl_mail, R.id.rl_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_avatar:
                selectImg();
                break;
            case R.id.rl_name:
                EditTextUtil.showSoftInputFromWindow(this,etName);
                break;
            case R.id.rl_sign:
                EditTextUtil.showSoftInputFromWindow(this,etSign);
                break;
            case R.id.rl_sex:
            case R.id.et_sex:
                showSimpleBottomSheetList();
                break;
            case R.id.rl_age:
                EditTextUtil.showSoftInputFromWindow(this,etAge);
                break;
            case R.id.rl_mail:
                EditTextUtil.showSoftInputFromWindow(this,etMail);
                break;
            case R.id.rl_save:
                sava();
                break;
        }
    }

    private void getUserInfo(){
        Call<Data<User>> getUserInfoCall = userService.getByAccount(account);
        getUserInfoCall.enqueue(new Callback<Data<User>>() {
            @Override
            public void onResponse(Call<Data<User>> call, Response<Data<User>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    user = response.body().getData();
                    etName.setText(user.getName());
                    etSign.setText(user.getSign());
                    etSex.setText(user.getSex());
                    etAge.setText(String.valueOf(user.getAge()));
                    etMail.setText(user.getMail());
                    Picasso.with(mContext)
                            .load(ApiConfig.BASE_URl+user.getAvatar())
                            .transform(new CircleCornerTransForm())
                            .into(ivAvatar);
                }
            }

            @Override
            public void onFailure(Call<Data<User>> call, Throwable t) {

            }
        });
    }

    private void selectImg() {
        PictureSelector.create(this)
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
                        Animation animation = AnimationUtils.loadAnimation(SetInfoActivity.this, R.anim.ps_anim_modal_in);
                        view.startAnimation(animation);
                        return animation.getDuration();
                    }
                })
                .isMaxSelectEnabledMask(true)
                .isDirectReturnSingle(true)
                .setMaxSelectNum(maxSelectNum)
                .setMaxVideoSelectNum(maxSelectVideoNum)
                .setRecyclerAnimationMode(animationMode)
                .isGif(false)
                .forResult(new OnResultCallbackListener<LocalMedia>() {
                    @Override
                    public void onResult(ArrayList<LocalMedia> result) {
                        System.out.println("result"+result);
                        System.out.println("初始路径"+result.get(0).getPath());
                        System.out.println("绝对路径"+result.get(0).getRealPath());
                        path = result.get(0).getPath();
                        realPath = result.get(0).getRealPath();
                        if (result.get(0)!=null) {
                            Uri uri = Uri.parse(result.get(0).getPath()); // 创建URI对象
                            ivAvatar.setImageURI(uri);
                            selectImg = true;
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }

    //选择性别
    private void showSimpleBottomSheetList() {
        new BottomSheet.BottomListSheetBuilder(this)
                .setTitle("更多")
                .addItem("男")
                .addItem("女")
                .setIsCenter(true)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    switch (position){
                        case 0:
                            etSex.setText("男");
                            break;
                        case 1:
                            etSex.setText("女");
                            break;
                    }
                })
                .build()
                .show();
    }

    private void sava() {
        MediaType textType = MediaType.parse("text/plain");
        RequestBody accountBody = RequestBody.create(textType, account);
        RequestBody nameBody = RequestBody.create(textType, etName.getText().toString());
        RequestBody signBody = RequestBody.create(textType, etSign.getText().toString());
        RequestBody sexBody = RequestBody.create(textType, etSex.getText().toString());
        RequestBody ageBody = RequestBody.create(textType, etAge.getText().toString());
        RequestBody mailBody = RequestBody.create(textType, etMail.getText().toString());

        MultipartBody.Part filePart = null;
        if (selectImg) {
            File file = new File(realPath);
//        if (file!=null) {
            RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
            filePart = MultipartBody.Part.createFormData("imgFile", file.getName(), fileBody);
//        }
        }

        Map<String, RequestBody> params = new HashMap<>();
        params.put("account", accountBody);
        params.put("name", nameBody);
        params.put("sign", signBody);
        params.put("sex", sexBody);
        params.put("age", ageBody);
        params.put("mail", mailBody);

        Call<Data<Boolean>> publishTrendCall = userService.setUser(params, filePart);
        publishTrendCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()){
                        XToastUtils.toast("保存成功");
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {
                System.out.println("t:"+t.toString());
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
}