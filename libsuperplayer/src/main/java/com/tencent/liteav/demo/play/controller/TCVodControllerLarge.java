package com.tencent.liteav.demo.play.controller;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.tinytiger.common.adapter.RecommendHAdapter;
import com.tinytiger.common.base.BaseApp;
import com.tinytiger.common.net.data.home.RecommendBean;
import com.tinytiger.common.utils.Dp2PxUtils;
import com.tencent.liteav.demo.play.R;
import com.tencent.liteav.demo.play.SuperPlayerConst;
import com.tencent.liteav.demo.play.bean.TCPlayImageSpriteInfo;
import com.tencent.liteav.demo.play.bean.TCPlayKeyFrameDescInfo;
import com.tencent.liteav.demo.play.utils.TCTimeUtils;
import com.tencent.liteav.demo.play.view.TCPointSeekBar;
import com.tencent.liteav.demo.play.view.TCVideoQulity;
import com.tencent.liteav.demo.play.view.TCVodQualityView;
import com.tencent.rtmp.TXImageSprite;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liyuejiao on 2018/7/3.
 * <p>
 * 超级播放器全屏控制界面
 */
public class TCVodControllerLarge extends TCVodControllerBase
        implements View.OnClickListener, TCVodQualityView.Callback, TCPointSeekBar.OnSeekBarPointClickListener {
    private static final String TAG = "TCVodControllerLarge";
    private RelativeLayout mLayoutTop;
    private LinearLayout mLayoutBottom;
    private ImageView mIvPause;

    private TextView mTvQuality;
    private ImageView iv_arrow;
    private TCVodQualityView mVodQualityView;

    protected RecyclerView right_recycler_view;

    private TXImageSprite mTXImageSprite;
    private List<TCPlayKeyFrameDescInfo> mTXPlayKeyFrameDescInfos;
    private TextView mTvVttText;
    private int mSelectedPos = -1;

    public TCVodControllerLarge(Context context) {
        super(context);
        initViews(context);
    }

    public TCVodControllerLarge(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public TCVodControllerLarge(Context context, AttributeSet attrs, int defStyleAttr) {
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
        ll_right.setVisibility(View.VISIBLE);
    }

    /**
     * 隐藏播放控制界面
     */
    @Override
    void onHide() {
        mLayoutTop.setVisibility(View.GONE);
        mLayoutBottom.setVisibility(View.GONE);
        mVodQualityView.setVisibility(View.GONE);
        mTvVttText.setVisibility(GONE);
        ll_right.setVisibility(View.GONE);
    }

    private long rightTime = 0L;
    private Context context;

    private void initViews(Context context) {
        this.context = context;
        mLayoutInflater.inflate(R.layout.vod_controller_large, this);

        mLayoutTop = findViewById(R.id.layout_top);
        mLayoutTop.setOnClickListener(this);
        mLayoutBottom = findViewById(R.id.layout_bottom);
        mLayoutBottom.setOnClickListener(this);
        mIvPause = findViewById(R.id.iv_pause);

        mTvCurrent = findViewById(R.id.tv_current);
        mTvDuration = findViewById(R.id.tv_duration);

        mSeekBarProgress = findViewById(R.id.seekbar_progress);

        mSeekBarProgress.setOnSeekBarChangeListener(this);

        //网络页
        mNetworkView = findViewById(R.id.mNetworkView);
        tvNetworkInfo = findViewById(R.id.tvNetworkInfo);
        tv_network_connect = findViewById(R.id.tv_network_connect);

        mTvQuality = findViewById(R.id.tv_quality);

        mPbLiveLoading = findViewById(R.id.pb_live);

        rlOver = findViewById(R.id.rlOver);
        mLayoutReplay = findViewById(R.id.layout_replay);
        bottom_recycler_view = findViewById(R.id.bottom_recycler_view);
        findViewById(R.id.tv_replay_share_zone).setOnClickListener(this);
        findViewById(R.id.tv_replay_share_circle).setOnClickListener(this);
        findViewById(R.id.tv_replay_share_qq).setOnClickListener(this);
        findViewById(R.id.tv_replay_share_wechat).setOnClickListener(this);

        right_recycler_view = findViewById(R.id.right_recycler_view);
        ll_right = findViewById(R.id.ll_right);
        iv_arrow = findViewById(R.id.iv_arrow);

        mVodQualityView = findViewById(R.id.vodQualityView);
        mVodQualityView.setCallback(this);


        mBackground = findViewById(R.id.iv_background);

        mLayoutReplay.setOnClickListener(this);

        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.iv_fullscreen).setOnClickListener(this);
        findViewById(R.id.ib_more).setOnClickListener(this);
        findViewById(R.id.iv_arrow).setOnClickListener(this);

        mIvPause.setOnClickListener(this);

        mTvQuality.setOnClickListener(this);
        mTvVttText = findViewById(R.id.large_tv_vtt_text);
        mTvVttText.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                float time = mTXPlayKeyFrameDescInfos != null ? mTXPlayKeyFrameDescInfos.get(mSelectedPos).time : 0;
                mVodController.seekTo((int) time);
                mVodController.resume();
                mTvVttText.setVisibility(GONE);
                updateReplay(false);
            }
        });
        if (mDefaultVideoQuality != null) {
            mTvQuality.setText(mDefaultVideoQuality.title);
        }
        mGestureVolumeBrightnessProgressLayout = findViewById(R.id.gesture_progress);
        mGestureVideoProgressLayout = findViewById(R.id.video_progress_layout);

        mAdapter = new RecommendHAdapter();
        bottom_recycler_view.setAdapter(mAdapter);
        right_recycler_view.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mVodController.clickRecommend(mAdapter.getData().get(position).id, mAdapter.getData().get(position).video_url, mAdapter.getData().get(position).cover);
            }
        });

        right_recycler_view.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (System.currentTimeMillis() - rightTime > 5000) {
                    if (mHideViewRunnable != null) {
                        getHandler().removeCallbacks(mHideViewRunnable);
                        getHandler().postDelayed(mHideViewRunnable, 7000);
                    }
                    rightTime = System.currentTimeMillis();
                }
                return false;
            }
        });
        bottom_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        right_recycler_view.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        initAnimation();

        tv_network_connect.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mNetworkType == 2) {
                    BaseApp.Type4G = true;
                }
                mVodController.clickNetworkType(mNetworkType);
            }
        });
    }

    /**
     * 横屏侧栏目状态
     */
    private Boolean isExpand = false;

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.iv_back || i == R.id.iv_fullscreen) {
            mVodController.onBackPress(SuperPlayerConst.PLAYMODE_FULLSCREEN);

        } else if (i == R.id.iv_pause) {
            changePlayState();

        } else if (i == R.id.tv_quality) {
            showQualityView();
            if (isExpand) {
                mRightAnimation.start();
                isExpand = false;
            }

        } else if (i == R.id.layout_replay) {
            replay();
        } else if (i == R.id.ib_more) {
            mVodController.onMore(false);
        } else if (i == R.id.tv_replay_share_wechat) {
            mVodController.clickShareType("WEIXIN");
        } else if (i == R.id.tv_replay_share_qq) {
            mVodController.clickShareType("QQ");
        } else if (i == R.id.tv_replay_share_circle) {
            mVodController.clickShareType("WEIXIN_CIRCLE");
        } else if (i == R.id.tv_replay_share_zone) {
            mVodController.clickShareType("QQZONE");
        } else if (i == R.id.iv_arrow) {
            if (isExpand) {
                mRightAnimation.start();
            } else {
                mLeftAnimation.start();
            }
            isExpand = !isExpand;
        }
    }


    private ObjectAnimator mLeftAnimation = null;
    private ObjectAnimator mRightAnimation = null;

    private void initAnimation() {
        int px = Dp2PxUtils.dip2px(context, 90);
        mRightAnimation = ObjectAnimator.ofFloat(ll_right, "translationX", 0f, px);
        mRightAnimation.setDuration(400);
        mRightAnimation.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                iv_arrow.setImageResource(R.mipmap.video_icon_arrow_left);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });


        mLeftAnimation = ObjectAnimator.ofFloat(ll_right, "translationX", px, 0f);
        mLeftAnimation.setDuration(400);
        mLeftAnimation.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                iv_arrow.setImageResource(R.mipmap.video_icon_arrow_right);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }


    /**
     * 返回直播
     */
    private void backToLive() {
        mVodController.resumeLive();
    }

    /**
     * 显示右侧更多设置
     */
    private void showMoreView() {
        hide();

    }

    /**
     * 显示多分辨率UI
     */
    private void showQualityView() {
        if (mVideoQualityList == null || mVideoQualityList.size() == 0) {
            return;
        }
        // 设置默认显示分辨率文字
        mVodQualityView.setVisibility(View.VISIBLE);
        if (!mFirstShowQuality && mDefaultVideoQuality != null) {
            for (int i = 0; i < mVideoQualityList.size(); i++) {
                TCVideoQulity quality = mVideoQualityList.get(i);
                if (quality != null && quality.title != null && quality.title.equals(mDefaultVideoQuality.title)) {
                    mVodQualityView.setDefaultSelectedQuality(i);
                    break;
                }
            }
            mFirstShowQuality = true;
        }
        mVodQualityView.setVideoQualityList(mVideoQualityList);
    }


    /**
     * 更新默认清晰度
     *
     * @param videoQulity
     */
    public void updateVideoQulity(TCVideoQulity videoQulity) {
        mDefaultVideoQuality = videoQulity;
        if (mTvQuality != null) {
            mTvQuality.setText(videoQulity.title);
        }
        if (mVideoQualityList != null && mVideoQualityList.size() != 0) {
            for (int i = 0; i < mVideoQualityList.size(); i++) {
                TCVideoQulity quality = mVideoQualityList.get(i);
                if (quality != null && quality.title != null && quality.title.equals(mDefaultVideoQuality.title)) {
                    mVodQualityView.setDefaultSelectedQuality(i);
                    break;
                }
            }
        }
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
        }
        // 未播放
        else {
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


    public void updateVttAndImages(TCPlayImageSpriteInfo info) {
        if (mTXImageSprite != null) {
            releaseTXImageSprite();
        }

        mGestureVideoProgressLayout.setProgressVisibility(info == null || info.imageUrls == null || info.imageUrls.size() == 0);
    }


    public void updateKeyFrameDescInfos(List<TCPlayKeyFrameDescInfo> list) {
        mTXPlayKeyFrameDescInfos = list;
    }

    @Override
    public void release() {
        super.release();
        releaseTXImageSprite();
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

    private void releaseTXImageSprite() {
        if (mTXImageSprite != null) {
            mTXImageSprite.release();
            mTXImageSprite = null;
        }
    }


 /*   @Override
    protected void onGestureVideoProgress(int progress) {
        super.onGestureVideoProgress(progress);

    }*/

    @Override
    public void onSeekBarPointClick(final View view, final int pos) {
        if (mTXPlayKeyFrameDescInfos != null) {
            //ELK点击上报
            mSelectedPos = pos;
            view.post(new Runnable() {
                @Override
                public void run() {
                    int[] location = new int[2];
                    view.getLocationInWindow(location);

                    int viewX = location[0];
                    TCPlayKeyFrameDescInfo info = mTXPlayKeyFrameDescInfos.get(pos);
                    String content = info.content;

                    mTvVttText.setText(TCTimeUtils.formattedTime((long) info.time) + " " + content);
                    mTvVttText.setVisibility(VISIBLE);
                    adjustVttTextViewPos(viewX);
                }
            });
        }
    }

    /**
     * 根据 PointView x坐标计算出 TextView应该显示出来的位置
     *
     * @param viewX
     */
    private void adjustVttTextViewPos(final int viewX) {
        mTvVttText.post(new Runnable() {
            @Override
            public void run() {
                int width = mTvVttText.getWidth();

                int marginLeft = viewX - width / 2;

                LayoutParams params = (LayoutParams) mTvVttText.getLayoutParams();
                params.leftMargin = marginLeft;

                if (marginLeft < 0) {
                    params.leftMargin = 0;
                }

                if (marginLeft + width > getScreenWidth()) {
                    params.leftMargin = getScreenWidth() - width;
                }

                mTvVttText.setLayoutParams(params);
            }
        });
    }

    private int getScreenWidth() {
        return getResources().getDisplayMetrics().widthPixels;
    }


    /**
     * 更新播放类型
     *
     * @param playType
     */
    @Override
    public void updatePlayType(int playType) {
        super.updatePlayType(playType);
        mTvDuration.setVisibility(View.VISIBLE);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void onQualitySelect(TCVideoQulity quality) {
        mVodController.onQualitySelect(quality);
        mVodQualityView.setVisibility(View.GONE);
    }

    @Override
    protected void onToggleControllerView() {
        super.onToggleControllerView();

    }


    private RecommendHAdapter mAdapter;

    public void showIntroContentList(ArrayList<RecommendBean> bean) {
        mAdapter.setNewInstance(bean);
    }
}
