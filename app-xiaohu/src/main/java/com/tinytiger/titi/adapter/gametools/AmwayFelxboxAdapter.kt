package com.tinytiger.titi.adapter.gametools


import android.animation.Animator
import android.animation.Animator.AnimatorListener
import android.text.TextUtils
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.airbnb.lottie.LottieAnimationView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.event.ClassEvent
import com.tinytiger.common.net.MyNetworkUtil
import com.tinytiger.common.net.data.gametools.AssessTagBean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.utils.toast.ToastUtils
import com.tinytiger.titi.R
import org.greenrobot.eventbus.EventBus


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页_标签列表
 */
class AmwayFelxboxAdapter :
    BaseQuickAdapter<AssessTagBean, BaseViewHolder>(R.layout.home_item_felxbox2, null) {
    /**
     * 是否可点击
     */
    var showType = false

    override fun convert(holder: BaseViewHolder, item: AssessTagBean) {
        val rlInfo = holder.getView<LinearLayout>(R.id.rlInfo)
        val tvTitle = holder.getView<TextView>(R.id.tvTitle)
        val tvSize = holder.getView<TextView>(R.id.tvSize)
        val ivIcon = holder.getView<ImageView>(R.id.ivIcon)
        val animation_view = holder.getView<LottieAnimationView>(R.id.animation_view)
        tvTitle.text = "# ${item.viewpoint}"
        tvSize.text = StringUtils.sizeToString(item.like_num)
        setView(holder.adapterPosition, item.is_like, tvSize, rlInfo, tvTitle, ivIcon,animation_view)
        if (showType && listener != null) {
            rlInfo.setOnClickListener {

                if (!MyNetworkUtil.isNetworkAvailable(context)) {
                    ToastUtils.show(context, "当前网络不可用")
                    return@setOnClickListener
                }
                if (TextUtils.isEmpty(SpUtils.getString(R.string.token, ""))) {
                    EventBus.getDefault().post(ClassEvent("LoginActivity"))
                    return@setOnClickListener
                }
                if (item.is_like == 1) {
                    item.is_like = -1
                    item.like_num -= 1
                } else {
                    item.like_num += 1
                    item.is_like = 1
                }
                listener?.onAssessTag(item)
                setView(holder.adapterPosition, item.is_like, tvSize, rlInfo, tvTitle, ivIcon,animation_view)
                tvSize.text = StringUtils.sizeToString(item.like_num)
            }
        }
    }

    /**
     * 设置多样背景
     */
    private fun setView(
        position: Int,
        is_like: Int,
        tvSize: TextView,
        rlInfo: LinearLayout,
        tvTitle: TextView,
        ivIcon: ImageView,
        animation_view:LottieAnimationView
    ) {

        if (is_like == 1) {
            ivIcon.visibility= View.GONE
            animation_view.visibility= View.VISIBLE
            tvTitle.setTextColor(ContextCompat.getColor(context, R.color.gray66))
        }else{
            ivIcon.visibility= View.VISIBLE
            animation_view.visibility= View.GONE
            ivIcon.setImageResource(R.mipmap.icon_like_g)
            rlInfo.background = ContextCompat.getDrawable(context, R.drawable.solid_rectangle_15_f1f8f2)
        }

        when (position % 3) {
            1 -> {
                tvSize.setTextColor(ContextCompat.getColor(context, R.color.color_0fb50a))
                if (is_like == 1) {
                    rlInfo.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_15_0fb50a)
                    animation_view.setAnimation("amin_felxbox_green.json")
                    setAnimation(animation_view,tvSize)
                } else {
                    tvTitle.setTextColor(ContextCompat.getColor(context, R.color.color_0fb50a))
                }
            }
            2 -> {
                tvSize.setTextColor(ContextCompat.getColor(context, R.color.color_d7ab00))
                if (is_like == 1) {
                    rlInfo.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_15_d7ab00)
                    animation_view.setAnimation("amin_felxbox_yellow.json")
                    setAnimation(animation_view,tvSize)
                } else {
                    tvTitle.setTextColor(ContextCompat.getColor(context, R.color.color_d7ab00))
                }
            }
            else -> {
                tvSize.setTextColor(ContextCompat.getColor(context, R.color.color_ff556e))
                if (is_like == 1) {
                    rlInfo.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_15_ff556e)
                    animation_view.setAnimation("amin_felxbox.json")
                    setAnimation(animation_view,tvSize)
                } else {
                    tvTitle.setTextColor(ContextCompat.getColor(context, R.color.color_ff556e))
                }
            }
        }
    }


    private fun setAnimation(lottieLike:LottieAnimationView,tvSize:View){
        lottieLike.playAnimation()
        lottieLike.addAnimatorListener(object : AnimatorListener {

            override fun onAnimationStart(animation: Animator?) {
                tvSize.startAnimation(AnimationUtils.loadAnimation(context, R.anim.view_scale));
            }
            override fun onAnimationEnd(animation: Animator?) {

            }
            override fun onAnimationCancel(animation: Animator?) {}
            override fun onAnimationRepeat(animation: Animator?) {}
        })
    }


    interface OnAssessTagListener {
        fun onAssessTag(item: AssessTagBean)
    }

    var listener: OnAssessTagListener? = null
}
