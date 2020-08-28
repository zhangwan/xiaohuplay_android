package com.tinytiger.common.utils;

import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.view.ViewGroup;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 *
 * @author zhw_luke
 * @date 2019/11/13 0013 下午 10:14
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc view 操作工具类
 */
public class ViewUtils {

    public static void setMargins (View v, int l, int t, int r, int b) {
        if (v.getLayoutParams() instanceof ViewGroup.MarginLayoutParams) {
            ViewGroup.MarginLayoutParams p = (ViewGroup.MarginLayoutParams) v.getLayoutParams();
            p.setMargins(l, t, r, b);
            v.requestLayout();
        }
    }
    public  class EmojiInputFilter  implements InputFilter {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                int type = Character.getType(source.charAt(i));
                if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                    return "";
                }
            }
            return null;
        }
    }

    /**
     * 过滤输入表情
     */
    public static InputFilter[] inputFilters=new InputFilter[]{
            new InputFilter() {
                Pattern emoji = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]",
                        Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    Matcher emojiMatcher = emoji.matcher(source);
                    if (emojiMatcher.find() ) {
                        return "";
                    }
                    return null;
                }

            },
            new InputFilter() {
                @Override
                public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                    for (int i = start; i < end; i++) {
                        int type = Character.getType(source.charAt(i));
                        if (type == Character.SURROGATE || type == Character.OTHER_SYMBOL) {
                            return "";
                        }
                    }
                    return null;
                }
            },
            new InputFilter.LengthFilter(10),
    };

}

