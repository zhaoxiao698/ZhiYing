package com.zhaoxiao.zhiying.activity.mine;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.zhaoxiao.zhiying.activity.mine.ServiceProtocolActivity.KEY_PROTOCOL_TITLE;

public class AboutActivity extends BaseActivity {
    @BindView(R.id.copyright)
    TextView copyright;

    @Override
    protected int initLayout() {
        return R.layout.activity_about;
    }

    @Override
    protected void initData() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy", Locale.CHINA);
        String currentYear = dateFormat.format(new Date());
        copyright.setText(String.format(getResources().getString(R.string.copyright), currentYear));
    }

    @OnClick({R.id.iv_back, R.id.tv_user_protocol, R.id.tv_privacy_protocol})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_user_protocol:
                navigateTo(ServiceProtocolActivity.class,KEY_PROTOCOL_TITLE, "用户协议");
                break;
            case R.id.tv_privacy_protocol:
                navigateTo(ServiceProtocolActivity.class,KEY_PROTOCOL_TITLE, "隐私政策");
                break;
        }
    }
}