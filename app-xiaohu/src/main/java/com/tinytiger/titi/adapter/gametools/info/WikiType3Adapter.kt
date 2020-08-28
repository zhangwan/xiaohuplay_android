package com.tinytiger.titi.adapter.gametools.info


import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.gametools.wiki.WikiCategoryInfoBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.wiki.GameWikiDetailActivity


/**
 *
 * @author zhw_luke
 * @date 2020/3/26 0026 下午 4:31
 * @Copyright 小虎互联科技
 * @since 2.0.0
 * @doc wiki 左图右文
 */
class WikiType3Adapter : BaseQuickAdapter<WikiCategoryInfoBean, BaseViewHolder>(R.layout.game_wiki_item_imgtext2, null) {

    var game_id=""
    override fun convert(holder: BaseViewHolder, item: WikiCategoryInfoBean) {
        holder.setText(R.id.tvTitle,item.name)
        GlideUtil.loadImg(holder.getView(R.id.ivIcon),item.img_url)
        holder.getView<View>(R.id.rl_content).setOnClickListener {
            GameWikiDetailActivity.actionStart(context,  item.id)
        }

    }



}