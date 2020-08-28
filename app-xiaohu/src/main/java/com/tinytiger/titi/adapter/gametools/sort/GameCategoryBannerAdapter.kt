package com.tinytiger.titi.adapter.gametools.sort


import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.recyclerview.widget.RecyclerView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.gametools.category.GameCategoryBannerBean
import com.tinytiger.common.net.data.wiki.BannerBean
import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 游戏分类详情 顶部 banner   adapter
*/
class GameCategoryBannerAdapter(data:ArrayList<GameCategoryBannerBean.DataBean>) :
    BaseQuickAdapter<GameCategoryBannerBean.DataBean, BaseViewHolder>(R.layout.wiki_item_type_banner, data) {

    var height = 200
    var width = 500
    override fun convert(holder: BaseViewHolder, item: GameCategoryBannerBean.DataBean) {
        val ll_content = holder.getView<View>(R.id.ll_content)
        if(data.size>1) {
            val params = ll_content.layoutParams
            params.width = width
            params.height = height
            if(params is RecyclerView.LayoutParams){
                params.leftMargin=0
            }
            ll_content.layoutParams = params
        }else{
            val params = ll_content.layoutParams
            params.width = LinearLayout.LayoutParams.MATCH_PARENT
            params.height = height
            if(params is RecyclerView.LayoutParams){
                params.leftMargin=context.resources.getDimensionPixelOffset(R.dimen.size10)
            }

            ll_content.layoutParams = params
        }
        GlideUtil.loadImg(holder.getView<ImageView>(R.id.iv_banner),item.img_url)

    }


}