package com.tinytiger.titi.adapter.circle.home

import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.net.data.AdBean
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R
import com.tinytiger.titi.utils.BannerStartUtils
import com.youth.banner.adapter.BannerAdapter
import com.youth.banner.util.BannerUtils

/**
 * @author lmq001
 * @date 2020/05/25 18:46
 * @copyright 小虎互联科技
 * @doc 圈子图片
 */
class CircleImageNetAdapter(mDatas: List<AdBean>?) : BannerAdapter<AdBean, BaseViewHolder>(mDatas) {

    override fun onCreateHolder(parent: ViewGroup?, viewType: Int): BaseViewHolder {
        val imageView = BannerUtils.getView(parent!!, R.layout.circle_item_banner_image) as ImageView
        //通过裁剪实现圆角
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            BannerUtils.setBannerRound(imageView, 30f)
        }
        return BaseViewHolder(imageView)
    }

    override fun onBindView(holder: BaseViewHolder, data: AdBean, position: Int, size: Int) {
        GlideUtil.loadImg(holder.getView(R.id.imageView), data.image)
        holder.getView<ImageView>(R.id.imageView).setOnClickListener {
            BannerStartUtils.setStartIntent3(data)
        }
    }

}



