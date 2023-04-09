package com.zhaoxiao.zhiying.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;

import com.donkingliang.labels.LabelsView;
import com.zhaoxiao.zhiying.R;

import butterknife.BindView;

public class SearchBarView extends LinearLayout {

    private final LayoutParams layoutParams1;
    private final LayoutParams layoutParams2;
    ImageView ivSearch;
    TextView tvSearch;
    LinearLayout layoutSearch;
    TextView tvRight;
    ImageView ivRight1;
    ImageView ivRight2;
    CardView cardView;

    private onViewClick mClick;

    public enum CardViewType {
        White(1),
        Gray(2);

        int value;

        CardViewType(int value) {
            this.value = value;
        }

        static CardViewType get(int value) {
            if (value == 2) {
                return Gray;
            }
            return White;
        }
    }

    public SearchBarView(Context context) {
        this(context, null);
    }

    public SearchBarView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SearchBarView(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        LayoutInflater.from(context).inflate(R.layout.layout_search_bar, this);
        initView();
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.SearchBarView, defStyleAttr, 0);
        layoutParams1 = (LayoutParams) ivRight1.getLayoutParams();
        layoutParams2 = (LayoutParams) ivRight2.getLayoutParams();
        int count = array.getIndexCount();
        for (int i = 0; i < count; i++) {
            int attr = array.getIndex(i);
            switch (attr) {
                case R.styleable.SearchBarView_sb_search:
                    if(array.getBoolean(attr,true))
                        layoutSearch.setVisibility(VISIBLE);
                    else
                        layoutSearch.setVisibility(GONE);
                    break;
                case R.styleable.SearchBarView_sb_searchDrawable:
                    ivSearch.setImageResource(array.getResourceId(attr, 0));
                    break;
                case R.styleable.SearchBarView_sb_searchHintText:
                    tvSearch.setText(array.getString(attr));
                    break;
                case R.styleable.SearchBarView_sb_rightText:
                    tvRight.setText(array.getString(attr));
                    tvRight.setVisibility(VISIBLE);
                    break;
                case R.styleable.SearchBarView_sb_rightTextSize:
                    tvRight.setTextSize(TypedValue.COMPLEX_UNIT_PX, array.getDimension(attr, 0));
                    break;
                case R.styleable.SearchBarView_sb_rightTextColor:
                    tvRight.setTextColor(array.getColor(attr, Color.GRAY));
                    break;
                case R.styleable.SearchBarView_sb_rightDrawable1:
                    ivRight1.setImageResource(array.getResourceId(attr, 0));
                    ivRight1.setVisibility(VISIBLE);
                    break;
                case R.styleable.SearchBarView_sb_rightDrawable2:
                    ivRight2.setImageResource(array.getResourceId(attr, 0));
                    ivRight2.setVisibility(VISIBLE);
                    break;
                case R.styleable.SearchBarView_sb_rightDrawable1_width:
                    layoutParams1.width = (int) array.getDimension(attr, 0);
                    break;
                case R.styleable.SearchBarView_sb_rightDrawable1_height:
                    layoutParams1.height = (int) array.getDimension(attr, 0);
                    break;
                case R.styleable.SearchBarView_sb_rightDrawable2_width:
                    layoutParams2.width = (int) array.getDimension(attr, 0);
                    break;
                case R.styleable.SearchBarView_sb_rightDrawable2_height:
                    layoutParams2.height = (int) array.getDimension(attr, 0);
                    break;
                case R.styleable.SearchBarView_sb_rightDrawable1_marginLeft:
                    layoutParams1.leftMargin = (int) array.getDimension(attr, 0);
                    break;
                case R.styleable.SearchBarView_sb_rightDrawable2_marginLeft:
                    layoutParams2.leftMargin = (int) array.getDimension(attr, 0);
                    break;
                case R.styleable.SearchBarView_sb_type:
                    if(array.getInt(attr,1)==2){
                        cardView.setCardBackgroundColor(getResources().getColor(R.color.light_gray));
                        cardView.setCardElevation(0);
                    } else {
                        cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
                        cardView.setCardElevation(2);
                    }
                    break;
                case R.styleable.SearchBarView_sb_rightDrawable1_tint:
                    Drawable drawable1 = ivRight1.getDrawable();
                    Drawable wrap1 = DrawableCompat.wrap(drawable1);
                    DrawableCompat.setTint(wrap1, array.getColor(attr, Color.GRAY));
                    ivRight1.setImageDrawable(wrap1);
                    break;
                case R.styleable.SearchBarView_sb_rightDrawable2_tint:
                    Drawable drawable2 = ivRight2.getDrawable();
                    Drawable wrap2 = DrawableCompat.wrap(drawable2);
                    DrawableCompat.setTint(wrap2, array.getColor(attr, Color.GRAY));
                    ivRight2.setImageDrawable(wrap2);
                    break;
            }
        }
        array.recycle();
        layoutSearch.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.searchClick();
            }
        });
        tvRight.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.rightTextClick();
            }
        });
        ivRight1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.rightDrawable1Click();
            }
        });
        ivRight2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                mClick.rightDrawable2Click();
            }
        });

    }

    private void initView() {
        ivSearch = findViewById(R.id.iv_search);
        tvSearch = findViewById(R.id.tv_search);
        layoutSearch = findViewById(R.id.layout_search);
        tvRight = findViewById(R.id.tv_right);
        ivRight1 = findViewById(R.id.iv_right1);
        ivRight2 = findViewById(R.id.iv_right2);
        cardView = findViewById(R.id.cardView);
    }

    public void setOnViewClick(onViewClick click) {
        this.mClick = click;
    }

    //设置搜索图标
    public void setSearchDrawable(int res) {
        if (ivSearch != null) {
            ivSearch.setImageResource(res);
        }
    }

    //设置搜索提示词
    public void setSearchHintText(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvSearch.setText(title);
        }
    }

    //设置右文字
    public void setRightText(String title) {
        if (!TextUtils.isEmpty(title)) {
            tvRight.setText(title);
        }
    }

    //设置右文字大小
    public void setRightTextSize(int size) {
        if (tvRight != null) {
            tvRight.setTextSize(TypedValue.COMPLEX_UNIT_SP, size);
        }
    }

    //设置右文字颜色
    public void setRightTextColor(int color) {
        if (tvRight != null) {
            tvRight.setTextColor(color);
        }
    }

    //设置右图标1
    public void setRightDrawable1(int res) {
        if (ivRight1 != null) {
            ivRight1.setImageResource(res);
        }
    }

    //设置右图标2
    public void setRightDrawable2(int res) {
        if (ivRight2 != null) {
            ivRight2.setImageResource(res);
        }
    }

    //设置右图标1宽度
    public void setRightDrawable1_Width(int res) {
        if (ivRight1 != null) {
            layoutParams1.width = res;
        }
    }

    //设置右图标1高度
    public void setRightDrawable1_Height(int res) {
        if (ivRight1 != null) {
            layoutParams1.height = res;
        }
    }

    //设置右图标2宽度
    public void setRightDrawable2_Width(int res) {
        if (ivRight2 != null) {
            layoutParams2.width = res;
        }
    }

    //设置右图标2高度
    public void setRightDrawable2_Height(int res) {
        if (ivRight2 != null) {
            layoutParams2.height = res;
        }
    }

    //设置右图标1marginLeft
    public void setRightDrawable1_MarginLeft(int res) {
        if (ivRight1 != null) {
            layoutParams1.leftMargin = res;
        }
    }

    //设置右图标2marginLeft
    public void setRightDrawable2_MarginLeft(int res) {
        if (ivRight2 != null) {
            layoutParams2.leftMargin = res;
        }
    }

    //设置cardView类型
    public void setCardViewType(CardViewType cardViewType) {
        if(cardViewType==CardViewType.Gray){
            cardView.setCardBackgroundColor(getResources().getColor(R.color.background_gray));
            cardView.setCardElevation(0);
        } else {
            cardView.setCardBackgroundColor(getResources().getColor(R.color.white));
            cardView.setCardElevation(2);
        }
    }

    //设置右图标1tint
    public void setRightDrawable1_Tint(int color) {
        Drawable drawable1 = ivRight2.getDrawable();
        Drawable wrap1 = DrawableCompat.wrap(drawable1);
        DrawableCompat.setTint(wrap1, color);
        ivRight2.setImageDrawable(wrap1);
    }

    //设置右图标2tint
    public void setRightDrawable2_Tint(int color) {
        Drawable drawable2 = ivRight2.getDrawable();
        Drawable wrap2 = DrawableCompat.wrap(drawable2);
        DrawableCompat.setTint(wrap2, color);
        ivRight2.setImageDrawable(wrap2);
    }

    public interface onViewClick {
        void searchClick();

        void rightTextClick();

        void rightDrawable1Click();

        void rightDrawable2Click();
    }

}
