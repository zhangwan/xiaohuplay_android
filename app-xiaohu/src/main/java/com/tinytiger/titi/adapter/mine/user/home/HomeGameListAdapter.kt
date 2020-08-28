package com.tinytiger.titi.adapter.mine.user.home


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.center.MineGameBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 游戏库 adapter
*/
class HomeGameListAdapter : BaseQuickAdapter<MineGameBean, BaseViewHolder>(R.layout.game_item_lib, null) {

    override fun convert(holder: BaseViewHolder, item: MineGameBean) {
        GlideUtil.loadImg(holder.getView(R.id.iv_image),item.logo)
        holder.setText(R.id.tv_name,item.name)
        holder.setText(R.id.tv_desc,item.one_introduce)

    }



}