package com.tinytiger.common.widget.radarview;

import android.content.Context;
import android.os.Build;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.RelativeSizeSpan;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tinytiger.common.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Description:
 * Copyright  : Copyright (c) 2017
 * Company    : 年糕妈妈
 * Author     : 段宇鹏
 * Date       : 2/22/17
 */
public class RadarViewRoot extends LinearLayout implements View.OnClickListener {

    private Context context;
    private View rootView;
    private OnSelectListener onSelectListener;
    private ArrayList<TextView> buttons;
    private RadarView radarView;
    private TextView button1, button2, button3, button4, button5;
    private TextView value1, value2, value3, value4, value5;

    public RadarViewRoot(Context context) {
        this(context, null);
    }

    public RadarViewRoot(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadarViewRoot(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView();
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        Log.d("qclqcl", "onLayout,l:" + l + ",t:" + t);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        Log.d("qclqcl", "w:" + w + ",h:" + h);
        //一旦size发生改变，重新绘制
        postInvalidate();
        super.onSizeChanged(w, h, oldw, oldh);
    }


    private void initView() {
        Log.d("qclqcl", "initView:");
        rootView = inflate(context, R.layout.custom_rardarview_root, this);

        radarView = rootView.findViewById(R.id.radarView);

        button1 = rootView.findViewById(R.id.button1);
        button2 = rootView.findViewById(R.id.button2);
        button3 = rootView.findViewById(R.id.button3);
        button4 = rootView.findViewById(R.id.button4);
        button5 = rootView.findViewById(R.id.button5);

        value1 = rootView.findViewById(R.id.value1);
        value2 = rootView.findViewById(R.id.value2);
        value3 = rootView.findViewById(R.id.value3);
        value4 = rootView.findViewById(R.id.value4);
        value5 = rootView.findViewById(R.id.value5);

        buttons = new ArrayList<>(5);
        buttons.add(button1);
        buttons.add(button2);
        buttons.add(button3);
        buttons.add(button4);
        buttons.add(button5);

        button1.setTag(0);
        button1.setOnClickListener(this);
        button2.setTag(1);
        button2.setOnClickListener(this);
        button3.setTag(2);
        button3.setOnClickListener(this);
        button4.setTag(3);
        button4.setOnClickListener(this);
        button5.setTag(4);
        button5.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        int position = (int) v.getTag();
        setSelectButton(position);
        //        onSelectListener.doInSelect(position);
    }

    //设置各门得分
    public void setData(List<Float> data) {
        if (data == null || data.size() != 5) {
            return;
        }
        radarView.setData(data, false);
        value1.setText(changTVsize(data.get(0).toString()));
        value2.setText(changTVsize(data.get(1).toString()));
        value3.setText(changTVsize(data.get(2).toString()));
        value4.setText(changTVsize(data.get(3).toString()));
        value5.setText(changTVsize(data.get(4).toString()));
    }


    /**
     * Description: 选中监听
     * Copyright  : Copyright (c) 2017
     * Company    : 年糕妈妈
     * Author     : 段宇鹏
     * Date       : 2/26/17
     */
    public interface OnSelectListener {
        void doInSelect(int select);

        void putAway();
    }

    public OnSelectListener getOnSelectListener() {
        return onSelectListener;
    }

    public void setOnSelectListener(OnSelectListener onSelectListener) {
        this.onSelectListener = onSelectListener;
    }

    public void putAway() {
        onSelectListener.putAway();
    }

    public void setSelectButton(int index) {
        if (buttons == null) {
            return;
        }
        for (int i = 0; i < buttons.size(); i++) {
            if (i == index) {
                setSelected(buttons.get(i));
            } else {
                setUnSelected(buttons.get(i));
            }
        }
    }

    private void setSelected(TextView button) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            button.setBackground(context.getResources().getDrawable(R.drawable.shape_show_all_indicator_rectangle_active));
        }
        button.setTextColor(getResources().getColor(R.color.color_fff8d9));

    }

    private void setUnSelected(TextView button) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
//            button.setBackground(context.getResources().getDrawable(R.drawable.shape_show_all_indicator_rectangle));
        }
        button.setTextColor(getResources().getColor(R.color.color_fff8d9));
    }

    /**
     * 改变同一个textview中使字体不一致，一大一小
     *
     * @param value
     * @return
     */
    public SpannableString changTVsize(String value) {
        SpannableString spannableString = new SpannableString(value);
        if (value.contains(".")) {
            spannableString.setSpan(new RelativeSizeSpan(0.7f), value.indexOf("."), value.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
        return spannableString;
    }
}
