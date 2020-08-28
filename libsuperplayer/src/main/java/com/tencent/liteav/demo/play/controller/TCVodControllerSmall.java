package com.tencent.liteav.demo.play.controller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tinytiger.common.adapter.RecommendHAdapter;
import com.tinytiger.common.base.BaseApp;
import com.tinytiger.common.net.data.home.RecommendBean;
import com.tencent.liteav.demo.play.R;
import com.tencent.liteav.demo.play.SuperPlayerConst;

import java.util.ArrayList;

/**
 * Created by liyuejiao on 2018/7/3.
 * <p>
 * 超级播放器小窗口控制界面
 */
public class TCVodControllerSmall extends TCVodControllerBase implements View.OnClickListener {
    private static final String TAG = "TCVodControllerSmall";
    private RelativeLayout mLayoutTop;
    private LinearLayout mLayoutBottom;
    private ImageView mIvPause;
    private ImageView mIvFullScreen;

    public TCVodControllerSmall(Context context) {
        super(context);
        initViews(context);
    }

    public TCVodControllerSmall(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public TCVodControllerSmall(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    /**
     * 显示播放控制界面
     */
    @Override
    void onShow() {
        mLayoutTop.setVisibility(View.VISIBLE);
        mLayoutBottom.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏播放控制界面
     */
    @Override
    void onHide() {
        mLayoutTop.setVisibility(View.GONE);
        mLayoutBottom.setVisibility(View.GONE);
    }

    private void initViews(Context context) {
        mLayoutInflater.inflate(R.layout.vod_controller_small, this);

        mLayoutTop = findViewById(R.id.layout_top);
        mLayoutTop.setOnClickListener(this);
        mLayoutBottom =  findViewById(R.id.layout_bottom);
        mLayoutBottom.setOnClickListener(this);

        mIvPause =findViewById(R.id.iv_pause);
        mTvCurrent = findViewById(R.id.tv_current);
        mTvDuration =  findViewById(R.id.tv_duration);
        mSeekBarProgress =  findViewById(R.id.seekbar_progress);

        mIvFullScreen =  findViewById(R.id.iv_fullscreen);

        mPbLiveLoading =  findViewById(R.id.pb_live);

        //结束页
        rlOver = findViewById(R.id.rlOver);
        mLayoutReplay = findViewById(R.id.layout_replay);
        bottom_recycler_view = findViewById(R.id.bottom_recycler_view);

        //网络页
        mNetworkView= findViewById(R.id.mNetworkView);
        tvNetworkInfo =  findViewById(R.id.tvNetworkInfo);
        tv_network_connect = findViewById(R.id.tv_network_connect);

        mIvPause.setOnClickListener(this);
        mIvFullScreen.setOnClickListener(this);
        mLayoutTop.setOnClickListener(this);
        mLayoutReplay.setOnClickListener(this);
        tv_network_connect.setOnClickListener(this);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.ib_more).setOnClickListener(this);
        mSeekBarProgress.setOnSeekBarChangeListener(this);

        mGestureVolumeBrightnessProgressLayout = findViewById(R.id.gesture_progress);

        mGestureVideoProgressLayout =  findViewById(R.id.video_progress_layout);

        mBackground = findViewById(R.id.iv_background);

        mAdapter = new RecommendHAdapter();

        bottom_recycler_view.setAdapter(mAdapter);
        bottom_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        mAdapter.setOnItemClickListener(new OnItemClickListener(){

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mVodController.clickRecommend(mAdapter.getData().get(position).id, mAdapter.getData().get(position).video_url,mAdapter.getData().get(position).cover);
            }
        });
    }


    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back) {
            onBack();
        } else if (i == R.id.iv_pause) {
            changePlayState();
        } else if (i == R.id.iv_fullscreen) {
            fullScreen();
        } else if (i == R.id.layout_replay) {
            replay();
        } else if (i == R.id.ib_more) {
            mVodController.onMore(true);
        } else if (i == R.id.tv_network_connect) {
            if (mNetworkType==2){
                BaseApp.Type4G=true;
            }
            mVodController.clickNetworkType(mNetworkType);
        }
    }

    /**
     * 返回窗口模式
     */
    private void onBack() {
        mVodController.onBackPress(SuperPlayerConst.PLAYMODE_WINDOW);
    }

    /**
     * 全屏
     */
    private void fullScreen() {
        mVodController.onRequestPlayMode(SuperPlayerConst.PLAYMODE_FULLSCREEN);
    }

    /**
     * 更新播放UI
     *
     * @param isStart
     */
    public void updatePlayState(boolean isStart) {
        // 播放中
        if (isStart) {
            mIvPause.setImageResource(R.mipmap.ic_vod_pause_normal);
        } else {
            // 未播放
            mIvPause.setImageResource(R.mipmap.video_icon_play);
        }
    }

    /**
     * 更新标题
     *
     * @param title
     */
    @Override
    public void updateTitle(String title) {
        super.updateTitle(title);
    }


    /**
     * 更新播放类型
     *
     * @param playType
     */
    @Override
    public void updatePlayType(int playType) {
        super.updatePlayType(playType);
        switch (playType) {
            case SuperPlayerConst.PLAYTYPE_VOD:
                mTvDuration.setVisibility(View.VISIBLE);
                break;
            case SuperPlayerConst.PLAYTYPE_LIVE:

                mTvDuration.setVisibility(View.GONE);
                break;
            case SuperPlayerConst.PLAYTYPE_LIVE_SHIFT:
                mTvDuration.setVisibility(View.GONE);
                break;
                default:
        }
    }

    private RecommendHAdapter mAdapter;

    public void showIntroContentList(ArrayList<RecommendBean> bean) {
        mAdapter.setNewInstance(bean);
    }

}
