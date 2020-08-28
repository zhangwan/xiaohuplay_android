package com.tinytiger.titi.widget.view.Anim;

import android.content.Context;
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
 * @doc 收藏动画view
 * @since 2.1.0
 */
public class ConllectionView extends LinearLayout {

    public ConllectionView(@NonNull Context context) {
        this(context, null);
    }

    public ConllectionView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ConllectionView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private ImageView ivConllection;
    private TextView tvConllection;

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.view_like, this);
        ivConllection = view.findViewById(R.id.ivLike);
        tvConllection = view.findViewById(R.id.tvLike);
        ivConllection.setImageResource(R.mipmap.icon_collect_nor);
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

                if (is_conllection == 1) {
                    is_conllection = -1;
                    conllection_num -= 1;
                } else {
                    is_conllection = 1;
                    conllection_num += 1;
                }
                tvConllection.setText(StringUtils.sizeToString(conllection_num));
                setLise();

                if (mListener != null) {
                    mListener.onLikeView();
                }
            }
        });
    }

    public boolean showNum = true;

    private int is_conllection;
    public int conllection_num;

    /**
     * 初始化数据
     *
     * @param is_conllection
     * @param conllection_num
     */
    public void setConllection(int is_conllection, int conllection_num) {
        this.is_conllection = is_conllection;
        this.conllection_num = conllection_num;
        //   带上晓伟，他好这口
        ivConllection.setScaleType(ImageView.ScaleType.CENTER);
        if (is_conllection == 1) {
            ivConllection.setImageResource(R.mipmap.icon_collect_sel);
        } else {
            ivConllection.setImageResource(R.mipmap.icon_collect_nor);
        }

        if (showNum){
            tvConllection.setText(StringUtils.sizeToString(conllection_num));
        }else {
            tvConllection.setVisibility(View.GONE);
        }
    }

    private WebPDrawable webpDrawable = null;

    private void setLise() {
        if (is_conllection == 1) {
            if (webpDrawable == null) {
                webpDrawable = new WebPDrawable(new ResourceStreamLoader(getContext(), R.mipmap.webp_collect));
                webpDrawable.setLoopLimit(1);

            }
            webpDrawable.registerAnimationCallback(new Animatable2Compat.AnimationCallback() {
                @Override
                public void onAnimationEnd(Drawable drawable) {
                    super.onAnimationEnd(drawable);
                    ivConllection.setScaleType(ImageView.ScaleType.CENTER);
                    ivConllection.setImageResource(R.mipmap.icon_collect_sel);
                }
            });
            ivConllection.setScaleType(ImageView.ScaleType.FIT_CENTER);
            ivConllection.setImageDrawable(webpDrawable);
            webpDrawable.reset();
        } else {
            ivConllection.setScaleType(ImageView.ScaleType.CENTER);
            ivConllection.setImageResource(R.mipmap.icon_collect_nor);
        }
    }


    public OnLikeViewListener mListener = null;

    public interface OnLikeViewListener {

        void onLikeView();
    }

}
