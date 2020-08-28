package com.tinytiger.titi.widget.view.Anim;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.airbnb.lottie.LottieAnimationView;
import com.tinytiger.common.event.ClassEvent;
import com.tinytiger.common.net.MyNetworkUtil;
import com.tinytiger.common.utils.FastClickUtil;
import com.tinytiger.common.utils.preference.SpUtils;
import com.tinytiger.common.utils.toast.ToastUtils;
import com.tinytiger.titi.R;

import org.greenrobot.eventbus.EventBus;

/**
 * @author zhw_luke
 * @date 2020/4/1 0001 下午 6:12
 * @Copyright 小虎互联科技
 * @doc 游戏 收藏动画view
 * @since 2.1.0
 */
public class AnimFollowGameView extends LinearLayout {

    public AnimFollowGameView(@NonNull Context context) {
        this(context, null);
    }

    public AnimFollowGameView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimFollowGameView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private LottieAnimationView animation_view;

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_game_follow, this);
        animation_view = view.findViewById(R.id.animation_view);
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (FastClickUtil.isFastClickTiming()) {
                    return;
                }
                if (SpUtils.getString(R.string.token, "").isEmpty()) {
                    EventBus.getDefault().post(new ClassEvent("LoginActivity"));
                    return;
                }
                if (!MyNetworkUtil.isNetworkAvailable(getContext())) {
                    ToastUtils.show(getContext(), "当前网络不可用");
                    return;
                }

                if (follow_status == 0) {
                    follow_status = 1;
                    animation_view.playAnimation();
                } else {
                    follow_status = 0;
                    animation_view.setFrame(0);
                }

                if (mListener != null) {
                    mListener.onFollowGameView(follow_status);
                }
            }
        });
    }


    public int follow_status = 0;

    /**
     * 初始化数据
     *
     * @param status 状态
     */
    public void setFollowGame(int status) {
        if (status==follow_status){
            return;
        }
        this.follow_status = status;
        if (follow_status != 0) {
            animation_view.setFrame((int) animation_view.getMaxFrame());
        } else {
            animation_view.setFrame(0);
        }
    }


    public OnFollowGameListener mListener = null;

    public void setOnFollowGameListener(OnFollowGameListener mListener) {
        this.mListener = mListener;
    }

    public interface OnFollowGameListener {

        void onFollowGameView(int follow_status);
    }

}
