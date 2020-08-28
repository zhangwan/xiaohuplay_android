package com.tinytiger.titi.adapter.gametools.sort


import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.gametools.category.GameCategoryBean
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.category.GameCategoryDetailActivity


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 游戏分类列表  右边菜单 纯文字 adapter 子grid
*/
class GameCategoryListAdapter(data:ArrayList<GameCategoryBean.ChildCateBean>) :
    BaseQuickAdapter<GameCategoryBean.ChildCateBean, BaseViewHolder>(R.layout.game_item_type_list, data) {

    override fun convert(holder: BaseViewHolder, item: GameCategoryBean.ChildCateBean) {
        holder.setText(R.id.tv_title, item.cate_name)

        holder.getView<View>(R.id.tv_title).setOnClickListener {
            GameCategoryDetailActivity.actionStart(context,item.cate_name,item.id)
        }

    }


}