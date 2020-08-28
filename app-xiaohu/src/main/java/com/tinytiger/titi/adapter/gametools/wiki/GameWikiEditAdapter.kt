package com.tinytiger.titi.adapter.gametools.wiki


import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.gametools.GameInfoBean
import com.tinytiger.common.net.data.gametools.Wiki_info
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.info.GameDetailActivity


/**
 * @author lmq001
 * @date 2020/06/01 11:10
 * @copyright 小虎互联科技
 * @doc 查看词条编辑者
 */
class GameWikiEditAdapter : BaseQuickAdapter<Wiki_info, BaseViewHolder>(R.layout.game_item_wiki_edit, null) {

    override fun convert(holder: BaseViewHolder, bean: Wiki_info) {
        holder.setText(R.id.tv_name, bean.nickname)
        holder.setText(R.id.tv_time, bean.create_time)
    }

}