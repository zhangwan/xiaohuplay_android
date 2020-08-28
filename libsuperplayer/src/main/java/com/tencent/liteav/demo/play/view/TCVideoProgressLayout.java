package com.tencent.liteav.demo.play.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.tencent.liteav.demo.play.R;

/**
 *
 * @author zhw_luke
 * @date 2019/12/11 0011 下午 4:50
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 播放器进度弹框
 */
public class TCVideoProgressLayout extends RelativeLayout {
    private TextView mTvTime;
    private ProgressBar mProgressBar;
    private HideRunnable mHideRunnable;
    private int duration = 1000;

    public TCVideoProgressLayout(Context context) {
        super(context);
        init(context);
    }

    public TCVideoProgressLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.video_progress_layout, this);
        mProgressBar =  findViewById(R.id.progress_pb_bar);
        mTvTime = findViewById(R.id.progress_tv_time);
        setVisibility(GONE);
        mHideRunnable = new HideRunnable();
    }

    //显示
    public void show() {
        setVisibility(VISIBLE);
        removeCallbacks(mHideRunnable);
        postDelayed(mHideRunnable, duration);
    }

    public void setTimeText(String text) {
        mTvTime.setText(text);
    }

    //设置进度
    public void setProgress(int progress,int max) {
        mProgressBar.setProgress(progress);
        if (mProgressBar.getMax()!=max){
            mProgressBar.setMax(max);
        }
    }

    //设置持续时间
    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setProgressVisibility(boolean enable) {
        mProgressBar.setVisibility(enable ? VISIBLE : GONE);
    }

    //隐藏自己的Runnable
    private class HideRunnable implements Runnable {
        @Override
        public void run() {
            TCVideoProgressLayout.this.setVisibility(GONE);
        }
    }
}
