package com.tinytiger.titi.adapter.gametools.sort


import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.gametools.category.GameCategoryBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 游戏分类列表  左边菜单 adapter
*/
class GameSortMenuAdapter:
    BaseQuickAdapter<GameCategoryBean, BaseViewHolder>(R.layout.game_item_type_menu, null) {

    var prePosition = 0

    override fun convert(holder: BaseViewHolder, item: GameCategoryBean) {
        holder.setText(R.id.tv_title, item.cate_name)
        GlideUtil.loadImg( holder.getView(R.id.iv_image),item.icon)

        if (prePosition == holder.adapterPosition) {
            holder.setTextColor(R.id.tv_title,ContextCompat.getColor(context, R.color.gray_4C3B30))
            holder.setBackgroundColor(R.id.ll_content, ContextCompat.getColor(context, R.color.gray_f5f6f7))
        } else {
            holder.setTextColor(R.id.tv_title,ContextCompat.getColor(context, R.color.gray_8A8A8A))
            holder.setBackgroundColor(R.id.ll_content, ContextCompat.getColor(context, R.color.white))
        }
    }

     fun setSelection( pos:Int) {
        this.prePosition = pos;
        notifyDataSetChanged();
    }



}