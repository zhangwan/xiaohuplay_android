package com.tinytiger.titi.adapter.gametools



import android.view.ViewGroup
import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder

import com.tinytiger.common.utils.ScreenUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.titi.R


/*
* @author Tamas
* create at 2020/02/26 0016
* Email: ljw_163mail@163.com
* description: 游戏介绍 画廊效果 adapter 横竖屏
*/
class GameIntroAdapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.game_item_introduction, null) {

    var isHorizontal = true

    var video_url = ""

    override fun convert(holder: BaseViewHolder, bean: String) {
       // val fl_container = holder.getView<FrameLayout>(R.id.fl_container)

     val   iv_image= holder.getView<ImageView>(R.id.iv_image)

        val params = iv_image.layoutParams
        if (isHorizontal) {
          //  params.width = ScreenUtil.dp2px(context, 300f)

            params.width = ViewGroup.LayoutParams.WRAP_CONTENT

            params.height = ScreenUtil.dp2px(context, 153f)
        } else {
           // params.width = ScreenUtil.dp2px(context, 160f)

            params.width = ViewGroup.LayoutParams.WRAP_CONTENT
            params.height = ScreenUtil.dp2px(context, 272f)
        }
        iv_image.layoutParams = params

        GlideUtil.loadImg(holder.getView(R.id.iv_image),bean)
        holder.setGone(R.id.iv_play, !(holder.adapterPosition == 0 &&video_url.isNotEmpty()))

    }


}