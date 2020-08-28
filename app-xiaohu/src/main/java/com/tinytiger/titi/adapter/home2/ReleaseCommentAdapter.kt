package com.tinytiger.titi.adapter.home2


import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.AppCompatRatingBar
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.makeramen.roundedimageview.RoundedImageView
import com.tinytiger.common.net.data.home2.AmwayBean
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.utils.image.GlideUtil
import com.tinytiger.common.view.textview.ShowAllTextView
import com.tinytiger.titi.R
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.LikeView


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 我的作品  安利文
 */
class ReleaseCommentAdapter :
    BaseQuickAdapter<AmwayBean, BaseViewHolder>(R.layout.release_item_comment, null) {

    /**
     * 使用在我的作品中
     * 是否显示选中状态
     */
    var showDeleteIcon = false

    override fun convert(holder: BaseViewHolder, item: AmwayBean) {
        val iv_game = holder.getView<RoundedImageView>(R.id.iv_game)
        val tvName = holder.getView<MedalTextView>(R.id.tvName)
        val tvTime = holder.getView<TextView>(R.id.tvTime)
        val tvRead = holder.getView<TextView>(R.id.tvRead)
        val viewLike = holder.getView<LikeView>(R.id.viewLike)
        val tvDesc = holder.getView<ShowAllTextView>(R.id.tvDesc)
        val tvTitle = holder.getView<TextView>(R.id.tvTitle)
        val iv_more = holder.getView<ImageView>(R.id.iv_more)
        val tvShare = holder.getView<TextView>(R.id.tvShare)
        val ratingBar = holder.getView<AppCompatRatingBar>(R.id.ratingBar)

        GlideUtil.loadImg(iv_game, item.logo)
        tvName.setNickname(item.name)
        tvTime.text = TimeZoneUtil.getShortTimeShowString(item.create_time)
        tvRead.text = StringUtils.sizeToString(item.comment_num)
        viewLike.setLike(item.is_like, item.like_num)
        tvDesc.create(ShowAllTextView.Builder().setText(StringUtils.toPlainText(item.content)))
        tvShare.text = StringUtils.sizeToString(item.share_num)
        ratingBar.rating = Math.round(item.score).toFloat()
        if (item.title.isNotEmpty()) {
            holder.setGone(R.id.tvTitle, false)
            tvTitle.text = item.title
        } else {
            holder.setGone(R.id.tvTitle, true)
        }

        viewLike.mListener = LikeView.OnLikeViewListener {
            listener?.onLike(item)
        }
        iv_more.setOnClickListener {
            listener!!.onDelete(item, iv_more)
        }
    }


    interface OnHomePushListener {
        fun onLike(item: AmwayBean)
        fun onChecked(check: Boolean)
        fun onDelete(item: AmwayBean, view: View)
    }

    var listener: OnHomePushListener? = null

}
