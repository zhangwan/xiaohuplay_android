package com.tinytiger.titi.adapter.other

import android.widget.ImageView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.widget.picbrowser.ImagePreviewLoader
import com.tinytiger.titi.R

/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 九宫格图片展示
 */
class Image9Adapter : BaseQuickAdapter<String, BaseViewHolder>(R.layout.view_iamge9_item, null) {

    var height = 200
    override fun convert(holder: BaseViewHolder, item: String) {
        val ivIcon = holder.getView<ImageView>(R.id.ivIcon)
        val lp = ivIcon.layoutParams
        lp.height = height
        ivIcon.layoutParams = lp

        GlideUtil.loadImgs(ivIcon, item)
        ivIcon.setOnClickListener {
            if (FastClickUtil.isFastClick(800)) {
                return@setOnClickListener
            }
            ImagePreviewLoader.showImagePreview(context, holder.adapterPosition, data)
        }
    }
}