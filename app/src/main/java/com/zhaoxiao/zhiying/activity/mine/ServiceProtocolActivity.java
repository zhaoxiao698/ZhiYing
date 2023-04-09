package com.zhaoxiao.zhiying.activity.mine;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.xuexiang.xaop.annotation.MemoryCache;
import com.xuexiang.xui.utils.StatusBarUtils;
import com.xuexiang.xutil.resource.ResUtils;
import com.xuexiang.xutil.resource.ResourceUtils;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ServiceProtocolActivity extends BaseActivity {

    @BindView(R.id.tv_protocol_text)
    TextView tvProtocolText;
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private String title;

    public static final String KEY_PROTOCOL_TITLE = "key_protocol_title";

    /**
     * 用户协议asset本地保存路径
     */
    private static final String ACCOUNT_PROTOCOL_ASSET_PATH = "protocol/account_protocol.txt";

    /**
     * 隐私政策asset本地保存路径
     */
    private static final String PRIVACY_PROTOCOL_ASSET_PATH = "protocol/privacy_protocol.txt";

    @Override
    protected int initLayout() {
        return R.layout.activity_service_protocol;
    }

    @Override
    protected void initData() {
        title = (String) getIntent().getSerializableExtra(KEY_PROTOCOL_TITLE);
        tvTitle.setText(title);
        if (title.equals(ResUtils.getString(R.string.title_user_protocol))) {
            tvProtocolText.setText(getAccountProtocol());
        } else {
            tvProtocolText.setText(getPrivacyProtocol());
        }
    }

    @MemoryCache("account_protocol")
    private String getAccountProtocol() {
        return ResourceUtils.readStringFromAssert(ACCOUNT_PROTOCOL_ASSET_PATH);
    }

    @MemoryCache("privacy_protocol")
    private String getPrivacyProtocol() {
        return ResourceUtils.readStringFromAssert(PRIVACY_PROTOCOL_ASSET_PATH);
    }

    @OnClick(R.id.iv_back)
    public void onClick() {
        finish();
    }
}