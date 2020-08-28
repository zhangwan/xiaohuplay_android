package com.tinytiger.titi.adapter.gametools.wiki



import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.utils.image.GlideUtil

import com.tinytiger.titi.R

import com.tinytiger.titi.ui.game.category.bean.WikiBeanMutli



/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 百科 主页面 adapter
*/
class MultiWikiAdapter(data: ArrayList<WikiBeanMutli>) :
    BaseMultiItemQuickAdapter<WikiBeanMutli, BaseViewHolder>(data) {


    companion object {
        const val TYPE_LEVEL_TITLE = 0 //标题
        const val TYPE_LEVEL_WIKI = 1 //详情 list
    }


    init {
        addItemType(TYPE_LEVEL_TITLE, R.layout.wiki_item_title)
        addItemType(TYPE_LEVEL_WIKI, R.layout.wiki_item_info)
    }


    override fun convert(holder: BaseViewHolder, data: WikiBeanMutli) {
        when (holder.itemViewType) {
            TYPE_LEVEL_TITLE -> {
                holder.setText(R.id.tv_title,data.title)

            }
            TYPE_LEVEL_WIKI -> {
                val bean = data.wikiBean
                holder.setText(R.id.tv_title,bean.name)

                GlideUtil.loadImg(holder.getView(R.id.iv_avatar),bean.logo)

                // # 模块分类  1百科详情界面 2招募管理界面
                if(bean.category_id == 2){
                    holder.setText(R.id.tv_info,"管理员招募中")
                }else{
                    holder.setText(R.id.tv_info,"${bean.total}个词条")
                }
            }

            else -> {

            }
        }
    }

}