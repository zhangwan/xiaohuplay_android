package com.tinytiger.titi.adapter.home2.push

import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.center.MineGameBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.widget.view.DownloadProgressView
import com.xwdz.download.core.DownloadEntry


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页_新奇游戏
 */
class HomeGame2dapter :
    BaseQuickAdapter<MineGameBean, BaseViewHolder>(R.layout.home_push_item_game2, null) {
    val dpvLits = ArrayList<DownloadProgressView>()

    override fun convert(holder: BaseViewHolder, item: MineGameBean) {
        holder.setGone(R.id.vLine, holder.adapterPosition / 3 != 0)
        holder.setText(R.id.tvName, item.name)
        holder.setText(R.id.tvDesc, item.one_introduce)
        GlideUtil.loadImg(holder.getView(R.id.ivLogo), item.logo)

        val dpvProgress = holder.getView<DownloadProgressView>(R.id.dpvProgress)
        dpvProgress.post {
            dpvLits.add(dpvProgress)
            dpvProgress.setDownloadUrl(item)
        }

        holder.getView<View>(R.id.rl_content).setOnClickListener {
            GameDetailActivity.actionStart(context, item.id,0)
        }
    }

    fun notifyUpdate(item: DownloadEntry) {
        for (i in dpvLits) {
            if (i.downloadEntry != null && i.downloadEntry!!.id == item.id) {
                i.setDownloadData(item)
            }
        }
    }

    fun installApk(){
        for (i in dpvLits){
            i.newinstallApk()
        }
    }
}
