package com.tinytiger.common.view.title;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import com.tinytiger.common.R;
import com.tinytiger.common.utils.image.GlideUtil;

/**
 *
 *@author luke
 *@date 2018/5/30 19:24
 *@doc
 *
 */
public class TitleView extends RelativeLayout {

    private Context context;
    public TitleView(Context context) {
        super(context);
        this.context=context;
        initView();
    }

    public TitleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        initView();
        initAttrs(attrs);
    }


    private void initAttrs(AttributeSet attrs) {
        TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.TitleView, 0, 0);
        try {
            //中间文本内容
            CharSequence text = ta.getText(R.styleable.TitleView_CenterText);
            if (!TextUtils.isEmpty(text)) {
                tv_title.setVisibility(View.VISIBLE);
                tv_title.setText(text);
            } else {
                tv_title.setVisibility(View.GONE);
            }
            int center_color = ta.getColor(R.styleable.TitleView_CenterTextColor, ContextCompat.getColor(context, R.color.gray28));
            tv_title.setTextColor(center_color);


            //左边文本内容
            CharSequence leftDownText = ta.getText(R.styleable.TitleView_leftDownText);
            if (!TextUtils.isEmpty(leftDownText)) {
                left_back_iv.setVisibility(View.GONE);
                left_down_tv.setVisibility(View.VISIBLE);
                left_down_tv.setText(leftDownText);
                int leftDownColor = ta.getColor(R.styleable.TitleView_leftDownColor, ContextCompat.getColor(context, R.color.gray28));
                left_down_tv.setTextColor(leftDownColor);
            } else {
                left_down_tv.setVisibility(View.GONE);
                int rightVisible = ta.getInt(R.styleable.TitleView_showLeftIcon, 0);
                if (rightVisible == 0) {
                    left_back_iv.setVisibility(View.VISIBLE);
                } else if (rightVisible == 1) {
                    left_back_iv.setVisibility(View.GONE);
                } else if (rightVisible == 2) {
                    left_back_iv.setVisibility(View.INVISIBLE);
                }
            }

            //右边
            CharSequence rightTxt = ta.getText(R.styleable.TitleView_rightText);

            if (!TextUtils.isEmpty(leftDownText)) {
                right_tv.setText(rightTxt);
            }
            int righttxt_color = ta.getColor(R.styleable.TitleView_rightTxtColor, ContextCompat.getColor(context, R.color.gray28));
            right_tv.setTextColor(righttxt_color);


            // 右邊選擇按鈕
            CharSequence righttxt = ta.getText(R.styleable.TitleView_rightDownText);
            if (!TextUtils.isEmpty(righttxt)) {
                right_down_tv.setVisibility(View.VISIBLE);
                right_down_tv.setText(righttxt);
                right_tv.setVisibility(View.GONE);
            } else {
                right_down_tv.setVisibility(View.GONE);
            }

            int rightVisible = ta.getInt(R.styleable.TitleView_showLine, 0);
            if (rightVisible == 0) {
                ivLine.setVisibility(View.VISIBLE);
            } else if (rightVisible == 1) {
                ivLine.setVisibility(View.GONE);
            } else if (rightVisible == 2) {
                ivLine.setVisibility(View.INVISIBLE);
            }

        }finally {
            ta.recycle();
        }
    }
    TextView tv_title,left_down_tv;

    TextView  right_tv,right_down_tv;
    ImageView left_back_iv,right_iv,ivLine;

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.ui_title, this);

        tv_title= findViewById(R.id.tv_title);

        left_back_iv= findViewById(R.id.left_back_iv);
        left_down_tv= findViewById(R.id.left_down_tv);

        right_iv= findViewById(R.id.right_iv);
        right_tv= findViewById(R.id.right_tv);
        right_down_tv= findViewById(R.id.right_down_tv);

        ivLine= findViewById(R.id.ivLine);
    }



    /**
     * 设置返回监听

     * @param onClickListener
     */
    public void setBackOnClick(OnClickListener onClickListener) {
        left_back_iv.setOnClickListener(onClickListener);
    }

    public void setLeftDownOnClick(OnClickListener onClickListener ) {
        left_down_tv.setOnClickListener(onClickListener);
    }

    public void setLeftDown(int rightVisible ) {
        if (rightVisible == View.VISIBLE) {
            left_down_tv.setVisibility(View.VISIBLE);
        } else if (rightVisible == View.GONE) {
            left_down_tv.setVisibility(View.GONE);
        } else if (rightVisible == View.INVISIBLE) {
            left_down_tv.setVisibility(View.INVISIBLE);
        }
    }

    public void setIvLine(int rightVisible ) {
        if (rightVisible == View.VISIBLE) {
            ivLine.setVisibility(View.VISIBLE);
        } else if (rightVisible == View.GONE) {
            ivLine.setVisibility(View.GONE);
        } else if (rightVisible == View.INVISIBLE) {
            ivLine.setVisibility(View.INVISIBLE);
        }
    }

    public void setRightOnClick(OnClickListener onClickListener) {
        right_tv.setOnClickListener(onClickListener);
    }

    public void setCenterTxt(String text) {
        tv_title.setVisibility(View.VISIBLE);
        tv_title.setText(text);
    }




    public String getCenterTxt() {
        return tv_title.getText().toString();
    }


    public void setLeftDownTxt(String text) {
        left_back_iv.setVisibility(View.GONE);
        left_down_tv.setVisibility(View.VISIBLE);
        left_down_tv.setText(text);
    }

    public void setRightTxt(String text) {
        right_tv.setText(text);
        right_tv.setVisibility(View.VISIBLE);
    }


    public void setRightTxtColor(int color){
        right_tv.setTextColor(color);
    }

    public void setRightV(int v) {
        right_tv.setVisibility(v);
    }

    public String getRightTxt() {
        return right_tv.getText().toString();
    }

    public void setRightIcon(int icon) {
        right_tv.setCompoundDrawablesWithIntrinsicBounds(
                ContextCompat.getDrawable(context, icon), null, null, null);
    }

    public void setRightVis(int rightVisible) {
        right_tv.setVisibility(rightVisible);
    }

    public ImageView getRightIV() {
        return right_iv;
    }

    public void setRightIV(String icon) {
        right_iv.setVisibility(View.VISIBLE);
        GlideUtil.loadImg(right_iv,icon);
    }
    public void setRightIV(String icon ,int error) {
        right_iv.setVisibility(View.VISIBLE);
        GlideUtil.loadImg(right_iv,icon,error);
    }

    public void setRightIVClick(OnClickListener onClickListener) {
        right_iv.setVisibility(View.VISIBLE);
        right_iv.setOnClickListener(onClickListener);
    }

    public void setRightIV(int v) {
        right_iv.setVisibility(v);
    }
    public void setRightIVIcon(int icon) {
        right_iv.setImageDrawable(ContextCompat.getDrawable(context, icon));
    }


    public void setRightDownClick( OnClickListener onClickListener) {
        right_down_tv.setOnClickListener(onClickListener);
    }

    public void setRightDownTxt(String text ) {
        right_down_tv.setText(text);
    }
}