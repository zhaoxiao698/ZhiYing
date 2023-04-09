package com.zhaoxiao.zhiying.entity.study;

import android.graphics.Typeface;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.zhaoxiao.zhiying.R;


public class PwdSwitch extends AppCompatActivity {

    public EditText etPwd;
    public ImageView ivPwdSwitch;
    public Boolean pwdFlag = false;

    public PwdSwitch(EditText etPwd, ImageView ivPwdSwitch) {
        this.etPwd = etPwd;
        this.ivPwdSwitch = ivPwdSwitch;

        //构造的时候就设置监听事件
        this.ivPwdSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pwdSwitch();
            }
        });
    }

    public void pwdSwitch() {
        pwdFlag = !pwdFlag;
        if (pwdFlag) {
            ivPwdSwitch.setImageResource(
                    R.drawable.invisible);
            etPwd.setInputType(
                    InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            ivPwdSwitch.setImageResource(
                    R.drawable.visible);
            etPwd.setInputType(
                    InputType.TYPE_TEXT_VARIATION_PASSWORD |
                            InputType.TYPE_CLASS_TEXT);
            etPwd.setTypeface(Typeface.DEFAULT);
        }
    }
}
