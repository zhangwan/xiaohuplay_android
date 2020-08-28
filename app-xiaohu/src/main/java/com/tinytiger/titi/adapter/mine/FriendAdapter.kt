package com.tinytiger.titi.adapter.mine


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.user.FriendBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.mine.homepage.HomepageActivity
import com.tinytiger.titi.widget.text.MedalTextView


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索用户记录
 */
class FriendAdapter :
    BaseQuickAdapter<FriendBean, BaseViewHolder>(
        R.layout.mine_item_me_friend,
        null
    ) {
    var uid = ""
    override fun convert(holder: BaseViewHolder, bean: FriendBean) {
        holder.getView<AvatarView>(R.id.avUser).setAvatar(bean.avatar, bean.master_type)
            .setUserId(bean.user_id)

        val tv_nickname = holder.getView<MedalTextView>(R.id.tvName)
        tv_nickname.setNickname(bean.nickname)
        tv_nickname.setMedalIcon(bean.medal_image)

        GlideUtil.loadImg(holder.getView<ImageView>(R.id.ivIcon), bean.avatar)

        val tvAttention = holder.getView<TextView>(R.id.tvAttention)
        //1:互相关注 0:已关注 -1:未关注 -2:自己

        if (uid.isEmpty()) {
            setMutualMe(bean.is_mutual, tvAttention)
        } else {
            setMutual(bean.is_mutual, tvAttention)
        }


        tvAttention.setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            listener?.onType(bean, holder.adapterPosition)
        }
    }


    interface onFriendListener {
        fun onType(mBean: FriendBean, postion: Int)
    }

    var listener: onFriendListener? = null

    private fun setMutualMe(is_mutual: Int, tvAttention: TextView) {
        tvAttention.visibility = View.VISIBLE

        if (is_mutual == -1) {
            tvAttention.isSelected = false
            tvAttention.text = "+ 关注"
            tvAttention.setTextColor(ContextCompat.getColor(context, R.color.color_ffcc03))
            tvAttention.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_15_ffcc03)

        } else {
            tvAttention.isSelected = true
            tvAttention.text = "互相关注"
            tvAttention.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
            tvAttention.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_20_aaaaaa)
        }

    }

    private fun setMutual(is_mutual: Int, tvAttention: TextView) {
        tvAttention.visibility = View.VISIBLE
        when (is_mutual) {
            1 -> {
                tvAttention.isSelected = true
                tvAttention.text = "互相关注"
                tvAttention.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                tvAttention.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_20_aaaaaa)
            }
            0 -> {
                tvAttention.isSelected = true
                tvAttention.text = "已关注"
                tvAttention.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                tvAttention.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_20_aaaaaa)
            }
            -1 -> {
                tvAttention.isSelected = false
                tvAttention.text = "+ 关注"
                tvAttention.setTextColor(ContextCompat.getColor(context, R.color.color_ffcc03))
                tvAttention.background = ContextCompat.getDrawable(context, R.drawable.stroke_rectangle_15_ffcc03)
            }
            -2 -> {
                tvAttention.visibility = View.GONE
            }
        }
    }

}