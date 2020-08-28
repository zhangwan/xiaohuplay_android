package com.tinytiger.titi.adapter.gametools.sort


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.titi.R


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 游戏百科  历史搜索数据 adapter
*/
class GameSearchHistoryAdapter(data:ArrayList<String>) :
    BaseQuickAdapter<String, BaseViewHolder>(R.layout.game_item_search_history ,data) {

     var showDelete = false

    override fun convert(holder: BaseViewHolder, item: String) {
        holder.setText(R.id.tv_title, item)
        holder.setGone(R.id.tv_delete,!showDelete)
    }


}