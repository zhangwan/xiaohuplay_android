package com.tinytiger.titi.adapter.home2.push


import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.center.MineGameBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.ui.game.info.GameDetailActivity
import com.tinytiger.titi.ui.yungame.YunGameActivity
import com.tinytiger.titi.widget.view.DownloadProgressView
import com.xwdz.download.core.DownloadEntry


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 首页_每日推荐
 */
class HomeGame1dapter :
    BaseQuickAdapter<MineGameBean, BaseViewHolder>(R.layout.home_push_item_game1, null) {

    private val dpvLits = ArrayList<DownloadProgressView>()
    private val YunGame = ArrayList<TextView>()

    override fun convert(holder: BaseViewHolder, item: MineGameBean) {
        holder.setGone(R.id.vLine, holder.adapterPosition / 2 != 0)
        holder.setText(R.id.tvName, item.name)
        holder.setText(R.id.tvDesc, item.one_introduce)
        GlideUtil.loadImg(holder.getView(R.id.ivLogo), item.logo)

        val tvYunGame = holder.getView<TextView>(R.id.tvYunGame)
        if (item.is_cloud_game == 1) {
            holder.setGone(R.id.tvLine, false)
            tvYunGame.visibility = View.VISIBLE
            tvYunGame.setOnClickListener {
                YunGameActivity.actionStart(context, item.id, item.package_name_android)
            }
        } else {
            tvYunGame.visibility = View.GONE
            holder.setGone(R.id.tvLine, true)
        }
        tvYunGame.setTag(R.id.tag_text, item.download_url)
        YunGame.add(tvYunGame)
        val dpvProgress = holder.getView<DownloadProgressView>(R.id.dpvProgress)
        dpvProgress.post {
            dpvLits.add(dpvProgress)
            if (item.is_cloud_game == 1) {
                dpvProgress.setPackageUi(2)
            }
            dpvProgress.setDownloadUrl(item)
            setTextBG(item.download_url, dpvProgress)
        }


        holder.getView<View>(R.id.rl_content).setOnClickListener {
            GameDetailActivity.actionStart(context, item.id, 0)
        }
    }

    fun notifyUpdate(item: DownloadEntry) {
        for (i in dpvLits) {
            if (i.downloadEntry != null && i.downloadEntry!!.id == item.id) {
                i.setDownloadData(item)
                setTextBG(item.id, i)
            }
        }
    }

    fun installApk() {
        for (i in dpvLits) {
            i.newinstallApk()
        }
    }

    fun setTextBG(id: String, item: DownloadProgressView) {
        for (j in YunGame) {
            if (id == j.getTag(R.id.tag_text)) {

                //      Logger.d(id+"------${item.apkInfo}--------"+item.status)

                if (item.status == DownloadEntry.Status.COMPLETED) {
                    j.setTextColor(ContextCompat.getColor(context, R.color.white))
                    if (item.apkInfo == 0) {
                        j.setTextColor(ContextCompat.getColor(context, R.color.white))
                    } else if (item.apkInfo == 2 ) {
                        j.setTextColor(ContextCompat.getColor(context, R.color.white))
                    } else {
                        j.setTextColor(ContextCompat.getColor(context, R.color.gray33))
                    }
                } else {
                    j.setTextColor(ContextCompat.getColor(context, R.color.gray33))
                }
            }
        }
    }
}
