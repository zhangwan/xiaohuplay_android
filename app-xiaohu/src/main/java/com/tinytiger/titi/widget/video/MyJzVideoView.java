package com.tinytiger.titi.widget.video;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.net.ConnectivityManager;
import android.os.Build;
import android.provider.Settings;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.RequiresPermission;

import com.tinytiger.common.base.BaseApp;
import com.tinytiger.common.net.MyNetworkUtil;
import com.tinytiger.common.utils.VolumeChangeObserver;
import com.tinytiger.common.utils.preference.SpUtils;
import com.tinytiger.titi.R;
import com.tinytiger.titi.utils.net.NetworkType;
import com.tinytiger.titi.utils.net.NetworkUtil;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import cn.jzvd.JZDataSource;
import cn.jzvd.JZMediaInterface;
import cn.jzvd.JZUtils;
import cn.jzvd.Jzvd;
import cn.jzvd.JzvdStd;

/**
 * @author zwy
 * create at 2020/6/12 0012
 * description:
 */
public class MyJzVideoView extends JzvdStd {
    private boolean mIsWifi;
    //网络
    protected RelativeLayout mNetworkView;
    protected TextView mTvNetworkInfo;
    protected TextView mTvNetworkConnect;
    public TextView mTvTotal;
    protected TextView mIvVolume;
    protected LinearLayout mLLOver;
    public ImageView mIvStart;


    // 1-wifi 2-移动网络 0无网络3暂停 4
    protected static int mNetworkType = 1;

    //1静音 2播放
    protected static int stateVolume = 1;

    private ClickUi clickUi;

    private Long videoLength;

    int mVolume;
    public void setVolumeChange(int volume) {
        SpUtils.saveSP("volume",volume);
        if(mAudioManager!=null) {
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        }
        if (volume > 0) {
            mIvVolume.setBackgroundResource(R.mipmap.loudspeaker);
        } else {
            mIvVolume.setBackgroundResource(R.mipmap.loudspeaker_mute);
        }
    }


    public interface ClickUi {
        void onClickUiToggle();

        void onClickStart();

        void onShareClick();
    }

    public void setClickUi(ClickUi clickUi) {
        this.clickUi = clickUi;
    }


    public void setType(int type) {
        mNetworkType = type;
        showNetwork();
    }


    public void isNetWorkState() {
        NetworkType networkType = NetworkUtil.getNetworkType(this.getContext());
        mNetworkType = 2;
        if (networkType == NetworkType.NETWORK_NO) {
            if (!MyNetworkUtil.isNetworkAvailable(this.getContext())) {
                mNetworkType = 0;
            }
        } else if (networkType == NetworkType.NETWORK_WIFI) {
            mNetworkType = 1;
        }
    }

    public MyJzVideoView(Context context) {
        super(context);
    }

    public MyJzVideoView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
//        WIFI_TIP_DIALOG_SHOWED = true;
        //网络页
        mNetworkView = findViewById(R.id.mNetworkView);
        mTvNetworkInfo = findViewById(R.id.tvNetworkInfo);
        mTvNetworkConnect = findViewById(R.id.tv_network_connect);
        mTvTotal = findViewById(R.id.tv_total);
        mIvVolume = findViewById(R.id.iv_volume);
        mLLOver = findViewById(R.id.over_ll);
        mIvStart = findViewById(R.id.iv_start);
        mIvVolume.setOnClickListener(this);
        findViewById(R.id.share_text).setOnClickListener(this);
        findViewById(R.id.replay_text).setOnClickListener(this);
        mTvNetworkConnect.setOnClickListener(this);
        findViewById(R.id.fullscreen).setVisibility(View.GONE);
        mLLOver.setOnClickListener(this);


    }

    @Override
    public void setUp(JZDataSource jzDataSource, int screen) {
        if ((System.currentTimeMillis() - gobakFullscreenTime) < 200) {
            return;
        }

        if ((System.currentTimeMillis() - gotoFullscreenTime) < 200) {
            return;
        }


        super.setUp(jzDataSource, screen, JZMediaExo.class);
        titleTextView.setText(jzDataSource.title);
        Jzvd.SAVE_PROGRESS = true;
        setScreen(screen);
    }

    @Override
    public void startVideo() {
        Log.d(TAG, "startVideo [" + this.hashCode() + "] ");
        setCurrentJzvd(this);
        try {
            Constructor<JZMediaInterface> constructor = mediaInterfaceClass.getConstructor(Jzvd.class);
            this.mediaInterface = constructor.newInstance(this);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        addTextureView();
        mAudioManager = (AudioManager) getApplicationContext().getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.requestAudioFocus(onAudioFocusChangeListener, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT);
        JZUtils.scanForActivity(getContext()).getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        onStatePreparing();
        mVolume = SpUtils.getInt("volume", 0);
        if (mVolume == 0) {
            SpUtils.saveSP("volume", 0);
            mIvVolume.setBackgroundResource(R.mipmap.loudspeaker_mute);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
        } else {
            SpUtils.saveSP("volume", mVolume);
            mIvVolume.setBackgroundResource(R.mipmap.loudspeaker);
            mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, mVolume, 0);
        }

        if (clickUi != null) {
            clickUi.onClickStart();
        }
        isNetWorkState();
        if (mNetworkType != 1) {
            showNetwork();
        }

    }


    public void setSystemVolume(int volume) {
        NotificationManager notificationManager = (NotificationManager) this.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !notificationManager.isNotificationPolicyAccessGranted()) {
            this.getContext().startActivity(new Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS));
        } else {
            AudioManager mAudioManager = (AudioManager) this.getContext().getSystemService(Context.AUDIO_SERVICE);
            mAudioManager.setStreamVolume(AudioManager.STREAM_SYSTEM, volume, AudioManager.FLAG_PLAY_SOUND);
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.iv_volume:
                mVolume = SpUtils.getInt("volume", 0);
                if (mVolume == 0) {
                    mIvVolume.setBackgroundResource(R.mipmap.loudspeaker);
                    SpUtils.saveSP("volume", 7);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 7, 0);
                } else {
                    mIvVolume.setBackgroundResource(R.mipmap.loudspeaker_mute);
                    mAudioManager.setStreamVolume(AudioManager.STREAM_MUSIC, 0, 0);
                }
                break;
            case R.id.surface_container:
            case R.id.poster:
                if (clickUi != null) {
                    clickUi.onClickUiToggle();
                }
                break;
            case R.id.share_text:
                if (clickUi != null) {
                    clickUi.onShareClick();
                }
                break;
            case R.id.replay_text:
                clickStart();
                mLLOver.setVisibility(GONE);
                break;
            case R.id.tv_network_connect:
                if (mNetworkType == 0) {
                    isNetWorkState();
                    if (mNetworkType == 1) {
                        hideNetwork();
                        if (state == STATE_PAUSE) {
                            startButton.performClick();
                        } else {
                            startVideo();
                        }
                    } else {
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
            default:
        }
    }


    @Override
    public int getLayoutId() {
        return R.layout.view_video_play;
    }

    public void hideNetwork() {
        mNetworkView.setVisibility(GONE);
    }

    public void showNetwork() {
        if (mNetworkType == 0) {
            mLLOver.setVisibility(View.GONE);
            mIvStart.setVisibility(View.GONE);
            Jzvd.releaseAllVideos();
            startButton.setVisibility(View.GONE);
            mNetworkView.setVisibility(VISIBLE);
            mTvNetworkInfo.setText("当前网络不可用，请点击重新连接");
            mTvNetworkConnect.setText("重新连接");
        } else if (mNetworkType == 2) {
            mLLOver.setVisibility(View.GONE);
            mIvStart.setVisibility(View.GONE);
            startButton.setVisibility(View.GONE);
            if (!BaseApp.Type4G) {
                Jzvd.releaseAllVideos();
                mNetworkView.setVisibility(VISIBLE);
                mTvNetworkInfo.setText("当前网络状态为2G/3G/4G网络");
                mTvNetworkConnect.setText("继续播放");
            } else {
                hideNetwork();
                startButton.performClick();
            }

        } else if (mNetworkType == 4) {
            mLLOver.setVisibility(View.GONE);
            mIvStart.setVisibility(View.GONE);
            Jzvd.releaseAllVideos();
            startButton.setVisibility(View.GONE);
            mNetworkView.setVisibility(VISIBLE);
            mTvNetworkInfo.setText("视频播放失败");
            mTvNetworkConnect.setText("重新播放");
        } else if (mNetworkType == 1) {
            hideNetwork();
            mIvStart.setVisibility(View.GONE);
            mLLOver.setVisibility(View.GONE);
            startButton.performClick();
        }

    }

    @Override
    public void showWifiDialog() {
        Jzvd.SAVE_PROGRESS = true;
        isNetWorkState();
        showNetwork();

    }


    @Override
    public void onProgress(int progress, long position, long duration) {
        super.onProgress(progress, position, duration);
        String seekTime = JZUtils.stringForTime(duration - position);
        mTvTotal.setText(seekTime);
    }

    @Override
    public void updateStartImage() {
        bottomContainer.setVisibility(View.INVISIBLE);
        mLLOver.setVisibility(View.GONE);
        if (mNetworkView.getVisibility() == VISIBLE) {
            mIvStart.setVisibility(View.GONE);
        }

        if (state == STATE_PREPARING || state == STATE_PREPARING_PLAYING || state == STATE_PLAYING) {
            startButton.setVisibility(View.GONE);
            mTvTotal.setVisibility(View.VISIBLE);
            mIvVolume.setVisibility(View.VISIBLE);
            mIvStart.setVisibility(View.GONE);
            hideNetwork();

        } else if (state == STATE_ERROR) {
            startButton.setVisibility(View.GONE);
            mTvTotal.setVisibility(View.GONE);
            mIvVolume.setVisibility(View.GONE);
        } else if (state == STATE_AUTO_COMPLETE) {
            hideNetwork();
            Jzvd.releaseAllVideos();
            mIvStart.setVisibility(View.GONE);
            mTvTotal.setVisibility(View.GONE);
            mLLOver.setVisibility(View.VISIBLE);
            mIvVolume.setVisibility(View.GONE);
            startButton.setVisibility(View.GONE);
        } else if (state == STATE_PAUSE) {
            mTvTotal.setVisibility(View.VISIBLE);
        } else {
            mTvTotal.setVisibility(View.GONE);
            mIvVolume.setVisibility(View.GONE);
        }

    }


    @Override
    public void onStatePlaying() {
        state = STATE_PREPARED;
        super.onStatePlaying();
    }

    @Override
    public void setAllControlsVisiblity(int topCon, int bottomCon, int startBtn, int loadingPro, int posterImg, int bottomPro, int retryLayout) {
        super.setAllControlsVisiblity(topCon, bottomCon, startBtn, loadingPro, posterImg, bottomPro, retryLayout);
        mRetryLayout.setVisibility(GONE);

    }
}
