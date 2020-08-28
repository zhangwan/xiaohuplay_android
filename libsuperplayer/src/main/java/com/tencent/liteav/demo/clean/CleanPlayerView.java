package com.tencent.liteav.demo.clean;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.tencent.liteav.demo.clean.controller.VodControllerBase;
import com.tencent.liteav.demo.clean.controller.VodControllerLarge;
import com.tencent.liteav.demo.clean.controller.VodControllerSmall;
import com.tencent.liteav.demo.play.R;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.SuperPlayerGlobalConfig;
import com.tencent.liteav.demo.play.SuperPlayerModel;

import com.tencent.liteav.demo.play.utils.SuperPlayerUtil;
import com.tencent.liteav.demo.play.v3.SuperPlayerModelWrapper;
import com.tencent.liteav.demo.play.view.TCVideoQulity;
import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXBitrateItem;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXVodPlayConfig;
import com.tencent.rtmp.TXVodPlayer;
import com.tencent.rtmp.ui.TXCloudVideoView;
import com.tinytiger.common.base.BaseApp;
import com.tinytiger.common.net.MyNetworkUtil;
import com.tinytiger.common.utils.toast.ToastUtils;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 *
 * @author zhw_luke
 * @date 2020/2/24 0024 下午 2:36
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 视频播放器 无附加功能版本
 */
public class CleanPlayerView extends RelativeLayout implements ITXVodPlayListener, ITXLivePlayListener {

    private Context mContext;

    private int mPlayMode = SuperPlayerConst.PLAYMODE_WINDOW;

    // UI
    private ViewGroup mRootView;
    private TXCloudVideoView mTXCloudVideoView;
    private VodControllerLarge mVodControllerLarge;
    private VodControllerSmall mVodControllerSmall;

    private ViewGroup.LayoutParams mLayoutParamWindowMode;
    private ViewGroup.LayoutParams mLayoutParamFullScreenMode;
    private LayoutParams mVodControllerSmallParams;
    private LayoutParams mVodControllerLargeParams;
    // 点播播放器
    private TXVodPlayer mVodPlayer;
    private TXVodPlayConfig mVodPlayConfig;

    private OnSuperPlayerViewCallback mPlayerViewCallback;
    private int mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PLAY;

    private int mCurrentPlayType;

    public String mCurrentPlayVideoURL;
    private SuperPlayerModelWrapper mCurrentModelWrapper;

    // 记录onPause暂停时的时间，在播放widevine格式的时候，onResume需要Seek回去，否则需要等到下一个I帧到来才能有画面。
    private float mCurrentTimeWhenPause;


    public CleanPlayerView(Context context) {
        super(context);
        initView(context);
    }

    public CleanPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public CleanPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(final Context context) {
        mContext = context;
        mRootView = (ViewGroup) LayoutInflater.from(context).inflate(R.layout.super_vod_player_view2, null);
        mTXCloudVideoView = mRootView.findViewById(R.id.cloud_video_view);
        mVodControllerLarge = mRootView.findViewById(R.id.controller_large);
        mVodControllerSmall = mRootView.findViewById(R.id.controller_small);

        mVodControllerSmallParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        mVodControllerLargeParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);

        mVodControllerLarge.setVodController(mVodController);
        mVodControllerSmall.setVodController(mVodController);

        removeAllViews();

        mRootView.removeView(mTXCloudVideoView);
        mRootView.removeView(mVodControllerSmall);
        mRootView.removeView(mVodControllerLarge);

        addView(mTXCloudVideoView);
        if (mPlayMode == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
            addView(mVodControllerLarge);
            mVodControllerLarge.hide();
        } else if (mPlayMode == SuperPlayerConst.PLAYMODE_WINDOW) {
            addView(mVodControllerSmall);
            mVodControllerSmall.hide();
        }

        post(new Runnable() {
            @Override
            public void run() {
                if (mPlayMode == SuperPlayerConst.PLAYMODE_WINDOW) {
                    mLayoutParamWindowMode = getLayoutParams();
                }
                try {
                    // 依据上层Parent的LayoutParam类型来实例化一个新的fullscreen模式下的LayoutParam
                    Class parentLayoutParamClazz = getLayoutParams().getClass();
                    Constructor constructor = parentLayoutParamClazz.getDeclaredConstructor(int.class, int.class);
                    mLayoutParamFullScreenMode = (ViewGroup.LayoutParams) constructor.newInstance(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 初始化点播播放器
     *
     * @param context
     */
    private void initVodPlayer(Context context) {
        if (mVodPlayer != null) {
            return;
        }
        mVodPlayer = new TXVodPlayer(context);
        mVodPlayer.setLoop(false);
        SuperPlayerGlobalConfig config = SuperPlayerGlobalConfig.getInstance();
        mVodPlayConfig = new TXVodPlayConfig();

       // filesDir.absolutePath

        mVodPlayConfig.setCacheFolderPath(context.getFilesDir().getPath() + "/txcache");
        mVodPlayConfig.setMaxCacheItems(10);
        mVodPlayer.setConfig(mVodPlayConfig);

        mVodPlayer.setRenderMode(config.renderMode);
        mVodPlayer.setVodListener(this);
        mVodPlayer.enableHardwareDecode(false);
    }

    /**
     * 设置播放器源url
     *
     * @param modelV3
     */
    public void playWithModel(SuperPlayerModel modelV3) {
        // 清空关键帧和视频打点信息
        mVodControllerLarge.updateVttAndImages(null);
        mVodControllerLarge.updateKeyFrameDescInfos(null);

        mCurrentTimeWhenPause = 0;
        SuperPlayerModelWrapper modelWrapper = new SuperPlayerModelWrapper(modelV3);
        mCurrentModelWrapper = modelWrapper;
        stopPlay();
        initVodPlayer(getContext());

        if (TextUtils.isEmpty(modelWrapper.requestModel.url)) {
            Toast.makeText(this.getContext(), "播放视频失败，播放连接为空", Toast.LENGTH_SHORT).show();
            return;
        }

        mVodPlayer.setPlayerView(mTXCloudVideoView);
        //默认设置720p
        playVodURL(modelWrapper.requestModel.url);
        mCurrentPlayType = SuperPlayerConst.PLAYTYPE_VOD;
        mVodControllerSmall.updatePlayType(SuperPlayerConst.PLAYTYPE_VOD);
        mVodControllerLarge.updatePlayType(SuperPlayerConst.PLAYTYPE_VOD);

        mVodControllerSmall.updateTitle(modelWrapper.requestModel.title);
        mVodControllerLarge.updateTitle(modelWrapper.requestModel.title);

        mVodControllerSmall.updateReplay(false);
        mVodControllerLarge.updateReplay(false);
        mVodControllerSmall.hideNetwork();
        mVodControllerLarge.hideNetwork();
    }

    private boolean mIsMultiBitrateStream;





    /**
     * 播放点播,开始播放
     */
    private void playVodURL(String url) {
        mCurrentPlayVideoURL = url;
        if (url.contains(".m3u8")) {
            mIsMultiBitrateStream = true;
        }
        if (mVodPlayer != null) {
            mDefaultSet = false;
            mVodPlayer.setAutoPlay(true);
            mVodPlayer.setVodListener(this);
            int ret = mVodPlayer.startPlay(url);
            if (ret == 0) {
                mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PLAY;
            }
            mVodControllerSmall.updateLiveLoadingState(true);
            mVodControllerLarge.updateLiveLoadingState(true);
        }
    }

    public void onResume() {
        if (mCurrentPlayState == SuperPlayerConst.PLAYSTATE_END) {
            //播放结束状态,不做操作
            return;
        }
        if (network == 2 && !BaseApp.Type4G) {
            return;
        }

        resume();
        if (MyNetworkUtil.isNetworkAvailable(mContext)) {
            mVodControllerSmall.hideNetwork();
            mVodControllerLarge.hideNetwork();
        }
    }

    /**
     * 开始播放
     */
    private void resume() {
        mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PLAY;
        if (mVodPlayer != null) {
            mVodPlayer.resume();
            // 解决Widevine有声音画面不动问题
            if (mCurrentModelWrapper != null && mCurrentModelWrapper.currentPlayingType == SuperPlayerModelWrapper.URL_DASH_WIDE_VINE && mCurrentTimeWhenPause != 0) {
                mVodPlayer.seek(mCurrentTimeWhenPause);
                mCurrentTimeWhenPause = 0;
            }
            //启动定时器,10s无销毁重新播放
            handler.sendEmptyMessageDelayed(101, 10000);
        }
    }

    public void onPause() {
        pause();
    }

    /**
     * 暂停播放
     */
    private void pause() {
        mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PAUSE;
        if (mVodPlayer != null) {
            // 解决Widevine有声音画面不动问题
            if (mCurrentModelWrapper != null && mCurrentModelWrapper.currentPlayingType == SuperPlayerModelWrapper.URL_DASH_WIDE_VINE) {
                mCurrentTimeWhenPause = mVodPlayer.getCurrentPlaybackTime();
            }
            mVodPlayer.pause();
        }
    }


    public void resetPlayer() {
        stopPlay();
    }

    /**
     * 停止播放
     */
    public void stopPlay() {
        if (mVodPlayer != null) {
            mVodPlayer.setVodListener(null);
            mVodPlayer.stopPlay(false);
        }
        handler.removeMessages(101);
        mVodControllerSmall.updateLiveLoadingState(false);
        mVodControllerLarge.updateLiveLoadingState(false);
        mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PAUSE;
    }


    /**
     * 设置超级播放器的回掉
     *
     * @param callback
     */
    public void setPlayerViewCallback(OnSuperPlayerViewCallback callback) {
        mPlayerViewCallback = callback;
    }

    //当前播放时间
    private int mVideoPos;

    /**
     * 外部按返回键
     */
    public void setPlayMode() {
        mVodController.onRequestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
    }

    /**
     * 播放器控制
     */
    private VodControllerBase.VodController mVodController = new VodControllerBase.VodController() {
        /**
         * 请求播放模式：窗口/全屏/悬浮窗
         * @param requestPlayMode
         */
        @Override
        public void onRequestPlayMode(int requestPlayMode) {
            if (mPlayMode == requestPlayMode) {
                return;
            }

            if (requestPlayMode == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
                setScreenOrientation(1);
            } else {
                setScreenOrientation(0);
            }
        }

        /**
         * 返回
         * @param playMode
         */
        @Override
        public void onBackPress(int playMode) {
            // 当前是全屏模式，返回切换成窗口模式
            if (playMode == SuperPlayerConst.PLAYMODE_FULLSCREEN) {
                onRequestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
            } else if (playMode == SuperPlayerConst.PLAYMODE_WINDOW) {
                // 当前是窗口模式，返回退出播放器
                if (mPlayerViewCallback != null) {
                    mPlayerViewCallback.onClickSmallReturnBtn();
                }
            }
        }

        @Override
        public void resume() {
            if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_VOD) {
                if (mVodPlayer != null) {
                    mVodPlayer.resume();
                    // 解决Widevine有声音画面不动问题
                    if (mCurrentModelWrapper != null && mCurrentModelWrapper.currentPlayingType == SuperPlayerModelWrapper.URL_DASH_WIDE_VINE && mCurrentTimeWhenPause != 0) {
                        mVodPlayer.seek(mCurrentTimeWhenPause);
                        mCurrentTimeWhenPause = 0;
                    }
                }
            }

            mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PLAY;
            mVodControllerSmall.updatePlayState(true);
            mVodControllerLarge.updatePlayState(true);

            mVodControllerLarge.updateReplay(false);
            mVodControllerSmall.updateReplay(false);
        }

        @Override
        public void pause() {
            if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_VOD) {
                if (mVodPlayer != null) {
                    mVodPlayer.pause();
                }
            }
            mCurrentPlayState = SuperPlayerConst.PLAYSTATE_PAUSE;
            mVodControllerSmall.updatePlayState(false);
            mVodControllerLarge.updatePlayState(false);
        }

        @Override
        public float getDuration() {
            return mVodPlayer.getDuration();
        }

        @Override
        public void seekTo(int position) {
            if (mVodPlayer != null) {
                mVodPlayer.seek(position);
            }
        }

        @Override
        public boolean isPlaying() {
            if (mCurrentPlayType == SuperPlayerConst.PLAYTYPE_VOD) {
                return mVodPlayer.isPlaying();
            } else {
                boolean type = mCurrentPlayState == SuperPlayerConst.PLAYSTATE_PLAY;
                if (mCurrentPlayState == SuperPlayerConst.PLAYSTATE_END) {
                    type = false;
                }
                return type;
            }
        }

        /**
         * 清晰度选择
         * @param quality
         */
        @Override
        public void onQualitySelect(TCVideoQulity quality) {
            mVodControllerLarge.updateVideoQulity(quality);
            if (mVodPlayer != null) {
                if (quality.index == -1) {
                    // 说明是非多bitrate的m3u8子流，需要手动seek
                    float currentTime = mVodPlayer.getCurrentPlaybackTime();
                    mVodPlayer.stopPlay(true);
                    mVodPlayer.setStartTime(currentTime);
                    mVodPlayer.startPlay(quality.url);
                } else {
                    // 说明是多bitrate的m3u8子流，会自动无缝seek
                    mVodPlayer.setBitrateIndex(quality.index);
                }
            }
        }

        /**
         * 重新播放
         */
        @Override
        public void onReplay() {
            if (!TextUtils.isEmpty(mCurrentPlayVideoURL)) {
                playVodURL(mCurrentPlayVideoURL);
            }

            if (mVodControllerLarge != null) {
                mVodControllerLarge.updateReplay(false);
            }
            if (mVodControllerSmall != null) {
                mVodControllerSmall.updateReplay(false);
            }
        }

        @Override
        public void resumeLive() {
            mVodControllerSmall.updatePlayType(SuperPlayerConst.PLAYTYPE_LIVE);
            mVodControllerLarge.updatePlayType(SuperPlayerConst.PLAYTYPE_LIVE);
        }

        @Override
        public void onMore(boolean type) {
            if (mPlayerViewCallback != null) {
                mPlayerViewCallback.onMoreView(type);
            }
        }


        @Override
        public void clickRecommend(String id, String url, String cover) {
            if (!MyNetworkUtil.isNetworkAvailable(mContext)) {
                ToastUtils.show(mContext, "当前网络不可用");
                return;
            }

            if (mPlayerViewCallback != null) {
                mPlayerViewCallback.clickRecommend(id, url);
            }
        }

        @Override
        public void clickShareType(String share) {
            if (!MyNetworkUtil.isNetworkAvailable(mContext)) {
                ToastUtils.show(mContext, "当前网络不可用");
                return;
            }
            if (mPlayerViewCallback != null) {
                mPlayerViewCallback.clickTypeShare(share);
            }
        }

        @Override
        public void clickNetworkType(int type) {
            //网络状态
            if (!MyNetworkUtil.isNetworkAvailable(mContext)) {
                ToastUtils.show(mContext, "当前网络不可用");
                return;
            }

            mVodControllerSmall.hideNetwork();
            mVodControllerLarge.hideNetwork();
            if (mCurrentPlayState == SuperPlayerConst.PLAYSTATE_END) {
                //播放结束状态,不做操作
                return;
            }

            if (mCurrentPlayState == SuperPlayerConst.PLAYSTATE_PAUSE) {
                resume();
            } else {
                mVodControllerSmall.updateLiveLoadingState(true);
                mVodControllerLarge.updateLiveLoadingState(true);
                playVodURL(mCurrentPlayVideoURL);
            }
        }
    };

    public Window window;
    /**
     * 0 竖屏
     * 1 左侧旋转
     * 2 右侧侧旋转
     */
    public boolean mScreenOrientation = true;

    public void setScreenOrientation(int type) {

        if (mPlayMode == SuperPlayerConst.PLAYMODE_WINDOW && type == 0) {
            return;
        }
        if (type == 0) {
            if (mVodControllerSmallParams == null) {
                return;
            }
            removeView(mVodControllerLarge);
            addView(mVodControllerSmall, mVodControllerSmallParams);
            setLayoutParams(mLayoutParamWindowMode);

        } else {
            if (mVodControllerLargeParams == null) {
                return;
            }
            //横屏状态下,左右横屏切换不重置UI
            if (mPlayMode != SuperPlayerConst.PLAYMODE_FULLSCREEN) {
                removeView(mVodControllerSmall);
                addView(mVodControllerLarge, mVodControllerLargeParams);
                setLayoutParams(mLayoutParamFullScreenMode);
            }
        }

        switch (type) {
            case 1:
                mScreenOrientation = true;
                mPlayMode = SuperPlayerConst.PLAYMODE_FULLSCREEN;
                rotateScreenOrientation(SuperPlayerConst.ORIENTATION_LANDSCAPE);
                break;
            case 2:
                mPlayMode = SuperPlayerConst.PLAYMODE_FULLSCREEN;
                mScreenOrientation = false;
                rotateScreenOrientation(SuperPlayerConst.ORIENTATION_LANDSCAPE);
                break;
            default:
                mPlayMode = SuperPlayerConst.PLAYMODE_WINDOW;
                rotateScreenOrientation(SuperPlayerConst.ORIENTATION_PORTRAIT);
        }
    }


    /**
     * 旋转屏幕方向
     *
     * @param orientation
     */
    private void rotateScreenOrientation(int orientation) {
        switch (orientation) {
            case SuperPlayerConst.ORIENTATION_LANDSCAPE:
                //横屏,隐藏状态栏
                if (mScreenOrientation) {
                    ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                } else {
                    ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE);
                }
                if (window != null) {
                    WindowManager.LayoutParams lp = window.getAttributes();
                    lp.flags = lp.flags | WindowManager.LayoutParams.FLAG_FULLSCREEN;
                    window.setAttributes(lp);
                }

                break;
            case SuperPlayerConst.ORIENTATION_PORTRAIT:
                //竖屏,显示状态栏
                ((Activity) mContext).setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                if (window != null) {
                    //获得 WindowManager.LayoutParams 属性对象
                    WindowManager.LayoutParams lp2 = window.getAttributes();
//LayoutParams.FLAG_FULLSCREEN 强制屏幕状态条栏弹出
                    lp2.flags &= (~WindowManager.LayoutParams.FLAG_FULLSCREEN);
//设置属性
                    window.setAttributes(lp2);
//不允许窗口扩展到屏幕之外  clear掉了
                    window.clearFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);

               //     fullScreen(false);
                }
                break;
            default:
        }
    }


    /**
     * 视频源排序
     *
     * @param groups
     */
    protected void sortGroups(ArrayList<TXBitrateItem> groups) {
        Collections.sort(groups, new Comparator<TXBitrateItem>() {
            @Override
            public int compare(TXBitrateItem lhs, TXBitrateItem rhs) {
                return rhs.bitrate - lhs.bitrate;
            }
        });
    }

    /**
     * 是否设置过视频分辨率
     */
    private boolean mDefaultSet;

    /**
     * 点播播放器回调
     *
     * @param player
     * @param event  事件id.id类型请参考 {@linkplain TXLiveConstants#PLAY_EVT_CONNECT_SUCC 播放事件列表}.
     * @param param
     */
    @Override
    public void onPlayEvent(TXVodPlayer player, int event, Bundle param) {
        switch (event) {
            case TXLiveConstants.PLAY_EVT_VOD_PLAY_PREPARED:
                //视频播放开始
                mVodControllerSmall.updateLiveLoadingState(false);
                mVodControllerLarge.updateLiveLoadingState(false);

                mVodControllerSmall.updatePlayState(true);
                mVodControllerLarge.updatePlayState(true);

                mVodControllerSmall.updateReplay(false);
                mVodControllerLarge.updateReplay(false);
                handler.removeMessages(101);

                if (mIsMultiBitrateStream) {
                    ArrayList<TXBitrateItem> bitrateItems = mVodPlayer.getSupportedBitrates();
                    if (bitrateItems == null || bitrateItems.size() == 0) {
                        return;
                    }
                    //masterPlaylist多清晰度，按照码率排序，从高到低
                    sortGroups(bitrateItems);

                    ArrayList<TCVideoQulity> videoQulities = new ArrayList<>();
                    int size = bitrateItems.size();
                    for (int i = 0; i < size; i++) {
                        TXBitrateItem bitrateItem = bitrateItems.get(i);
                        TCVideoQulity quality = SuperPlayerUtil.convertToVideoQuality(bitrateItem, i);
                        videoQulities.add(quality);
                    }

                    if (!mDefaultSet && videoQulities.size() > 1) {
                        int index = 1;
                        if (network == 2 && videoQulities.size() > 2) {
                            index = 2;
                        }
                        //设置首次视频分辨率
                        mVodPlayer.setBitrateIndex(videoQulities.get(index).index);
                        mVodControllerLarge.updateVideoQulity(videoQulities.get(index));
                        mDefaultSet = true;
                    }
                    mVodControllerLarge.setVideoQualityList(videoQulities);
                }


                break;
            case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME:
                //网络接收到首个可渲染的视频数据包(IDR
                handler.removeMessages(101);

                mVodControllerSmall.updateLiveLoadingState(false);
                mVodControllerLarge.updateLiveLoadingState(false);
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_END:
                //视频播放结束
                mCurrentPlayState = SuperPlayerConst.PLAYSTATE_END;

                mVodControllerSmall.updatePlayState(false);
                mVodControllerLarge.updatePlayState(false);
                mVodControllerSmall.updateLiveLoadingState(false);
                mVodControllerLarge.updateLiveLoadingState(false);
                mVodControllerSmall.updateReplay(true);
                mVodControllerLarge.updateReplay(true);

                handler.removeMessages(101);
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS:
                // 加载播放进度, 单位是秒
                int progress = param.getInt(TXLiveConstants.EVT_PLAY_PROGRESS_MS);
                int duration_ms = param.getInt(TXLiveConstants.EVT_PLAYABLE_DURATION_MS);
                int duration = param.getInt(TXLiveConstants.EVT_PLAY_DURATION_MS);

                mVideoPos = progress;
                mVodControllerSmall.updateVideoProgress(progress, duration, duration_ms);
                mVodControllerLarge.updateVideoProgress(progress, duration, duration_ms);
                handler.removeMessages(101);
                break;
            case TXLiveConstants.PLAY_ERR_HLS_KEY:
            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT:
                // 播放点播文件失败,HLS 解密 key 获取失败,3次重连网络播放失败
                onPause();
                mVodControllerSmall.updatePlayState(false);
                mVodControllerLarge.updatePlayState(false);

                mVodControllerSmall.updateLiveLoadingState(false);
                mVodControllerLarge.updateLiveLoadingState(false);
                setNetType(3);
                break;
            case TXLiveConstants.PLAY_WARNING_RECONNECT:
                mVodControllerSmall.updateLiveLoadingState(true);
                mVodControllerLarge.updateLiveLoadingState(true);
                break;
            case TXLiveConstants.PLAY_EVT_PLAY_LOADING:
                //视频播放 loading
                mVodControllerSmall.updateLiveLoadingState(true);
                mVodControllerLarge.updateLiveLoadingState(true);
                break;
            case TXLiveConstants.PLAY_EVT_VOD_LOADING_END:
                mVodControllerSmall.updateLiveLoadingState(false);
                mVodControllerLarge.updateLiveLoadingState(false);
                break;
            default:

        }

        if (event < 0
                && event != TXLiveConstants.PLAY_ERR_HLS_KEY
                && event != TXLiveConstants.PLAY_ERR_NET_DISCONNECT) {
            mVodPlayer.stopPlay(true);
            mVodControllerSmall.updatePlayState(false);
            mVodControllerLarge.updatePlayState(false);
            Logger.d(param.getString(TXLiveConstants.EVT_DESCRIPTION));
            //点播H265解码失败
            Toast.makeText(mContext, param.getString(TXLiveConstants.EVT_DESCRIPTION), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onNetStatus(TXVodPlayer player, Bundle status) {

    }

    /**
     * 直播播放器回调
     *
     * @param event 事件id.id类型请参考 {@linkplain TXLiveConstants#PUSH_EVT_CONNECT_SUCC 播放事件列表}.
     * @param param
     */
    @Override
    public void onPlayEvent(int event, Bundle param) {

    }

    @Override
    public void onNetStatus(Bundle status) {

    }

    public void requestPlayMode(int playMode) {
        if (playMode == SuperPlayerConst.PLAYMODE_WINDOW) {
            if (mVodController != null) {
                mVodController.onRequestPlayMode(SuperPlayerConst.PLAYMODE_WINDOW);
            }
        } else if (playMode == SuperPlayerConst.PLAYMODE_FLOAT) {
            if (mVodController != null) {
                mVodController.onRequestPlayMode(SuperPlayerConst.PLAYMODE_FLOAT);
            }
        }
    }

    /**
     * 获取当前播放模式
     *
     * @return
     */
    public int getPlayMode() {
        return mPlayMode;
    }

    /**
     * 获取当前播放状态
     *
     * @return
     */
    public int getPlayState() {
        return mCurrentPlayState;
    }

    /**
     * SuperPlayerView的回调接口
     */
    public interface OnSuperPlayerViewCallback {

        /**
         * 点击小播放模式的返回按钮
         */
        void onClickSmallReturnBtn();


        void clickRecommend(String id, String url);

        void clickTypeShare(String share);

        void onMoreView(boolean type);

    }

    public void release() {
        if (mVodControllerSmall != null) {
            mVodControllerSmall.release();
        }
        if (mVodControllerLarge != null) {
            mVodControllerLarge.release();
        }
        handler.removeCallbacksAndMessages(null);
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        try {
            release();
        } catch (Exception e) {
        } catch (Error e) {
        }
    }

    private int network = 1;

    /**
     * @param type 1-wifi 2-移动网络 0无网络3暂停
     */
    public void setNetType(int type) {
        //network不同网络类型,设置顺序不同,不要修改

        switch (type) {
            case 1:
            case 2:
                network = type;
                if (mVodPlayer == null) {
                    return;
                }
                mVodControllerSmall.hideNetwork();
                mVodControllerLarge.hideNetwork();

                if (mVodPlayer.isPlaying()) {
                    return;
                }
                //当前进度大于3说明加载成功,继续播放
                if (mVideoPos > 3) {
                    onResume();
                } else {
                    playVodURL(mCurrentPlayVideoURL);
                }
                break;
            case 3:
                if (mVodPlayer == null) {
                    return;
                }
                if (network == 1) {
                    mVodControllerSmall.showNetwork(4);
                    mVodControllerLarge.showNetwork(4);
                    type = 1;
                } else {
                    mVodControllerSmall.showNetwork(1);
                    mVodControllerLarge.showNetwork(1);
                }
                mVodControllerSmall.updateLiveLoadingState(false);
                mVodControllerLarge.updateLiveLoadingState(false);
                onPause();
                break;
            default:
        }
        network = type;
    }

    private Handler handler = new Handler() {
        int mPos = 0;

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 101:
                    if (mVideoPos > 3) {
                        mPos = mVideoPos;
                        sendEmptyMessageDelayed(102, 1000);
                    }
                    playVodURL(mCurrentPlayVideoURL);
                    break;
                case 102:
                    mVodPlayer.setStartTime(mPos);
                    break;
                default:
            }
        }
    };

}
