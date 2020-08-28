package com.tinytiger.titi.widget.text;


import android.content.Context;
import android.content.res.TypedArray;

import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tinytiger.common.event.ClassEvent;
import com.tinytiger.common.utils.Dp2PxUtils;
import com.tinytiger.common.utils.image.GlideUtil;
import com.tinytiger.titi.R;

import org.greenrobot.eventbus.EventBus;

/*
 * @author Tamas
 * create at 2020/4/1 0001
 * Email: ljw_163mail@163.com
 * description: LinearLayout 昵称 后面加勋章的图案
 */
public class MedalTextView extends LinearLayout {

    public MedalTextView(@NonNull Context context) {
        this(context, null);
    }

    public MedalTextView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MedalTextView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init(context);
    }

    private String text;
    private int textColor;
    private float textSize;
    private boolean is_bold;

    private ImageView ivMedal;
    private TextView tvNickname;

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.MedalTextView);
            is_bold = typedArray.getBoolean(R.styleable.MedalTextView_medal_is_bold, false);
            textSize = typedArray.getDimension(R.styleable.MedalTextView_medal_textSize, 0f);
            textColor = typedArray.getColor(R.styleable.MedalTextView_medal_textColor, 0);
            typedArray.recycle();
        }
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.medal_name_item_layout, this);
        ivMedal = view.findViewById(R.id.iv_medal);
        tvNickname = view.findViewById(R.id.tv_medal);

        if (is_bold) {
            tvNickname.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
        }
        if (textSize != 0) {
            tvNickname.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        }
        if (textColor != 0) {
            tvNickname.setTextColor(textColor);
        }

    }

    //设置勋章url
    public MedalTextView setMedalIcon(String url) {
        if (url != null && !url.isEmpty()) {
            tvNickname.setPadding(0, 0, Dp2PxUtils.dip2px(tvNickname.getContext(), 35), 0);
            GlideUtil.loadImg(ivMedal, url);
            ivMedal.setVisibility(VISIBLE);
        }else {
            ivMedal.setVisibility(GONE);
        }
        return this;
    }

    //设置昵称
    public MedalTextView setNickname(String text) {
        if (text != null && !text.isEmpty()) {
            tvNickname.setText(text);
        }
        return this;
    }

    public MedalTextView setNickname(String text,String url) {
        if (text != null && !text.isEmpty()) {
            tvNickname.setText(text);
        }
        if (url != null && !url.isEmpty()) {
            tvNickname.setPadding(0, 0, Dp2PxUtils.dip2px(tvNickname.getContext(), 35), 0);
            GlideUtil.loadImg(ivMedal, url);
            ivMedal.setVisibility(VISIBLE);
        }else {
            ivMedal.setVisibility(GONE);
        }
        return this;
    }

    public MedalTextView setUserId(String user) {
        tvNickname.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(new ClassEvent("HomepageActivity", user));
            }
        });
        return this;
    }
}
