package com.tencent.liteav.demo.play.utils;

import android.app.Activity;
import android.app.Service;
import android.content.ContentResolver;
import android.content.Context;
import android.media.AudioManager;
import android.provider.Settings;
import android.view.MotionEvent;
import android.view.Window;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;

/**
 * @author zhw_luke
 * @date 2019/12/18 0018 下午 4:16
 * @Copyright 小虎互联科技
 * @doc 视频 进度,亮度,声音调节
 * @since 5.0.0
 */
public class VideoGestureUtil {
    private static final String TAG = "VideoGestureUtil";

    //video progress快进快退
    private static final int NONE = 0, VOLUME = 1, BRIGHTNESS = 2, VIDEO_PROGRESS = 3;
    private
    int mScrollMode = NONE;


    private VideoGestureListener mVideoGestureListener;

    private int offsetX = 20;

    private int mVideoWidth;

    //brightness
    private float mBrightness = 1;
    private Window mWindow;
    private WindowManager.LayoutParams mLayoutParams;
    private ContentResolver mResolver;

    //audio
    private AudioManager mAudioManager;
    private int mMaxVolume = 0;
    private int mOldVolume = 0;
    private int mVideoProgress;
    private int mDownProgress;

    public VideoGestureUtil(Context context) {
        mAudioManager = (AudioManager) context.getSystemService(Service.AUDIO_SERVICE);
        mMaxVolume = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);

        if (context instanceof Activity) {
            mWindow = ((Activity) context).getWindow();
            mLayoutParams = mWindow.getAttributes();
            mBrightness = mLayoutParams.screenBrightness;
        }

        mResolver = context.getContentResolver();
    }

    public void setVideoGestureListener(VideoGestureListener videoGestureListener) {
        mVideoGestureListener = videoGestureListener;
    }

    public void reset(int videoWidth, int maxProgress) {
        mVideoProgress = 0;
        mVideoWidth = videoWidth;
        mScrollMode = NONE;
        mOldVolume = mAudioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        if (mOldVolume > 10) {
            mOldVolume -= 3;
        }
        if (mOldVolume < 1) {
            mOldVolume = 5;
        }

        mBrightness = mLayoutParams.screenBrightness;
        if (mBrightness == -1) {
            //一开始是默认亮度的时候，获取系统亮度，计算比例值
            mBrightness = getBrightness() / 255.0f;
            //系统亮度获取失败
            if (mBrightness > 1) {
                mBrightness = 0.6f;
            }
        }

        mDownProgress = maxProgress;
    }

    public boolean isVideoProgressModel() {
        return mScrollMode == VIDEO_PROGRESS;
    }

    public int getVideoProgress() {
        return mVideoProgress;
    }

    private float downY = 0;
    int mVolume =0;
    public void check(int height, MotionEvent downEvent, MotionEvent moveEvent, float distanceX, float distanceY) {
        switch (mScrollMode) {
            case NONE:
                //offset是让快进快退不要那么敏感的值

                if (Math.abs(downEvent.getX() - moveEvent.getX()) > offsetX) {
                    mScrollMode = VIDEO_PROGRESS;
                } else {
                    int halfVideoWidth = mVideoWidth / 2;
                    if (downEvent.getX() < halfVideoWidth) {
                        mScrollMode = BRIGHTNESS;
                    } else {
                        mScrollMode = VOLUME;
                    }
                }
                break;
            case VOLUME:
                // 音量
                if (downEvent.getY() != downY) {
                    downY = downEvent.getY();
                    mVolume = mOldVolume;
                }
                int newVolume = (int) ((downEvent.getY() - moveEvent.getY()) / 30);
                mOldVolume = mVolume+newVolume;

                if (mOldVolume < 0) {
                    mOldVolume = 0;
                } else if (mOldVolume > 14) {
                    mOldVolume = 14;
                }


                mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mOldVolume, AudioManager.FLAG_PLAY_SOUND);
                if (mVideoGestureListener != null) {
                    mVideoGestureListener.onVolumeGesture((mOldVolume / (float) 14) * 100);
                }
                break;
            case BRIGHTNESS:
                //亮度
                //  float newBrightness = height == 0 ? 0 : (downEvent.getY() - moveEvent.getY()) / (height * 3);
                float newBrightness = height == 0 ? 0 : (downEvent.getY() - moveEvent.getY()) / mVideoWidth;
                newBrightness += mBrightness;

                if (newBrightness < 0) {
                    newBrightness = 0;
                } else if (newBrightness > 1) {
                    newBrightness = 1;
                }
                if (mLayoutParams != null) {
                    mLayoutParams.screenBrightness = newBrightness;
                }
                if (mWindow != null) {
                    mWindow.setAttributes(mLayoutParams);
                }

                if (mVideoGestureListener != null) {
                    mVideoGestureListener.onBrightnessGesture(newBrightness);
                }
                break;
            case VIDEO_PROGRESS:
                float dis = moveEvent.getX() - downEvent.getX();
                float percent = dis / mVideoWidth;

                mVideoProgress = (int) (mDownProgress + percent * 60000);
                if (mVideoGestureListener != null) {
                    mVideoGestureListener.onSeekGesture(mVideoProgress);
                }
                break;
            default:
        }
    }


    private int getBrightness() {
        if (mResolver != null) {
            return Settings.System.getInt(mResolver, Settings.System.SCREEN_BRIGHTNESS, 255);
        } else {
            return 180;
        }
    }


    /**
     * 用于提供给外部实现的视频手势处理接口
     */

    public interface VideoGestureListener {
        //亮度手势，手指在Layout左半部上下滑动时候调用
        void onBrightnessGesture(float newBrightness);

        //音量手势，手指在Layout右半部上下滑动时候调用
        void onVolumeGesture(float volumeProgress);

        //进度改变
        void onSeekGesture(int time);
    }

}
