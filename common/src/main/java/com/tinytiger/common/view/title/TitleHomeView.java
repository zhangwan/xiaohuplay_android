package com.tinytiger.common.view.title;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.tinytiger.common.R;
import com.tinytiger.common.view.RefreshView;


/**
 *
 * @author zhw_luke
 * @date 2020/4/24 0024 下午 2:26
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 版本3 首页导航
 *      版本2 2019/8/30 14:24
 *      版本1 2018/5/30 19:24
 */
public class TitleHomeView extends RelativeLayout {

    private Context context;

    public TitleHomeView(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public TitleHomeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView();
    }


    private TextView ivTitle1, ivTitle2;
    private View ivLine;
    private int page = 0;

    private void initView() {
        LayoutInflater.from(context).inflate(R.layout.ui_title_home, this);
        ivTitle1 = findViewById(R.id.tvTitle1);
        ivTitle2 = findViewById(R.id.tvTitle2);
      //  ivTitle3 = findViewById(R.id.tvTitle3);
        ivLine = findViewById(R.id.ivLine);

        ivTitle1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page != 0) {
                    setTitle(0);
                    if (onHomePageListener != null) {
                        onHomePageListener.onPage(page);
                    }
                }
            }
        });

        ivTitle2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page != 1) {

                    setTitle(1);
                    if (onHomePageListener != null) {
                        onHomePageListener.onPage(page);
                    }

                }
            }
        });
       /* ivTitle3.setVisibility(View.GONE);
        ivTitle3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (page != 2) {
                    setTitle(2);
                    if (onHomePageListener != null) {
                        onHomePageListener.onPage(page);
                    }

                }
            }
        });*/
        setAnimatorBig(ivTitle1);
    }


    public void setSwitch(int index) {
        setTitle(index);
    }

    float end = 0;
    private void setTitle(int newPage) {
        if (page == 0) {
            setAnimatorSmall(ivTitle1);
        } else if (page == 1) {
            setAnimatorSmall(ivTitle2);
        } else {
         //   setAnimatorSmall(ivTitle3);
        }
        page = newPage;

        if (page == 0) {
            setAnimatorBig(ivTitle1);
        } else if (page == 1) {
            setAnimatorBig(ivTitle2);
        } else {
            //setAnimatorBig(ivTitle3);
        }
        ivLine.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (page == 0) {
                    end = ivTitle1.getX();
                } else if (page == 1) {
                    end = ivTitle2.getX();
                } else {
                   // end = ivTitle3.getX();
                }
                ObjectAnimator.ofFloat(ivLine, "translationX", ivLine.getX(), end).setDuration(200).start();
            }
        }, 130);
    }


    public OnHomePageListener onHomePageListener;

    public interface OnHomePageListener {
        void onPage(int index);
    }

    /**
     * 放大
     *
     * @param view
     */
    public void setAnimatorBig(TextView view) {
        view.setTypeface(Typeface.DEFAULT_BOLD);
        view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.gray28));
        view.setTextSize(20);
        RefreshView.setMargins(view,0,0,0,-7);
    }

    /**
     * 缩小
     *
     * @param view
     */
    public void setAnimatorSmall(TextView view) {
        view.setTextColor(ContextCompat.getColor(view.getContext(), R.color.gray77));
        view.setTypeface(Typeface.DEFAULT);
        view.setTextSize(12);
        RefreshView.setMargins(view,0,0,0,0);
    }
}