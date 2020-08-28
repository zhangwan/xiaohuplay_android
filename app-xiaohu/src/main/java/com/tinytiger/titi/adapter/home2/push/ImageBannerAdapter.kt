package com.tinytiger.titi.adapter.home2.push

import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.home2.BannerAdBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.utils.preference.SpUtils
import com.tinytiger.common.widget.radarview.RadarViewRoot
import com.tinytiger.titi.R


class ImageBannerAdapter :
    BaseQuickAdapter<BannerAdBean, BaseViewHolder>(R.layout.item_home_banner, null) {
    var bannerClickListener: ((bean: BannerAdBean) -> Unit)? = null
    var bespokeClickListener: ((bean: BannerAdBean, type: String,position:Int) -> Unit)? = null
    override fun convert(holder: BaseViewHolder, item: BannerAdBean) {
        var ivBannerBg = holder.getView<ImageView>(R.id.ivBannerBg)
        var tvLabel = holder.getView<TextView>(R.id.tv_label0)
        var tvLabel1 = holder.getView<TextView>(R.id.tv_label1)
        var tvLabel2 = holder.getView<TextView>(R.id.tv_label2)
        var radarRoot = holder.getView<RadarViewRoot>(R.id.radarRoot)
        var mTvSecondPlay = holder.getView<TextView>(R.id.tvBespoke)

        GlideUtil.loadImg(ivBannerBg, item.image, 6f)
        if (!TextUtils.isEmpty(item?.gameInfo?.name)) {
            holder.setText(R.id.tvBannerGameName, item?.gameInfo?.name)
        }
        if ("1" == item?.cloud_game || "1" == item.gameInfo?.appointment_type || "2" == item.gameInfo?.appointment_type) {
            if ("1" == item.cloud_game) {
                mTvSecondPlay.setBackgroundResource(R.mipmap.second_play)
                mTvSecondPlay.setTextColor(
                    ContextCompat.getColor(mTvSecondPlay.context, R.color.gray33))
                mTvSecondPlay.text = mTvSecondPlay.context.getString(R.string.home_second_play)
            }
            if ("1" == item.gameInfo?.appointment_type) {
                mTvSecondPlay.setBackgroundResource(R.mipmap.second_play)
                mTvSecondPlay.setTextColor(
                    ContextCompat.getColor(mTvSecondPlay.context, R.color.gray33))
                mTvSecondPlay.text = mTvSecondPlay.context.getString(R.string.home_bespoke)
            }
            mTvSecondPlay.isEnabled=true
            if ("2" == item.gameInfo?.appointment_type) {
                if (SpUtils.getString(com.tinytiger.common.R.string.token, "").isNotEmpty()) {
                    mTvSecondPlay.setBackgroundResource(R.drawable.solid_rectangle_5_d8d8d8)
                    mTvSecondPlay.setTextColor(ContextCompat.getColor(mTvSecondPlay.context, R.color.white))
                    mTvSecondPlay.text = mTvSecondPlay.context.getString(R.string.home_bespoke_succeed)
                    mTvSecondPlay.isEnabled=false
                } else {
                    mTvSecondPlay.setBackgroundResource(R.mipmap.second_play)
                    mTvSecondPlay.setTextColor(ContextCompat.getColor(mTvSecondPlay.context, R.color.gray33))
                    mTvSecondPlay.text = mTvSecondPlay.context.getString(R.string.home_bespoke)
                }

            }
            holder.setGone(R.id.tvBespoke, false)
        } else {
            holder.setGone(R.id.tvBespoke, true)
        }

        if (item.gameInfo?.cate_info != null) {
            for (i in 0 until item.gameInfo?.cate_info!!.size) {
                if (i > 2) {
                    break
                }
                when (i) {
                    0 -> {
                        tvLabel.text = item.gameInfo?.cate_info!![i]
                    }
                    1 -> {
                        tvLabel1.text = item.gameInfo?.cate_info!![i]
                    }
                    2 -> {
                        tvLabel2.text = item.gameInfo?.cate_info!![i]
                    }
                }
            }
        }
        //雷达图展示
        val leader_mage = item.gameInfo?.leader_mage
        if (leader_mage != null) {
            holder.setGone(R.id.radarRoot, false)
            var radarData = mutableListOf<Float>()
            radarData.add(leader_mage.playway_score!!.toFloat())
            radarData.add(leader_mage.music_score!!.toFloat())
            radarData.add(leader_mage.conscience_score!!.toFloat())
            radarData.add(leader_mage.control_score!!.toFloat())
            radarData.add(leader_mage.art_score!!.toFloat())
            radarRoot.setData(radarData)
        } else {
            holder.setGone(R.id.radarRoot, true)
        }
        if (TextUtils.isEmpty(tvLabel.text)) {
            tvLabel.visibility = View.GONE
        } else {
            tvLabel.visibility = View.VISIBLE
        }
        var maxLen = tvLabel.text.length + tvLabel1.text.length
        if (maxLen > 13 || TextUtils.isEmpty(tvLabel1.text.toString())) {
            tvLabel1.visibility = View.GONE
        } else {
            tvLabel1.visibility = View.VISIBLE
        }
        var maxLen01 = tvLabel.text.length + tvLabel1.text.length + tvLabel2.text.length
        if (maxLen01 > 13 || TextUtils.isEmpty(tvLabel2.text.toString())) {
            tvLabel2.visibility = View.GONE
        } else {
            tvLabel2.visibility = View.VISIBLE
        }
        ivBannerBg.setOnClickListener {
            bannerClickListener?.invoke(item)
        }
        mTvSecondPlay.setOnClickListener {
            bespokeClickListener?.invoke(item, mTvSecondPlay.text.toString(),holder.adapterPosition)
        }
        if (item.gameInfo == null) {
            holder.setGone(R.id.llLabel, true)
            holder.setGone(R.id.tvBannerGameName, true)
            holder.setGone(R.id.tvBespoke, true)
        } else {
            holder.setGone(R.id.llLabel, false)
            holder.setGone(R.id.tvBannerGameName, false)
        }


    }
}