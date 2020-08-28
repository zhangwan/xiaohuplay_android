package com.tinytiger.titi.adapter.home2.find


import android.view.View
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.ad.AdClassify
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.category.GameCategoryDetailActivity
import com.tinytiger.titi.ui.game.category.GameCategoryListActivity


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页_二级分类列表
 */
class GameClassifyAdapter(data: ArrayList<AdClassify>) :
    BaseQuickAdapter<AdClassify, BaseViewHolder>(R.layout.home_item_find_classify, data) {

    override fun convert(holder: BaseViewHolder, item: AdClassify) {
        if (item.game_category_id != 0) {
            holder.setText(R.id.tvName, item.cate_name)
            holder.getView<ImageView>(R.id.iv_game).scaleType = ImageView.ScaleType.CENTER_CROP
            GlideUtil.loadImg(holder.getView(R.id.iv_game), item.icon)

            holder.getView<View>(R.id.rl_content).setOnClickListener {
                GameCategoryDetailActivity.actionStart(
                    context, item.cate_name, item.game_category_id
                )
            }

        } else {    //更多分类
            holder.setText(R.id.tvName, "更多分类")
            holder.getView<ImageView>(R.id.iv_game).scaleType = ImageView.ScaleType.CENTER
            holder.setImageResource(R.id.iv_game, R.mipmap.ic_home_more_classify)

            holder.getView<View>(R.id.rl_content).setOnClickListener {
                GameCategoryListActivity.actionStart(context)
            }
        }
    }
}
