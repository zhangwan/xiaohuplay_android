package com.tinytiger.titi.widget.view.Anim;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.vectordrawable.graphics.drawable.Animatable2Compat;

import com.github.penfeizhou.animation.loader.ResourceStreamLoader;
import com.github.penfeizhou.animation.webp.WebPDrawable;
import com.tinytiger.common.event.ClassEvent;
import com.tinytiger.common.net.MyNetworkUtil;
import com.tinytiger.common.utils.FastClickUtil;
import com.tinytiger.common.utils.StringUtils;
import com.tinytiger.common.utils.preference.SpUtils;
import com.tinytiger.common.utils.toast.ToastUtils;
import com.tinytiger.titi.R;

import org.greenrobot.eventbus.EventBus;

/**
 * @author zhw_luke
 * @date 2020/4/1 0001 下午 6:12
 * @Copyright 小虎互联科技
 * @doc 点赞动画view
 * @since 2.1.0
 */
public class LikeView extends LinearLayout {

    public LikeView(@NonNull Context context) {
        this(context, null);
    }

    public LikeView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    int like_nor = 0;
    int like_sel = 0;

    public LikeView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray ta = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.LikeView, 0, 0);
        like_sel = ta.getResourceId(R.styleable.LikeView_like_sel, R.mipmap.icon_like_2);
        like_nor = ta.getResourceId(R.styleable.LikeView_like_nor, R.mipmap.icon_like);
        init(context);
    }

    private ImageView ivLike;
    private TextView tvLike;

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_like, this);
        ivLike = view.findViewById(R.id.ivLike);
        tvLike = view.findViewById(R.id.tvLike);
        view.findViewById(R.id.llLike).setOnClickListener(new OnClickListener() {
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

                if (is_like == 1) {
                    is_like = -1;
                    like_num -= 1;
                } else {
                    is_like = 1;
                    like_num += 1;
                }
                if (showNum && isShowZero()) {
                    tvLike.setVisibility(View.VISIBLE);
                    tvLike.setText(StringUtils.sizeToString(like_num));
                } else {
                    tvLike.setVisibility(View.INVISIBLE);
                }
                setLise();

                if (mListener != null) {
                    mListener.onLikeView();
                }
            }
        });
    }

    /**
     * 是否显示数字
     */
    public boolean showNum = true;

    /**
     * 数字为 0 时是否显示 (默认显示)
     */
    public boolean showZero = true;

    /**
     * 点赞状态
     */
    public int is_like = -1;
    /**
     * 点赞数量
     */
    public int like_num;

    /**
     * 是否显示数字0
     *
     * @return
     */
    private boolean isShowZero() {
        return like_num != 0 || showZero;
    }

    /**
     * 初始化数据
     *
     * @param is_like
     * @param like_num
     */
    public void setLike(int is_like, int like_num) {
        this.is_like = is_like;
        this.like_num = like_num;
        //   带上晓伟，他好这口
        ivLike.setScaleType(ImageView.ScaleType.CENTER);
        if (is_like == 1) {
            ivLike.setImageResource(like_sel);
        } else {
            ivLike.setImageResource(like_nor);
        }

        if (showNum && isShowZero()) {
            tvLike.setVisibility(View.VISIBLE);
            tvLike.setText(StringUtils.sizeToString(like_num));
        } else {
            tvLike.setVisibility(View.GONE);
        }
    }

    private WebPDrawable webpDrawable = null;

    private void setLise() {
        if (is_like == 1) {
            if (webpDrawable == null) {
                webpDrawable = new WebPDrawable(new ResourceStreamLoader(getContext(), R.mipmap.webp_like));
                webpDrawable.setLoopLimit(1);
            }
            webpDrawable.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    super.onAnimationEnd(drawable);
                    ivLike.setScaleType(ImageView.ScaleType.CENTER);
                    ivLike.setImageResource(like_sel);
                }
            });
            ivLike.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ivLike.setImageDrawable(webpDrawable);
            webpDrawable.reset();
        } else {
            ivLike.setScaleType(ImageView.ScaleType.CENTER);
            ivLike.setImageResource(like_nor);
        }
    }


    public OnLikeViewListener mListener = null;

    public interface OnLikeViewListener {

        void onLikeView();
    }

}
