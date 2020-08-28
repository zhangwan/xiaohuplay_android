package com.tinytiger.titi.adapter.search



import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R

import com.tinytiger.common.net.data.circle.CircleBean
import com.tinytiger.common.utils.umeng.SearchAgentUtils
import com.tinytiger.titi.ui.circle.detail.CirclesDetailsActivity

/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 圈子
 */
class SearchCircleAdapter :
    BaseQuickAdapter<CircleBean, BaseViewHolder>(R.layout.search_item_circle, null) {
    /**
     * 匹配字段
     */
    var searchTxt = ""
    override fun convert(holder: BaseViewHolder, item: CircleBean) {
        if (item.background!=null){
            GlideUtil.loadImg(holder.getView<ImageView>(R.id.ivIcon),item.background)
        }
        holder.setText(R.id.tvNum, StringUtils.sizeToString(item.hots))

        holder.setText(R.id.tv_title, "#${item.name}#")
        holder.getView<View>(R.id.ll_content).setOnClickListener {
            CirclesDetailsActivity.actionStart(context,item.id,"","")
            SearchAgentUtils.setSearchRoute(searchTxt,2)
        }


    }

}