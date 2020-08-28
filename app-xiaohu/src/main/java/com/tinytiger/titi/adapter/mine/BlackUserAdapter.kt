package com.tinytiger.titi.adapter.mine



import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import com.tinytiger.common.net.data.mine.UserBlackBean
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil

import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R
import com.tinytiger.titi.widget.text.MedalTextView


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索用户记录
 */
class BlackUserAdapter :
    BaseQuickAdapter<UserBlackBean.DataBean.BlackBean, BaseViewHolder>(
        R.layout.setting_item_blackuser,
        null
    ) {

    override fun convert(holder: BaseViewHolder, item: UserBlackBean.DataBean.BlackBean) {

        val tv_nickname: MedalTextView = holder.getView(R.id.tvName)
        tv_nickname.setNickname(item.nickname)
        tv_nickname.setMedalIcon(item.medal_image)

        holder.setText(R.id.tvBlack, TimeZoneUtil.getShortTimeShowString(item.create_time) + " 加入黑名单")

        holder.getView<AvatarView>(R.id.avUser).setAvatar(item.avatar, item.master_type)
            .setUserId(item.user_id)

        holder.getView<TextView>(R.id.tvAttention).setOnClickListener {
            if (FastClickUtil.isFastClickTiming()) {
                return@setOnClickListener
            }
            listener?.onType(item.user_id,holder.adapterPosition)
        }
    }

    interface OnBlackUserListener {
        fun onType(uid:String, postion: Int)
    }

    var listener: OnBlackUserListener? = null

}