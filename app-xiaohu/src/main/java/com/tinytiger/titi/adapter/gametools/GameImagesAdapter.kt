package com.tinytiger.titi.adapter.gametools


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.orhanobut.logger.Logger
import com.tinytiger.common.net.data.mine.ContentBean
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 游戏库 adapter
*/
class GameImagesAdapter(data:ArrayList<String>) : BaseQuickAdapter<String, BaseViewHolder>(R.layout.game_item_image, data) {

    override fun convert(holder: BaseViewHolder, item: String) {
            GlideUtil.loadImg(holder.getView(R.id.iv_image),item)

    }



}