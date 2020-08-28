package com.tinytiger.common.view.textview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.StaticLayout;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.tinytiger.common.R;
import com.tinytiger.common.utils.ScreenUtil;
import com.tinytiger.common.view.text.MyLinkedMovementMethod;

import java.util.List;


/**
 * @author zhw_luke
 * @date 2020/3/23 0023 下午 6:31
 * @Copyright 小虎互联科技
 * @doc 在超出maxLines时在末尾显示 "...全文"
 * @since 2.0.0
 */
public class ShowAllTextView extends AppCompatTextView implements ShowAllSpan.OnAllSpanClickListener, ShowLabelSpan.OnLabelSpanClickListener {

    public static String TEXT = "1001";
    public static String IMG = "1002";
    public static String IMAGE_TEXT = "1003";
    /**
     * 最大显示行数
     */
    private int maxShowLines = 4;
    /**
     * 初始显示行数
     */
    private int initShowLines = 4;
    private boolean viewClick = true;
    //一个布局里面有控件显示true 一个显示false，以这个标签为准
    private boolean renewViewClick = false;
    private String rightMenuText = "全文";
    private String rightStopText = "收起";
    private StringBuffer contentBuffer;
    private TextView mTvTag;
    SpannableStringBuilder spannableString;
    private List labelList;
    private String imageLabel;
    private String type;
    //是否已解答 true 已解答 false 未解答
    boolean resolved;
    private int leftMargin = 0;//左边距
    private int rightMargin = 0;//右边距
    private OnLabelClickListener clickListener;
    int count = 0;//次数

    public interface OnLabelClickListener {
        void onLabelClick(View widget, int index, String type);
    }

    public ShowAllTextView(Context context) {
        super(context);
    }

    public ShowAllTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setLineSpacing(0, 1.2f);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ShowAllTextView);
        viewClick = a.getBoolean(R.styleable.ShowAllTextView_viewClick, true);
        renewViewClick = a.getBoolean(R.styleable.ShowAllTextView_renewViewClick, false);
        rightMenuText = a.getString(R.styleable.ShowAllTextView_rightMenuText);
        maxShowLines = a.getInteger(R.styleable.ShowAllTextView_maxShowLines, 4);
        initShowLines = a.getInteger(R.styleable.ShowAllTextView_maxShowLines, 4);
        leftMargin = a.getDimensionPixelOffset(R.styleable.ShowAllTextView_leftMargin, 45);
        rightMargin = a.getDimensionPixelOffset(R.styleable.ShowAllTextView_leftMargin, 45);


    }

    public void setDocText(SpannableStringBuilder text, int showLines) {
        super.setText(text);
        maxShowLines = showLines;

        spannableString.append(rightStopText);
        spannableString.setSpan(new ShowAllSpan(getContext(), 1002, this), spannableString.length() - rightStopText.length(), spannableString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        create(new Builder()
                .setLabelList(labelList)
                .setImgLabel(imageLabel)
                .setType(type)
                .setOnClickListener(clickListener));

    }

    public void setDocText() {
        spannableString.replace(spannableString.length() - rightStopText.length(), spannableString.length(), "");
        maxShowLines = initShowLines;
        create(new Builder()
                .setLabelList(labelList)
                .setImgLabel(imageLabel)
                .setType(type)
                .setOnClickListener(clickListener));

    }

    public void create(Builder builder) {
        CharSequence text = builder.text;
        this.labelList = builder.labelList;
        this.imageLabel = builder.imgLabel;
        this.type = builder.type;
        this.resolved = builder.resolved;
        this.clickListener = builder.clickListener;


        String txt = "";
        if (!TextUtils.isEmpty(text)) {
            txt = text.toString();
        }
        super.setText(text);
        if(spannableString==null){
            spannableString = new SpannableStringBuilder(txt);
        }
        if (TEXT.equals(type)) {
            setContentAndTag(txt, labelList, type, clickListener);
        } else if (IMG.equals(type)) {
            setContentAndTag(txt, imageLabel, type, clickListener);
        } else if (IMAGE_TEXT.equals(type)) {
            setContentAndTag(txt, imageLabel, labelList, type, clickListener);
        }
        getLastIndexForLimit();
    }

    public void clearTxt() {
        if (spannableString != null) {
            spannableString = null;
            super.setText("");
        }
    }

    public static class Builder {
        private CharSequence text;
        private List<String> labelList;
        private String imgLabel;
        private String type;
        //是否已解答 true 已解答 false 未解答
        private boolean resolved;
        private OnLabelClickListener clickListener;


        public Builder setText(CharSequence text) {
            this.text = text;
            return this;
        }

        public Builder setLabelList(List<String> labelList) {
            this.labelList = labelList;
            return this;
        }

        public Builder setImgLabel(String imgLabel) {
            this.imgLabel = imgLabel;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder isResolved(boolean resolved) {
            this.resolved = resolved;
            return this;
        }

        public Builder setOnClickListener(OnLabelClickListener clickListener) {
            this.clickListener = clickListener;
            return this;
        }

    }

    private void getLastIndexForLimit() {
        if (maxShowLines < 0) {
            this.setText(spannableString);
            return;
        }
        //每行文本的布局宽度
        int width = ScreenUtil.getScreenWidth();
        if (width <= 0) {
            width = 1080;
        }


        width = width - leftMargin - rightMargin;
        //实例化StaticLayout 传入相应参数
        StaticLayout staticLayout = new StaticLayout(spannableString, this.getPaint(), width, Layout.Alignment.ALIGN_NORMAL, 1, 0, false);
        //判断content是行数是否超过最大限制行数3行
        if (staticLayout.getLineCount() > maxShowLines) {
            //获取到第三行最后一个文字的下标
            int index = staticLayout.getLineStart(maxShowLines) - 1;
            //定义收起后的文本内容
            CharSequence charSequence = spannableString.subSequence(0, index - 4);
            SpannableStringBuilder builder = new SpannableStringBuilder(charSequence);
            String str = "..." + rightMenuText;
            builder.append(str);
            if (viewClick || renewViewClick) {
                builder.setSpan(new ShowAllSpan(getContext(), 1001, this), builder.length() - rightMenuText.length(), builder.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
            } else {
                //背景颜色
                BackgroundColorSpan bgColorSpan = new BackgroundColorSpan(Color.parseColor("#ffffff"));
                builder.setSpan(new ForegroundColorSpan(Color.parseColor("#ffcc03")), builder.length() - rightMenuText.length(), builder.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
                builder.setSpan(bgColorSpan, builder.length() - rightMenuText.length(), builder.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            }
            this.setText(builder);

            setMovementMethod(MyLinkedMovementMethod.getInstance());
        } else {
            this.setText(spannableString);
        }
    }


    /**
     * 设置行数
     *
     * @param showLines 最大折叠行
     */
    public void setMaxLine(int showLines) {
        initShowLines = showLines;
        maxShowLines = showLines;
    }

    /**
     * 添加标签
     *
     * @param
     */
    private void setContentAndTag(String content, List<String> tags, String type, OnLabelClickListener clickListener) {
        this.clickListener = clickListener;
        contentBuffer = new StringBuffer();
        for (int i = 0; i < tags.size(); i++) {
            //将每个tag的内容添加到content后边，之后将用drawable替代这些tag所占的位置
            contentBuffer.append(tags.get(i));
        }
        contentBuffer.append("  ");
        contentBuffer.append(content);
        SpannableStringBuilder spannableString = new SpannableStringBuilder(contentBuffer);
        for (int i = 0; i < tags.size(); i++) {

            int startIndex = getLastLength(tags, i);
            int endIndex = startIndex + tags.get(i).length();
            int left = 0;
            if (i > 0) {
                left = 15;
            }

            Drawable drawable1 = this.getResources().getDrawable(R.mipmap.link);
            drawable1.setBounds(0, 0, drawable1.getIntrinsicWidth(), drawable1.getIntrinsicHeight());
            CenteredImageSpan imageSpan1 = new CenteredImageSpan(drawable1, left, 0);
//            ImageSpan imageSpan1 = new ImageSpan(drawable1, ImageSpan.ALIGN_BASELINE);
            spannableString.setSpan(imageSpan1, startIndex, startIndex + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ShowLabelSpan(getContext(), i, TEXT, this), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        this.spannableString = spannableString;
        setText(spannableString);
        setMovementMethod(MyLinkedMovementMethod.getInstance());
        setGravity(Gravity.CENTER_VERTICAL);
    }

    /**
     * 添加Image表情
     *
     * @param
     */
    private void setContentAndTag(String content, String tags, String type, OnLabelClickListener clickListener) {
        this.clickListener = clickListener;
        contentBuffer = new StringBuffer();
        contentBuffer.append(tags);
        contentBuffer.append(content);
        SpannableStringBuilder spannableString = new SpannableStringBuilder(contentBuffer);
        //R.layout.tag是每个标签的布局
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.view_label, null);
        mTvTag = view.findViewById(R.id.tv_tag);
        mTvTag.setSelected(resolved);
        mTvTag.setText(tags);
        Bitmap bitmap = convertViewToBitmap(view);
        Drawable d = new BitmapDrawable(bitmap);
        //缺少这句的话，不会报错，但是图片不回显示
        d.setBounds(0, 0, mTvTag.getWidth(), mTvTag.getHeight());
        CenteredImageSpan span = new CenteredImageSpan(d, 0, 10);
        int startIndex;
        int endIndex;
        startIndex = 0;
        endIndex = startIndex + tags.length();
        spannableString.setSpan(span, startIndex, endIndex, ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(new ShowLabelSpan(getContext(), 0, IMG, this), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        this.spannableString = spannableString;
        setText(spannableString);
        setMovementMethod(MyLinkedMovementMethod.getInstance());
        setGravity(Gravity.CENTER_VERTICAL);

    }

    /**
     * 添加文字和图片标签
     *
     * @param content       内容
     * @param img           图片标签
     * @param tagList       文字标签
     * @param type          type
     * @param clickListener 回调
     */
    private void setContentAndTag(String content, String img, List<String> tagList, String type, OnLabelClickListener clickListener) {
        this.clickListener = clickListener;
        int imgLength = !TextUtils.isEmpty(imageLabel) ? imageLabel.length() : 0;
        contentBuffer = new StringBuffer();
        contentBuffer.append(img);
        if (tagList != null) {

            for (int i = 0; i < tagList.size(); i++) {

                //将每个tag的内容添加到content后边，之后将用drawable替代这些tag所占的位置
                contentBuffer.append(tagList.get(i));
            }
        }
        contentBuffer.append("  ");
        contentBuffer.append(content);
        spannableString = new SpannableStringBuilder(contentBuffer);
        //R.layout.tag是每个标签的布局
        View view = LayoutInflater.from(this.getContext()).inflate(R.layout.view_label, null);
        mTvTag = view.findViewById(R.id.tv_tag);
        mTvTag.setSelected(resolved);
        mTvTag.setText(img);
        Bitmap bitmap = convertViewToBitmap(view);
        Drawable d = new BitmapDrawable(bitmap);
        //缺少这句的话，不会报错，但是图片不回显示
        d.setBounds(0, 0, mTvTag.getWidth(), mTvTag.getHeight());
        CenteredImageSpan span = new CenteredImageSpan(d, 0, 10);
        int startIndex;
        int endIndex;
        startIndex = 0;
        endIndex = startIndex + img.length();
        spannableString.setSpan(span, startIndex, endIndex, ImageSpan.ALIGN_BASELINE);
        spannableString.setSpan(new ShowLabelSpan(getContext(), 0, IMG, this), startIndex, endIndex, Spanned.SPAN_INCLUSIVE_INCLUSIVE);

        for (int i = 0; i < tagList.size(); i++) {
            int startIndex1 = getLastLength(tagList, i);
            int endIndex1 = startIndex1 + tagList.get(i).length();
//            if (i > 1) {
//                endIndex1 = endIndex1 + 1;
//            }
            int left = 0;
            if (i > 0) {
                left = 15;
            }

            Drawable drawable1 = this.getResources().getDrawable(R.mipmap.link);
            drawable1.setBounds(0, 0, drawable1.getIntrinsicWidth(), drawable1.getIntrinsicHeight());
            CenteredImageSpan imageSpan1 = new CenteredImageSpan(drawable1, left, 0);
            spannableString.setSpan(imageSpan1, startIndex1 + imgLength, startIndex1 + imgLength + 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
            spannableString.setSpan(new ShowLabelSpan(getContext(), i, TEXT, this), startIndex1 + imgLength, endIndex1 + imgLength, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        }
        setText(spannableString);
        setMovementMethod(MyLinkedMovementMethod.getInstance());
        setGravity(Gravity.CENTER_VERTICAL);
    }

    private static Bitmap convertViewToBitmap(View view) {
        view.measure(MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED), MeasureSpec
                .makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight() + 5);
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        return bitmap;

    }

    private int getLastLength(List<String> list, int maxLength) {
        int length = 0;
        for (int i = 0; i < maxLength; i++) {
            length += list.get(i).length();
        }
        return length;
    }


    @Override
    public void onClick(View widget, int type) {
        if (type == 1001) {
            if (viewClick || renewViewClick) {
                setDocText(spannableString, -1);
            }
        } else {
            setDocText();
        }

    }

    @Override
    public void onLabelOnClick(View widget, int index, String type) {
        if (clickListener != null) {
            clickListener.onLabelClick(widget, index, type);
        }
    }
}
