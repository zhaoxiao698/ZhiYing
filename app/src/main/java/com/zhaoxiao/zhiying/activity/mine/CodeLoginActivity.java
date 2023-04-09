package com.zhaoxiao.zhiying.activity.mine;

import android.content.Intent;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.xuexiang.xui.XUI;
import com.xuexiang.xui.utils.DensityUtils;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.alpha.XUIAlphaTextView;
import com.xuexiang.xui.widget.button.roundbutton.RoundButton;
import com.xuexiang.xui.widget.popupwindow.ViewTooltip;
import com.xuexiang.xui.widget.popupwindow.popup.XUIPopup;
import com.xuexiang.xui.widget.toast.XToast;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.api.ApiConfig;
import com.zhaoxiao.zhiying.api.UserService;
import com.zhaoxiao.zhiying.entity.mine.CodeResponse;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.view.DemoPopup;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;
import static com.zhaoxiao.zhiying.activity.mine.ServiceProtocolActivity.KEY_PROTOCOL_TITLE;

public class CodeLoginActivity extends BaseActivity {
    @BindView(R.id.ll_area)
    LinearLayout llArea;
    @BindView(R.id.et_phone)
    EditText etPhone;
    @BindView(R.id.cb_protocol)
    CheckBox cbProtocol;
    @BindView(R.id.tv_user_protocol)
    XUIAlphaTextView tvUserProtocol;
    @BindView(R.id.tv_privacy_protocol)
    XUIAlphaTextView tvPrivacyProtocol;
    @BindView(R.id.btn_getCode)
    RoundButton btnGetCode;
    @BindView(R.id.btn_password_login)
    RoundButton btnPasswordLogin;
    @BindView(R.id.ll_phone)
    LinearLayout llPhone;

    ViewTooltip viewTooltip;
    DemoPopup demoPopup;
    private XUIPopup mNormalPopup;

    private UserService userService;

    public static final String CONTENT = "您的验证码是123456，在10分钟内有效。如非本人操作请忽略本短信。";
    public static final String CONTENT1 = "您的验证码是";
    public static final String CONTENT2 = "，在10分钟内有效。如非本人操作请忽略本短信。";
    public static final String PARAM_API_KEY = "apikey";
    public static final String PARAM_MOBILE = "mobile";
    public static final String PARAM_CONTENT = "content";
    public static final int RESULT_CLOSE = 100;

    @Override
    protected int initLayout() {
        return R.layout.activity_code_login;
    }

    @Override
    protected void initData() {
        userService = (UserService) getService(UserService.class);

        demoPopup = (DemoPopup) new DemoPopup(this).setPopupGravity(Gravity.BOTTOM | Gravity.CENTER);

        viewTooltip = ViewTooltip
                .on(cbProtocol)
                .color(Color.BLACK)
                .position(ViewTooltip.Position.BOTTOM)
                .text("前先勾选协议")
                .clickToHide(true)
                .autoHide(false, 0)
                .animation(new ViewTooltip.FadeTooltipAnimation(500));

//        XToast.Config.get()
//                //位置设置为居中
//                .setGravity(Gravity.CENTER);

        etPhone.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//                XToastUtils.success("输入完成");
                return false;
            }
        });
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                XToastUtils.success("输入前");
//                System.out.println("输入前");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                XToastUtils.success("输入中");
//                System.out.println("输入中");
            }

            @Override
            public void afterTextChanged(Editable editable) {
//                XToastUtils.success("输入后");
//                System.out.println("输入后");
                llPhone.setBackground(getResources().getDrawable(R.drawable.shape_input_account));
            }
        });

        cbProtocol.setOnCheckedChangeListener((compoundButton, b) -> {
//            viewTooltip.autoHide(b,0);
            viewTooltip.close();
        });
    }

    @OnClick({R.id.ll_area, R.id.btn_getCode, R.id.btn_password_login,R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ll_area:
                XToastUtils.info("当前只支持国内手机号登录");
                break;
            case R.id.btn_getCode:
                String phone = etPhone.getText().toString().trim();
                if (StringUtils.isEmpty(phone)) {
                    XToastUtils.error("请输入手机号");
                    llPhone.setBackground(getResources().getDrawable(R.drawable.shape_input_account_error));
                } else if (!StringUtils.isMobile(phone)) {
                    XToastUtils.error("手机号格式不正确");
                    llPhone.setBackground(getResources().getDrawable(R.drawable.shape_input_account_error));
                } else if (!cbProtocol.isChecked()) {
                    viewTooltip.show();

//                    demoPopup.showPopupWindow(cbProtocol);

//                    initNormalPopupIfNeed();
//                    mNormalPopup.setAnimStyle(XUIPopup.ANIM_GROW_FROM_CENTER);
//                    mNormalPopup.setPreferredDirection(XUIPopup.DIRECTION_BOTTOM);
//                    mNormalPopup.show(cbProtocol);
                } else {
//                    XToastUtils.success("登录");
                    getCode(phone);
                }
                break;
            case R.id.btn_password_login:
                finish();
                navigateTo(PasswordLoginActivity.class);
                break;
            case R.id.tv_user_protocol:
                navigateTo(ServiceProtocolActivity.class,KEY_PROTOCOL_TITLE, "用户协议");
                break;
            case R.id.tv_privacy_protocol:
                navigateTo(ServiceProtocolActivity.class,KEY_PROTOCOL_TITLE, "隐私政策");
                break;
        }
    }

    private void getCode(String mobile) {
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_API_KEY, ApiConfig.API_KEY);
        params.put(PARAM_MOBILE, mobile);
        String code = StringUtils.randomCode();
        String content = null;
        try {
            content = URLEncoder.encode(CONTENT1+code+CONTENT2,"GBK");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        SpUtils.getInstance(this).setString("code",code,10*SpUtils.TIME_MINUTES);
        params.put(PARAM_CONTENT, content);

        Call<CodeResponse> codeCall = userService.sendCode(ApiConfig.SEND_CODE_URL, params);
        codeCall.enqueue(new Callback<CodeResponse>() {
            @Override
            public void onResponse(Call<CodeResponse> call, Response<CodeResponse> response) {
                System.out.println("获取验证码："+response.body().toString());
                navigateTo(CodeLogin2Activity.class, "phone", mobile);
            }

            @Override
            public void onFailure(Call<CodeResponse> call, Throwable t) {

            }
        });
    }

    private void initNormalPopupIfNeed() {
        if (mNormalPopup == null) {
            mNormalPopup = new XUIPopup(this);
            TextView textView = new TextView(this);
            textView.setLayoutParams(mNormalPopup.generateLayoutParam(
                    WRAP_CONTENT,
                    WRAP_CONTENT
            ));
            textView.setLineSpacing(DensityUtils.dp2px(this, 4), 1.0f);
            int padding = DensityUtils.dp2px(this, 20);
            textView.setPadding(padding, padding, padding, padding);
            textView.setText("请先勾选协议");
            textView.setTextColor(ContextCompat.getColor(this, R.color.xui_config_color_content_text));
            textView.setTypeface(XUI.getDefaultTypeface());
            mNormalPopup.setContentView(textView);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode == RESULT_CLOSE){
            finish();
        }
    }
}