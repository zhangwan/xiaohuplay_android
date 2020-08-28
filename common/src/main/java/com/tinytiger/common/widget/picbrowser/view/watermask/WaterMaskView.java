package com.tinytiger.common.widget.picbrowser.view.watermask;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tinytiger.common.R;


/**
 * @author lmq001
 * @date 2020/07/27 10:50
 * @copyright 小虎互联科技
 * @doc 自定义水印view 支持设置logo、公司名称、相关信息
 */
public class WaterMaskView extends RelativeLayout {
    private ImageView btLogoImg;
    private TextView tvCompanyText;
    private TextView tvInfoText;

    /**
     * logo的图片资源ID
     */
    private int logoButtonDrawable;
    /**
     * 公司名称字符串
     */
    private String companyText;
    /**
     * 相关信息字符串
     */
    private String infoText;

    public WaterMaskView(Context context) {
        this(context, null);
    }

    public WaterMaskView(Context context, AttributeSet attrs) {
        super(context, attrs);

        LayoutInflater.from(context).inflate(R.layout.layout_watermask, this, true);
        btLogoImg = (ImageView) findViewById(R.id.iv_logo_img);
        tvCompanyText = (TextView) findViewById(R.id.tv_company_text);
        tvInfoText = (TextView) findViewById(R.id.tv_info_text);

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.WaterMaskView);
        if (typedArray != null) {
            //logo和公司名称这一栏的背景色
            int titleBarBackGround = typedArray.getColor(R.styleable.WaterMaskView_title_background_color, Color.TRANSPARENT);
            setBackgroundColor(titleBarBackGround);

            //设置logo的文字
//            String logoButtonText = typedArray.getString(R.styleable.WaterMaskView_logo_button_text);
//            if (!TextUtils.isEmpty(logoButtonText)) {
//                btLogoImg.setText(logoButtonText);
//                //设置logo文字颜色
//                int logoButtonTextColor = typedArray.getColor(R.styleable.WaterMaskView_logo_button_text_color, Color.WHITE);
//                btLogoImg.setTextColor(logoButtonTextColor);
//            } else {
//                //设置logo的icon 这里是二选一 要么只能是文字 要么只能是图片
//                logoButtonDrawable = typedArray.getResourceId(R.styleable.WaterMaskView_logo_button_drawable, R.drawable.ic_hoo_logo);
//                if (logoButtonDrawable != -1) {
//                    btLogoImg.setBackgroundResource(logoButtonDrawable);
//                }
//            }

            logoButtonDrawable = typedArray.getResourceId(R.styleable.WaterMaskView_logo_button_drawable, R.mipmap.ic_hoo_logo);
            btLogoImg.setImageResource(logoButtonDrawable);

            //获取公司名称
            companyText = typedArray.getString(R.styleable.WaterMaskView_company_text);
            if (!TextUtils.isEmpty(companyText)) {
                tvCompanyText.setText(companyText);
            }
            //获取公司名称显示颜色
            int titleTextColor = typedArray.getColor(R.styleable.WaterMaskView_title_text_color, Color.WHITE);
            tvCompanyText.setTextColor(titleTextColor);

            //设置相关信息文字
            if (!TextUtils.isEmpty(infoText)) {
                tvInfoText.setText(infoText);
                //设置相关信息文字颜色
                int infoTextColor = typedArray.getColor(R.styleable.WaterMaskView_info_text_color, Color.WHITE);
                tvInfoText.setTextColor(infoTextColor);
            } else {
                infoText = typedArray.getString(R.styleable.WaterMaskView_info_text);

                if (!TextUtils.isEmpty(infoText)) {
                    tvInfoText.setText(infoText);
                    //设置相关信息文字颜色
                    int infoTextColor = typedArray.getColor(R.styleable.WaterMaskView_info_text_color, Color.WHITE);
                    tvInfoText.setTextColor(infoTextColor);
                }
            }

            typedArray.recycle();
        }

    }

    /**
     * 设置logo的图片资源ID
     *
     * @param btLogoImg
     */
    public void setBtLogoImg(ImageView btLogoImg) {
        this.btLogoImg = btLogoImg;
        btLogoImg.setImageResource(logoButtonDrawable);
    }

    /**
     * 设置公司名称文字
     *
     * @param companyText
     */
    public void setCompanyText(String companyText) {
        this.companyText = companyText;
        tvCompanyText.setText(companyText);
    }

    /**
     * 设置相关信息文字
     *
     * @param infoText
     */
    public void setInfoText(String infoText) {
        this.infoText = infoText;
        tvInfoText.setText(infoText);
    }

}
