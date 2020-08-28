package com.tinytiger.titi.adapter.gametools.sort


import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.gametools.category.GameCategoryBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.category.GameCategoryDetailActivity


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 游戏分类列表  右边菜单 图文 adapter 子grid
*/
class GameCategoryImageAdapter(data:ArrayList<GameCategoryBean.ChildCateBean>) :
    BaseQuickAdapter<GameCategoryBean.ChildCateBean, BaseViewHolder>(R.layout.game_item_type_image, data) {

    override fun convert(holder: BaseViewHolder, item: GameCategoryBean.ChildCateBean) {
        holder.setText(R.id.tv_title, item.cate_name)
        GlideUtil.loadImg(holder.getView(R.id.iv_image),item.icon)

        holder.getView<View>(R.id.ll_content).setOnClickListener {
            GameCategoryDetailActivity.actionStart(context,item.cate_name,item.id)
        }

    }


}