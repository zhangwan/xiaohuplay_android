package com.tinytiger.titi.adapter.home2.push


import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.center.MineGameBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.info.GameDetailActivity


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页_标签列表
 */
class HomeGame3dapter :
    BaseQuickAdapter<MineGameBean, BaseViewHolder>(R.layout.home_push_item_game3, null) {

    override fun convert(holder: BaseViewHolder, item: MineGameBean) {
        holder.setText(R.id.tvName, item.name)
        holder.setText(R.id.tvDesc, item.cate_name.replace(",","/"))
        GlideUtil.loadImg(holder.getView(R.id.ivLogo), item.thumbnail)

        holder.getView<View>(R.id.rl_content).setOnClickListener {
            GameDetailActivity.actionStart(context, item.id,0)
        }

    }
}
