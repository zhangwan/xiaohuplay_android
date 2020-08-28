package com.tinytiger.titi.adapter.mine

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.gametools.GameInfoBean
import com.tinytiger.common.net.data.gametools.PlayBean
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.view.title.AvatarView
import com.tinytiger.titi.R

/**
 * @author zwy
 * create at 2020/7/3 0003
 * description:
 */
class GameCloudAdapter : BaseQuickAdapter<PlayBean, BaseViewHolder>(R.layout.item_game_cloud, null) {

    override fun convert(holder: BaseViewHolder, bean: PlayBean) {
        holder.getView<AvatarView>(R.id.avUserTitle).setAvatar(bean.game_logo)
        holder.setText(R.id.tvGameName, bean.game_name)
        holder.setText(R.id.tvGameTime, TimeZoneUtil.getShortTimeShowString(bean.create_time))
        holder.setText(R.id.tvDuration, "-"+TimeZoneUtil.convertTime(bean.play_time!!.toInt(),true))

    }
}