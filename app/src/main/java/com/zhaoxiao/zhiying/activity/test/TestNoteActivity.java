package com.zhaoxiao.zhiying.activity.test;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.xuexiang.xui.utils.XToastUtils;
import com.xuexiang.xui.widget.dialog.bottomsheet.BottomSheet;
import com.xuexiang.xui.widget.dialog.materialdialog.MaterialDialog;
import com.zhaoxiao.zhiying.R;
import com.zhaoxiao.zhiying.activity.BaseActivity;
import com.zhaoxiao.zhiying.activity.study.ArticleActivity;
import com.zhaoxiao.zhiying.api.StudyService;
import com.zhaoxiao.zhiying.api.TestService;
import com.zhaoxiao.zhiying.entity.study.ArticleNote;
import com.zhaoxiao.zhiying.entity.study.Data;
import com.zhaoxiao.zhiying.entity.test.TestNoteDetail;
import com.zhaoxiao.zhiying.util.StringUtils;
import com.zhaoxiao.zhiying.util.spTime.SpUtils;
import com.zhaoxiao.zhiying.view.CircleCornerTransForm;

import java.io.Serializable;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestNoteActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_top_title)
    TextView tvTopTitle;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.rl_title)
    RelativeLayout rlTitle;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_sub_type)
    TextView tvSubType;
    @BindView(R.id.iv_more)
    ImageView ivMore;
    @BindView(R.id.et_note)
    EditText etNote;
    @BindView(R.id.rl_link)
    LinearLayout rlLink;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.divider)
    View divider;
    @BindView(R.id.tv_note)
    TextView tvNote;
    private Map<String, Object> map;
    private String questionInfo;
    private String subType;
    private String img;
    private boolean flag;
    private TestService testService;
    private String account;
    private Integer questionId;
    private Integer table;

    private Boolean link = false;
    private Boolean edit = false;

    @Override
    protected int initLayout() {
        return R.layout.activity_test_note;
    }

    @Override
    protected void initData() {
        account = SpUtils.getInstance(this).getString("account", "");

        map = (Map<String, Object>) getIntent().getSerializableExtra("map");
        questionInfo = (String) map.get("questionInfo");
        subType = (String) map.get("subType");
        questionId = (Integer) map.get("questionId");
        table = (Integer) map.get("table");
        link = (Boolean) map.get("link");
        edit = (Boolean) map.get("edit");
        if (map.get("account")!=null){
            account = (String) map.get("account");
        }

        if (link) {
            rlLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    map.put("questionId",questionId);
                    map.put("table",table);
                    navigateTo(QuestionDetailActivity.class, "map", (Serializable) map);
                }
            });
        }
        if (edit == null || !edit) {
            etNote.setEnabled(false);
            btnSave.setVisibility(View.INVISIBLE);
            btnSave.setEnabled(false);
            ivMore.setVisibility(View.GONE);
            tvTopTitle.setText("笔记");
            etNote.setHint("");
        }

//        account = SpUtils.getInstance(this).getString("account", "");
        testService = (TestService) getService(TestService.class);

        tvTitle.setText(questionInfo);
        tvSubType.setText(subType);

        if (etNote.getText().toString().trim().equals("")) {
            btnSave.setEnabled(false);
            flag = false;
        } else {
            btnSave.setEnabled(true);
            flag = true;
        }
        etNote.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().equals("")) {
                    btnSave.setEnabled(false);
                    flag = false;
                } else {
                    btnSave.setEnabled(true);
                    flag = true;
                }
            }
        });

        getNode();
    }

    private void getNode() {
        Call<Data<TestNoteDetail>> testNoteCall = testService.getTestNote(account, questionId, table);
        testNoteCall.enqueue(new Callback<Data<TestNoteDetail>>() {
            @Override
            public void onResponse(Call<Data<TestNoteDetail>> call, Response<Data<TestNoteDetail>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    TestNoteDetail testNoteDetail = response.body().getData();
                    if (testNoteDetail != null) {
                        tvTime.setText(StringUtils.formatDateTime(testNoteDetail.getAddTime()));
                        etNote.setText(testNoteDetail.getInfo());
                        tvNote.setText(testNoteDetail.getInfo());
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<TestNoteDetail>> call, Throwable t) {

            }
        });
    }

    @OnClick({R.id.iv_back, R.id.btn_save, R.id.iv_more})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.btn_save:
                saveNote(false);
                break;
            case R.id.iv_more:
                showSimpleBottomSheetList();
                break;
        }
    }

    private void saveNote(boolean isShare) {
        Call<Data<Boolean>> saveNoteCall = testService.saveTestNote(account, questionId,table, etNote.getText().toString().trim());
        saveNoteCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()) {
                        XToastUtils.toast("保存成功");
                        if (isShare) {
                            XToastUtils.toast("跳转分享页面");
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }

    //更多
    private void showSimpleBottomSheetList() {
        new BottomSheet.BottomListSheetBuilder(this)
                .setTitle("更多")
                .addItem("分享")
                .addItem("删除")
                .setIsCenter(true)
                .setOnSheetItemClickListener((dialog, itemView, position, tag) -> {
                    dialog.dismiss();
                    if (position == 0) {
                        if (flag) {
                            saveNote(true);
                        } else {
                            XToastUtils.toast("笔记不能为空");
                        }
//                        XToastUtils.toast("分享");
                    } else if (position == 1) {
                        new MaterialDialog.Builder(this)
                                .iconRes(R.drawable.icon_warning)
                                .title(R.string.tip_warning)
                                .content("笔记删除后无法恢复，确定要删除吗？")
                                .positiveText("取消")
                                .negativeText("确定删除")
                                .negativeColor(getResources().getColor(R.color.g_red))
                                .onNegative((dialog1, which) -> {
//                                    XToastUtils.toast("确定删除");
                                    deleteNote();
                                })
                                .show();
                    }
                })
                .build()
                .show();
    }

    private void deleteNote() {
        Call<Data<Boolean>> deleteNoteCall = testService.deleteTestNote(account, questionId,table);
        deleteNoteCall.enqueue(new Callback<Data<Boolean>>() {
            @Override
            public void onResponse(Call<Data<Boolean>> call, Response<Data<Boolean>> response) {
                if (response.body() != null && response.body().getCode() == 10000) {
                    if (response.body().getData()) {
                        XToastUtils.toast("删除成功");
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<Data<Boolean>> call, Throwable t) {

            }
        });
    }
}