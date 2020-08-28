package com.tinytiger.common.view.text;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.tinytiger.common.R;
import com.tinytiger.common.utils.Dp2PxUtils;

/*
 * @author Tamas
 * create at 2019/11/15 0015
 * Email: ljw_163mail@163.com
 * description:
 */
public class BaseItemLayout extends FrameLayout {
    private ImageView ivImage;
    private TextView tvTitle;
    private TextView tvSubtitle;
    public TextView tvInfo;
    private TextView tvDot;

    private int resourceId;
    private String titleText;
    private String subtitleText;
    private String infoText;


    public BaseItemLayout(Context context) {
        this(context, null);
    }

    public BaseItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BaseItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttr(context, attrs);
        init(context);
    }

    private void initAttr(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs,
                    R.styleable.BaseItemView);
            resourceId = typedArray.getResourceId(R.styleable.BaseItemView_image, 0);
            titleText = typedArray.getString(R.styleable.BaseItemView_titleText);
            subtitleText = typedArray.getString(R.styleable.BaseItemView_subtitleText);
            infoText = typedArray.getString(R.styleable.BaseItemView_infoText);
            typedArray.recycle();
        }
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.base_item_layout, this);
        ivImage = view.findViewById(R.id.iv_image);
        tvTitle = view.findViewById(R.id.tv_title);
        tvSubtitle = view.findViewById(R.id.tv_subtitle);
        tvInfo = view.findViewById(R.id.tv_info);
        tvDot=view.findViewById(R.id.tv_dot);

        tvTitle.setText(titleText);
        tvInfo.setText(infoText);
        if (resourceId != 0) {
            int dp = Dp2PxUtils.dip2px(context,6);
            ivImage.setImageResource(resourceId);
            ivImage.setPadding(dp,dp,dp,dp);
        }
        if (!TextUtils.isEmpty(subtitleText)) {
            tvSubtitle.setText(subtitleText);
            tvSubtitle.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 获取 信息
     *
     * @return
     */
    public String getContextText() {
        return infoText;
    }

    /**
     * 设置 内容
     *
     * @param content
     */
    public void setContentText(String content) {
        infoText = content;
        if (tvInfo == null) {
            return;
        }
        tvInfo.setText(infoText);
    }
    public void setDotVisible(int visible){
        tvDot.setVisibility(visible);
    }


    /**
     * 设置 title
     *
     * @param title
     */
    public void setTitleText(String title) {
        titleText = title;
        if (tvTitle == null) {
            return;
        }
        tvTitle.setText(titleText);
    }

    /**
     * 设置 次标题
     *
     * @param subtitle
     */
    public void setSubtitleText(String subtitle) {
        subtitleText = subtitle;
        if (tvSubtitle == null) {
            return;
        }
        tvSubtitle.setText(subtitle);
        tvSubtitle.setVisibility(View.VISIBLE);
    }

}
