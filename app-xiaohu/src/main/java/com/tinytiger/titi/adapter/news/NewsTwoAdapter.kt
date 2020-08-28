package com.tinytiger.titi.adapter.news


import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.chad.library.adapter.base.BaseMultiItemQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.tinytiger.common.utils.FastClickUtil
import com.tinytiger.common.utils.StringUtils
import com.tinytiger.common.utils.TimeZoneUtil
import com.tinytiger.common.view.textview.ShowAllTextView
import com.tinytiger.common.view.title.AvatarView

import com.tinytiger.titi.R
import com.tinytiger.titi.data.comment.NewsTwoMulti
import com.tinytiger.titi.widget.text.MedalTextView
import com.tinytiger.titi.widget.view.Anim.LikeView


/**
 *
 * @author zhw_luke
 * @date 2019/11/8 0008 上午 9:57
 * @Copyright 小虎互联科技
 * @since 5.0.0
 * @doc 资讯二级评论页
 */
class NewsTwoAdapter(data: ArrayList<NewsTwoMulti>) :
    BaseMultiItemQuickAdapter<NewsTwoMulti, BaseViewHolder>(data) {

    init {
        addItemType(1, R.layout.comment_item_two_top)
        addItemType(2, R.layout.comment_item_two)

    }

    var author_id = "0"
    var lz_id = "0"
    var userId = "0"

    var tvTitle:TextView?=null
    override fun convert(holder: BaseViewHolder, item: NewsTwoMulti) {
        when (holder.itemViewType) {
            1 -> {
                val bean = item.recommendBean
                holder.getView<AvatarView>(R.id.avUser)
                    .setAvatar(bean.replysUserinfo.avatar, bean.replysUserinfo.master_type)
                    .setUserId(bean.user_id)
                holder.getView<MedalTextView>(R.id.comment_item_userName)
                    .setNickname(StringUtils.stringName(bean.replysUserinfo.nickname),
                        bean.replysUserinfo.medal_image).setUserId(bean.replysUserinfo.user_id)
                lz_id = bean.replysUserinfo.user_id
                when (bean.replysUserinfo.user_id) {
                    author_id -> {
                        holder.setText(R.id.comment_item_author, "作者")
                        holder.setGone(R.id.comment_item_author, false)
                    }
                    lz_id -> {
                        holder.setText(R.id.comment_item_author, "楼主")
                        holder.setGone(R.id.comment_item_author, false)
                    }
                    else -> {
                        holder.setGone(R.id.comment_item_author, true)
                    }
                }

                holder.getView<ShowAllTextView>(R.id.satvDesc)
                    .create(ShowAllTextView.Builder().setText(bean.content))
                holder.setText(R.id.comment_item_time,
                    TimeZoneUtil.getShortTimeShowString(bean.create_time))
                holder.setGone(R.id.comment_item_hot, bean.has_badge == 1)

                val viewLike = holder.getView<LikeView>(R.id.viewLike)
                viewLike.setLike(bean.is_like, bean.like_num)
                viewLike.mListener = LikeView.OnLikeViewListener {
                    bean.like_num = viewLike.like_num
                    bean.is_like = viewLike.is_like
                    mListener?.clickLike(bean.id, bean.is_like)
                }

                if (bean.replysUserinfo.user_id == userId) {
                    holder.setGone(R.id.comment_item_more, false)
                    holder.getView<View>(R.id.comment_item_more).setOnClickListener {
                        if (bean.replysUserinfo.user_id == userId) {
                            mListener?.clickDelete(bean.id)
                        }
                    }
                } else {
                    holder.setGone(R.id.comment_item_more, true)
                }

                holder.getView<View>(R.id.ll_content).setOnClickListener {
                    mListener?.clickReply(item)
                }

                tvTitle=holder.getView(R.id.tvTitle)
                val tvSelect1 = holder.getView<TextView>(R.id.tvSelect1)
                val tvSelect2 = holder.getView<TextView>(R.id.tvSelect2)
                val tvSelect3 = holder.getView<TextView>(R.id.tvSelect3)

                tvSelect1.setOnClickListener {
                    if (FastClickUtil.isFastClick()) {
                        return@setOnClickListener
                    }
                    tvSelect1.setTextColor(ContextCompat.getColor(context, R.color.color_ffcc03))
                    tvSelect2.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                    tvSelect3.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                    mListener?.onSelectClick(1)
                }
                tvSelect2.setOnClickListener {
                    if (FastClickUtil.isFastClick()) {
                        return@setOnClickListener
                    }
                    tvSelect1.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                    tvSelect2.setTextColor(ContextCompat.getColor(context, R.color.color_ffcc03))
                    tvSelect3.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                    mListener?.onSelectClick(2)
                }
                tvSelect3.setOnClickListener {
                    if (FastClickUtil.isFastClick()) {
                        return@setOnClickListener
                    }
                    tvSelect1.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                    tvSelect2.setTextColor(ContextCompat.getColor(context, R.color.grayAA))
                    tvSelect3.setTextColor(ContextCompat.getColor(context, R.color.color_ffcc03))
                    mListener?.onSelectClick(0)
                }
            }
            2 -> {
                val bean = item.recommendBean
                holder.getView<AvatarView>(R.id.avUser)
                    .setAvatar(bean.replysUserinfo.avatar, bean.replysUserinfo.master_type)
                    .setUserId(bean.replysUserinfo.user_id)
                holder.getView<MedalTextView>(R.id.comment_item_userName)
                    .setNickname(StringUtils.stringName(bean.replysUserinfo.nickname),
                        bean.replysUserinfo.medal_image).setUserId(bean.replysUserinfo.user_id)

                when (bean.replysUserinfo.user_id) {
                    author_id -> {
                        holder.setText(R.id.comment_item_author, "作者")
                        holder.setGone(R.id.comment_item_author, false)
                    }
                    lz_id -> {
                        holder.setText(R.id.comment_item_author, "楼主")
                        holder.setGone(R.id.comment_item_author, false)
                    }
                    else -> {
                        holder.setGone(R.id.comment_item_author, true)
                    }
                }
                if (bean.commentUserinfo != null && bean.replysUserinfo.nickname != bean.commentUserinfo.nickname) {
                    val nickname = StringUtils.stringName(bean.commentUserinfo.nickname)
                    val sb = SpannableString("回复 @${nickname}:${bean.content}")
                    sb.setSpan(ForegroundColorSpan(ContextCompat.getColor(context, R.color.grayAA)),
                        0, nickname.length + 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
                    holder.setText(R.id.satvDesc, sb)
                } else {
                    holder.setText(R.id.satvDesc, bean.content)
                }

                holder.setText(R.id.comment_item_time,
                    TimeZoneUtil.getShortTimeShowString(bean.create_time))
                holder.setGone(R.id.comment_item_hot, bean.has_badge == 1)

                val viewLike = holder.getView<LikeView>(R.id.viewLike)
                viewLike.setLike(bean.is_like, bean.like_num)
                viewLike.mListener = LikeView.OnLikeViewListener {
                    bean.like_num = viewLike.like_num
                    bean.is_like = viewLike.is_like
                    mListener?.clickLike(bean.id, bean.is_like)
                }

                if (bean.replysUserinfo.user_id == userId) {
                    holder.setGone(R.id.comment_item_more, false)
                    holder.getView<View>(R.id.comment_item_more).setOnClickListener {
                        if (bean.replysUserinfo.user_id == userId) {
                            mListener?.clickDelete(bean.id)
                        }
                    }
                } else {
                    holder.setGone(R.id.comment_item_more, true)
                }

                holder.getView<View>(R.id.ll_content).setOnClickListener {
                    /*  if (bean.replysUserinfo.user_id == userId) {
                          mListener?.clickReply(item)
                      }*/
                    mListener?.clickReply(item)
                }
            }
        }
    }

    var mListener: setOnNewsTwoListener? = null

    interface setOnNewsTwoListener {

        fun onSelectClick(comment_type: Int)

        fun clickLike(id: Int, is_like: Int)
        fun clickDelete(id: Int)

        fun clickReply(bean: NewsTwoMulti)

    }

    fun delComment(comment_id: Int) {
        for (i in data) {
            if (comment_id == i.recommendBean.id) {
                remove(i)
                break
            }
        }
    }

    fun setTitleSize(size:Int){
        tvTitle?.text="全部回复($size)"


    }

}
