package com.tinytiger.titi.adapter.gametools.sort


import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.center.MineGameBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.widget.view.DownloadProgressView
import com.xwdz.download.core.DownloadEntry


/*
* @author Tamas
* create at 2020/1/7 0007
* Email: ljw_163mail@163.com
* description: 游戏分类详情 list adapter
*/
class GameCategoryDetailAdapter :
    BaseQuickAdapter<MineGameBean, BaseViewHolder>(R.layout.game_item_type_detail, null) {

    val dpvList = ArrayList<DownloadProgressView>()

    override fun convert(holder: BaseViewHolder, item: MineGameBean) {

        GlideUtil.loadImg(holder.getView(R.id.iv_avatar), item.logo)
        holder.setText(R.id.tv_name, item.name)
        holder.setText(R.id.tv_desc, item.one_introduce)


        if (item.score <= 2) {
            holder.setText(R.id.tv_rate, "暂无评分")
        } else {
            holder.setText(R.id.tv_rate, item.score.toString() + "分")
        }


        val dpvProgress = holder.getView<DownloadProgressView>(R.id.dpvProgress)
        dpvProgress.post {
            dpvList.add(dpvProgress)
            dpvProgress.setDownloadUrl(item)
        }

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