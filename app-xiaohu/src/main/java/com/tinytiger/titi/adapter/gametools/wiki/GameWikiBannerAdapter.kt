package com.tinytiger.titi.adapter.gametools.wiki


import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.AdBean
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 游戏百科 顶部 banner   adapter
*/
class GameWikiBannerAdapter(data: ArrayList<AdBean>) :
    BaseQuickAdapter<AdBean, BaseViewHolder>(R.layout.wiki_item_type_banner, data) {

    var height = 200
    var width = 500
    override fun convert(holder: BaseViewHolder, bean: AdBean) {
        val ll_content = holder.getView<View>(R.id.ll_content)
        if (data.size > 1) {
            val params = ll_content.layoutParams
            params.width = width
            params.height = height
            ll_content.layoutParams = params
        }
        GlideUtil.loadImg(holder.getView(R.id.iv_banner), bean.image)
    }


}