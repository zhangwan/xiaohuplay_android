package com.tinytiger.titi.adapter.home2.push

import android.widget.LinearLayout
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.home2.GameBannerBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.info.GameDetailActivity

/**
 * 首页推荐-游戏列表
 */
class HomeGamePushAdapter :
    BaseQuickAdapter<GameBannerBean, BaseViewHolder>(R.layout.item_home_game, null) {

    override fun convert(holder: BaseViewHolder, item: GameBannerBean) {
        GlideUtil.loadImg(holder.getView(R.id.ivLogo), item.game_logo)
        var tvName = holder.getView<TextView>(R.id.tv_name)
        tvName.text=item.game_name
        holder.getView<LinearLayout>(R.id.llContent).setOnClickListener {
            GameDetailActivity.actionStart(context,item.game_id!!,8)
        }
    }
}