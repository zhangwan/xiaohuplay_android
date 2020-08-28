package com.tinytiger.titi.adapter.mine.user.mygame


import android.view.View
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.center.MineGameBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.widget.view.DownloadProgressView
import com.xwdz.download.core.DownloadEntry


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 我的游戏 下载 adapter
*/
class MineGameDownloadAdapter : BaseQuickAdapter<MineGameBean, BaseViewHolder>(R.layout.mine_item_game_download, null) {

    val dpvList = ArrayList<DownloadProgressView>()

    override fun convert(holder: BaseViewHolder, item: MineGameBean) {
        GlideUtil.loadImg(holder.getView(R.id.iv_avatar), item.logo)
        holder.setText(R.id.tv_name, item.name)


        val dpvProgress = holder.getView<DownloadProgressView>(R.id.dpvProgress)
        dpvProgress.post {
            dpvList.add(dpvProgress)
            dpvProgress.setDownloadUrl(item)
        }
       val ivMore= holder.getView<View>(R.id.ivMore)
        ivMore.setOnClickListener {
            mOnGameDownloadListener?.clickMore(ivMore,item,dpvProgress.status)
        }


    }


    fun notifyUpdate(item: DownloadEntry) {
        for (i in dpvList) {
            if (i.downloadEntry != null && i.downloadEntry!!.id == item.id) {
                i.setDownloadData(item)
            }
        }
    }

     var mOnGameDownloadListener: OnGameDownloadListener? = null

    interface OnGameDownloadListener {
        //更多
        fun clickMore(view: View, item: MineGameBean, status:DownloadEntry.Status)

    }

}