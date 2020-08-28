package com.tinytiger.titi.adapter.gametools


import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.center.MineGameBean
import com.tinytiger.common.net.data.gametools.GameInfoBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.utils.StringFormatUtil
import com.tinytiger.titi.widget.view.DownloadProgressView
import com.xwdz.download.core.DownloadEntry


/**
 *
 * @author zhw_luke
 * @date 2020/3/30 0030 下午 3:12
 * @Copyright 小虎互联科技
 * @since 2.1.0
 * @doc 游戏排行
 */
class GameLibrayAdapter :
    BaseQuickAdapter<GameInfoBean, BaseViewHolder>(R.layout.game_item_library, null) {
    var dpvList = ArrayList<DownloadProgressView>()

    override fun convert(holder: BaseViewHolder, bean: GameInfoBean) {
        holder.setGone(R.id.ivTab, false)
        holder.setGone(R.id.tvTab, true)
        when (holder.layoutPosition + 1) {
            1 -> {
                holder.setImageResource(R.id.ivTab, R.mipmap.icon_game_top1)
            }
            2 -> {
                holder.setImageResource(R.id.ivTab, R.mipmap.icon_game_top2)
            }
            3 -> {
                holder.setImageResource(R.id.ivTab, R.mipmap.icon_game_top3)
            }
            else -> {
                holder.setGone(R.id.ivTab, true)
                holder.setGone(R.id.tvTab, false)
                holder.setText(R.id.tvTab, "${holder.layoutPosition + 1}")
            }
        }

        GlideUtil.loadImg(holder.getView(R.id.iv_image), bean.logo)
        holder.setText(R.id.tvName, bean.name)

        if (bean.show_score == 1) {
            holder.setText(R.id.tvInfo, StringFormatUtil.changTVsize(bean.score.toString()))
            holder.setGone(R.id.tvInfo, false)
            holder.setGone(R.id.tv_unit, false)
        } else {    //显示暂无评分
            holder.setGone(R.id.tvInfo, true)
            holder.setGone(R.id.tv_unit, true)
        }

        var tag = ""
        if (bean.cate_list != null && bean.cate_list.size > 0) {
            for (i in bean.cate_list) {
                tag = "${tag + i} "
            }
        }
        holder.setText(R.id.tvLabel, tag)

        var dpvProgress = holder.getView<DownloadProgressView>(R.id.dpvProgress)
        dpvProgress.setTextSize(12)
        dpvProgress.setViewHeight(26)
        dpvProgress.post {
            dpvList.add(dpvProgress)
            dpvProgress.setDownloadUrl(composeMineGame(bean))
        }

        holder.getView<View>(R.id.rlItem).setOnClickListener {
            GameDetailActivity.actionStart(context, bean.id, 3)
        }
    }

    /**
     * 组装成MineGame数据
     */
    private fun composeMineGame(bean: GameInfoBean): MineGameBean {
        var item = MineGameBean()
        item.id = bean.id
        item.name = bean.name
        item.logo = bean.logo
        item.download_url = bean.download_url
        item.one_introduce = bean.one_introduce
        item.package_name_android = bean.package_name_android
        item.package_size_android = bean.package_size_android
        item.score = bean.score
        item.thumbnail = bean.thumbnail
        item.version = bean.version
        //未知参数
        item.cate_name = ""
        item.has_application = true

        return item
    }

    fun installApk() {
        for (i in dpvList) {
            i.newinstallApk()
        }
    }

    fun notifyUpdate(item: DownloadEntry) {
        for (i in dpvList) {
            if (i.downloadEntry != null && i.downloadEntry!!.id == item.id) {
                i.setDownloadData(item)
            }
        }
    }

}