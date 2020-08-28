package com.tinytiger.titi.widget.video;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.tinytiger.common.base.BaseApp;
import com.tinytiger.common.utils.AppManager;
import com.tinytiger.common.utils.preference.SpUtils;
import com.tinytiger.titi.R;
import com.tinytiger.titi.utils.net.NetworkType;
import com.tinytiger.titi.utils.net.NetworkUtil;

import cn.jzvd.JZMediaInterface;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * @author zhw_luke
 * @date 2020/6/12 0012 下午 5:20
 * @Copyright 小虎互联科技
 * @doc 视频播放器
 * @since 3.0.0
 */
public class  PlayerView extends JzvdStd {
    private boolean mIsWifi;
    //网络
    protected RelativeLayout mNetworkView;
    protected TextView mTvNetworkInfo;
    protected TextView mTvNetworkConnect;

    protected TextView retry_text, share_text;
    public LinearLayout over_ll;
    protected ImageView iv_pause,top_share_iv;

    int mVolume;

    // 1-wifi 2-移动网络 0无网络3暂停 4
    protected static int mNetworkType = 1;

    public BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
                boolean isWifi = JZUtils.isWifiConnected(context);
                isNetWorkState();
//                if (mIsWifi == isWifi) {
//                    return;
//                }
                mIsWifi = isWifi;
                if (!mIsWifi && !WIFI_TIP_DIALOG_SHOWED && (state == STATE_PLAYING||state==1)) {
                    startButton.performClick();
                    showWifiDialog();
                }
            }
        }
    };
    public void setType(int type){
        mNetworkType=type;
        showNetwork();
    }

    public void isNetWorkState() {
        NetworkType networkType = NetworkUtil.getNetworkType(this.getContext());
        mNetworkType = 2;
        if (networkType == NetworkType.NETWORK_NO) {
            mNetworkType = 0;
        } else if (networkType == NetworkType.NETWORK_WIFI) {
            mNetworkType = 1;
        }
    }

    public PlayerView(Context context) {
        super(context);
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);

        //网络页
        mNetworkView = findViewById(R.id.mNetworkView);
        mTvNetworkInfo = findViewById(R.id.tvNetworkInfo);
        mTvNetworkConnect = findViewById(R.id.tv_network_connect);
        //网络页
        retry_text = findViewById(R.id.retry_text);
        over_ll = findViewById(R.id.over_ll);
        share_text = findViewById(R.id.share_text);
        top_share_iv= findViewById(R.id.top_share_iv);

        SAVE_PROGRESS = true;
        iv_pause = findViewById(R.id.iv_pause);
        findViewById(R.id.replay_text).setOnClickListener(this);
        iv_pause.setOnClickListener(this);
        mTvNetworkConnect.setOnClickListener(this);

    }
    private int max=0;

    @Override
    public int getLayoutId() {
        return R.layout.view_video_play2;
    }

    public void showNetwork() {
        if (mNetworkType == 0) {

            Jzvd.releaseAllVideos();
            startButton.setVisibility(View.GONE);
            mNetworkView.setVisibility(VISIBLE);
            mTvNetworkInfo.setText("当前网络不可用，请点击重新连接");
            mTvNetworkConnect.setText("重新连接");
        } else if (mNetworkType == 2) {
            if (!BaseApp.Type4G) {
                Jzvd.releaseAllVideos();
                mNetworkView.setVisibility(VISIBLE);
                mTvNetworkInfo.setText("当前网络状态为2G/3G/4G网络");
                mTvNetworkConnect.setText("继续播放");
            }else{
                hideNetwork();
                startButton.performClick();
            }

        } else if (mNetworkType == 4) {
            Jzvd.releaseAllVideos();
            startButton.setVisibility(View.GONE);
            mRetryLayout.setVisibility(VISIBLE);
            retry_text.setText("视频播放失败");
            mRetryBtn.setText("重新播放");
        }else if(mNetworkType==1){
            hideNetwork();
            if (state == STATE_PAUSE) {
                startButton.performClick();
            } else {
                startVideo();
            }
        }

    }

    @Override
    public void showWifiDialog() {
        showNetwork();
    }
    public void hideNetwork() {
        mNetworkView.setVisibility(GONE);
    }
    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.retry_btn:
                if (mNetworkType == 0) {
                    isNetWorkState();
                    if (mNetworkType == 1) {
                        hideNetwork();
                        if (state == STATE_PAUSE) {
                            startButton.performClick();
                        } else {
                            startVideo();
                        }
                    }else{
                        showNetwork();
                    }
                } else if (mNetworkType == 2) {
                    BaseApp.Type4G = true;
                    hideNetwork();
                    WIFI_TIP_DIALOG_SHOWED = true;
                    if (state == STATE_PAUSE) {
                        startButton.performClick();
                    } else {
                        startVideo();
                    }
                }
                break;
            case R.id.replay_text:
                clickStart();
                over_ll.setVisibility(GONE);
                break;
            case R.id.iv_pause:
                clickStart();
                break;
            case R.id.tv_network_connect:

                if(mNetworkType==0){
                    showNetwork();
                }else if(mNetworkType==2){
                    BaseApp.Type4G = true;
                    hideNetwork();
                    WIFI_TIP_DIALOG_SHOWED = true;
                    if (state == STATE_PAUSE) {
                        startButton.performClick();
                    } else {
                        startVideo();
                    }
                }

                break;
            default:

        }
    }
    @Override
    public void registerWifiListener(Context context) {
        if (context == null) {
            return;
        }
        mIsWifi = JZUtils.isWifiConnected(context);
        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        context.registerReceiver(wifiReceiver, intentFilter);
    }

    @Override
    public void unregisterWifiListener(Context context) {
        if (context == null) {
            return;
        }
        try {
            context.unregisterReceiver(wifiReceiver);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onProgress(int progress, long position, long duration) {
        super.onProgress(progress, position, duration);
        totalTimeTextView.setText("/"+JZUtils.stringForTime(duration));
    }

    @Override
    public void showVolumeDialog(float deltaY, int volumePercent){
        int newVolume = (int) ( -deltaY/50.0)+mGestureDownVolume;
        mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
        max = mAudioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        volumePercent = (int) (((float)newVolume/max)*100);
        SpUtils.saveSP("volume",newVolume);
        super.showVolumeDialog(deltaY, volumePercent);
    }


    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int posterImg, int bottomPro, int retryLayout) {
        super.setAllControlsVisiblity(topCon, bottomCon, startBtn, loadingPro, posterImg, bottomPro, retryLayout);
        iv_pause.setVisibility(startBtn);
    }


    @Override
    public void startVideo() {
        WIFI_TIP_DIALOG_SHOWED=true;
        if (screen == SCREEN_FULLSCREEN && state == STATE_AUTO_COMPLETE) {
            registerWifiListener(getApplicationContext());
            JZMediaInterface.SAVED_SURFACE = null;
            addTextureView();
            AudioManager mAudioManager = (AudioManager) getContext().getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
            JZUtils.scanForActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
            onStatePreparing();
        } else {
            super.startVideo();
        }
        mVolume = SpUtils.getInt("volume", 0);
        if (mVolume == 0) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        } else {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume, 0);
        }
    }

    @Override
    public void onCompletion() {
        if (screen == SCREEN_FULLSCREEN) {
            onStateAutoComplete();
        } else {
            super.onCompletion();
        }
    }

    @Override
    public void setScreenNormal() {
        super.setScreenNormal();

        backButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void setScreenFullscreen() {
        super.setScreenFullscreen();
        fullscreenButton.setImageResource(R.mipmap.jz_shrink);
    }


    /**
     * 更新ui状态
     */
    @Override
    public void updateStartImage() {
        super.updateStartImage();

        if (state == STATE_AUTO_COMPLETE) {
            startButton.setVisibility(GONE);
            over_ll.setVisibility(VISIBLE);
            if (shareShow) {
                share_text.setVisibility(VISIBLE);
            } else {
                share_text.setVisibility(GONE);
            }
        }
        if (state == STATE_PLAYING) {

            iv_pause.setVisibility(VISIBLE);
            iv_pause.setImageResource(R.mipmap.ic_vod_pause_normal);
        } else if (state == STATE_ERROR) {
            iv_pause.setVisibility(INVISIBLE);
        } else if (state == STATE_AUTO_COMPLETE) {
            iv_pause.setVisibility(VISIBLE);
            iv_pause.setImageResource(R.mipmap.video_icon_play);
        } else {
            iv_pause.setImageResource(R.mipmap.video_icon_play);
        }

    }

    @Override
    protected void clickBack() {
        // backPress();
        AppManager.getAppManager().finishActivity();
    }

    private Boolean shareShow;

    /**
     * 是否显示分享按钮
     *
     * @param show
     */
    public View setShareType(Boolean show) {
        shareShow = show;
        return share_text;
    }
    public View setShareMore(Boolean show) {
        shareShow = show;
        return top_share_iv;
    }
  /*  @Override
    public void reset() {
        super.reset();
        if (state==STATE_NORMAL||state == STATE_PLAYING || state == STATE_PAUSE) {
           // Logger.d("reset");
        }

    }*/

    @Override
    public void onStatePlaying() {
        state = STATE_PREPARED;
        super.onStatePlaying();
    }

}
