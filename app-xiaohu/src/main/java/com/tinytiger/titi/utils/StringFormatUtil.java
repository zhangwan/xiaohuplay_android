package com.tinytiger.titi.utils;

import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.RelativeSizeSpan;
import android.view.ViewTreeObserver;
import android.widget.TextView;

import java.math.BigDecimal;

/*
 * @author Tamas
 * create at 2019/11/13 0013
 * Email: ljw_163mail@163.com
 * description:
 */
public class StringFormatUtil {


    /**
     * 验证手机格式
     */
    public static boolean isMobileMatch(String mobiles) {
        if (mobiles == null || mobiles.length() != 11) {
            return false;
        }
        String regExp = "^[1][0-9]{10}$";
//        String regExp = "^((13[0-9])|(15[^4])|(18[0,2,3,5-9])|(17[0-8])|(147))\\d{8}$";
        if (TextUtils.isEmpty(mobiles)) return false;
        else return mobiles.matches(regExp);
    }


    /**
     * 数字格式
     */
    public static String formatBigNum(int num) {
        try {
            StringBuffer sb = new StringBuffer();


            BigDecimal b0 = new BigDecimal("1000");
            BigDecimal b1 = new BigDecimal("10000");
            BigDecimal b2 = new BigDecimal("100000000");
            BigDecimal b3 = new BigDecimal(num);

            String formatedNum = "";//输出结果
            String unit = "";//单位

            if (b3.compareTo(b0) == -1) {
                sb.append(b3.toString());
            } else if ((b3.compareTo(b0) == 0 || b3.compareTo(b0) == 1)
                    || b3.compareTo(b1) == -1) {
                formatedNum = b3.divide(b0).toString();
                unit = "k";
            } else if ((b3.compareTo(b1) == 0 && b3.compareTo(b1) == 1)
                    || b3.compareTo(b2) == -1) {
                formatedNum = b3.divide(b1).toString();
                unit = "w";
            } else if (b3.compareTo(b2) == 0 || b3.compareTo(b2) == 1) {
                formatedNum = b3.divide(b2).toString();
                unit = "亿";
            }
            if (!"".equals(formatedNum)) {
                int i = formatedNum.indexOf(".");
                if (i == -1) {
                    sb.append(formatedNum).append(unit);
                } else {
                    i = i + 1;
                    String v = formatedNum.substring(i, i + 1);
                    if (!v.equals("0")) {
                        sb.append(formatedNum.substring(0, i + 1)).append(unit);
                    } else {
                        sb.append(formatedNum.substring(0, i - 1)).append(unit);
                    }
                }
            }
            if (sb.length() == 0)
                return "0";
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return String.valueOf(num);
        }

    }

    public static String getMobileReplace(String mobile) {
        if (isMobileMatch(mobile)) {
            return mobile.substring(0, 3) + "****" + mobile.substring(7, mobile.length());
        } else {
            return mobile;
        }
    }


    /**
     * 设置textView结尾...后面显示的文字和颜色
     *
     * @param context    上下文
     * @param textView   textview
     * @param minLines   最少的行数
     * @param originText 原文本
     * @param endText    结尾文字
     * @param endColorID 结尾文字颜色id
     * @param isExpand   当前是否是展开状态
     */
    public static void toggleEllipsize(final Context context,
                                       final TextView textView,
                                       final int minLines,
                                       final String originText,
                                       final String endText,
                                       final int endColorID,
                                       final boolean isExpand) {
        if (TextUtils.isEmpty(originText)) {
            return;
        }
        textView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver
                .OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (isExpand) {
                    textView.setText(originText);
                } else {
                    int paddingLeft = textView.getPaddingLeft();
                    int paddingRight = textView.getPaddingRight();
                    TextPaint paint = textView.getPaint();
                    float moreText = textView.getTextSize() * endText.length();
                    float availableTextWidth = (textView.getWidth() - paddingLeft - paddingRight) *
                            minLines - moreText;
                    CharSequence ellipsizeStr = TextUtils.ellipsize(originText, paint,
                            availableTextWidth, TextUtils.TruncateAt.END);
                    if (ellipsizeStr.length() < originText.length()) {
                        CharSequence temp = ellipsizeStr + endText;
                        SpannableStringBuilder ssb = new SpannableStringBuilder(temp);
                        ssb.setSpan(new ForegroundColorSpan(context.getResources().getColor
                                        (endColorID)),
                                temp.length() - endText.length(), temp.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                        textView.setText(ssb);
                    } else {
                        textView.setText(originText);
                    }
                }
                if (Build.VERSION.SDK_INT >= 16) {
                    textView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                } else {
                    textView.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }
            }
        });
    }


    public static void toggleEllipsize(final Context context,
                                       final TextView textView,
                                       final int minLines,
                                       final String originText,
                                       final String endText,
                                       final int endColorID) {
        if (TextUtils.isEmpty(originText)) {
            return;
        }


        textView.post(new Runnable() {
            @Override
            public void run() {
                int paddingLeft = textView.getPaddingLeft();
                int paddingRight = textView.getPaddingRight();
                TextPaint paint = textView.getPaint();
                float moreText = textView.getTextSize() * endText.length();
                float availableTextWidth = (textView.getWidth() - paddingLeft - paddingRight) *
                        minLines - moreText;
                CharSequence ellipsizeStr = TextUtils.ellipsize(originText, paint,
                        availableTextWidth, TextUtils.TruncateAt.END);
                CharSequence temp = ellipsizeStr + endText;
                SpannableStringBuilder ssb = new SpannableStringBuilder(temp);
                ssb.setSpan(new ForegroundColorSpan(context.getResources().getColor
                                (endColorID)),
                        temp.length() - endText.length(), temp.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
                if (ellipsizeStr.length() < originText.length()) {
                    textView.setText(ssb);
                } else {
                    textView.setText(originText);
                }
            }
        });
    }


    public static void getLastIndexForLimit(Context context, TextView tv, int maxLine, String content) {
        //获取TextView的画笔对象
        TextPaint paint = tv.getPaint();
        //每行文本的布局宽度
        int width = context.getResources().getDisplayMetrics().widthPixels - dip2px(context, 40);
        //实例化StaticLayout 传入相应参数
        StaticLayout staticLayout = new StaticLayout(content, paint, width, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        //判断content是行数是否超过最大限制行数3行
        if (staticLayout.getLineCount() > maxLine) {
            //定义展开后的文本内容

            //获取到第三行最后一个文字的下标
            int index = staticLayout.getLineStart(maxLine) - 1;
            //定义收起后的文本内容
            String substring = content.substring(0, index - 4) + "..." + "查看全部";
            SpannableString elipseString = new SpannableString(substring);
            //给查看全部设成蓝色
            elipseString.setSpan(new ForegroundColorSpan(Color.parseColor("#0079e2")), substring.length() - 4, substring.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            //设置收起后的文本内容
            tv.setText(elipseString);
            //将textview设成选中状态 true用来表示文本未展示完全的状态,false表示完全展示状态，用于点击时的判断
            tv.setSelected(true);
        } else {
            //没有超过 直接设置文本
            tv.setText(content);
        }
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context mContext, float dpValue) {
        final float scale = mContext.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 改变同一个textview中使字体不一致，一大一小
     *
     * @param value
     * @return
     */
    public static SpannableString changTVsize(String value) {
        SpannableString spannableString = new SpannableString(value);
        if (value.contains(".")) {
            spannableString.setSpan(new RelativeSizeSpan(0.7f), value.indexOf("."), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }

}
