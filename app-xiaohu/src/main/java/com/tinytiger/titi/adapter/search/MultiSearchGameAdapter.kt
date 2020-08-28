package com.tinytiger.titi.adapter.search


import android.view.View

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.search.SearchBeanMulti
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.umeng.SearchAgentUtils
import com.tinytiger.titi.R

import com.tinytiger.titi.ui.game.category.GameCategoryDetailActivity
import com.tinytiger.titi.ui.game.info.GameDetailActivity



/**
 *
 * @author zhw_luke
 * @date 2019/11/12 0012 上午 10:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 搜索数据展示页
 */
class MultiSearchGameAdapter(data: ArrayList<SearchBeanMulti>) :
    BaseMultiItemQuickAdapter<SearchBeanMulti, BaseViewHolder>(data) {

    init {
        //title
        addItemType(2, R.layout.search_item_game2)

        //游戏
        addItemType(1, R.layout.search_item_game)
        addItemType(0, R.layout.item_text2)
    }

    var dp5 = 5
    /**
     * 匹配字段
     */
    var searchTxt = ""
    val category = ArrayList<SearchBeanMulti>()
    override fun convert(helper: BaseViewHolder, item: SearchBeanMulti) {
        when (helper.itemViewType) {
            2 -> {
                val mData = item.mSearchGameInfo
                helper.setText(R.id.tvInfo, mData.cate_name)
                helper.getView<View>(R.id.rlItem).setOnClickListener {
                    GameCategoryDetailActivity.actionStart(context, mData.cate_name, mData.id.toInt())
                }
            }
            1 -> {
                val mData = item.mSearchGameInfo

                var cate_two = ""
                if (mData.cate_two_level != null && mData.cate_two_level.size > 0) {
                    var size=0
                    for (i in mData.cate_two_level) {
                        size++
                        if(size<4){
                            cate_two = "$cate_two${i.cate_name}/"
                        }
                    }
                    cate_two=cate_two.substring(0,cate_two.length-1)
                }
                helper.setText(R.id.tvInfo, cate_two)
                helper.setText(R.id.tvName, mData.name)
                GlideUtil.loadImg(helper.getView(R.id.iv_image), mData.logo)
                helper.getView<View>(R.id.rlItem).setOnClickListener {
                    GameDetailActivity.actionStart(context, mData.id,5)
                    SearchAgentUtils.setSearchRoute(searchTxt,0)
                }
            }
            0 -> {
                helper.getView<View>(R.id.ll_content).setOnClickListener {
                    remove(item)
                    addData(5, category.subList(5, category.size))
                }
            }
        }
    }

}
