package com.tencent.liteav.demo.play.controller;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.recyclerview.widget.RecyclerView;

import com.orhanobut.logger.Logger;
import com.tinytiger.common.utils.image.GlideUtil;
import com.tencent.liteav.demo.play.R;
import com.tencent.liteav.demo.play.utils.TCTimeUtils;
import com.tencent.liteav.demo.play.utils.VideoGestureUtil;
import com.tencent.liteav.demo.play.view.TCVideoProgressLayout;
import com.tencent.liteav.demo.play.view.TCVideoQulity;
import com.tencent.liteav.demo.play.view.TCVolumeBrightnessProgressLayout;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

/**
 * Created by liyuejiao on 2018/7/3.
 */

public abstract class TCVodControllerBase extends RelativeLayout implements SeekBar.OnSeekBarChangeListener {
    private static final String TAG = "TCVodControllerBase";
    protected LayoutInflater mLayoutInflater;
    protected VodController mVodController;
    protected GestureDetector mGestureDetector;
    private boolean isShowing;

    //当前设置清晰度
    protected TCVideoQulity mDefaultVideoQuality;
    protected ArrayList<TCVideoQulity> mVideoQualityList;
    protected int mPlayType;
    protected String mTitle;

    protected TextView mTvCurrent;
    protected TextView mTvDuration;
    protected AppCompatSeekBar mSeekBarProgress;

    protected RelativeLayout rlOver;
    protected TextView mLayoutReplay;
    protected RecyclerView bottom_recycler_view;
    protected LinearLayout ll_right;
    protected ProgressBar mPbLiveLoading;

    //网络
    protected RelativeLayout mNetworkView;
    protected TextView tvNetworkInfo;
    protected TextView tv_network_connect;

    protected ImageView mBackground;

    protected VideoGestureUtil mVideoGestureUtil;
    protected TCVolumeBrightnessProgressLayout mGestureVolumeBrightnessProgressLayout;
    //进度时间提示框
    protected TCVideoProgressLayout mGestureVideoProgressLayout;

    protected HideViewControllerViewRunnable mHideViewRunnable;

    // 标记状态，避免SeekBar由于视频播放的update而跳动
    protected boolean mIsChangingSeekBarProgress;

    protected boolean mFirstShowQuality;


    public TCVodControllerBase(Context context) {
        super(context);
        init();
    }

    public TCVodControllerBase(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TCVodControllerBase(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHideViewRunnable = new HideViewControllerViewRunnable(this);
        mLayoutInflater = LayoutInflater.from(getContext());
        mGestureDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
               /* if (mNetworkType>0){
                    return true;
                }

                changePlayState();
                show();
                if (mHideViewRunnable != null) {
                    TCVodControllerBase.this.getHandler().removeCallbacks(mHideViewRunnable);
                    TCVodControllerBase.this.getHandler().postDelayed(mHideViewRunnable, 7000);
                }*/
                return true;
            }

            //如果双击的话，则onSingleTapConfirmed不会执行

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                onToggleControllerView();
                return true;
            }

            @Override
            public boolean onScroll(MotionEvent downEvent, MotionEvent moveEvent, float distanceX, float distanceY) {

                if (downEvent == null || moveEvent == null) {
                    return false;
                }
                if (mVideoGestureUtil != null && mGestureVolumeBrightnessProgressLayout != null) {
                    mVideoGestureUtil.check(mGestureVolumeBrightnessProgressLayout.getHeight(), downEvent, moveEvent, distanceX, distanceY);
                }
                return true;
            }

            @Override
            public boolean onDown(MotionEvent e) {

                if (mVideoGestureUtil != null) {
                    mVideoGestureUtil.reset(TCVodControllerBase.this.getWidth(), mSeekBarProgress.getProgress());
                }
                return true;
            }

        });
        mGestureDetector.setIsLongpressEnabled(false);


        mVideoGestureUtil = new VideoGestureUtil(getContext());
        mVideoGestureUtil.setVideoGestureListener(new VideoGestureUtil.VideoGestureListener() {
            @Override
            public void onBrightnessGesture(float newBrightness) {
                //亮度
                if (mGestureVolumeBrightnessProgressLayout != null) {
                    mGestureVolumeBrightnessProgressLayout.setProgress((int) (newBrightness * 100));
                    mGestureVolumeBrightnessProgressLayout.setImageResource(R.drawable.ic_light_max);
                    mGestureVolumeBrightnessProgressLayout.show();
                }
            }

            @Override
            public void onVolumeGesture(float volumeProgress) {
                //声音
                if (mGestureVolumeBrightnessProgressLayout != null) {
                    mGestureVolumeBrightnessProgressLayout.setImageResource(R.drawable.ic_volume_max);
                    mGestureVolumeBrightnessProgressLayout.setProgress((int) volumeProgress);
                    mGestureVolumeBrightnessProgressLayout.show();
                }
            }

            @Override
            public void onSeekGesture(int progress) {
                //时间进度
                mIsChangingSeekBarProgress = true;
                if (mGestureVideoProgressLayout != null) {
                    if (progress > mSeekBarProgress.getMax()) {
                        progress = mSeekBarProgress.getMax();
                    }
                    if (progress < 0) {
                        progress = 0;
                    }
                    mGestureVideoProgressLayout.setProgress(progress, mSeekBarProgress.getMax());
                    mGestureVideoProgressLayout.show();
                    mGestureVideoProgressLayout.setTimeText(TCTimeUtils.formattedTime(progress / 1000) + " / " + TCTimeUtils.formattedTime(mSeekBarProgress.getMax() / 1000));
                }

                if (mSeekBarProgress != null) {
                    mSeekBarProgress.setProgress(progress);
                }
            }
        });

    }


    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        int curProgress = seekBar.getProgress();
        int maxProgress = seekBar.getMax();
        if (curProgress >= 0 && curProgress <= maxProgress) {
            // 关闭重播按钮
            updateReplay(false);
            mVodController.seekTo(curProgress / 1000);
            mVodController.resume();
        }

        this.getHandler().postDelayed(mHideViewRunnable, 5000);
    }


    /**
     * 设置播放清晰度
     *
     * @param videoQualityList
     */
    public void setVideoQualityList(ArrayList<TCVideoQulity> videoQualityList) {
        mVideoQualityList = videoQualityList;
        mFirstShowQuality = false;
    }

    public void updateTitle(String title) {
        if (!TextUtils.isEmpty(title)) {
            mTitle = title;
        } else {
            mTitle = "这是新播放的视频";
        }
    }


    /**
     * 视频进度
     *
     * @param current           当前进度
     * @param duration          总进度
     * @param secondaryProgress 加载
     */
    public void updateVideoProgress(int current, int duration, int secondaryProgress) {
        if (mIsChangingSeekBarProgress){
            return;
        }

        //设置总进度
        if (mSeekBarProgress.getMax() != duration) {
            mSeekBarProgress.setMax(duration);
            mTvDuration.setText(TCTimeUtils.formattedTime(duration / 1000));
        }
        mSeekBarProgress.setProgress(current);
        mSeekBarProgress.setSecondaryProgress(secondaryProgress);
        mTvCurrent.setText(TCTimeUtils.formattedTime(current / 1000));
    }

    public void setVodController(VodController vodController) {
        mVodController = vodController;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector != null) {
            mGestureDetector.onTouchEvent(event);
        }

        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            this.getHandler().removeCallbacks(mHideViewRunnable);
        } else if (event.getAction() == MotionEvent.ACTION_UP) {
            this.getHandler().postDelayed(mHideViewRunnable, 5000);
            if (mVideoGestureUtil != null && mVideoGestureUtil.isVideoProgressModel()) {
                int progress = mVideoGestureUtil.getVideoProgress();

                if (progress >= mSeekBarProgress.getMax()) {
                    progress = mSeekBarProgress.getMax()-1;
                }
                if (progress < 0) {
                    progress = 0;
                }
                mSeekBarProgress.setProgress(progress);

                float percentage = progress * 1.0f / mSeekBarProgress.getMax();
                int seekTime = (int) (percentage * mVodController.getDuration());

                mVodController.seekTo(seekTime);
            }
            mIsChangingSeekBarProgress = false;
        }
        return true;
    }


    public void updateReplay(boolean replay) {
        if (rlOver != null) {
            rlOver.setVisibility(replay ? View.VISIBLE : View.GONE);
        }
        if (ll_right != null) {
            ll_right.setVisibility(replay ? View.GONE : View.VISIBLE);
        }
    }

    public void updateLiveLoadingState(boolean loading) {
        if (mPbLiveLoading != null) {
            mPbLiveLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
        }
    }

    /**
     * 重新播放
     */
    protected void replay() {
        updateReplay(false);
        mVodController.onReplay();
    }


    /**
     * 切换播放状态
     */
    protected void changePlayState() {
        // 播放中
        if (mVodController.isPlaying()) {
            mVodController.pause();
            show();
        }else if (!mVodController.isPlaying()) {
            // 未播放
            updateReplay(false);
            mVodController.resume();
            show();
        }
    }

    protected void onToggleControllerView() {
        if (isShowing) {
            hide();
        } else {
            show();
            if (mHideViewRunnable != null) {
                this.getHandler().removeCallbacks(mHideViewRunnable);
                this.getHandler().postDelayed(mHideViewRunnable, 7000);
            }
        }

    }

    protected int mNetworkType=0;
    //显示
    public void showNetwork(int type){
        mNetworkView.setVisibility(VISIBLE);
        mNetworkType=type;
        if (type==1){
            tvNetworkInfo.setText("当前网络不可用，请点击重新连接");
            tv_network_connect.setText("重新连接");
        }else if (type==2){
            tvNetworkInfo.setText("正在使用非WIFI网络，继续播放将消耗流量");
            tv_network_connect.setText("继续播放");

        }else if (type==4){
            tvNetworkInfo.setText("视频播放失败");
            tv_network_connect.setText("重新播放");
        }

    }

    public void hideNetwork(){
        mNetworkView.setVisibility(GONE);
        mNetworkType=0;
    }



    public void show() {
        isShowing = true;
        onShow();
    }

    public void hide() {
        isShowing = false;
        onHide();
    }

    public void release() {

    }

    //封面图
    public void setOverVideo(String url){
        if (TextUtils.isEmpty(url)){
            mBackground.setVisibility(View.GONE);
        }else {
            mBackground.setVisibility(View.VISIBLE);
            GlideUtil.loadImg(mBackground,url);
        }
    }

    abstract void onShow();

    abstract void onHide();


    public void updatePlayType(int playType) {
        mPlayType = playType;
    }

    public interface VodController {

        void onRequestPlayMode(int requestPlayMode);

        void onBackPress(int playMode);

        void resume();

        void pause();

        float getDuration();


        void seekTo(int position);

        boolean isPlaying();

        void onQualitySelect(TCVideoQulity quality);


        void onReplay();

        void resumeLive();

        void onMore(boolean type);

        void clickRecommend(String id, String url,String cover);

        void clickShareType(String share);


        void clickNetworkType(int type);
    }


    private static class HideViewControllerViewRunnable implements Runnable {
        public WeakReference<TCVodControllerBase> mWefControlBase;

        public HideViewControllerViewRunnable(TCVodControllerBase base) {
            mWefControlBase = new WeakReference<>(base);
        }

        @Override
        public void run() {
            if (mWefControlBase != null && mWefControlBase.get() != null) {
                mWefControlBase.get().hide();
            }
        }
    }
}
